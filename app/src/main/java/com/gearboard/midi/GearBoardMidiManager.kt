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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val midiManager: MidiManager
) {
    companion object {
        private const val TAG = "GearBoardMidi"
    }

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
        midiManager.registerDeviceCallback(deviceCallback, handler)
        refreshDeviceList()
    }

    /**
     * Refresh the list of available MIDI devices.
     */
    fun refreshDeviceList() {
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
     */
    fun disconnect() {
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
     * Send a Control Change message.
     * @param ccNumber 0-127
     * @param value 0-127
     */
    fun sendControlChange(ccNumber: Int, value: Int) {
        val channel = midiChannel.coerceIn(0, 15)
        val statusByte = (0xB0 or channel).toByte()
        val data = byteArrayOf(statusByte, ccNumber.toByte(), value.toByte())

        sendMidiData(data)

        // Emit event for monitor
        _midiEvents.tryEmit(
            MidiEvent(
                type = MidiEventType.CONTROL_CHANGE,
                channel = channel,
                data1 = ccNumber,
                data2 = value,
                direction = MidiDirection.OUTGOING,
                rawBytes = data
            )
        )
    }

    /**
     * Send a Program Change message.
     * @param program 0-127
     */
    fun sendProgramChange(program: Int) {
        val channel = midiChannel.coerceIn(0, 15)
        val statusByte = (0xC0 or channel).toByte()
        val data = byteArrayOf(statusByte, program.toByte())

        sendMidiData(data)

        _midiEvents.tryEmit(
            MidiEvent(
                type = MidiEventType.PROGRAM_CHANGE,
                channel = channel,
                data1 = program,
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
        try {
            activeInputPort?.send(data, 0, data.size)
        } catch (e: Exception) {
            Log.e(TAG, "Error sending MIDI data", e)
        }
    }

    /**
     * Receiver for incoming MIDI messages from the connected device.
     */
    private val midiReceiver = object : MidiReceiver() {
        override fun onSend(data: ByteArray, offset: Int, count: Int, timestamp: Long) {
            if (count < 1) return

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
     */
    fun cleanup() {
        disconnect()
        midiManager.unregisterDeviceCallback(deviceCallback)
    }
}
