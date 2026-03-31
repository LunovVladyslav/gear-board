package com.gearboard.midi

import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiInputPort
import android.media.midi.MidiManager
import android.media.midi.MidiOutputPort
import android.media.midi.MidiReceiver
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.gearboard.domain.model.MidiDirection
import com.gearboard.domain.model.MidiEvent
import com.gearboard.domain.model.MidiEventType
import com.gearboard.ui.components.ConnectionState
import com.gearboard.ui.components.ConnectionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.job
import javax.inject.Inject
import javax.inject.Singleton

/**
 * GearBoardMidiManager — central MIDI controller.
 *
 * Manages USB and BLE MIDI connections, sends CC/PC messages,
 * and emits MIDI events for the monitor screen.
 */
@Singleton
class GearBoardMidiManager @Inject constructor(
    private val midiManager: MidiManager,
    private val blePeripheral: BleMidiPeripheral
) {
    companion object {
        private const val TAG = "GearBoardMidi"
        private const val CC_THROTTLE_MS = 33L  // max ~30 msg/sec per CC
        private const val MAX_RECONNECT_ATTEMPTS = 5
    }

    // Rate limiting: last sent timestamp per CC number (0-127)
    private val ccLastSentMs = LongArray(128) { 0L }

    // Drop-last pending pattern: stores latest pending value+channel per CC
    private val pendingCC = arrayOfNulls<Pair<Int, Int>>(128) // (value, channel)
    private val pendingScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // BLE reconnect tracking
    private var reconnectAttempts = 0
    private val _bleReconnectAttempts = MutableStateFlow(0)
    val bleReconnectAttempts: StateFlow<Int> = _bleReconnectAttempts.asStateFlow()
    private val managerScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    // Connection state
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    // MIDI events stream (for monitor)
    private val _midiEvents = MutableSharedFlow<MidiEvent>(extraBufferCapacity = 100)
    val midiEvents: SharedFlow<MidiEvent> = _midiEvents.asSharedFlow()

    // Available devices
    private val _availableDevices = MutableStateFlow<List<MidiDeviceInfo>>(emptyList())
    val availableDevices: StateFlow<List<MidiDeviceInfo>> = _availableDevices.asStateFlow()

    // Active connection
    private var activeInputPort: MidiInputPort? = null
    private var activeOutputPort: MidiOutputPort? = null
    private var connectedDeviceName: String? = null
    private var connectedType: ConnectionType? = null

    // MIDI channel (0-15)
    var midiChannel: Int = 0

    /** Live Mode clock receiver — set by LiveModeViewModel when synced mode is active. */
    var clockReceiver: com.gearboard.midi.MidiClockReceiver? = null

    private val handler = Handler(Looper.getMainLooper())

    // Device callback for USB hotplug
    private val deviceCallback = object : MidiManager.DeviceCallback() {
        override fun onDeviceAdded(device: MidiDeviceInfo) {
            Log.d(TAG, "MIDI device added: ${getDeviceName(device)}")
            refreshDeviceList()
        }

        override fun onDeviceRemoved(device: MidiDeviceInfo) {
            Log.d(TAG, "MIDI device removed: ${getDeviceName(device)}")
            refreshDeviceList()
            // If the removed device was our connected device, disconnect
            if (connectedDeviceName == getDeviceName(device)) {
                disconnect()
            }
        }
    }

    init {
        // Register for device hotplug events
        @Suppress("DEPRECATION")
        midiManager.registerDeviceCallback(deviceCallback, handler)
        refreshDeviceList()

        // Wire BLE peripheral incoming data to our MIDI event stream
        blePeripheral.onMidiDataReceived = { data ->
            parseMidiBytes(data, MidiDirection.INCOMING)
        }

        // Auto-reconnect BLE peripheral on disconnect
        managerScope.launch {
            blePeripheral.state.collect { state ->
                if (state is BleMidiPeripheral.PeripheralState.Idle &&
                    _connectionState.value is ConnectionState.Connected &&
                    (_connectionState.value as? ConnectionState.Connected)?.type == ConnectionType.BLUETOOTH
                ) {
                    // BLE host disconnected
                    if (reconnectAttempts < MAX_RECONNECT_ATTEMPTS) {
                        reconnectAttempts++
                        _bleReconnectAttempts.value = reconnectAttempts
                        Log.d(TAG, "BLE disconnected, auto-reconnect attempt $reconnectAttempts/$MAX_RECONNECT_ATTEMPTS")
                        blePeripheral.startAdvertising()
                    } else {
                        Log.w(TAG, "BLE max reconnect attempts reached")
                        _connectionState.value = ConnectionState.Error("BLE connection lost")
                    }
                } else if (state is BleMidiPeripheral.PeripheralState.Connected) {
                    reconnectAttempts = 0
                    _bleReconnectAttempts.value = 0
                }
            }
        }
    }

    /**
     * Refresh the list of available MIDI devices.
     */
    fun refreshDeviceList() {
        @Suppress("DEPRECATION")
        val devices = midiManager.devices?.toList() ?: emptyList()
        _availableDevices.value = devices
        Log.d(TAG, "Found ${devices.size} MIDI devices")
    }

    /**
     * Connect to a MIDI device.
     */
    fun connectToDevice(deviceInfo: MidiDeviceInfo, type: ConnectionType) {
        val deviceName = getDeviceName(deviceInfo)
        _connectionState.value = ConnectionState.Connecting(deviceName)

        midiManager.openDevice(deviceInfo, { device ->
            if (device == null) {
                Log.e(TAG, "Failed to open device: $deviceName")
                _connectionState.value = ConnectionState.Error("Failed to open $deviceName")
                return@openDevice
            }

            try {
                // Open input port (for sending MIDI TO the device)
                if (device.info.inputPortCount > 0) {
                    activeInputPort = device.openInputPort(0)
                }

                // Open output port (for receiving MIDI FROM the device)
                if (device.info.outputPortCount > 0) {
                    activeOutputPort = device.openOutputPort(0)
                    activeOutputPort?.connect(midiReceiver)
                }

                connectedDeviceName = deviceName
                connectedType = type
                _connectionState.value = ConnectionState.Connected(deviceName, type)
                Log.d(TAG, "Connected to $deviceName via $type")

            } catch (e: Exception) {
                Log.e(TAG, "Error connecting to $deviceName", e)
                _connectionState.value = ConnectionState.Error(e.message ?: "Connection error")
            }
        }, handler)
    }

    /**
     * Disconnect from the current device.
     * Cancels any pending throttled CC sends before closing ports to avoid
     * sending stale values on the next connection.
     */
    fun disconnect() {
        pendingScope.coroutineContext.job.children.forEach { it.cancel() }
        pendingCC.fill(null)
        try {
            activeOutputPort?.disconnect(midiReceiver)
            activeInputPort?.close()
            activeOutputPort?.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error closing MIDI ports", e)
        }

        activeInputPort = null
        activeOutputPort = null
        connectedDeviceName = null
        connectedType = null
        _connectionState.value = ConnectionState.Disconnected
        Log.d(TAG, "Disconnected")
    }

    /**
     * Send a Control Change message using the global MIDI channel.
     * @param ccNumber 0-127
     * @param value 0-127
     */
    fun sendControlChange(ccNumber: Int, value: Int) {
        sendControlChange(ccNumber, value, midiChannel + 1)
    }

    /**
     * Send a Control Change message on a specific channel.
     *
     * Uses a drop-last throttle: at most one message per CC is sent per [CC_THROTTLE_MS] window
     * (~30 msg/sec). If a new value arrives while throttled, it replaces the pending value and
     * fires after the window expires — ensuring the final position is always transmitted.
     *
     * CC 0 is guarded and never forwarded (reserved for bank select MSB in [sendPreset]).
     *
     * The message is sent via both paths simultaneously:
     * - USB/BLE device input port ([activeInputPort])
     * - BLE peripheral to a connected Mac/PC host ([blePeripheral]) when connected
     *
     * @param ccNumber 0-127 (0 is silently dropped)
     * @param value 0-127
     * @param channel 1-16 (MIDI channel)
     */
    fun sendControlChange(ccNumber: Int, value: Int, channel: Int) {
        val cc = ccNumber.coerceIn(0, 127)
        if (cc == 0) return // guard: never send CC 0 (bank select MSB)
        val v = value.coerceIn(0, 127)
        val ch = (channel - 1).coerceIn(0, 15)

        val now = System.currentTimeMillis()
        if (now - ccLastSentMs[cc] >= CC_THROTTLE_MS) {
            ccLastSentMs[cc] = now
            pendingCC[cc] = null
            sendMidiCC(cc, v, ch)
        } else {
            pendingCC[cc] = Pair(v, ch)
            pendingScope.launch {
                delay(CC_THROTTLE_MS)
                pendingCC[cc]?.let { (pv, pch) ->
                    pendingCC[cc] = null
                    ccLastSentMs[cc] = System.currentTimeMillis()
                    sendMidiCC(cc, pv, pch)
                }
            }
        }
    }

    private fun sendMidiCC(cc: Int, value: Int, channel: Int) {
        val statusByte = (0xB0 or channel).toByte()
        val data = byteArrayOf(statusByte, cc.toByte(), value.toByte())
        Log.d(TAG, "sendCC: ch=$channel cc=$cc val=$value")
        try {
            activeInputPort?.send(data, 0, data.size)
            val peripheralState = blePeripheral.state.value
            if (peripheralState is BleMidiPeripheral.PeripheralState.Connected) {
                blePeripheral.sendMidiData(data)
            }
        } catch (e: java.io.IOException) {
            Log.w(TAG, "MIDI send failed on CC $cc: ${e.message}")
            _connectionState.value = ConnectionState.Error("Send failed: ${e.message}")
        } catch (e: IllegalStateException) {
            Log.w(TAG, "MIDI port closed during send: ${e.message}")
        }
        _midiEvents.tryEmit(
            MidiEvent(
                type = MidiEventType.CONTROL_CHANGE,
                channel = channel,
                data1 = cc,
                data2 = value,
                direction = MidiDirection.OUTGOING,
                rawBytes = data
            )
        )
    }

    /**
     * Send a Program Change message using the global MIDI channel.
     * @param program 0-127
     */
    fun sendProgramChange(program: Int) {
        sendProgramChange(program, midiChannel + 1)
    }

    /**
     * Send a Program Change message on a specific channel.
     * @param program 0-127
     * @param channel 1-16 (MIDI channel)
     */
    fun sendProgramChange(program: Int, channel: Int) {
        val ch = (channel - 1).coerceIn(0, 15)
        val statusByte = (0xC0 or ch).toByte()
        val data = byteArrayOf(statusByte, program.toByte())

        try {
            activeInputPort?.send(data, 0, data.size)
            val peripheralState = blePeripheral.state.value
            if (peripheralState is BleMidiPeripheral.PeripheralState.Connected) {
                blePeripheral.sendMidiData(data)
            }
        } catch (e: java.io.IOException) {
            Log.w(TAG, "MIDI send failed on PC $program: ${e.message}")
            _connectionState.value = ConnectionState.Error("Send failed: ${e.message}")
        } catch (e: IllegalStateException) {
            Log.w(TAG, "MIDI port closed during send: ${e.message}")
        }

        _midiEvents.tryEmit(
            MidiEvent(
                type = MidiEventType.PROGRAM_CHANGE,
                channel = ch,
                data1 = program,
                data2 = 0,
                direction = MidiDirection.OUTGOING,
                rawBytes = data
            )
        )
    }

    /**
     * Send a Note On message.
     * @param note 0-127
     * @param velocity 0-127
     * @param channel 1-16 (MIDI channel)
     */
    fun sendNoteOn(note: Int, velocity: Int, channel: Int) {
        val ch = (channel - 1).coerceIn(0, 15)
        val statusByte = (0x90 or ch).toByte()
        val data = byteArrayOf(statusByte, note.toByte(), velocity.toByte())
        Log.d(TAG, "sendNoteOn: ch=$ch note=$note vel=$velocity")

        try {
            activeInputPort?.send(data, 0, data.size)
            val peripheralState = blePeripheral.state.value
            if (peripheralState is BleMidiPeripheral.PeripheralState.Connected) {
                blePeripheral.sendMidiData(data)
            }
        } catch (e: java.io.IOException) {
            Log.w(TAG, "MIDI send failed on NoteOn $note: ${e.message}")
            _connectionState.value = ConnectionState.Error("Send failed: ${e.message}")
        } catch (e: IllegalStateException) {
            Log.w(TAG, "MIDI port closed during send: ${e.message}")
        }

        _midiEvents.tryEmit(
            MidiEvent(
                type = MidiEventType.NOTE_ON,
                channel = ch,
                data1 = note,
                data2 = velocity,
                direction = MidiDirection.OUTGOING,
                rawBytes = data
            )
        )
    }

    /**
     * Send Bank Select + Program Change for a preset.
     * Bank Select: CC 0 (MSB) = bank / 128, CC 32 (LSB) = bank % 128, then PC.
     */
    fun sendPreset(bank: Int, program: Int) {
        val ch = midiChannel.coerceIn(0, 15)
        val bankMsb = (bank / 128).coerceIn(0, 127)
        val bankLsb = (bank % 128).coerceIn(0, 127)
        val prog = program.coerceIn(0, 127)
        try {
            val msbData = byteArrayOf((0xB0 or ch).toByte(), 0x00, bankMsb.toByte())
            val lsbData = byteArrayOf((0xB0 or ch).toByte(), 0x20, bankLsb.toByte())
            val pcData = byteArrayOf((0xC0 or ch).toByte(), prog.toByte())
            val peripheralConnected = blePeripheral.state.value is BleMidiPeripheral.PeripheralState.Connected
            // Bank Select MSB (CC 0)
            activeInputPort?.send(msbData, 0, msbData.size)
            if (peripheralConnected) blePeripheral.sendMidiData(msbData)
            // Bank Select LSB (CC 32)
            activeInputPort?.send(lsbData, 0, lsbData.size)
            if (peripheralConnected) blePeripheral.sendMidiData(lsbData)
            // Program Change
            activeInputPort?.send(pcData, 0, pcData.size)
            if (peripheralConnected) blePeripheral.sendMidiData(pcData)
            Log.d(TAG, "sendPreset: bank=$bank prog=$program ch=$ch")
        } catch (e: java.io.IOException) {
            Log.w(TAG, "MIDI send failed on sendPreset bank=$bank prog=$program: ${e.message}")
            _connectionState.value = ConnectionState.Error("Send failed: ${e.message}")
        } catch (e: IllegalStateException) {
            Log.w(TAG, "MIDI port closed during sendPreset: ${e.message}")
        }
    }

    /**
     * Send a Note Off message.
     * @param note 0-127
     * @param channel 1-16 (MIDI channel)
     */
    fun sendNoteOff(note: Int, channel: Int) {
        val ch = (channel - 1).coerceIn(0, 15)
        val statusByte = (0x80 or ch).toByte()
        val data = byteArrayOf(statusByte, note.toByte(), 0)
        Log.d(TAG, "sendNoteOff: ch=$ch note=$note")

        try {
            activeInputPort?.send(data, 0, data.size)
            val peripheralState = blePeripheral.state.value
            if (peripheralState is BleMidiPeripheral.PeripheralState.Connected) {
                blePeripheral.sendMidiData(data)
            }
        } catch (e: java.io.IOException) {
            Log.w(TAG, "MIDI send failed on NoteOff $note: ${e.message}")
            _connectionState.value = ConnectionState.Error("Send failed: ${e.message}")
        } catch (e: IllegalStateException) {
            Log.w(TAG, "MIDI port closed during send: ${e.message}")
        }

        _midiEvents.tryEmit(
            MidiEvent(
                type = MidiEventType.NOTE_OFF,
                channel = ch,
                data1 = note,
                data2 = 0,
                direction = MidiDirection.OUTGOING,
                rawBytes = data
            )
        )
    }

    /**
     * Send raw MIDI bytes to the connected device.
     */
    private fun sendMidiData(data: ByteArray) {
        // Send via USB/BLE MIDI device connection
        try {
            activeInputPort?.send(data, 0, data.size)
        } catch (e: Exception) {
            Log.e(TAG, "Error sending MIDI data via device port", e)
        }

        // Also send via BLE peripheral if a host (Mac/PC) is connected
        val peripheralState = blePeripheral.state.value
        Log.d(TAG, "sendMidiData: peripheralState=$peripheralState")
        if (peripheralState is BleMidiPeripheral.PeripheralState.Connected) {
            Log.d(TAG, "sendMidiData: forwarding to BLE peripheral")
            blePeripheral.sendMidiData(data)
        } else {
            Log.d(TAG, "sendMidiData: BLE peripheral not connected, skipping")
        }
    }

    /**
     * Receiver for incoming MIDI messages from the connected device.
     */
    private val midiReceiver = object : MidiReceiver() {
        override fun onSend(data: ByteArray, offset: Int, count: Int, timestamp: Long) {
            if (count < 1) return

            // Route system real-time single-byte messages to clock receiver
            val firstByte = data[offset].toInt() and 0xFF
            when (firstByte) {
                0xF8 -> { clockReceiver?.onTick(timestamp / 1_000_000); return }
                0xFA -> { clockReceiver?.onStart(); return }
                0xFB -> { clockReceiver?.onContinue(); return }
                0xFC -> { clockReceiver?.onStop(); return }
                0xF2 -> {
                    if (count >= 3) {
                        val lsb = data[offset + 1].toInt() and 0x7F
                        val msb = data[offset + 2].toInt() and 0x7F
                        clockReceiver?.onSongPositionPointer(lsb, msb)
                    }
                    return
                }
            }

            val statusByte = data[offset].toInt() and 0xFF
            val channel = statusByte and 0x0F
            val messageType = statusByte and 0xF0

            val event = when (messageType) {
                0x90 -> { // Note On
                    if (count >= 3) {
                        MidiEvent(
                            type = MidiEventType.NOTE_ON,
                            channel = channel,
                            data1 = data[offset + 1].toInt() and 0x7F,
                            data2 = data[offset + 2].toInt() and 0x7F,
                            direction = MidiDirection.INCOMING,
                            rawBytes = data.copyOfRange(offset, offset + count)
                        )
                    } else null
                }
                0x80 -> { // Note Off
                    if (count >= 3) {
                        MidiEvent(
                            type = MidiEventType.NOTE_OFF,
                            channel = channel,
                            data1 = data[offset + 1].toInt() and 0x7F,
                            data2 = data[offset + 2].toInt() and 0x7F,
                            direction = MidiDirection.INCOMING,
                            rawBytes = data.copyOfRange(offset, offset + count)
                        )
                    } else null
                }
                0xB0 -> { // Control Change
                    if (count >= 3) {
                        MidiEvent(
                            type = MidiEventType.CONTROL_CHANGE,
                            channel = channel,
                            data1 = data[offset + 1].toInt() and 0x7F,
                            data2 = data[offset + 2].toInt() and 0x7F,
                            direction = MidiDirection.INCOMING,
                            rawBytes = data.copyOfRange(offset, offset + count)
                        )
                    } else null
                }
                0xC0 -> { // Program Change
                    if (count >= 2) {
                        MidiEvent(
                            type = MidiEventType.PROGRAM_CHANGE,
                            channel = channel,
                            data1 = data[offset + 1].toInt() and 0x7F,
                            data2 = 0,
                            direction = MidiDirection.INCOMING,
                            rawBytes = data.copyOfRange(offset, offset + count)
                        )
                    } else null
                }
                0xE0 -> { // Pitch Bend
                    if (count >= 3) {
                        MidiEvent(
                            type = MidiEventType.PITCH_BEND,
                            channel = channel,
                            data1 = data[offset + 1].toInt() and 0x7F,
                            data2 = data[offset + 2].toInt() and 0x7F,
                            direction = MidiDirection.INCOMING,
                            rawBytes = data.copyOfRange(offset, offset + count)
                        )
                    } else null
                }
                else -> {
                    MidiEvent(
                        type = MidiEventType.UNKNOWN,
                        channel = channel,
                        data1 = if (count >= 2) data[offset + 1].toInt() and 0x7F else 0,
                        data2 = if (count >= 3) data[offset + 2].toInt() and 0x7F else 0,
                        direction = MidiDirection.INCOMING,
                        rawBytes = data.copyOfRange(offset, offset + count)
                    )
                }
            }

            event?.let { _midiEvents.tryEmit(it) }
        }
    }

    /**
     * Parse raw MIDI bytes and emit events.
     * Used by BleMidiPeripheral for incoming data from the host.
     */
    private fun parseMidiBytes(data: ByteArray, direction: MidiDirection) {
        if (data.isEmpty()) return
        val statusByte = data[0].toInt() and 0xFF

        // System Real-Time messages — single byte, no channel
        when (statusByte) {
            0xF8 -> { clockReceiver?.onTick(System.currentTimeMillis()); return }
            0xFA -> { clockReceiver?.onStart(); return }
            0xFB -> { clockReceiver?.onContinue(); return }
            0xFC -> { clockReceiver?.onStop(); return }
            0xF2 -> {
                // Song Position Pointer — 3 bytes: 0xF2, LSB, MSB
                if (data.size >= 3) {
                    val lsb = data[1].toInt() and 0x7F
                    val msb = data[2].toInt() and 0x7F
                    clockReceiver?.onSongPositionPointer(lsb, msb)
                }
                return
            }
        }

        val channel = statusByte and 0x0F
        val messageType = statusByte and 0xF0
        val d1 = if (data.size >= 2) data[1].toInt() and 0x7F else 0
        val d2 = if (data.size >= 3) data[2].toInt() and 0x7F else 0

        val type = when (messageType) {
            0x90 -> MidiEventType.NOTE_ON
            0x80 -> MidiEventType.NOTE_OFF
            0xB0 -> MidiEventType.CONTROL_CHANGE
            0xC0 -> MidiEventType.PROGRAM_CHANGE
            0xE0 -> MidiEventType.PITCH_BEND
            else -> MidiEventType.UNKNOWN
        }

        _midiEvents.tryEmit(
            MidiEvent(
                type = type, channel = channel,
                data1 = d1, data2 = d2,
                direction = direction, rawBytes = data
            )
        )
    }

    /**
     * Get a human-readable device name.
     */
    fun getDeviceName(deviceInfo: MidiDeviceInfo): String {
        val properties = deviceInfo.properties
        return properties.getString(MidiDeviceInfo.PROPERTY_NAME)
            ?: properties.getString(MidiDeviceInfo.PROPERTY_MANUFACTURER)
            ?: "Unknown MIDI Device"
    }

    /**
     * Get the connection type for a device (USB or BLE based on transport).
     */
    fun getDeviceConnectionType(deviceInfo: MidiDeviceInfo): ConnectionType {
        return when (deviceInfo.type) {
            MidiDeviceInfo.TYPE_USB -> ConnectionType.USB
            MidiDeviceInfo.TYPE_BLUETOOTH -> ConnectionType.BLUETOOTH
            else -> ConnectionType.USB // Default to USB for virtual devices
        }
    }

    /**
     * Check if currently connected.
     */
    val isConnected: Boolean
        get() = _connectionState.value is ConnectionState.Connected

    /**
     * Cleanup — call when app is destroyed.
     *
     * Cancels all pending throttled CC jobs in [pendingScope] (drop-last queue),
     * cancels the BLE reconnect/monitor scope [managerScope], unregisters the USB
     * device hotplug callback, and closes any open MIDI ports via [disconnect].
     */
    fun cleanup() {
        disconnect()
        pendingScope.cancel()
        managerScope.cancel()
        midiManager.unregisterDeviceCallback(deviceCallback)
    }

    // --- Test helpers ---

    /** Inject a mock [MidiInputPort] for unit tests. Not for production use. */
    @androidx.annotation.VisibleForTesting
    fun setInputPortForTest(port: android.media.midi.MidiInputPort) {
        activeInputPort = port
    }

    /** Feed raw MIDI bytes into the receiver as if they arrived from a device. */
    @androidx.annotation.VisibleForTesting
    fun simulateIncomingBytes(data: ByteArray) {
        midiReceiver.onSend(data, 0, data.size, System.currentTimeMillis())
    }
}
