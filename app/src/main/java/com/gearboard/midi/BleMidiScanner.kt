package com.gearboard.midi

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiManager
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
 * BLE MIDI Scanner — scans for Bluetooth LE MIDI devices.
 *
 * BLE MIDI devices advertise the MIDI Service UUID: 03B80E5A-EDE8-4B33-A751-6CE34EC4C700
 * After discovery, devices are opened via MidiManager.openBluetoothDevice().
 */
@Singleton
class BleMidiScanner @Inject constructor(
    @ApplicationContext private val context: Context,
    private val midiManager: MidiManager
) {
    companion object {
        private const val TAG = "BleMidiScanner"
        // Standard BLE MIDI Service UUID
        val MIDI_SERVICE_UUID: UUID = UUID.fromString("03B80E5A-EDE8-4B33-A751-6CE34EC4C700")
    }

    data class BleDevice(
        val name: String,
        val address: String,
        val rssi: Int
    )

    private val bluetoothManager: BluetoothManager? =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager

    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter
    private var bleScanner: BluetoothLeScanner? = null

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _discoveredDevices = MutableStateFlow<List<BleDevice>>(emptyList())
    val discoveredDevices: StateFlow<List<BleDevice>> = _discoveredDevices.asStateFlow()

    private val deviceSet = mutableSetOf<String>() // Track by address to avoid duplicates

    private val scanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device ?: return
            val address = device.address ?: return

            if (address !in deviceSet) {
                deviceSet.add(address)
                val name = device.name ?: "Unknown BLE MIDI"
                val bleDevice = BleDevice(name, address, result.rssi)
                _discoveredDevices.value = _discoveredDevices.value + bleDevice
                Log.d(TAG, "Discovered BLE MIDI: $name ($address) RSSI: ${result.rssi}")
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e(TAG, "BLE scan failed with error code: $errorCode")
            _isScanning.value = false
        }
    }

    /**
     * Start scanning for BLE MIDI devices.
     * Requires BLUETOOTH_SCAN permission on API 31+.
     */
    @SuppressLint("MissingPermission")
    fun startScan() {
        if (_isScanning.value) return
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            Log.w(TAG, "Bluetooth not available or disabled")
            return
        }

        deviceSet.clear()
        _discoveredDevices.value = emptyList()

        bleScanner = bluetoothAdapter.bluetoothLeScanner
        val scanFilter = ScanFilter.Builder()
            .setServiceUuid(ParcelUuid(MIDI_SERVICE_UUID))
            .build()
        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        bleScanner?.startScan(listOf(scanFilter), scanSettings, scanCallback)
        _isScanning.value = true
        Log.d(TAG, "BLE MIDI scan started")
    }

    /**
     * Stop scanning.
     */
    @SuppressLint("MissingPermission")
    fun stopScan() {
        if (!_isScanning.value) return
        try {
            bleScanner?.stopScan(scanCallback)
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping scan", e)
        }
        _isScanning.value = false
        Log.d(TAG, "BLE MIDI scan stopped")
    }

    /**
     * Open a BLE MIDI device for connection.
     * This uses MidiManager.openBluetoothDevice() which internally handles
     * the BLE GATT connection and MIDI service setup.
     */
    @SuppressLint("MissingPermission")
    fun openBleDevice(address: String, callback: (MidiDeviceInfo?) -> Unit) {
        val device = bluetoothAdapter?.getRemoteDevice(address)
        if (device == null) {
            Log.e(TAG, "BLE device not found: $address")
            callback(null)
            return
        }

        midiManager.openBluetoothDevice(device, { midiDevice ->
            if (midiDevice != null) {
                Log.d(TAG, "BLE MIDI device opened: ${device.name}")
                callback(midiDevice.info)
            } else {
                Log.e(TAG, "Failed to open BLE MIDI device: ${device.name}")
                callback(null)
            }
        }, null)
    }

    val isBluetoothAvailable: Boolean
        get() = bluetoothAdapter != null

    @SuppressLint("MissingPermission")
    val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true
}
