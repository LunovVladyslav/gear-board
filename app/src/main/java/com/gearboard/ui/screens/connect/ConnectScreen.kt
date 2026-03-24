package com.gearboard.ui.screens.connect

import android.Manifest
import android.media.midi.MidiDeviceInfo
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SignalWifi4Bar
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gearboard.midi.BleMidiPeripheral
import com.gearboard.midi.BleMidiScanner
import com.gearboard.ui.components.ConnectionState
import com.gearboard.ui.components.ConnectionType
import com.gearboard.ui.theme.GearBoardColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectScreen(
    viewModel: ConnectViewModel = hiltViewModel()
) {
    val connectionState by viewModel.connectionState.collectAsStateWithLifecycle()
    val usbDevices by viewModel.availableDevices.collectAsStateWithLifecycle()
    val showBleScanSheet by viewModel.showBleScanSheet.collectAsStateWithLifecycle()
    val isScanning by viewModel.isScanning.collectAsStateWithLifecycle()
    val bleDevices by viewModel.discoveredBleDevices.collectAsStateWithLifecycle()
    val peripheralState by viewModel.peripheralState.collectAsStateWithLifecycle()

    // Permission launcher for BLE
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        viewModel.setPermissionsGranted(allGranted)
    }

    // Permission launcher specifically for advertise
    val advertisePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            viewModel.startAdvertising()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GearBoardColors.Background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Connection status card
        item {
            ConnectionStatusCard(connectionState) {
                viewModel.disconnect()
            }
        }

        // USB MIDI section
        item {
            SectionTitle("USB MIDI DEVICES")
        }

        if (usbDevices.isEmpty()) {
            item {
                EmptyStateMessage(
                    icon = Icons.Default.Usb,
                    message = "No USB MIDI devices found",
                    subMessage = "Connect a USB MIDI device via OTG cable"
                )
            }
        } else {
            items(usbDevices, key = { it.id }) { device ->
                DeviceCard(
                    name = viewModel.getDeviceName(device),
                    type = viewModel.getDeviceType(device),
                    isConnected = connectionState is ConnectionState.Connected &&
                            (connectionState as? ConnectionState.Connected)?.deviceName == viewModel.getDeviceName(device),
                    onClick = { viewModel.connectUsb(device) }
                )
            }
        }

        // Refresh USB button
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { viewModel.refreshDevices() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GearBoardColors.SurfaceElevated,
                        contentColor = GearBoardColors.TextPrimary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Refresh, "Refresh", modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("REFRESH USB", fontSize = 12.sp, letterSpacing = 1.sp)
                }
            }
        }

        // Bluetooth MIDI section
        item {
            Spacer(Modifier.height(8.dp))
            SectionTitle("BLUETOOTH MIDI")
        }

        item {
            Button(
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.BLUETOOTH_SCAN,
                                Manifest.permission.BLUETOOTH_CONNECT
                            )
                        )
                    } else {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GearBoardColors.Accent,
                    contentColor = GearBoardColors.TextOnAccent
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = connectionState !is ConnectionState.Connected
            ) {
                Icon(Icons.Default.BluetoothSearching, "Scan", modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("SCAN FOR BLE MIDI HARDWARE", fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }
        }

        // === ADVERTISE TO HOST (Mac/PC/Linux) ===
        item {
            Spacer(Modifier.height(16.dp))
            SectionTitle("CONNECT TO COMPUTER")
        }

        item {
            Text(
                "Advertise as a BLE MIDI controller so your Mac, Windows, or Linux can connect to this device.",
                color = GearBoardColors.TextDisabled,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }

        // Peripheral state card
        item {
            PeripheralStateCard(
                state = peripheralState,
                onStart = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        advertisePermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.BLUETOOTH_ADVERTISE,
                                Manifest.permission.BLUETOOTH_CONNECT
                            )
                        )
                    } else {
                        viewModel.startAdvertising()
                    }
                },
                onStop = { viewModel.stopAdvertising() }
            )
        }

        // Instructions
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(GearBoardColors.Surface)
                    .border(1.dp, GearBoardColors.BorderDefault, RoundedCornerShape(8.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("HOW TO CONNECT", color = GearBoardColors.Accent, fontSize = 11.sp, letterSpacing = 2.sp, fontWeight = FontWeight.Bold)
                Text("macOS: Audio MIDI Setup → Bluetooth → Connect", color = GearBoardColors.TextSecondary, fontSize = 11.sp)
                Text("Windows: Use a BLE MIDI driver (e.g. MIDIberry)", color = GearBoardColors.TextSecondary, fontSize = 11.sp)
                Text("Linux: Use bluez with BLE MIDI support", color = GearBoardColors.TextSecondary, fontSize = 11.sp)
            }
        }
    }

    // BLE Scan Bottom Sheet
    if (showBleScanSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.stopBleScan() },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = GearBoardColors.Surface,
            contentColor = GearBoardColors.TextPrimary
        ) {
            BleScanContent(
                devices = bleDevices,
                isScanning = isScanning,
                onDeviceClick = { viewModel.connectBle(it) },
                onClose = { viewModel.stopBleScan() }
            )
        }
    }
}

@Composable
private fun ConnectionStatusCard(
    state: ConnectionState,
    onDisconnect: () -> Unit
) {
    val (bgColor, textColor, statusText) = when (state) {
        is ConnectionState.Disconnected -> Triple(GearBoardColors.Surface, GearBoardColors.TextSecondary, "Not Connected")
        is ConnectionState.Scanning -> Triple(GearBoardColors.Surface, GearBoardColors.Accent, "Scanning...")
        is ConnectionState.Connecting -> Triple(GearBoardColors.Surface, GearBoardColors.Accent, "Connecting to ${state.deviceName}...")
        is ConnectionState.Connected -> Triple(
            GearBoardColors.SurfaceVariant,
            if (state.type == ConnectionType.USB) GearBoardColors.ConnectedUsb else GearBoardColors.ConnectedBle,
            "${state.deviceName} • ${state.type.name}"
        )
        is ConnectionState.Error -> Triple(GearBoardColors.DangerBackground, GearBoardColors.DangerText, state.message)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, GearBoardColors.BorderDefault, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "CONNECTION STATUS",
                    color = GearBoardColors.TextSecondary,
                    fontSize = 10.sp,
                    letterSpacing = 2.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = statusText,
                    color = textColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            if (state is ConnectionState.Connected) {
                IconButton(onClick = onDisconnect) {
                    Icon(
                        Icons.Default.LinkOff,
                        contentDescription = "Disconnect",
                        tint = GearBoardColors.DangerText
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = GearBoardColors.TextSecondary,
        fontSize = 12.sp,
        letterSpacing = 2.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun EmptyStateMessage(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    message: String,
    subMessage: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(GearBoardColors.Surface)
            .border(1.dp, GearBoardColors.BorderDefault, RoundedCornerShape(8.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, null, tint = GearBoardColors.TextDisabled, modifier = Modifier.size(32.dp))
        Text(message, color = GearBoardColors.TextSecondary, fontSize = 14.sp)
        Text(subMessage, color = GearBoardColors.TextDisabled, fontSize = 12.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun DeviceCard(
    name: String,
    type: ConnectionType,
    isConnected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isConnected) GearBoardColors.SurfaceVariant else GearBoardColors.Surface)
            .border(
                width = 1.dp,
                color = if (isConnected) GearBoardColors.ConnectedUsb else GearBoardColors.BorderDefault,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(enabled = !isConnected, onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = if (type == ConnectionType.USB) Icons.Default.Usb else Icons.Default.Bluetooth,
            contentDescription = null,
            tint = if (isConnected) GearBoardColors.ConnectedUsb else GearBoardColors.Accent,
            modifier = Modifier.size(24.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(name, color = GearBoardColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(type.name, color = GearBoardColors.TextSecondary, fontSize = 11.sp, letterSpacing = 1.sp)
        }
        if (isConnected) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(GearBoardColors.ConnectedUsb)
            )
        }
    }
}

@Composable
private fun BleScanContent(
    devices: List<BleMidiScanner.BleDevice>,
    isScanning: Boolean,
    onDeviceClick: (BleMidiScanner.BleDevice) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "BLE MIDI DEVICES",
                color = GearBoardColors.Accent,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, "Close", tint = GearBoardColors.TextSecondary)
            }
        }

        Spacer(Modifier.height(8.dp))

        // Scanning indicator
        if (isScanning) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = GearBoardColors.Accent,
                    strokeWidth = 2.dp
                )
                Spacer(Modifier.width(8.dp))
                Text("Scanning...", color = GearBoardColors.TextSecondary, fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(12.dp))

        // Device list
        if (devices.isEmpty()) {
            Text(
                text = if (isScanning) "Searching for devices..." else "No BLE MIDI devices found",
                color = GearBoardColors.TextDisabled,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                textAlign = TextAlign.Center
            )
        } else {
            devices.forEach { device ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(GearBoardColors.SurfaceVariant)
                        .clickable { onDeviceClick(device) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Default.Bluetooth,
                        null,
                        tint = GearBoardColors.ConnectedBle,
                        modifier = Modifier.size(20.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(device.name, color = GearBoardColors.TextPrimary, fontSize = 14.sp)
                        Text(device.address, color = GearBoardColors.TextDisabled, fontSize = 10.sp)
                    }
                    // RSSI signal indicator
                    Icon(
                        Icons.Default.SignalWifi4Bar,
                        "Signal",
                        tint = when {
                            device.rssi > -50 -> GearBoardColors.ConnectedUsb
                            device.rssi > -70 -> GearBoardColors.Accent
                            else -> GearBoardColors.DangerText
                        },
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun PeripheralStateCard(
    state: BleMidiPeripheral.PeripheralState,
    onStart: () -> Unit,
    onStop: () -> Unit
) {
    val (bgColor, statusText, dotColor) = when (state) {
        is BleMidiPeripheral.PeripheralState.Idle -> Triple(
            GearBoardColors.Surface, "Ready to advertise", GearBoardColors.TextDisabled
        )
        is BleMidiPeripheral.PeripheralState.Advertising -> Triple(
            GearBoardColors.Surface, "Advertising... waiting for host", GearBoardColors.Accent
        )
        is BleMidiPeripheral.PeripheralState.Connected -> Triple(
            GearBoardColors.SurfaceVariant, "Connected: ${state.deviceName}", GearBoardColors.ConnectedBle
        )
        is BleMidiPeripheral.PeripheralState.Error -> Triple(
            GearBoardColors.DangerBackground, state.message, GearBoardColors.DangerText
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, dotColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
            Text(statusText, color = dotColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }

        when (state) {
            is BleMidiPeripheral.PeripheralState.Idle,
            is BleMidiPeripheral.PeripheralState.Error -> {
                Button(
                    onClick = onStart,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GearBoardColors.ConnectedBle,
                        contentColor = GearBoardColors.TextPrimary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Bluetooth, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("ADVERTISE TO HOST", fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }
            is BleMidiPeripheral.PeripheralState.Advertising -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = GearBoardColors.Accent,
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Waiting for host...", color = GearBoardColors.TextSecondary, fontSize = 12.sp)
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = onStop,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GearBoardColors.SurfaceElevated,
                            contentColor = GearBoardColors.TextPrimary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("STOP", fontSize = 12.sp, letterSpacing = 1.sp)
                    }
                }
            }
            is BleMidiPeripheral.PeripheralState.Connected -> {
                Button(
                    onClick = onStop,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GearBoardColors.DangerBackground,
                        contentColor = GearBoardColors.DangerText
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.LinkOff, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("DISCONNECT HOST", fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }
        }
    }
}
