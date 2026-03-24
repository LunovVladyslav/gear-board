package com.gearboard.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gearboard.ui.theme.GearBoardColors

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val midiChannel by viewModel.midiChannel.collectAsStateWithLifecycle()
    val sendOnChange by viewModel.sendOnChange.collectAsStateWithLifecycle()
    val controlSize by viewModel.controlSize.collectAsStateWithLifecycle()
    val hapticEnabled by viewModel.hapticEnabled.collectAsStateWithLifecycle()
    val keepScreenOn by viewModel.keepScreenOn.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()
    val autoReconnect by viewModel.autoReconnect.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GearBoardColors.Background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                "SETTINGS",
                color = GearBoardColors.Accent,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )
        }

        // === MIDI Section ===
        item { SectionLabel("MIDI") }

        item {
            SettingRow(title = "MIDI Channel", subtitle = "Channel ${midiChannel + 1}") {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    (0..15).forEach { ch ->
                        val isSelected = midiChannel == ch
                        Text(
                            text = "${ch + 1}",
                            color = if (isSelected) GearBoardColors.TextOnAccent else GearBoardColors.TextSecondary,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isSelected) GearBoardColors.Accent else GearBoardColors.SurfaceElevated)
                                .clickable { viewModel.setMidiChannel(ch) }
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }

        item {
            ToggleSettingRow(
                title = "Send on Change",
                subtitle = "Send MIDI CC when knob values change",
                checked = sendOnChange,
                onCheckedChange = { viewModel.setSendOnChange(it) }
            )
        }

        item {
            ToggleSettingRow(
                title = "Auto Reconnect",
                subtitle = "Reconnect to last device on startup",
                checked = autoReconnect,
                onCheckedChange = { viewModel.setAutoReconnect(it) }
            )
        }

        // === UI Section ===
        item { Spacer(Modifier.height(4.dp)); SectionLabel("INTERFACE") }

        item {
            SettingRow(
                title = "Control Size",
                subtitle = "${(controlSize * 100).toInt()}%"
            ) {
                Slider(
                    value = controlSize,
                    onValueChange = { viewModel.setControlSize(it) },
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

        item {
            ToggleSettingRow(
                title = "Haptic Feedback",
                subtitle = "Vibrate when turning knobs",
                checked = hapticEnabled,
                onCheckedChange = { viewModel.setHapticEnabled(it) }
            )
        }

        item {
            ToggleSettingRow(
                title = "Keep Screen On",
                subtitle = "Prevent screen timeout during use",
                checked = keepScreenOn,
                onCheckedChange = { viewModel.setKeepScreenOn(it) }
            )
        }

        // === Premium Section ===
        item { Spacer(Modifier.height(4.dp)); SectionLabel("PREMIUM") }

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
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        if (isPremium) "All features unlocked" else "3 presets • 1 pedal • 2 effects",
                        color = GearBoardColors.TextDisabled,
                        fontSize = 12.sp
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
                    ) {
                        Text("$4.99", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // === Reset ===
        item { Spacer(Modifier.height(12.dp)) }

        item {
            Button(
                onClick = { viewModel.resetToDefaults() },
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        color = GearBoardColors.TextSecondary,
        fontSize = 12.sp,
        letterSpacing = 2.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun SettingRow(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
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
