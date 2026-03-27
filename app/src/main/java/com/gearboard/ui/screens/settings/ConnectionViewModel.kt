package com.gearboard.ui.screens.settings

import android.media.midi.MidiDeviceInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gearboard.midi.BleMidiPeripheral
import com.gearboard.midi.BleMidiScanner
import com.gearboard.midi.GearBoardMidiManager
import com.gearboard.ui.components.ConnectionState
import com.gearboard.ui.components.ConnectionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ConnectionViewModel — handles all MIDI connection logic.
 * Used by SettingsScreen for the CONNECTION section.
 * Keeps SettingsViewModel clean (preferences only).
 */
@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val midiManager: GearBoardMidiManager,
    private val bleScanner: BleMidiScanner,
    private val blePeripheral: BleMidiPeripheral
) : ViewModel() {

    val connectionState: StateFlow<ConnectionState> = midiManager.connectionState
    val availableDevices: StateFlow<List<MidiDeviceInfo>> = midiManager.availableDevices

    val isScanning: StateFlow<Boolean> = bleScanner.isScanning
    val discoveredBleDevices: StateFlow<List<BleMidiScanner.BleDevice>> = bleScanner.discoveredDevices

    val peripheralState: StateFlow<BleMidiPeripheral.PeripheralState> = blePeripheral.state

    private val _showBleScanSheet = MutableStateFlow(false)
    val showBleScanSheet: StateFlow<Boolean> = _showBleScanSheet.asStateFlow()

    private val _permissionsGranted = MutableStateFlow(false)

    val isBluetoothAvailable: Boolean get() = bleScanner.isBluetoothAvailable
    val isBluetoothEnabled: Boolean get() = bleScanner.isBluetoothEnabled

    init {
        midiManager.refreshDeviceList()
    }

    fun setPermissionsGranted(granted: Boolean) {
        _permissionsGranted.value = granted
    }

    // --- USB ---
    fun connectUsb(deviceInfo: MidiDeviceInfo) {
        midiManager.connectToDevice(deviceInfo, ConnectionType.USB)
    }

    // --- BLE Central (scan for MIDI hardware) ---
    fun startBleScan() {
        _showBleScanSheet.value = true
        bleScanner.startScan()
        viewModelScope.launch {
            delay(15_000)
            if (bleScanner.isScanning.value) bleScanner.stopScan()
        }
    }

    fun stopBleScan() {
        bleScanner.stopScan()
        _showBleScanSheet.value = false
    }

    fun connectBle(device: BleMidiScanner.BleDevice) {
        bleScanner.stopScan()
        _showBleScanSheet.value = false
        bleScanner.openBleDevice(device.address) { deviceInfo ->
            if (deviceInfo != null) {
                midiManager.connectToDevice(deviceInfo, ConnectionType.BLUETOOTH)
            }
        }
    }

    // --- BLE Peripheral (advertise to Mac/PC/Linux) ---
    fun startAdvertising() {
        blePeripheral.startAdvertising()
    }

    fun stopAdvertising() {
        blePeripheral.stopAdvertising()
    }

    // --- Common ---
    fun disconnect() {
        midiManager.disconnect()
    }

    fun refreshDevices() {
        midiManager.refreshDeviceList()
    }

    fun getDeviceName(info: MidiDeviceInfo): String = midiManager.getDeviceName(info)
    fun getDeviceType(info: MidiDeviceInfo): ConnectionType = midiManager.getDeviceConnectionType(info)
}
