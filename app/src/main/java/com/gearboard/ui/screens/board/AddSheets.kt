package com.gearboard.ui.screens.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.domain.model.ControlBlock
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.FaderOrientation
import com.gearboard.ui.theme.GearBoardColors

// --- Pedal block templates ---
private val pedalBlockTemplates = listOf(
    "Overdrive/Distortion" to listOf(
        ControlType.Knob(label = "Drive", ccNumber = 0),
        ControlType.Knob(label = "Tone", ccNumber = 0),
        ControlType.Knob(label = "Level", ccNumber = 0),
        ControlType.Toggle(label = "Boost", ccNumber = 0),
        ControlType.Toggle(label = "On/Off", ccNumber = 0)
    ),
    "Modulation" to listOf(
        ControlType.Knob(label = "Rate", ccNumber = 0),
        ControlType.Knob(label = "Depth", ccNumber = 0),
        ControlType.Knob(label = "Mix", ccNumber = 0),
        ControlType.Selector(label = "Mode", ccNumber = 0, positions = listOf("A", "B")),
        ControlType.Toggle(label = "On/Off", ccNumber = 0)
    ),
    "Delay" to listOf(
        ControlType.Knob(label = "Time", ccNumber = 0),
        ControlType.Knob(label = "Feedback", ccNumber = 0),
        ControlType.Knob(label = "Mix", ccNumber = 0),
        ControlType.Tap(label = "Tap Tempo", ccNumber = 0),
        ControlType.Toggle(label = "On/Off", ccNumber = 0)
    ),
    "Reverb" to listOf(
        ControlType.Knob(label = "Decay", ccNumber = 0),
        ControlType.Knob(label = "Mix", ccNumber = 0),
        ControlType.Toggle(label = "On/Off", ccNumber = 0)
    ),
    "Wah/Expression" to listOf(
        ControlType.Fader(label = "Expression", ccNumber = 0, orientation = FaderOrientation.HORIZONTAL),
        ControlType.Toggle(label = "On/Off", ccNumber = 0)
    ),
    "EQ" to listOf(
        ControlType.Fader(label = "65Hz", ccNumber = 0, orientation = FaderOrientation.VERTICAL),
        ControlType.Fader(label = "125Hz", ccNumber = 0, orientation = FaderOrientation.VERTICAL),
        ControlType.Fader(label = "250Hz", ccNumber = 0, orientation = FaderOrientation.VERTICAL),
        ControlType.Fader(label = "500Hz", ccNumber = 0, orientation = FaderOrientation.VERTICAL),
        ControlType.Fader(label = "1kHz", ccNumber = 0, orientation = FaderOrientation.VERTICAL),
        ControlType.Fader(label = "2kHz", ccNumber = 0, orientation = FaderOrientation.VERTICAL),
        ControlType.Fader(label = "4kHz", ccNumber = 0, orientation = FaderOrientation.VERTICAL),
        ControlType.Fader(label = "8kHz", ccNumber = 0, orientation = FaderOrientation.VERTICAL)
    )
)

// --- Effect block templates ---
private val effectBlockTemplates = listOf(
    "Chorus" to listOf(
        ControlType.Knob(label = "Rate", ccNumber = 0),
        ControlType.Knob(label = "Depth", ccNumber = 0),
        ControlType.Knob(label = "Mix", ccNumber = 0)
    ),
    "Phaser" to listOf(
        ControlType.Knob(label = "Rate", ccNumber = 0),
        ControlType.Knob(label = "Depth", ccNumber = 0),
        ControlType.Knob(label = "Feedback", ccNumber = 0)
    ),
    "Tremolo" to listOf(
        ControlType.Knob(label = "Speed", ccNumber = 0),
        ControlType.Knob(label = "Depth", ccNumber = 0)
    ),
    "Noise Gate" to listOf(
        ControlType.Knob(label = "Threshold", ccNumber = 0),
        ControlType.Knob(label = "Decay", ccNumber = 0)
    ),
    "Compressor" to listOf(
        ControlType.Knob(label = "Threshold", ccNumber = 0),
        ControlType.Knob(label = "Ratio", ccNumber = 0),
        ControlType.Knob(label = "Gain", ccNumber = 0)
    )
)

/**
 * AddBlockSheet — bottom sheet for adding a new pedal or effect block.
 * Allows entering a name and optionally choosing a template.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBlockSheet(
    title: String,
    templates: List<Pair<String, List<ControlType>>> = pedalBlockTemplates,
    onAddBlock: (ControlBlock) -> Unit,
    onDismiss: () -> Unit
) {
    var blockName by remember { mutableStateOf("") }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = GearBoardColors.TextPrimary,
        unfocusedTextColor = GearBoardColors.TextPrimary,
        focusedBorderColor = GearBoardColors.Accent,
        unfocusedBorderColor = GearBoardColors.BorderDefault,
        cursorColor = GearBoardColors.Accent,
        focusedLabelColor = GearBoardColors.Accent,
        unfocusedLabelColor = GearBoardColors.TextSecondary,
        focusedContainerColor = GearBoardColors.SurfaceVariant,
        unfocusedContainerColor = GearBoardColors.SurfaceVariant
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = GearBoardColors.Surface,
        contentColor = GearBoardColors.TextPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                title.uppercase(),
                color = GearBoardColors.Accent,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Block name
            OutlinedTextField(
                value = blockName,
                onValueChange = { if (it.length <= 20) blockName = it },
                label = { Text("BLOCK NAME") },
                singleLine = true,
                colors = textFieldColors,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // Create empty
            TemplateItem(
                name = "Create Empty",
                type = "No controls",
                controlCount = 0,
                onClick = {
                    val name = blockName.ifBlank { "New Block" }
                    onAddBlock(ControlBlock(name = name, type = "Custom"))
                }
            )

            // Templates
            Text(
                "FROM TEMPLATE",
                color = GearBoardColors.TextSecondary,
                fontSize = 10.sp,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            templates.forEach { (templateName, controls) ->
                TemplateItem(
                    name = templateName,
                    type = "${controls.size} controls",
                    controlCount = controls.size,
                    onClick = {
                        val name = blockName.ifBlank { templateName }
                        onAddBlock(
                            ControlBlock(
                                name = name,
                                type = templateName,
                                controls = controls.map { regenerateId(it) }
                            )
                        )
                    }
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

/** Convenience wrappers */
@Composable
fun AddPedalBlockSheet(
    onAddBlock: (ControlBlock) -> Unit,
    onDismiss: () -> Unit
) {
    AddBlockSheet(
        title = "Add Pedal Block",
        templates = pedalBlockTemplates,
        onAddBlock = onAddBlock,
        onDismiss = onDismiss
    )
}

@Composable
fun AddEffectBlockSheet(
    onAddBlock: (ControlBlock) -> Unit,
    onDismiss: () -> Unit
) {
    AddBlockSheet(
        title = "Add Effect Block",
        templates = effectBlockTemplates,
        onAddBlock = onAddBlock,
        onDismiss = onDismiss
    )
}

/** Rename block dialog */
@Composable
fun RenameBlockDialog(
    currentName: String,
    onRename: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newName by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GearBoardColors.Surface,
        title = {
            Text("RENAME BLOCK", color = GearBoardColors.Accent, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
        },
        text = {
            OutlinedTextField(
                value = newName,
                onValueChange = { if (it.length <= 20) newName = it },
                label = { Text("NAME", color = GearBoardColors.TextSecondary) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = GearBoardColors.TextPrimary,
                    unfocusedTextColor = GearBoardColors.TextPrimary,
                    focusedBorderColor = GearBoardColors.Accent,
                    unfocusedBorderColor = GearBoardColors.BorderDefault,
                    cursorColor = GearBoardColors.Accent,
                    focusedContainerColor = GearBoardColors.SurfaceVariant,
                    unfocusedContainerColor = GearBoardColors.SurfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = { if (newName.isNotBlank()) onRename(newName.trim()) },
                enabled = newName.isNotBlank()
            ) {
                Text("RENAME", color = GearBoardColors.Accent, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = GearBoardColors.TextSecondary)
            }
        }
    )
}

@Composable
private fun TemplateItem(
    name: String,
    type: String,
    controlCount: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(GearBoardColors.SurfaceVariant)
            .border(1.dp, GearBoardColors.BorderDefault, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = name.uppercase(),
            color = GearBoardColors.TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp
        )
        Text(
            text = type,
            color = GearBoardColors.TextDisabled,
            fontSize = 11.sp
        )
    }
}

/** Generate a new UUID for a control to avoid ID collisions when instantiating templates. */
private fun regenerateId(control: ControlType): ControlType {
    val newId = java.util.UUID.randomUUID().toString()
    return when (control) {
        is ControlType.Knob -> control.copy(id = newId)
        is ControlType.Toggle -> control.copy(id = newId)
        is ControlType.Tap -> control.copy(id = newId)
        is ControlType.Selector -> control.copy(id = newId)
        is ControlType.Fader -> control.copy(id = newId)
        is ControlType.PresetNav -> control.copy(id = newId)
        is ControlType.Pad -> control.copy(id = newId)
    }
}
