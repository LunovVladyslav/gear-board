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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import com.gearboard.domain.model.AbSlot
import com.gearboard.ui.components.AbToggle
import com.gearboard.ui.components.GearToggle
import com.gearboard.ui.components.ToggleVariant
import com.gearboard.ui.theme.GearBoardColors
import com.gearboard.ui.theme.GearBoardDimensions
import androidx.compose.runtime.CompositionLocalProvider
import com.gearboard.ui.theme.LocalAccentColor

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
    onCustomize: () -> Unit = {},
    onAbSwitch: (AbSlot) -> Unit = {},
    onBadgeTap: (ControlType) -> Unit = {},
    isDeviceConnected: Boolean = false,
    modifier: Modifier = Modifier,
    controlScale: Float = 1.0f
) {
    var showMenu by remember { mutableStateOf(false) }
    val accentColor = androidx.compose.ui.graphics.Color(block.appearance.accentColor)

    // Hide UUID if persistence accidentally sets name = id
    val displayName = if (block.name == block.id || block.name.isBlank()) {
        block.type.uppercase()
    } else {
        block.name.uppercase()
    }

    CompositionLocalProvider(LocalAccentColor provides accentColor) {
        Column(
            modifier = modifier
                .padding(4.dp)
                .widthIn(min = GearBoardDimensions.PedalCardWidth)
                .clip(RoundedCornerShape(GearBoardDimensions.RadiusM))
                .background(GearBoardColors.Surface)
                .border(
                    width = GearBoardDimensions.BorderThin,
                    color = if (block.enabled) accentColor.copy(alpha = 0.3f) else GearBoardColors.BorderDefault,
                    shape = RoundedCornerShape(GearBoardDimensions.RadiusM)
                )
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header: name + menu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val headerFontSize = if (block.appearance.headerStyle == com.gearboard.domain.model.HeaderStyle.BOLD) 15.sp else 12.sp

                Column(modifier = Modifier.weight(1f).padding(end = 4.dp)) {
                    Text(
                        text = displayName,
                        fontSize = headerFontSize,
                        fontWeight = FontWeight.Bold,
                        color = if (block.enabled) accentColor else GearBoardColors.TextDisabled,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (block.appearance.headerStyle != com.gearboard.domain.model.HeaderStyle.MINIMAL && block.type.isNotBlank() && block.name != block.id && block.type.uppercase() != displayName) {
                        Text(
                            text = block.type.uppercase(),
                            fontSize = 9.sp,
                            color = GearBoardColors.TextSecondary,
                            letterSpacing = 1.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // A/B toggle
                AbToggle(
                    currentSlot = block.abSlot,
                    onSlotSelected = onAbSwitch
                )

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
                        text = { Text("Customize", color = GearBoardColors.TextPrimary, fontSize = 13.sp) },
                        onClick = { showMenu = false; onCustomize() }
                    )
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

            // Unmapped banner (only when device is connected and some controls lack CC)
            if (isDeviceConnected) {
                val unmappedCount = block.controls.count { control ->
                    when (control) {
                        is ControlType.Knob     -> control.ccNumber == 0
                        is ControlType.Toggle   -> control.ccNumber == 0
                        is ControlType.Tap      -> control.ccNumber == 0
                        is ControlType.Selector -> control.ccNumber == 0
                        is ControlType.Fader    -> control.ccNumber == 0
                        is ControlType.PresetNav -> false
                        is ControlType.Pad       -> false
                    }
                }
                if (unmappedCount > 0) {
                    Text(
                        text = "$unmappedCount unmapped",
                        fontSize = 10.sp,
                        color = GearBoardColors.Accent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(GearBoardColors.Accent.copy(alpha = 0.12f))
                            .clickable { onBadgeTap(block.controls.first()) }
                            .padding(horizontal = 6.dp, vertical = 3.dp)
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
                onBadgeTap = onBadgeTap,
                onReorder = onReorder,
                enabled = block.enabled,
                controlScale = controlScale,
                layoutMode = block.layoutMode
            )

            // + Add control (small inline)
            IconButton(
                onClick = onAddControl,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    "Add control",
                    tint = accentColor.copy(alpha = 0.6f),
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
}
