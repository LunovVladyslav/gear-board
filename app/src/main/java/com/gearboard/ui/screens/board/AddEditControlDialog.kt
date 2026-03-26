package com.gearboard.ui.screens.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.DisplayFormat
import com.gearboard.domain.model.FaderOrientation
import com.gearboard.ui.theme.GearBoardColors

private enum class ControlKind(val display: String) {
    KNOB("Knob"),
    TOGGLE("Toggle"),
    TAP("Tap"),
    SELECTOR("Selector"),
    FADER("Fader"),
    PAD("Pad"),
    PRESET_NAV("Preset Nav")
}

/**
 * AddEditControlDialog — dialog for adding or editing a control.
 * Shows type selection, label, CC number, channel, and type-specific fields.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditControlDialog(
    existingControl: ControlType? = null,
    onConfirm: (ControlType) -> Unit,
    onDelete: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    val isEditMode = existingControl != null
    val title = if (isEditMode) "EDIT CONTROL" else "ADD CONTROL"

    // Derive initial values from existing control
    var selectedKind by remember {
        mutableStateOf(
            when (existingControl) {
                is ControlType.Knob -> ControlKind.KNOB
                is ControlType.Toggle -> ControlKind.TOGGLE
                is ControlType.Tap -> ControlKind.TAP
                is ControlType.Selector -> ControlKind.SELECTOR
                is ControlType.Fader -> ControlKind.FADER
                is ControlType.Pad -> ControlKind.PAD
                is ControlType.PresetNav -> ControlKind.PRESET_NAV
                null -> ControlKind.KNOB
            }
        )
    }
    var label by remember {
        mutableStateOf(existingControl?.label ?: "")
    }
    var ccNumber by remember {
        mutableStateOf(
            when (existingControl) {
                is ControlType.Knob -> existingControl.ccNumber.toString()
                is ControlType.Toggle -> existingControl.ccNumber.toString()
                is ControlType.Tap -> existingControl.ccNumber.toString()
                is ControlType.Selector -> existingControl.ccNumber.toString()
                is ControlType.Fader -> existingControl.ccNumber.toString()
                else -> "0"
            }
        )
    }
    var midiChannel by remember {
        mutableIntStateOf(existingControl?.midiChannel ?: 1)
    }

    // Selector-specific
    var positionsText by remember {
        mutableStateOf(
            (existingControl as? ControlType.Selector)?.positions?.joinToString(", ") ?: "A, B"
        )
    }

    // Pad-specific
    var noteNumber by remember {
        mutableStateOf(
            (existingControl as? ControlType.Pad)?.noteNumber?.toString() ?: "36"
        )
    }

    // Fader-specific
    var isHorizontal by remember {
        mutableStateOf(
            (existingControl as? ControlType.Fader)?.orientation == FaderOrientation.HORIZONTAL
        )
    }

    // Toggle-specific
    var pulseMode by remember {
        mutableStateOf(
            (existingControl as? ControlType.Toggle)?.pulseMode ?: true
        )
    }

    // Display Format (Knob/Fader)
    var displayFormat by remember {
        mutableStateOf(
            when (existingControl) {
                is ControlType.Knob -> existingControl.displayFormat
                is ControlType.Fader -> existingControl.displayFormat
                else -> DisplayFormat.ZERO_TO_TEN
            }
        )
    }

    // Validation
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val labelError = when {
        label.isBlank() -> "Required"
        label.length > 12 -> "Max 12 chars"
        else -> null
    }
    val ccNum = ccNumber.toIntOrNull()
    val ccError = when {
        selectedKind == ControlKind.PAD || selectedKind == ControlKind.PRESET_NAV -> null
        ccNum == null -> "Required"
        ccNum !in 0..127 -> "0-127"
        else -> null
    }
    val noteNum = noteNumber.toIntOrNull()
    val noteError = when {
        selectedKind != ControlKind.PAD -> null
        noteNum == null -> "Required"
        noteNum !in 0..127 -> "0-127"
        else -> null
    }
    val positions = positionsText.split(",").map { it.trim() }.filter { it.isNotBlank() }
    val posError = when {
        selectedKind != ControlKind.SELECTOR -> null
        positions.size < 2 -> "Min 2"
        positions.size > 6 -> "Max 6"
        else -> null
    }
    val isValid = labelError == null && ccError == null && noteError == null && posError == null

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = GearBoardColors.TextPrimary,
        unfocusedTextColor = GearBoardColors.TextPrimary,
        focusedBorderColor = GearBoardColors.Accent,
        unfocusedBorderColor = GearBoardColors.BorderDefault,
        cursorColor = GearBoardColors.Accent,
        focusedLabelColor = GearBoardColors.Accent,
        unfocusedLabelColor = GearBoardColors.TextSecondary,
        errorBorderColor = GearBoardColors.DangerText,
        errorLabelColor = GearBoardColors.DangerText,
        errorTextColor = GearBoardColors.TextPrimary,
        focusedContainerColor = GearBoardColors.SurfaceVariant,
        unfocusedContainerColor = GearBoardColors.SurfaceVariant,
        errorContainerColor = GearBoardColors.SurfaceVariant
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GearBoardColors.Surface,
        titleContentColor = GearBoardColors.Accent,
        title = {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Control type selector
                if (!isEditMode) {
                    Text(
                        "CONTROL TYPE",
                        color = GearBoardColors.TextSecondary,
                        fontSize = 10.sp,
                        letterSpacing = 1.5.sp
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ControlKind.entries.forEach { kind ->
                            val isSelected = selectedKind == kind
                            Text(
                                text = kind.display,
                                color = if (isSelected) GearBoardColors.TextOnAccent else GearBoardColors.TextPrimary,
                                fontSize = 11.sp,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (isSelected) GearBoardColors.Accent else GearBoardColors.SurfaceElevated
                                    )
                                    .border(
                                        1.dp,
                                        if (isSelected) GearBoardColors.Accent else GearBoardColors.BorderDefault,
                                        RoundedCornerShape(6.dp)
                                    )
                                    .clickable { selectedKind = kind }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                // Label
                OutlinedTextField(
                    value = label,
                    onValueChange = { if (it.length <= 12) label = it },
                    label = { Text("LABEL") },
                    isError = labelError != null,
                    supportingText = labelError?.let { { Text(it) } },
                    singleLine = true,
                    colors = textFieldColors,
                    modifier = Modifier.fillMaxWidth()
                )

                // CC Number (not for Pad, PresetNav)
                if (selectedKind != ControlKind.PAD && selectedKind != ControlKind.PRESET_NAV) {
                    OutlinedTextField(
                        value = ccNumber,
                        onValueChange = { ccNumber = it.filter { c -> c.isDigit() }.take(3) },
                        label = { Text("CC NUMBER") },
                        isError = ccError != null,
                        supportingText = ccError?.let { { Text(it) } },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = textFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Channel
                OutlinedTextField(
                    value = midiChannel.toString(),
                    onValueChange = {
                        val ch = it.filter { c -> c.isDigit() }.take(2).toIntOrNull()
                        if (ch != null && ch in 1..16) midiChannel = ch
                    },
                    label = { Text("CHANNEL (1-16)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = textFieldColors,
                    modifier = Modifier.fillMaxWidth()
                )

                // Type-specific fields
                when (selectedKind) {
                    ControlKind.SELECTOR -> {
                        OutlinedTextField(
                            value = positionsText,
                            onValueChange = { positionsText = it },
                            label = { Text("POSITIONS (comma separated)") },
                            isError = posError != null,
                            supportingText = posError?.let { { Text(it) } },
                            singleLine = true,
                            colors = textFieldColors,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    ControlKind.PAD -> {
                        OutlinedTextField(
                            value = noteNumber,
                            onValueChange = { noteNumber = it.filter { c -> c.isDigit() }.take(3) },
                            label = { Text("NOTE NUMBER (0-127)") },
                            isError = noteError != null,
                            supportingText = noteError?.let { { Text(it) } },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = textFieldColors,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    ControlKind.FADER -> {
                        Text(
                            "ORIENTATION",
                            color = GearBoardColors.TextSecondary,
                            fontSize = 10.sp,
                            letterSpacing = 1.5.sp
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(false to "Vertical", true to "Horizontal").forEach { (isHoriz, txt) ->
                                val isSel = isHorizontal == isHoriz
                                Text(
                                    text = txt,
                                    color = if (isSel) GearBoardColors.TextOnAccent else GearBoardColors.TextPrimary,
                                    fontSize = 11.sp,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            if (isSel) GearBoardColors.Accent else GearBoardColors.SurfaceElevated
                                        )
                                        .clickable { isHorizontal = isHoriz }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                    ControlKind.TOGGLE -> {
                        Text(
                            "MODE",
                            color = GearBoardColors.TextSecondary,
                            fontSize = 10.sp,
                            letterSpacing = 1.5.sp
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(true to "Pulse", false to "On/Off").forEach { (isPulse, txt) ->
                                val isSel = pulseMode == isPulse
                                Text(
                                    text = txt,
                                    color = if (isSel) GearBoardColors.TextOnAccent else GearBoardColors.TextPrimary,
                                    fontSize = 11.sp,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            if (isSel) GearBoardColors.Accent else GearBoardColors.SurfaceElevated
                                        )
                                        .clickable { pulseMode = isPulse }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                    else -> {}
                }

                // Display Format (for Knob and Fader)
                if (selectedKind == ControlKind.KNOB || selectedKind == ControlKind.FADER) {
                    Text(
                        "DISPLAY FORMAT",
                        color = GearBoardColors.TextSecondary,
                        fontSize = 10.sp,
                        letterSpacing = 1.5.sp
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        DisplayFormat.entries.forEach { fmt ->
                            val isSel = displayFormat == fmt
                            Text(
                                text = fmt.display,
                                color = if (isSel) GearBoardColors.TextOnAccent else GearBoardColors.TextPrimary,
                                fontSize = 11.sp,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (isSel) GearBoardColors.Accent else GearBoardColors.SurfaceElevated
                                    )
                                    .border(
                                        1.dp,
                                        if (isSel) GearBoardColors.Accent else GearBoardColors.BorderDefault,
                                        RoundedCornerShape(6.dp)
                                    )
                                    .clickable { displayFormat = fmt }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                // Delete button (edit mode only)
                if (isEditMode && onDelete != null) {
                    Spacer(Modifier.height(4.dp))
                    Button(
                        onClick = { showDeleteConfirm = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GearBoardColors.DangerBackground,
                            contentColor = GearBoardColors.DangerText
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("DELETE", letterSpacing = 1.sp, fontSize = 12.sp)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (!isValid) return@TextButton
                    val control = buildControl(
                        kind = selectedKind,
                        existingId = existingControl?.id,
                        label = label.trim(),
                        ccNumber = ccNum ?: 0,
                        midiChannel = midiChannel,
                        positions = positions,
                        noteNumber = noteNum ?: 36,
                        isHorizontal = isHorizontal,
                        pulseMode = pulseMode,
                        displayFormat = displayFormat
                    )
                    onConfirm(control)
                },
                enabled = isValid
            ) {
                Text(
                    if (isEditMode) "SAVE" else "ADD",
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

    // Delete confirmation
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            containerColor = GearBoardColors.Surface,
            title = { Text("Delete Control?", color = GearBoardColors.DangerText) },
            text = { Text("\"$label\" will be permanently removed.", color = GearBoardColors.TextPrimary) },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirm = false
                    onDelete?.invoke()
                }) {
                    Text("DELETE", color = GearBoardColors.DangerText, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("CANCEL", color = GearBoardColors.TextSecondary)
                }
            }
        )
    }
}

private fun buildControl(
    kind: ControlKind,
    existingId: String?,
    label: String,
    ccNumber: Int,
    midiChannel: Int,
    positions: List<String>,
    noteNumber: Int,
    isHorizontal: Boolean,
    pulseMode: Boolean,
    displayFormat: DisplayFormat = DisplayFormat.ZERO_TO_TEN
): ControlType {
    val id = existingId ?: java.util.UUID.randomUUID().toString()
    return when (kind) {
        ControlKind.KNOB -> ControlType.Knob(
            id = id, label = label, ccNumber = ccNumber, midiChannel = midiChannel,
            displayFormat = displayFormat
        )
        ControlKind.TOGGLE -> ControlType.Toggle(
            id = id, label = label, ccNumber = ccNumber, midiChannel = midiChannel, pulseMode = pulseMode
        )
        ControlKind.TAP -> ControlType.Tap(
            id = id, label = label, ccNumber = ccNumber, midiChannel = midiChannel
        )
        ControlKind.SELECTOR -> ControlType.Selector(
            id = id, label = label, ccNumber = ccNumber, midiChannel = midiChannel, positions = positions
        )
        ControlKind.FADER -> ControlType.Fader(
            id = id, label = label, ccNumber = ccNumber, midiChannel = midiChannel,
            orientation = if (isHorizontal) FaderOrientation.HORIZONTAL else FaderOrientation.VERTICAL,
            displayFormat = displayFormat
        )
        ControlKind.PRESET_NAV -> ControlType.PresetNav(
            id = id, label = label, midiChannel = midiChannel
        )
        ControlKind.PAD -> ControlType.Pad(
            id = id, label = label, noteNumber = noteNumber, midiChannel = midiChannel
        )
    }
}
