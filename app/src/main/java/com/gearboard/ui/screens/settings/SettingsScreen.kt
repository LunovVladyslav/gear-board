package com.gearboard.ui.screens.settings

import android.Manifest
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.gearboard.ui.components.GearBoardTopBar
import com.gearboard.ui.theme.GearBoardColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onOpenMonitor: () -> Unit = {},
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    connectionViewModel: ConnectionViewModel = hiltViewModel()
) {
    // Settings state
    val sendOnChange by settingsViewModel.sendOnChange.collectAsStateWithLifecycle()
    val controlSize by settingsViewModel.controlSize.collectAsStateWithLifecycle()
    val hapticEnabled by settingsViewModel.hapticEnabled.collectAsStateWithLifecycle()
    val keepScreenOn by settingsViewModel.keepScreenOn.collectAsStateWithLifecycle()
    val midiChannel by settingsViewModel.midiChannel.collectAsStateWithLifecycle()
    val isPremium by settingsViewModel.isPremium.collectAsStateWithLifecycle()
    val autoReconnect by settingsViewModel.autoReconnect.collectAsStateWithLifecycle()

    var showChannelMenu by remember { mutableStateOf(false) }

    // Connection state
    val connectionState by connectionViewModel.connectionState.collectAsStateWithLifecycle()
    val allDevices by connectionViewModel.availableDevices.collectAsStateWithLifecycle()
    val usbDevices = allDevices.filter { connectionViewModel.getDeviceType(it) == ConnectionType.USB }
    val peripheralState by connectionViewModel.peripheralState.collectAsStateWithLifecycle()
    val showBleScanSheet by connectionViewModel.showBleScanSheet.collectAsStateWithLifecycle()
    val isScanning by connectionViewModel.isScanning.collectAsStateWithLifecycle()
    val bleDevices by connectionViewModel.discoveredBleDevices.collectAsStateWithLifecycle()

    // Permission launchers
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        connectionViewModel.setPermissionsGranted(allGranted)
        if (allGranted) connectionViewModel.startBleScan()
    }

    val advertisePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { it }) connectionViewModel.startAdvertising()
    }

    Scaffold(
        topBar = { GearBoardTopBar() }
    ) { innerPadding ->
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GearBoardColors.Background)
            .padding(innerPadding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ═══════════════════════════════════════
        // CONNECTION SECTION
        // ═══════════════════════════════════════
        item { SectionLabel("CONNECTION") }

        // Connection status card
        item { ConnectionStatusCard(connectionState) { connectionViewModel.disconnect() } }

        // --- Bluetooth ---
        item { SubSectionLabel("Connect to Bluetooth device") }

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
                        permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
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
                Icon(Icons.Default.Bluetooth, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    "CONNECT TO BLUETOOTH DEVICE",
                    fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp
                )
            }
        }

        // --- Let your computer find this phone ---
        item { SubSectionLabel("Let your computer find this phone") }

        item {
            PeripheralCard(
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
                        connectionViewModel.startAdvertising()
                    }
                },
                onStop = { connectionViewModel.stopAdvertising() }
            )
        }

        // Step-by-step instructions
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
                Text(
                    "HOW TO CONNECT VIA BLUETOOTH",
                    color = GearBoardColors.Accent, fontSize = 10.sp,
                    letterSpacing = 2.sp, fontWeight = FontWeight.Bold
                )
                StepText("1. Tap \"Start Advertising\" above")
                StepText("2. On your Mac: Audio MIDI Setup → Bluetooth → find your phone")
                StepText("   On Windows: use loopMIDI + MIDIberry")
                StepText("3. Select your phone as MIDI input in your DAW")
            }
        }

        // --- USB ---
        item { SubSectionLabel("USB Connection") }

        if (usbDevices.isEmpty()) {
            item { EmptyHint(Icons.Default.Usb, "No USB devices connected", "Connect your computer with a USB cable") }
        } else {
            items(usbDevices, key = { it.id }) { device ->
                DeviceRow(
                    name = connectionViewModel.getDeviceName(device),
                    type = connectionViewModel.getDeviceType(device),
                    isConnected = connectionState is ConnectionState.Connected &&
                            (connectionState as? ConnectionState.Connected)?.deviceName == connectionViewModel.getDeviceName(device),
                    onClick = { connectionViewModel.connectUsb(device) }
                )
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Button(
                    onClick = { connectionViewModel.refreshDevices() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GearBoardColors.SurfaceElevated,
                        contentColor = GearBoardColors.TextPrimary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Refresh, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("REFRESH USB", fontSize = 11.sp, letterSpacing = 1.sp)
                }
            }
        }

        // Auto-reconnect toggle
        item {
            ToggleSettingRow(
                title = "Auto Reconnect",
                subtitle = "Reconnect to last device on startup",
                checked = autoReconnect,
                onCheckedChange = { settingsViewModel.setAutoReconnect(it) }
            )
        }

        // ═══════════════════════════════════════
        // MIDI SECTION
        // ═══════════════════════════════════════
        item { Spacer(Modifier.height(4.dp)); SectionLabel("MIDI") }

        item {
            ToggleSettingRow(
                title = "Send on Change",
                subtitle = "Send MIDI CC when knob values change",
                checked = sendOnChange,
                onCheckedChange = { settingsViewModel.setSendOnChange(it) }
            )
        }

        item {
            Box {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(GearBoardColors.Surface)
                        .border(1.dp, GearBoardColors.BorderDefault, RoundedCornerShape(8.dp))
                        .clickable { showChannelMenu = true }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("MIDI Channel", color = GearBoardColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Text("Global channel for all controls", color = GearBoardColors.TextDisabled, fontSize = 12.sp)
                    }
                    Text(
                        if (midiChannel == 0) "OMNI" else "CH $midiChannel",
                        color = GearBoardColors.Accent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                DropdownMenu(
                    expanded = showChannelMenu,
                    onDismissRequest = { showChannelMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("OMNI", color = if (midiChannel == 0) GearBoardColors.Accent else GearBoardColors.TextPrimary, fontSize = 13.sp) },
                        onClick = { settingsViewModel.setMidiChannel(0); showChannelMenu = false }
                    )
                    (1..16).forEach { ch ->
                        DropdownMenuItem(
                            text = { Text("CH $ch", color = if (midiChannel == ch) GearBoardColors.Accent else GearBoardColors.TextPrimary, fontSize = 13.sp) },
                            onClick = { settingsViewModel.setMidiChannel(ch); showChannelMenu = false }
                        )
                    }
                }
            }
        }

        // MIDI Monitor link
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(GearBoardColors.Surface)
                    .border(1.dp, GearBoardColors.BorderDefault, RoundedCornerShape(8.dp))
                    .clickable(onClick = onOpenMonitor)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Default.Sensors, null, tint = GearBoardColors.Accent, modifier = Modifier.size(20.dp))
                    Column {
                        Text("MIDI Monitor", color = GearBoardColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Text("View live MIDI messages", color = GearBoardColors.TextDisabled, fontSize = 12.sp)
                    }
                }
                Icon(Icons.Default.ChevronRight, null, tint = GearBoardColors.TextSecondary, modifier = Modifier.size(20.dp))
            }
        }

        // ═══════════════════════════════════════
        // DISPLAY SECTION
        // ═══════════════════════════════════════
        item { Spacer(Modifier.height(4.dp)); SectionLabel("DISPLAY") }

        item {
            SettingRow(title = "Control Size", subtitle = "${(controlSize * 100).toInt()}%") {
                Slider(
                    value = controlSize,
                    onValueChange = { settingsViewModel.setControlSize(it) },
                    valueRange = 0.8f..1.4f,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = GearBoardColors.Accent,
                        activeTrackColor = GearBoardColors.Accent,
                        inactiveTrackColor = GearBoardColors.SurfaceElevated
                    )
                )
            }
        }

        // ═══════════════════════════════════════
        // INTERACTION SECTION
        // ═══════════════════════════════════════
        item { Spacer(Modifier.height(4.dp)); SectionLabel("INTERACTION") }

        item {
            ToggleSettingRow(
                title = "Haptic Feedback",
                subtitle = "Vibrate when turning knobs",
                checked = hapticEnabled,
                onCheckedChange = { settingsViewModel.setHapticEnabled(it) }
            )
        }

        item {
            ToggleSettingRow(
                title = "Keep Screen On",
                subtitle = "Prevent screen timeout during use",
                checked = keepScreenOn,
                onCheckedChange = { settingsViewModel.setKeepScreenOn(it) }
            )
        }

        // ═══════════════════════════════════════
        // ABOUT SECTION
        // ═══════════════════════════════════════
        item { Spacer(Modifier.height(4.dp)); SectionLabel("ABOUT") }

        // Premium
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(GearBoardColors.Surface)
                    .border(1.dp, GearBoardColors.BorderDefault, RoundedCornerShape(8.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        if (isPremium) "Premium Active ✓" else "GearBoard Free",
                        color = if (isPremium) GearBoardColors.ConnectedUsb else GearBoardColors.TextPrimary,
                        fontSize = 14.sp, fontWeight = FontWeight.Medium
                    )
                    Text(
                        if (isPremium) "All features unlocked" else "3 presets • 1 pedal • 2 effects",
                        color = GearBoardColors.TextDisabled, fontSize = 12.sp
                    )
                }
                if (!isPremium) {
                    Button(
                        onClick = { /* TODO: Google Play Billing */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GearBoardColors.Accent,
                            contentColor = GearBoardColors.TextOnAccent
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) { Text("$4.99", fontWeight = FontWeight.Bold) }
                }
            }
        }

        // Reset
        item {
            Button(
                onClick = { settingsViewModel.resetToDefaults() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GearBoardColors.DangerBackground,
                    contentColor = GearBoardColors.DangerText
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("RESET TO DEFAULTS", fontSize = 12.sp, letterSpacing = 1.sp)
            }
        }

        // Version
        item {
            Text(
                "GearBoard v1.0.0",
                color = GearBoardColors.TextDisabled,
                fontSize = 11.sp,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
        }
    }

    // BLE Scan Bottom Sheet
    if (showBleScanSheet) {
        ModalBottomSheet(
            onDismissRequest = { connectionViewModel.stopBleScan() },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = GearBoardColors.Surface,
            contentColor = GearBoardColors.TextPrimary
        ) {
            BleScanContent(
                devices = bleDevices,
                isScanning = isScanning,
                onDeviceClick = { connectionViewModel.connectBle(it) },
                onClose = { connectionViewModel.stopBleScan() }
            )
        }
    }
    } // end Scaffold
}

// ═══════════════════════════════════════
// Reusable composables
// ═══════════════════════════════════════

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        color = GearBoardColors.TextSecondary,
        fontSize = 12.sp, letterSpacing = 2.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun SubSectionLabel(text: String) {
    Text(
        text = text,
        color = GearBoardColors.TextPrimary,
        fontSize = 13.sp, fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Composable
private fun StepText(text: String) {
    Text(text, color = GearBoardColors.TextSecondary, fontSize = 11.sp, lineHeight = 16.sp)
}

@Composable
private fun SettingRow(title: String, subtitle: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(GearBoardColors.Surface)
            .border(1.dp, GearBoardColors.BorderDefault, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(title, color = GearBoardColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Text(subtitle, color = GearBoardColors.TextDisabled, fontSize = 12.sp)
        content()
    }
}

@Composable
private fun ToggleSettingRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(GearBoardColors.Surface)
            .border(1.dp, GearBoardColors.BorderDefault, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = GearBoardColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(subtitle, color = GearBoardColors.TextDisabled, fontSize = 12.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = GearBoardColors.TextOnAccent,
                checkedTrackColor = GearBoardColors.Accent,
                uncheckedThumbColor = GearBoardColors.TextSecondary,
                uncheckedTrackColor = GearBoardColors.SurfaceElevated
            )
        )
    }
}

@Composable
private fun ConnectionStatusCard(state: ConnectionState, onDisconnect: () -> Unit) {
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
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(1.dp, textColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("CONNECTION STATUS", color = GearBoardColors.TextSecondary, fontSize = 10.sp, letterSpacing = 2.sp)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier.size(8.dp).clip(CircleShape).background(textColor)
                    )
                    Text(statusText, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
            if (state is ConnectionState.Connected) {
                Button(
                    onClick = onDisconnect,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GearBoardColors.DangerBackground,
                        contentColor = GearBoardColors.DangerText
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.LinkOff, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Disconnect", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun PeripheralCard(
    state: BleMidiPeripheral.PeripheralState,
    onStart: () -> Unit,
    onStop: () -> Unit
) {
    val (bgColor, statusText, dotColor) = when (state) {
        is BleMidiPeripheral.PeripheralState.Idle -> Triple(GearBoardColors.Surface, "Ready", GearBoardColors.TextDisabled)
        is BleMidiPeripheral.PeripheralState.Advertising -> Triple(GearBoardColors.Surface, "Waiting for computer...", GearBoardColors.Accent)
        is BleMidiPeripheral.PeripheralState.Connected -> Triple(GearBoardColors.SurfaceVariant, "Connected: ${state.deviceName}", GearBoardColors.ConnectedBle)
        is BleMidiPeripheral.PeripheralState.Error -> Triple(GearBoardColors.DangerBackground, state.message, GearBoardColors.DangerText)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, dotColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(dotColor))
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
                    Text("START ADVERTISING", fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }
            is BleMidiPeripheral.PeripheralState.Advertising -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = GearBoardColors.Accent, strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp))
                    Text("Waiting for computer...", color = GearBoardColors.TextSecondary, fontSize = 12.sp)
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = onStop,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GearBoardColors.SurfaceElevated,
                            contentColor = GearBoardColors.TextPrimary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) { Text("STOP", fontSize = 11.sp, letterSpacing = 1.sp) }
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
                    Text("DISCONNECT", fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }
            }
        }
    }
}

@Composable
private fun DeviceRow(name: String, type: ConnectionType, isConnected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isConnected) GearBoardColors.SurfaceVariant else GearBoardColors.Surface)
            .border(
                1.dp,
                if (isConnected) GearBoardColors.ConnectedUsb else GearBoardColors.BorderDefault,
                RoundedCornerShape(8.dp)
            )
            .clickable(enabled = !isConnected, onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            if (type == ConnectionType.USB) Icons.Default.Usb else Icons.Default.Bluetooth,
            null,
            tint = if (isConnected) GearBoardColors.ConnectedUsb else GearBoardColors.Accent,
            modifier = Modifier.size(22.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(name, color = GearBoardColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(type.name, color = GearBoardColors.TextSecondary, fontSize = 11.sp, letterSpacing = 1.sp)
        }
        if (isConnected) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(GearBoardColors.ConnectedUsb))
        }
    }
}

@Composable
private fun EmptyHint(icon: ImageVector, message: String, subMessage: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(GearBoardColors.Surface)
            .border(1.dp, GearBoardColors.BorderDefault, RoundedCornerShape(8.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(icon, null, tint = GearBoardColors.TextDisabled, modifier = Modifier.size(28.dp))
        Text(message, color = GearBoardColors.TextSecondary, fontSize = 13.sp)
        Text(subMessage, color = GearBoardColors.TextDisabled, fontSize = 11.sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun BleScanContent(
    devices: List<BleMidiScanner.BleDevice>,
    isScanning: Boolean,
    onDeviceClick: (BleMidiScanner.BleDevice) -> Unit,
    onClose: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("BLUETOOTH DEVICES", color = GearBoardColors.Accent, fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, "Close", tint = GearBoardColors.TextSecondary)
            }
        }

        Spacer(Modifier.height(8.dp))

        if (isScanning) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = GearBoardColors.Accent, strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
                Text("Searching...", color = GearBoardColors.TextSecondary, fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(12.dp))

        if (devices.isEmpty()) {
            Text(
                if (isScanning) "Searching for devices..." else "No Bluetooth MIDI devices found",
                color = GearBoardColors.TextDisabled, fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
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
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Default.Bluetooth, null, tint = GearBoardColors.ConnectedBle, modifier = Modifier.size(20.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(device.name, color = GearBoardColors.TextPrimary, fontSize = 14.sp)
                        Text(device.address, color = GearBoardColors.TextDisabled, fontSize = 10.sp)
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}
