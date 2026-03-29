package com.gearboard.ui.screens.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.domain.model.AbSlot
import com.gearboard.domain.model.AmpBlock
import com.gearboard.domain.model.CabBlock
import com.gearboard.domain.model.ControlType
import com.gearboard.ui.components.AbToggle
import com.gearboard.ui.theme.GearBoardColors
import com.gearboard.ui.theme.GearBoardDimensions
import com.gearboard.ui.theme.LocalAccentColor

// ---------------------------------------------------------------------------
// AmpSection
// ---------------------------------------------------------------------------

/**
 * AmpSection — LazyRow of AmpBlockCards + "ADD AMPLIFIER BLOCK" button.
 * Mirrors the Pedals / Effects section structure.
 */
@Composable
fun AmpSection(
    ampBlocks: List<AmpBlock>,
    onAddBlock: () -> Unit,
    onRemoveBlock: (blockId: String) -> Unit,
    onAbSwitch: (blockId: String, slot: AbSlot) -> Unit,
    onAddControl: (blockId: String) -> Unit,
    onEditControl: (blockId: String, control: ControlType) -> Unit,
    onCustomize: (blockId: String) -> Unit,
    onRename: (blockId: String) -> Unit,
    onKnobValueChange: (blockId: String, knob: ControlType.Knob, value: Float) -> Unit,
    onToggle: (blockId: String, toggle: ControlType.Toggle) -> Unit,
    onTap: (blockId: String, tap: ControlType.Tap) -> Unit,
    onSelectorChange: (blockId: String, selector: ControlType.Selector, index: Int) -> Unit,
    onFaderValueChange: (blockId: String, fader: ControlType.Fader, value: Float) -> Unit,
    onPresetPrev: (blockId: String, nav: ControlType.PresetNav) -> Unit,
    onPresetNext: (blockId: String, nav: ControlType.PresetNav) -> Unit,
    onPadDown: (blockId: String, pad: ControlType.Pad) -> Unit,
    onPadUp: (blockId: String, pad: ControlType.Pad) -> Unit,
    onBadgeTap: (blockId: String, control: ControlType) -> Unit,
    modifier: Modifier = Modifier,
    controlScale: Float = 1.0f
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (ampBlocks.isEmpty()) {
            Text(
                text = "Choose an amp type to get started",
                color = GearBoardColors.TextSecondary,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.Center
            )
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(ampBlocks, key = { it.id }) { block ->
                    AmpBlockCard(
                        block = block,
                        onAbSwitch = { slot -> onAbSwitch(block.id, slot) },
                        onAddControl = { onAddControl(block.id) },
                        onEditControl = { control -> onEditControl(block.id, control) },
                        onCustomize = { onCustomize(block.id) },
                        onRename = { onRename(block.id) },
                        onRemove = { onRemoveBlock(block.id) },
                        onKnobValueChange = { knob, v -> onKnobValueChange(block.id, knob, v) },
                        onToggle = { toggle -> onToggle(block.id, toggle) },
                        onTap = { tap -> onTap(block.id, tap) },
                        onSelectorChange = { sel, idx -> onSelectorChange(block.id, sel, idx) },
                        onFaderValueChange = { fader, v -> onFaderValueChange(block.id, fader, v) },
                        onPresetPrev = { nav -> onPresetPrev(block.id, nav) },
                        onPresetNext = { nav -> onPresetNext(block.id, nav) },
                        onPadDown = { pad -> onPadDown(block.id, pad) },
                        onPadUp = { pad -> onPadUp(block.id, pad) },
                        onBadgeTap = { control -> onBadgeTap(block.id, control) },
                        controlScale = controlScale
                    )
                }
            }
        }

        Spacer(Modifier.height(4.dp))
        Button(
            onClick = onAddBlock,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = GearBoardColors.SurfaceElevated,
                contentColor = GearBoardColors.Accent
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.padding(start = 8.dp))
            Text("ADD AMPLIFIER BLOCK", fontSize = 12.sp, letterSpacing = 1.sp)
        }
    }
}

// ---------------------------------------------------------------------------
// AmpBlockCard
// ---------------------------------------------------------------------------

@Composable
fun AmpBlockCard(
    block: AmpBlock,
    onAbSwitch: (AbSlot) -> Unit,
    onAddControl: () -> Unit,
    onEditControl: (ControlType) -> Unit,
    onCustomize: () -> Unit,
    onRename: () -> Unit,
    onRemove: () -> Unit,
    onKnobValueChange: (ControlType.Knob, Float) -> Unit,
    onToggle: (ControlType.Toggle) -> Unit,
    onTap: (ControlType.Tap) -> Unit,
    onSelectorChange: (ControlType.Selector, Int) -> Unit,
    onFaderValueChange: (ControlType.Fader, Float) -> Unit,
    onPresetPrev: (ControlType.PresetNav) -> Unit,
    onPresetNext: (ControlType.PresetNav) -> Unit,
    onPadDown: (ControlType.Pad) -> Unit,
    onPadUp: (ControlType.Pad) -> Unit,
    onBadgeTap: (ControlType) -> Unit,
    modifier: Modifier = Modifier,
    controlScale: Float = 1.0f
) {
    var showMenu by remember { mutableStateOf(false) }
    val accentColor = androidx.compose.ui.graphics.Color(block.appearance.accentColor)

    CompositionLocalProvider(LocalAccentColor provides accentColor) {
        Column(
            modifier = modifier
                .padding(4.dp)
                .widthIn(min = GearBoardDimensions.PedalCardWidth)
                .clip(RoundedCornerShape(GearBoardDimensions.RadiusM))
                .background(GearBoardColors.Surface)
                .border(
                    width = GearBoardDimensions.BorderThin,
                    color = accentColor.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(GearBoardDimensions.RadiusM)
                )
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header: name + A/B toggle + menu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = block.name.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                )

                AbToggle(
                    currentSlot = block.currentSlot,
                    onSlotSelected = onAbSwitch
                )

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
                enabled = true,
                controlScale = controlScale,
                layoutMode = block.layoutMode
            )

            // + Add control (inline)
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
        }
    }
}

// ---------------------------------------------------------------------------
// CabSection
// ---------------------------------------------------------------------------

/**
 * CabSection — LazyRow of CabBlockCards + "ADD CABINET BLOCK" button.
 */
@Composable
fun CabSection(
    cabBlocks: List<CabBlock>,
    onAddBlock: () -> Unit,
    onRemoveBlock: (blockId: String) -> Unit,
    onAbSwitch: (blockId: String, slot: AbSlot) -> Unit,
    onAddControl: (blockId: String) -> Unit,
    onEditControl: (blockId: String, control: ControlType) -> Unit,
    onCustomize: (blockId: String) -> Unit,
    onRename: (blockId: String) -> Unit,
    onKnobValueChange: (blockId: String, knob: ControlType.Knob, value: Float) -> Unit,
    onToggle: (blockId: String, toggle: ControlType.Toggle) -> Unit,
    onTap: (blockId: String, tap: ControlType.Tap) -> Unit,
    onSelectorChange: (blockId: String, selector: ControlType.Selector, index: Int) -> Unit,
    onFaderValueChange: (blockId: String, fader: ControlType.Fader, value: Float) -> Unit,
    onPresetPrev: (blockId: String, nav: ControlType.PresetNav) -> Unit,
    onPresetNext: (blockId: String, nav: ControlType.PresetNav) -> Unit,
    onPadDown: (blockId: String, pad: ControlType.Pad) -> Unit,
    onPadUp: (blockId: String, pad: ControlType.Pad) -> Unit,
    onBadgeTap: (blockId: String, control: ControlType) -> Unit,
    modifier: Modifier = Modifier,
    controlScale: Float = 1.0f
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (cabBlocks.isEmpty()) {
            Text(
                text = "Choose a cabinet type to get started",
                color = GearBoardColors.TextSecondary,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                textAlign = TextAlign.Center
            )
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cabBlocks, key = { it.id }) { block ->
                    CabBlockCard(
                        block = block,
                        onAbSwitch = { slot -> onAbSwitch(block.id, slot) },
                        onAddControl = { onAddControl(block.id) },
                        onEditControl = { control -> onEditControl(block.id, control) },
                        onCustomize = { onCustomize(block.id) },
                        onRename = { onRename(block.id) },
                        onRemove = { onRemoveBlock(block.id) },
                        onKnobValueChange = { knob, v -> onKnobValueChange(block.id, knob, v) },
                        onToggle = { toggle -> onToggle(block.id, toggle) },
                        onTap = { tap -> onTap(block.id, tap) },
                        onSelectorChange = { sel, idx -> onSelectorChange(block.id, sel, idx) },
                        onFaderValueChange = { fader, v -> onFaderValueChange(block.id, fader, v) },
                        onPresetPrev = { nav -> onPresetPrev(block.id, nav) },
                        onPresetNext = { nav -> onPresetNext(block.id, nav) },
                        onPadDown = { pad -> onPadDown(block.id, pad) },
                        onPadUp = { pad -> onPadUp(block.id, pad) },
                        onBadgeTap = { control -> onBadgeTap(block.id, control) },
                        controlScale = controlScale
                    )
                }
            }
        }

        Spacer(Modifier.height(4.dp))
        Button(
            onClick = onAddBlock,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = GearBoardColors.SurfaceElevated,
                contentColor = GearBoardColors.Accent
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.padding(start = 8.dp))
            Text("ADD CABINET BLOCK", fontSize = 12.sp, letterSpacing = 1.sp)
        }
    }
}

// ---------------------------------------------------------------------------
// CabBlockCard
// ---------------------------------------------------------------------------

@Composable
fun CabBlockCard(
    block: CabBlock,
    onAbSwitch: (AbSlot) -> Unit,
    onAddControl: () -> Unit,
    onEditControl: (ControlType) -> Unit,
    onCustomize: () -> Unit,
    onRename: () -> Unit,
    onRemove: () -> Unit,
    onKnobValueChange: (ControlType.Knob, Float) -> Unit,
    onToggle: (ControlType.Toggle) -> Unit,
    onTap: (ControlType.Tap) -> Unit,
    onSelectorChange: (ControlType.Selector, Int) -> Unit,
    onFaderValueChange: (ControlType.Fader, Float) -> Unit,
    onPresetPrev: (ControlType.PresetNav) -> Unit,
    onPresetNext: (ControlType.PresetNav) -> Unit,
    onPadDown: (ControlType.Pad) -> Unit,
    onPadUp: (ControlType.Pad) -> Unit,
    onBadgeTap: (ControlType) -> Unit,
    modifier: Modifier = Modifier,
    controlScale: Float = 1.0f
) {
    var showMenu by remember { mutableStateOf(false) }
    val accentColor = androidx.compose.ui.graphics.Color(block.appearance.accentColor)

    CompositionLocalProvider(LocalAccentColor provides accentColor) {
        Column(
            modifier = modifier
                .padding(4.dp)
                .widthIn(min = GearBoardDimensions.PedalCardWidth)
                .clip(RoundedCornerShape(GearBoardDimensions.RadiusM))
                .background(GearBoardColors.Surface)
                .border(
                    width = GearBoardDimensions.BorderThin,
                    color = accentColor.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(GearBoardDimensions.RadiusM)
                )
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = block.name.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                )

                AbToggle(
                    currentSlot = block.currentSlot,
                    onSlotSelected = onAbSwitch
                )

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
                enabled = true,
                controlScale = controlScale,
                layoutMode = block.layoutMode
            )

            // + Add control (inline)
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
        }
    }
}
