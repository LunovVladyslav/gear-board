package com.gearboard.ui.screens.board

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.domain.model.AmpSettings
import com.gearboard.domain.model.CabinetSettings
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.FaderOrientation
import com.gearboard.ui.theme.GearBoardColors

enum class AmpTemplate {
    STANDARD;

    fun controls(): List<ControlType> = when (this) {
        STANDARD -> listOf(
            ControlType.Knob(label = "Gain", ccNumber = 0),
            ControlType.Knob(label = "Bass", ccNumber = 0),
            ControlType.Knob(label = "Mid", ccNumber = 0),
            ControlType.Knob(label = "Treble", ccNumber = 0),
            ControlType.Knob(label = "Presence", ccNumber = 0),
            ControlType.Knob(label = "Master", ccNumber = 0)
        )
    }
}

enum class CabTemplate {
    STANDARD;

    fun controls(): List<ControlType> = when (this) {
        STANDARD -> listOf(
            ControlType.Selector(label = "Model", ccNumber = 0, positions = listOf("4x12", "2x12", "1x12", "4x10")),
            ControlType.Selector(label = "Mic", ccNumber = 0, positions = listOf("SM57", "MD421", "Ribbon", "Condenser")),
            ControlType.Fader(label = "Position", ccNumber = 0, orientation = FaderOrientation.HORIZONTAL)
        )
    }
}

/**
 * AmpSection — displays amp controls using ControlRenderer.
 * Includes "+ ADD CONTROL" and "CLEAR ALL" buttons.
 */
@Composable
fun AmpSection(
    amp: AmpSettings,
    onKnobValueChange: (ControlType.Knob, Float) -> Unit,
    onToggle: (ControlType.Toggle) -> Unit,
    onTap: (ControlType.Tap) -> Unit,
    onSelectorChange: (ControlType.Selector, Int) -> Unit,
    onFaderValueChange: (ControlType.Fader, Float) -> Unit,
    onPresetPrev: (ControlType.PresetNav) -> Unit,
    onPresetNext: (ControlType.PresetNav) -> Unit,
    onPadDown: (ControlType.Pad) -> Unit,
    onPadUp: (ControlType.Pad) -> Unit,
    onEditControl: (ControlType) -> Unit,
    onAddControl: () -> Unit,
    onClearAll: () -> Unit,
    onCustomize: () -> Unit,
    onApplyAmpTemplate: (AmpTemplate) -> Unit = {},
    onBadgeTap: (ControlType) -> Unit = {},
    modifier: Modifier = Modifier,
    controlScale: Float = 1.0f
) {
    var showClearConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (amp.controls.isEmpty()) {
            // Empty state
            Text(
                "No amp controls yet",
                color = GearBoardColors.TextSecondary,
                fontSize = 12.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onApplyAmpTemplate(AmpTemplate.STANDARD) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GearBoardColors.Accent,
                        contentColor = GearBoardColors.TextOnAccent
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Standard Amp", fontSize = 10.sp, letterSpacing = 0.5.sp)
                }
                Button(
                    onClick = onAddControl,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GearBoardColors.SurfaceElevated,
                        contentColor = GearBoardColors.Accent
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.padding(start = 4.dp))
                    Text("Add Control", fontSize = 10.sp, letterSpacing = 0.5.sp)
                }
            }
        } else {
            RenderControlList(
                controls = amp.controls,
                onKnobValueChange = onKnobValueChange,
                onToggle = onToggle,
                onTap = onTap,
                onSelectorChange = onSelectorChange,
                onFaderValueChange = onFaderValueChange,
                onPresetPrev = onPresetPrev,
                onPresetNext = onPresetNext,
                onPadDown = onPadDown,
                onPadUp = onPadUp,
                onLongPress = onEditControl,
                onBadgeTap = onBadgeTap,
                enabled = amp.enabled,
                layoutMode = amp.layoutMode,
                controlScale = controlScale,
                modifier = Modifier.horizontalScroll(rememberScrollState())
            )

            // Buttons row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onAddControl,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GearBoardColors.SurfaceElevated,
                        contentColor = GearBoardColors.Accent
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.padding(start = 4.dp))
                    Text("ADD", fontSize = 10.sp, letterSpacing = 1.sp)
                }

                Button(
                    onClick = onCustomize,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GearBoardColors.SurfaceVariant,
                        contentColor = GearBoardColors.TextPrimary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("CUSTOMIZE", fontSize = 10.sp, letterSpacing = 1.sp)
                }

                Button(
                    onClick = { showClearConfirm = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GearBoardColors.DangerBackground,
                        contentColor = GearBoardColors.DangerText
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.DeleteSweep, null, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.padding(start = 4.dp))
                    Text("CLEAR", fontSize = 10.sp, letterSpacing = 1.sp)
                }
            }
        }
    }

    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            containerColor = GearBoardColors.Surface,
            title = { Text("Clear All Controls?", color = GearBoardColors.DangerText) },
            text = { Text("All amp controls will be removed.", color = GearBoardColors.TextPrimary) },
            confirmButton = {
                TextButton(onClick = { showClearConfirm = false; onClearAll() }) {
                    Text("CLEAR", color = GearBoardColors.DangerText)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) {
                    Text("CANCEL", color = GearBoardColors.TextSecondary)
                }
            }
        )
    }
}

/**
 * CabSection — displays cabinet controls using ControlRenderer.
 * Includes "+ ADD CONTROL" and "CLEAR ALL" buttons.
 */
@Composable
fun CabSection(
    cabinet: CabinetSettings,
    onKnobValueChange: (ControlType.Knob, Float) -> Unit,
    onToggle: (ControlType.Toggle) -> Unit,
    onTap: (ControlType.Tap) -> Unit,
    onSelectorChange: (ControlType.Selector, Int) -> Unit,
    onFaderValueChange: (ControlType.Fader, Float) -> Unit,
    onPresetPrev: (ControlType.PresetNav) -> Unit,
    onPresetNext: (ControlType.PresetNav) -> Unit,
    onPadDown: (ControlType.Pad) -> Unit,
    onPadUp: (ControlType.Pad) -> Unit,
    onEditControl: (ControlType) -> Unit,
    onAddControl: () -> Unit,
    onClearAll: () -> Unit,
    onCustomize: () -> Unit,
    onApplyCabTemplate: (CabTemplate) -> Unit = {},
    onBadgeTap: (ControlType) -> Unit = {},
    modifier: Modifier = Modifier,
    controlScale: Float = 1.0f
) {
    var showClearConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (cabinet.controls.isEmpty()) {
            // Empty state
            Text(
                "No cabinet controls yet",
                color = GearBoardColors.TextSecondary,
                fontSize = 12.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onApplyCabTemplate(CabTemplate.STANDARD) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GearBoardColors.Accent,
                        contentColor = GearBoardColors.TextOnAccent
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Standard Cab", fontSize = 10.sp, letterSpacing = 0.5.sp)
                }
                Button(
                    onClick = onAddControl,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GearBoardColors.SurfaceElevated,
                        contentColor = GearBoardColors.Accent
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.padding(start = 4.dp))
                    Text("Add Control", fontSize = 10.sp, letterSpacing = 0.5.sp)
                }
            }
        } else {
            RenderControlList(
                controls = cabinet.controls,
                onKnobValueChange = onKnobValueChange,
                onToggle = onToggle,
                onTap = onTap,
                onSelectorChange = onSelectorChange,
                onFaderValueChange = onFaderValueChange,
                onPresetPrev = onPresetPrev,
                onPresetNext = onPresetNext,
                onPadDown = onPadDown,
                onPadUp = onPadUp,
                onLongPress = onEditControl,
                onBadgeTap = onBadgeTap,
                enabled = cabinet.enabled,
                layoutMode = cabinet.layoutMode,
                controlScale = controlScale,
                modifier = Modifier.horizontalScroll(rememberScrollState())
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onAddControl,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GearBoardColors.SurfaceElevated,
                        contentColor = GearBoardColors.Accent
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.padding(start = 4.dp))
                    Text("ADD", fontSize = 10.sp, letterSpacing = 1.sp)
                }

                Button(
                    onClick = onCustomize,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GearBoardColors.SurfaceVariant,
                        contentColor = GearBoardColors.TextPrimary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("CUSTOMIZE", fontSize = 10.sp, letterSpacing = 1.sp)
                }

                Button(
                    onClick = { showClearConfirm = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GearBoardColors.DangerBackground,
                        contentColor = GearBoardColors.DangerText
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.DeleteSweep, null, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.padding(start = 4.dp))
                    Text("CLEAR", fontSize = 10.sp, letterSpacing = 1.sp)
                }
            }
        }
    }

    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            containerColor = GearBoardColors.Surface,
            title = { Text("Clear All Controls?", color = GearBoardColors.DangerText) },
            text = { Text("All cabinet controls will be removed.", color = GearBoardColors.TextPrimary) },
            confirmButton = {
                TextButton(onClick = { showClearConfirm = false; onClearAll() }) {
                    Text("CLEAR", color = GearBoardColors.DangerText)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) {
                    Text("CANCEL", color = GearBoardColors.TextSecondary)
                }
            }
        )
    }
}
