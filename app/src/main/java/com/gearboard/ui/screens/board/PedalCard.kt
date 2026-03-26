package com.gearboard.ui.screens.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.domain.model.ControlBlock
import com.gearboard.domain.model.ControlType
import com.gearboard.ui.components.GearToggle
import com.gearboard.ui.components.ToggleVariant
import com.gearboard.ui.theme.GearBoardColors
import com.gearboard.ui.theme.GearBoardDimensions

/**
 * PedalCard — displays a ControlBlock (pedal) with arbitrary controls.
 * Fixed width (160dp), used inside a horizontal LazyRow.
 */
@Composable
fun PedalCard(
    block: ControlBlock,
    onToggleEnabled: () -> Unit,
    onKnobValueChange: (ControlType.Knob, Float) -> Unit,
    onToggle: (ControlType.Toggle) -> Unit,
    onTap: (ControlType.Tap) -> Unit,
    onSelectorChange: (ControlType.Selector, Int) -> Unit,
    onFaderValueChange: (ControlType.Fader, Float) -> Unit,
    onPresetPrev: (ControlType.PresetNav) -> Unit,
    onPresetNext: (ControlType.PresetNav) -> Unit,
    onPadDown: (ControlType.Pad) -> Unit,
    onPadUp: (ControlType.Pad) -> Unit,
    onAddControl: () -> Unit,
    onEditControl: (ControlType) -> Unit,
    onReorder: (List<ControlType>) -> Unit,
    onRename: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    controlScale: Float = 1.0f
) {
    var showMenu by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .width(GearBoardDimensions.PedalCardWidth)
            .clip(RoundedCornerShape(GearBoardDimensions.RadiusM))
            .background(GearBoardColors.Surface)
            .border(
                width = GearBoardDimensions.BorderThin,
                color = if (block.enabled) GearBoardColors.BorderAccent.copy(alpha = 0.3f) else GearBoardColors.BorderDefault,
                shape = RoundedCornerShape(GearBoardDimensions.RadiusM)
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Header: name + menu
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = block.name.uppercase(),
                    color = if (block.enabled) GearBoardColors.Accent else GearBoardColors.TextDisabled,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = block.type.uppercase(),
                    color = GearBoardColors.TextDisabled,
                    fontSize = 9.sp,
                    letterSpacing = 1.sp
                )
            }

            // Context menu
            IconButton(
                onClick = { showMenu = true },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Default.MoreVert,
                    "Menu",
                    tint = GearBoardColors.TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Rename", color = GearBoardColors.TextPrimary, fontSize = 13.sp) },
                    onClick = { showMenu = false; onRename() }
                )
                DropdownMenuItem(
                    text = { Text("Add Control", color = GearBoardColors.TextPrimary, fontSize = 13.sp) },
                    onClick = { showMenu = false; onAddControl() }
                )
                DropdownMenuItem(
                    text = { Text("Delete Block", color = GearBoardColors.DangerText, fontSize = 13.sp) },
                    onClick = { showMenu = false; onRemove() }
                )
            }
        }

        // Controls
        RenderControlList(
            controls = block.controls,
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
            onReorder = onReorder,
            enabled = block.enabled,
            controlScale = controlScale
        )

        // + Add control (small inline)
        IconButton(
            onClick = onAddControl,
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                Icons.Default.Add,
                "Add control",
                tint = GearBoardColors.Accent.copy(alpha = 0.6f),
                modifier = Modifier.size(16.dp)
            )
        }

        // Enable/disable stomp
        Spacer(Modifier.height(4.dp))
        GearToggle(
            label = if (block.enabled) "ON" else "OFF",
            enabled = block.enabled,
            onToggle = onToggleEnabled,
            variant = ToggleVariant.STOMP
        )
    }
}
