package com.gearboard.ui.screens.connect

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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectViewModel @Inject constructor(
    private val midiManager: GearBoardMidiManager,
    private val bleScanner: BleMidiScanner,
    private val blePeripheral: BleMidiPeripheral
) : ViewModel() {

    // Connection state
    val connectionState: StateFlow<ConnectionState> = midiManager.connectionState

    // BLE reconnect attempt counter (exposed for ConnectionStatusBar)
    val bleReconnectAttempts: StateFlow<Int> = midiManager.bleReconnectAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Available USB MIDI devices
    val availableDevices: StateFlow<List<MidiDeviceInfo>> = midiManager.availableDevices

    // BLE scan state (Central mode — for connecting to BLE MIDI hardware)
    val isScanning: StateFlow<Boolean> = bleScanner.isScanning
    val discoveredBleDevices: StateFlow<List<BleMidiScanner.BleDevice>> = bleScanner.discoveredDevices

    // BLE peripheral state (Peripheral mode — for Mac/PC to connect to us)
    val peripheralState: StateFlow<BleMidiPeripheral.PeripheralState> = blePeripheral.state

    // UI state
    private val _showBleScanSheet = MutableStateFlow(false)
    val showBleScanSheet: StateFlow<Boolean> = _showBleScanSheet.asStateFlow()

    private val _permissionsGranted = MutableStateFlow(false)
    val permissionsGranted: StateFlow<Boolean> = _permissionsGranted.asStateFlow()

    val isBluetoothAvailable: Boolean get() = bleScanner.isBluetoothAvailable
    val isBluetoothEnabled: Boolean get() = bleScanner.isBluetoothEnabled
    val isPeripheralSupported: Boolean get() = blePeripheral.isPeripheralSupported

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
