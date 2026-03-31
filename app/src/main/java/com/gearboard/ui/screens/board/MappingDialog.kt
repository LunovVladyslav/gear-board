package com.gearboard.ui.screens.board

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.domain.model.ControlType
import com.gearboard.ui.theme.GearBoardColors

/**
 * MappingDialog — single-control mapping dialog.
 * Shows instructions for MIDI Learn and allows manual CC number entry.
 */
@Composable
fun MappingDialog(
    control: ControlType,
    onSave: (ccNumber: Int) -> Unit,
    onLearn: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    val controlLabel = control.label
    val currentCc = when (control) {
        is ControlType.Knob -> control.ccNumber
        is ControlType.Toggle -> control.ccNumber
        is ControlType.Tap -> control.ccNumber
        is ControlType.Selector -> control.ccNumber
        is ControlType.Fader -> control.ccNumber
        else -> 0
    }

    var ccText by remember { mutableStateOf(if (currentCc > 0) currentCc.toString() else "") }
    val ccNum = ccText.toIntOrNull()
    val isValid = ccNum != null && ccNum in 1..127

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = GearBoardColors.TextPrimary,
        unfocusedTextColor = GearBoardColors.TextPrimary,
        focusedBorderColor = GearBoardColors.Accent,
        unfocusedBorderColor = GearBoardColors.BorderDefault,
        cursorColor = GearBoardColors.Accent,
        focusedLabelColor = GearBoardColors.Accent,
        unfocusedLabelColor = GearBoardColors.TextSecondary,
        errorBorderColor = GearBoardColors.DangerText,
        focusedContainerColor = GearBoardColors.SurfaceVariant,
        unfocusedContainerColor = GearBoardColors.SurfaceVariant
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GearBoardColors.Surface,
        title = {
            Text(
                "MAP: ${controlLabel.uppercase()}",
                color = GearBoardColors.Accent,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Enter the CC number for this control:",
                    color = GearBoardColors.TextPrimary,
                    fontSize = 13.sp
                )

                OutlinedTextField(
                    value = ccText,
                    onValueChange = { ccText = it.filter { c -> c.isDigit() }.take(3) },
                    label = { Text("CC NUMBER (1-127)") },
                    isError = ccText.isNotEmpty() && !isValid,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = textFieldColors,
                    modifier = Modifier.fillMaxWidth()
                )

                if (onLearn != null) {
                    Button(
                        onClick = onLearn,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GearBoardColors.SurfaceElevated,
                            contentColor = GearBoardColors.Accent
                        )
                    ) {
                        Icon(Icons.Default.Sensors, null, modifier = androidx.compose.ui.Modifier.size(16.dp))
                        androidx.compose.foundation.layout.Spacer(androidx.compose.ui.Modifier.size(8.dp))
                        Text("MIDI LEARN", fontSize = 12.sp, letterSpacing = 1.sp)
                    }
                }

                Text(
                    "Tip: Tap MIDI Learn, then move the control in your plugin.",
                    color = GearBoardColors.TextDisabled,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (isValid) onSave(ccNum!!) },
                enabled = isValid
            ) {
                Text(
                    "SAVE",
                    color = if (isValid) GearBoardColors.Accent else GearBoardColors.TextDisabled,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = GearBoardColors.TextSecondary)
            }
        }
    )
}

/**
 * Composable that overlays an amber "!" badge on unmapped controls (ccNumber == 0).
 * Tapping the badge triggers [onBadgeTap].
 */
@Composable
fun UnmappedBadge(
    control: ControlType,
    onBadgeTap: () -> Unit,
    content: @Composable () -> Unit
) {
    val ccNumber = when (control) {
        is ControlType.Knob -> control.ccNumber
        is ControlType.Toggle -> control.ccNumber
        is ControlType.Tap -> control.ccNumber
        is ControlType.Selector -> control.ccNumber
        is ControlType.Fader -> control.ccNumber
        is ControlType.PresetNav -> 1 // PresetNav uses PC, not CC — never "unmapped"
        is ControlType.Pad -> 1       // Pad uses note, not CC
    }

    Box {
        content()

        if (ccNumber == 0) {
            Text(
                text = "!",
                color = GearBoardColors.TextOnAccent,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .offset(x = 4.dp, y = (-4).dp)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(com.gearboard.ui.theme.LocalAccentColor.current)
                    .clickable(onClick = onBadgeTap)
                    .padding(top = 1.dp)
                    .align(androidx.compose.ui.Alignment.TopEnd)
            )
        }
    }
}
