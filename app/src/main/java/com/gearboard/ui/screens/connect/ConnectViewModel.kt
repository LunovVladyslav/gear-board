package com.gearboard.ui.screens.connect

import android.media.midi.MidiDeviceInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gearboard.midi.BleMidiScanner
import com.gearboard.midi.GearBoardMidiManager
import com.gearboard.ui.components.ConnectionState
import com.gearboard.ui.components.ConnectionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectViewModel @Inject constructor(
    private val midiManager: GearBoardMidiManager,
    private val bleScanner: BleMidiScanner
) : ViewModel() {

    // Connection state
    val connectionState: StateFlow<ConnectionState> = midiManager.connectionState

    // Available USB MIDI devices
    val availableDevices: StateFlow<List<MidiDeviceInfo>> = midiManager.availableDevices

    // BLE scan state
    val isScanning: StateFlow<Boolean> = bleScanner.isScanning
    val discoveredBleDevices: StateFlow<List<BleMidiScanner.BleDevice>> = bleScanner.discoveredDevices

    // UI state
    private val _showBleScanSheet = MutableStateFlow(false)
    val showBleScanSheet: StateFlow<Boolean> = _showBleScanSheet.asStateFlow()

    private val _permissionsGranted = MutableStateFlow(false)
    val permissionsGranted: StateFlow<Boolean> = _permissionsGranted.asStateFlow()

    val isBluetoothAvailable: Boolean get() = bleScanner.isBluetoothAvailable
    val isBluetoothEnabled: Boolean get() = bleScanner.isBluetoothEnabled

    init {
        // Refresh USB device list on init
        midiManager.refreshDeviceList()
    }

    fun setPermissionsGranted(granted: Boolean) {
        _permissionsGranted.value = granted
    }

    /**
     * Connect to a USB MIDI device.
     */
    fun connectUsb(deviceInfo: MidiDeviceInfo) {
        midiManager.connectToDevice(deviceInfo, ConnectionType.USB)
    }

    /**
     * Start BLE scan and show bottom sheet.
     */
    fun startBleScan() {
        _showBleScanSheet.value = true
        bleScanner.startScan()

        // Auto-stop scan after 15 seconds
        viewModelScope.launch {
            delay(15_000)
            if (bleScanner.isScanning.value) {
                bleScanner.stopScan()
            }
        }
    }

    /**
     * Stop BLE scan and dismiss bottom sheet.
     */
    fun stopBleScan() {
        bleScanner.stopScan()
        _showBleScanSheet.value = false
    }

    /**
     * Connect to a discovered BLE MIDI device.
     */
    fun connectBle(device: BleMidiScanner.BleDevice) {
        bleScanner.stopScan()
        _showBleScanSheet.value = false

        bleScanner.openBleDevice(device.address) { deviceInfo ->
            if (deviceInfo != null) {
                midiManager.connectToDevice(deviceInfo, ConnectionType.BLUETOOTH)
            }
        }
    }

    /**
     * Disconnect current connection.
     */
    fun disconnect() {
        midiManager.disconnect()
    }

    /**
     * Refresh USB device list.
     */
    fun refreshDevices() {
        midiManager.refreshDeviceList()
    }

    fun getDeviceName(info: MidiDeviceInfo): String = midiManager.getDeviceName(info)
    fun getDeviceType(info: MidiDeviceInfo): ConnectionType = midiManager.getDeviceConnectionType(info)
}
