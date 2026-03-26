package com.gearboard.ui.screens.board

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.gearboard.ui.theme.GearBoardColors

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
    modifier: Modifier = Modifier,
    controlScale: Float = 1.0f
) {
    var showClearConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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
            enabled = amp.enabled,
            controlScale = controlScale
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
                Icon(Icons.Default.Add, null, modifier = Modifier.size(14.dp))
                Spacer(Modifier.padding(start = 4.dp))
                Text("ADD CONTROL", fontSize = 10.sp, letterSpacing = 1.sp)
            }

            if (amp.controls.isNotEmpty()) {
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
    modifier: Modifier = Modifier,
    controlScale: Float = 1.0f
) {
    var showClearConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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
            enabled = cabinet.enabled,
            controlScale = controlScale
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
                Icon(Icons.Default.Add, null, modifier = Modifier.size(14.dp))
                Spacer(Modifier.padding(start = 4.dp))
                Text("ADD CONTROL", fontSize = 10.sp, letterSpacing = 1.sp)
            }

            if (cabinet.controls.isNotEmpty()) {
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
