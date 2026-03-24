package com.gearboard.midi

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothGattServerCallback
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.os.ParcelUuid
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * BLE MIDI Peripheral — Advertises the Android device as a BLE MIDI controller.
 *
 * This allows Mac (Audio MIDI Setup), Windows (via BLE MIDI driver), and Linux
 * to discover and connect to this device as a standard BLE MIDI instrument.
 *
 * Uses the standard BLE MIDI specification:
 * - Service UUID: 03B80E5A-EDE8-4B33-A751-6CE34EC4C700
 * - Characteristic UUID: 7772E5DB-3868-4112-A1A9-F2669D106BF3
 *
 * The MIDI characteristic uses BLE MIDI packet format:
 * header/timestamp_high, timestamp_low, midi_status, data1, data2
 */
@Singleton
class BleMidiPeripheral @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "BleMidiPeripheral"

        // Standard BLE MIDI Service UUID
        val MIDI_SERVICE_UUID: UUID = UUID.fromString("03B80E5A-EDE8-4B33-A751-6CE34EC4C700")
        // Standard BLE MIDI Characteristic UUID
        val MIDI_CHARACTERISTIC_UUID: UUID = UUID.fromString("7772E5DB-3868-4112-A1A9-F2669D106BF3")
        // Client Characteristic Configuration Descriptor (for notifications)
        val CCCD_UUID: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }

    sealed class PeripheralState {
        data object Idle : PeripheralState()
        data object Advertising : PeripheralState()
        data class Connected(val deviceName: String) : PeripheralState()
        data class Error(val message: String) : PeripheralState()
    }

    private val bluetoothManager: BluetoothManager? =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager

    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter
    private var advertiser: BluetoothLeAdvertiser? = null
    private var gattServer: BluetoothGattServer? = null
    private var midiCharacteristic: BluetoothGattCharacteristic? = null
    private var connectedDevice: BluetoothDevice? = null
    private var notificationsEnabled = false

    private val _state = MutableStateFlow<PeripheralState>(PeripheralState.Idle)
    val state: StateFlow<PeripheralState> = _state.asStateFlow()

    // Callback for incoming MIDI data from the host (Mac/PC)
    var onMidiDataReceived: ((ByteArray) -> Unit)? = null

    /**
     * Start advertising as a BLE MIDI device and open GATT server.
     */
    @SuppressLint("MissingPermission")
    fun startAdvertising() {
        if (_state.value is PeripheralState.Advertising || _state.value is PeripheralState.Connected) {
            Log.w(TAG, "Already advertising or connected")
            return
        }

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            _state.value = PeripheralState.Error("Bluetooth not available or disabled")
            return
        }

        advertiser = bluetoothAdapter.bluetoothLeAdvertiser
        if (advertiser == null) {
            _state.value = PeripheralState.Error("BLE advertising not supported on this device")
            return
        }

        // Setup GATT server first
        setupGattServer()

        // Configure advertising
        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setConnectable(true)
            .setTimeout(0) // Advertise indefinitely
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .build()

        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            .addServiceUuid(ParcelUuid(MIDI_SERVICE_UUID))
            .build()

        val scanResponse = AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            .build()

        advertiser?.startAdvertising(settings, data, scanResponse, advertiseCallback)
        Log.d(TAG, "Starting BLE MIDI advertising...")
    }

    /**
     * Stop advertising and close GATT server.
     */
    @SuppressLint("MissingPermission")
    fun stopAdvertising() {
        try {
            advertiser?.stopAdvertising(advertiseCallback)
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping advertising", e)
        }

        try {
            connectedDevice?.let { gattServer?.cancelConnection(it) }
            gattServer?.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error closing GATT server", e)
        }

        gattServer = null
        midiCharacteristic = null
        connectedDevice = null
        notificationsEnabled = false
        _state.value = PeripheralState.Idle
        Log.d(TAG, "BLE MIDI peripheral stopped")
    }

    /**
     * Send MIDI data to the connected host (Mac/PC) via BLE notification.
     *
     * Wraps raw MIDI bytes in BLE MIDI packet format:
     * header, timestamp, midi_bytes
     */
    @SuppressLint("MissingPermission")
    fun sendMidiData(midiBytes: ByteArray) {
        if (!notificationsEnabled || connectedDevice == null || midiCharacteristic == null) {
            return
        }

        // BLE MIDI packet format
        val timestamp = (System.currentTimeMillis() % 8192).toInt()
        val header = (0x80 or ((timestamp shr 7) and 0x3F)).toByte()
        val timestampLow = (0x80 or (timestamp and 0x7F)).toByte()

        val packet = ByteArray(2 + midiBytes.size)
        packet[0] = header
        packet[1] = timestampLow
        System.arraycopy(midiBytes, 0, packet, 2, midiBytes.size)

        midiCharacteristic?.let { char ->
            char.value = packet
            try {
                gattServer?.notifyCharacteristicChanged(connectedDevice, char, false)
            } catch (e: Exception) {
                Log.e(TAG, "Error sending MIDI notification", e)
            }
        }
    }

    /**
     * Convenience: Send CC message via BLE
     */
    fun sendControlChange(channel: Int, ccNumber: Int, value: Int) {
        val statusByte = (0xB0 or (channel and 0x0F)).toByte()
        sendMidiData(byteArrayOf(statusByte, (ccNumber and 0x7F).toByte(), (value and 0x7F).toByte()))
    }

    /**
     * Convenience: Send Program Change via BLE
     */
    fun sendProgramChange(channel: Int, program: Int) {
        val statusByte = (0xC0 or (channel and 0x0F)).toByte()
        sendMidiData(byteArrayOf(statusByte, (program and 0x7F).toByte()))
    }

    // --- GATT Server Setup ---

    @SuppressLint("MissingPermission")
    private fun setupGattServer() {
        gattServer = bluetoothManager?.openGattServer(context, gattServerCallback)

        // Create MIDI service
        val midiService = BluetoothGattService(
            MIDI_SERVICE_UUID,
            BluetoothGattService.SERVICE_TYPE_PRIMARY
        )

        // Create MIDI characteristic (read + write + notify)
        midiCharacteristic = BluetoothGattCharacteristic(
            MIDI_CHARACTERISTIC_UUID,
            BluetoothGattCharacteristic.PROPERTY_READ or
                    BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE or
                    BluetoothGattCharacteristic.PROPERTY_NOTIFY,
            BluetoothGattCharacteristic.PERMISSION_READ or
                    BluetoothGattCharacteristic.PERMISSION_WRITE
        )

        // Add CCCD for notifications
        val cccd = BluetoothGattDescriptor(
            CCCD_UUID,
            BluetoothGattDescriptor.PERMISSION_READ or BluetoothGattDescriptor.PERMISSION_WRITE
        )
        midiCharacteristic?.addDescriptor(cccd)
        midiService.addCharacteristic(midiCharacteristic)

        gattServer?.addService(midiService)
        Log.d(TAG, "GATT server with MIDI service created")
    }

    // --- Callbacks ---

    private val advertiseCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            _state.value = PeripheralState.Advertising
            Log.d(TAG, "BLE MIDI advertising started successfully")
        }

        override fun onStartFailure(errorCode: Int) {
            val msg = when (errorCode) {
                ADVERTISE_FAILED_DATA_TOO_LARGE -> "Data too large"
                ADVERTISE_FAILED_TOO_MANY_ADVERTISERS -> "Too many advertisers"
                ADVERTISE_FAILED_ALREADY_STARTED -> "Already started"
                ADVERTISE_FAILED_INTERNAL_ERROR -> "Internal error"
                ADVERTISE_FAILED_FEATURE_UNSUPPORTED -> "Feature unsupported"
                else -> "Unknown error ($errorCode)"
            }
            _state.value = PeripheralState.Error("Advertise failed: $msg")
            Log.e(TAG, "Advertising failed: $msg")
        }
    }

    @SuppressLint("MissingPermission")
    private val gattServerCallback = object : BluetoothGattServerCallback() {
        override fun onConnectionStateChange(device: BluetoothDevice, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    connectedDevice = device
                    val name = device.name ?: device.address
                    _state.value = PeripheralState.Connected(name)
                    Log.d(TAG, "Host connected: $name")
                    // Stop advertising once connected
                    try {
                        advertiser?.stopAdvertising(advertiseCallback)
                    } catch (_: Exception) {}
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    connectedDevice = null
                    notificationsEnabled = false
                    _state.value = PeripheralState.Idle
                    Log.d(TAG, "Host disconnected")
                }
            }
        }

        override fun onCharacteristicReadRequest(
            device: BluetoothDevice, requestId: Int, offset: Int,
            characteristic: BluetoothGattCharacteristic
        ) {
            if (characteristic.uuid == MIDI_CHARACTERISTIC_UUID) {
                gattServer?.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, byteArrayOf())
            }
        }

        override fun onCharacteristicWriteRequest(
            device: BluetoothDevice, requestId: Int,
            characteristic: BluetoothGattCharacteristic,
            preparedWrite: Boolean, responseNeeded: Boolean,
            offset: Int, value: ByteArray?
        ) {
            if (characteristic.uuid == MIDI_CHARACTERISTIC_UUID && value != null) {
                // Parse incoming BLE MIDI data from host
                parseBLeMidiPacket(value)

                if (responseNeeded) {
                    gattServer?.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, null)
                }
            }
        }

        override fun onDescriptorWriteRequest(
            device: BluetoothDevice, requestId: Int,
            descriptor: BluetoothGattDescriptor,
            preparedWrite: Boolean, responseNeeded: Boolean,
            offset: Int, value: ByteArray?
        ) {
            if (descriptor.uuid == CCCD_UUID) {
                notificationsEnabled = value?.contentEquals(
                    BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                ) == true
                Log.d(TAG, "Notifications ${if (notificationsEnabled) "enabled" else "disabled"}")

                if (responseNeeded) {
                    gattServer?.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, null)
                }
            }
        }

        override fun onDescriptorReadRequest(
            device: BluetoothDevice, requestId: Int, offset: Int,
            descriptor: BluetoothGattDescriptor
        ) {
            if (descriptor.uuid == CCCD_UUID) {
                val returnValue = if (notificationsEnabled)
                    BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                else
                    BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
                gattServer?.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, returnValue)
            }
        }
    }

    /**
     * Parse a BLE MIDI packet from the host.
     * Format: header, timestamp, midi_status, data1, data2 ...
     */
    private fun parseBLeMidiPacket(packet: ByteArray) {
        if (packet.size < 3) return

        // Skip header and timestamp bytes, extract MIDI data
        val midiData = packet.copyOfRange(2, packet.size)
        onMidiDataReceived?.invoke(midiData)
    }

    val isPeripheralSupported: Boolean
        get() = bluetoothAdapter?.bluetoothLeAdvertiser != null
}
