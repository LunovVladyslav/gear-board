package com.gearboard.ui.screens.board

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gearboard.domain.model.AbSlot
import com.gearboard.domain.model.AmpBlock
import com.gearboard.domain.model.BlockAppearance
import com.gearboard.domain.model.BlockLayout
import com.gearboard.domain.model.CabBlock
import com.gearboard.domain.model.ControlType
import com.gearboard.ui.components.AbToggle
import com.gearboard.ui.components.SectionHeader
import com.gearboard.ui.theme.GearBoardColors
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import com.gearboard.ui.components.ConnectionDot
import com.gearboard.ui.screens.connect.ConnectViewModel
import com.gearboard.ui.theme.GearBoardDimensions

/**
 * BoardScreen — the main performance screen.
 *
 * Displays 4 collapsible sections:
 * 1. Pedals (horizontal scroll with blocks)
 * 2. Amp (single block with controls)
 * 3. Cabinet (single block with controls)
 * 4. Effects (horizontal scroll with blocks)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardScreen(
    viewModel: BoardViewModel = hiltViewModel(),
    connectViewModel: ConnectViewModel = hiltViewModel(),
    onSettingsClick: () -> Unit = {}
) {
    val connectionState by connectViewModel.connectionState.collectAsStateWithLifecycle()
    val boardState by viewModel.boardState.collectAsStateWithLifecycle()
    val controlScale by viewModel.controlSize.collectAsStateWithLifecycle()
    val globalMidiChannel by viewModel.globalMidiChannel.collectAsStateWithLifecycle()
    val pedalsExpanded by viewModel.pedalsExpanded.collectAsStateWithLifecycle()
    val ampExpanded by viewModel.ampExpanded.collectAsStateWithLifecycle()
    val cabExpanded by viewModel.cabExpanded.collectAsStateWithLifecycle()
    val effectsExpanded by viewModel.effectsExpanded.collectAsStateWithLifecycle()
    val showAddPedalBlock by viewModel.showAddPedalBlockDialog.collectAsStateWithLifecycle()
    val showAddEffectBlock by viewModel.showAddEffectBlockDialog.collectAsStateWithLifecycle()
    val showOnboarding by viewModel.showOnboarding.collectAsStateWithLifecycle()

    // Dialog state: which control to add/edit and where
    var addControlTarget by remember { mutableStateOf<AddControlTarget?>(null) }
    var editControlState by remember { mutableStateOf<EditControlState?>(null) }
    var renameTarget by remember { mutableStateOf<RenameTarget?>(null) }
    var customizeTarget by remember { mutableStateOf<CustomizeTarget?>(null) }
    var mappingControl by remember { mutableStateOf<MappingControlState?>(null) }

    // Add control dialog for amp/cab
    var showAddAmpControl by remember { mutableStateOf(false) }
    var showAddCabControl by remember { mutableStateOf(false) }
    var editAmpControl by remember { mutableStateOf<ControlType?>(null) }
    var editCabControl by remember { mutableStateOf<ControlType?>(null) }

    var showChannelMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Surface(
                color = GearBoardColors.Surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(GearBoardDimensions.TopBarHeight)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: app name
                    Text(
                        text = "GEARBOARD",
                        color = GearBoardColors.Accent,
                        fontSize = 12.sp,
                        letterSpacing = 3.sp,
                        modifier = Modifier.weight(1f)
                    )
                    // Center: MIDI channel chip + dropdown
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        MidiChannelChip(
                            channel = globalMidiChannel,
                            onClick = { showChannelMenu = true }
                        )
                        DropdownMenu(
                            expanded = showChannelMenu,
                            onDismissRequest = { showChannelMenu = false },
                            modifier = Modifier.background(GearBoardColors.Surface)
                        ) {
                            DropdownMenuItem(
                                text = { Text("OMNI (ALL)", color = GearBoardColors.TextPrimary) },
                                onClick = {
                                    viewModel.setGlobalMidiChannel(0)
                                    showChannelMenu = false
                                }
                            )
                            for (i in 1..16) {
                                DropdownMenuItem(
                                    text = { Text("CH $i", color = GearBoardColors.TextPrimary) },
                                    onClick = {
                                        viewModel.setGlobalMidiChannel(i)
                                        showChannelMenu = false
                                    }
                                )
                            }
                        }
                    }
                    // Right: connection dot + settings gear
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ConnectionDot(state = connectionState)
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(
                            onClick = onSettingsClick,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = GearBoardColors.TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(GearBoardColors.Background)
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 88.dp, top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
        item {
            SectionHeader(
                title = "Pedals",
                enabled = boardState.pedals.flatMap { it.controls }.hasAnyMapped(),
                onToggleEnabled = {
                    boardState.pedals.forEach { viewModel.togglePedalBlockEnabled(it.id) }
                },
                expanded = pedalsExpanded,
                onToggleExpanded = { viewModel.togglePedalsExpanded() },
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Column {
                    if (boardState.pedals.isEmpty()) {
                        EmptySectionHint("No pedals added yet")
                    } else {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(boardState.pedals, key = { it.id }) { block ->
                                PedalCard(
                                    block = block,
                                    onToggleEnabled = { viewModel.togglePedalBlockEnabled(block.id) },
                                    onKnobValueChange = { knob, value ->
                                        viewModel.onKnobValueChange(true, block.id, knob.id, knob, value)
                                    },
                                    onToggle = { toggle -> viewModel.onToggle(true, block.id, toggle.id, toggle) },
                                    onTap = { tap -> viewModel.onTap(tap) },
                                    onSelectorChange = { sel, idx ->
                                        viewModel.onSelectorChange(true, block.id, sel.id, sel, idx)
                                    },
                                    onFaderValueChange = { fader, value ->
                                        viewModel.onFaderValueChange(true, block.id, fader.id, fader, value)
                                    },
                                    onPresetPrev = { nav -> viewModel.onPresetPrev(true, block.id, nav.id, nav) },
                                    onPresetNext = { nav -> viewModel.onPresetNext(true, block.id, nav.id, nav) },
                                    onPadDown = { pad -> viewModel.sendPadOn(pad) },
                                    onPadUp = { pad -> viewModel.sendPadOff(pad) },
                                    onAddControl = {
                                        addControlTarget = AddControlTarget(true, block.id)
                                    },
                                    onEditControl = { control ->
                                        editControlState = EditControlState(true, block.id, control)
                                    },
                                    onReorder = { reordered ->
                                        viewModel.reorderControlsInBlock(true, block.id, reordered)
                                    },
                                    onRename = {
                                        renameTarget = RenameTarget(true, block.id, block.name)
                                    },
                                    onCustomize = {
                                        customizeTarget = CustomizeTarget.Block(true, block)
                                    },
                                    onRemove = { viewModel.removePedalBlock(block.id) },
                                    onAbSwitch = { slot -> viewModel.switchBlockAbSlot(true, block.id, slot) },
                                    onBadgeTap = { control ->
                                        mappingControl = MappingControlState(control, isPedals = true, blockId = block.id, section = "pedals")
                                    },
                                    controlScale = controlScale
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    AddButton("ADD PEDAL BLOCK") { viewModel.showAddPedalBlockDialog() }
                }
            }
        }

        // === AMP SECTION ===
        item {
            SectionHeader(
                title = "Amplifier",
                enabled = boardState.ampBlocks.flatMap { it.controls }.hasAnyMapped(),
                onToggleEnabled = {},
                expanded = ampExpanded,
                onToggleExpanded = { viewModel.toggleAmpExpanded() },
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Column {
                    // A/B toggle for amp
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        AbToggle(
                            currentSlot = boardState.ampBlocks.firstOrNull()?.currentSlot ?: AbSlot.A,
                            onSlotSelected = { viewModel.switchAmpAbSlot(it) }
                        )
                    }
                    AmpSection(
                    amp = boardState.ampBlocks.firstOrNull(),
                    onKnobValueChange = { knob, value ->
                        viewModel.onAmpKnobValueChange(knob.id, knob, value)
                    },
                    onToggle = { toggle -> viewModel.onAmpToggle(toggle.id, toggle) },
                    onTap = { tap -> viewModel.onTap(tap) },
                    onSelectorChange = { sel, idx ->
                        viewModel.onAmpSelectorChange(sel.id, sel, idx)
                    },
                    onFaderValueChange = { fader, value ->
                        viewModel.onAmpFaderValueChange(fader.id, fader, value)
                    },
                    onPresetPrev = { nav -> viewModel.onPresetPrev(false, "", nav.id, nav) },
                    onPresetNext = { nav -> viewModel.onPresetNext(false, "", nav.id, nav) },
                    onPadDown = { pad -> viewModel.sendPadOn(pad) },
                    onPadUp = { pad -> viewModel.sendPadOff(pad) },
                    onEditControl = { control -> editAmpControl = control },
                    onAddControl = { showAddAmpControl = true },
                    onClearAll = { viewModel.clearAmpControls() },
                    onCustomize = { customizeTarget = CustomizeTarget.Amp(boardState.ampBlocks.firstOrNull()) },
                    onApplyAmpTemplate = { viewModel.applyAmpTemplate(it) },
                    onBadgeTap = { control ->
                        mappingControl = MappingControlState(control, section = "amp")
                    },
                    controlScale = controlScale
                    )
                }
            }
        }

        // === CABINET SECTION ===
        item {
            SectionHeader(
                title = "Cabinet",
                enabled = boardState.cabBlocks.flatMap { it.controls }.hasAnyMapped(),
                onToggleEnabled = {},
                expanded = cabExpanded,
                onToggleExpanded = { viewModel.toggleCabExpanded() },
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Column {
                    // A/B toggle for cab
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        AbToggle(
                            currentSlot = boardState.cabBlocks.firstOrNull()?.currentSlot ?: AbSlot.A,
                            onSlotSelected = { viewModel.switchCabAbSlot(it) }
                        )
                    }
                    CabSection(
                    cab = boardState.cabBlocks.firstOrNull(),
                    onKnobValueChange = { knob, value ->
                        viewModel.onCabKnobValueChange(knob.id, knob, value)
                    },
                    onToggle = { toggle -> viewModel.onCabToggle(toggle.id, toggle) },
                    onTap = { tap -> viewModel.onTap(tap) },
                    onSelectorChange = { sel, idx ->
                        viewModel.onCabSelectorChange(sel.id, sel, idx)
                    },
                    onFaderValueChange = { fader, value ->
                        viewModel.onCabFaderValueChange(fader.id, fader, value)
                    },
                    onPresetPrev = { nav -> viewModel.onPresetPrev(false, "", nav.id, nav) },
                    onPresetNext = { nav -> viewModel.onPresetNext(false, "", nav.id, nav) },
                    onPadDown = { pad -> viewModel.sendPadOn(pad) },
                    onPadUp = { pad -> viewModel.sendPadOff(pad) },
                    onEditControl = { control -> editCabControl = control },
                    onAddControl = { showAddCabControl = true },
                    onClearAll = { viewModel.clearCabControls() },
                    onCustomize = { customizeTarget = CustomizeTarget.Cab(boardState.cabBlocks.firstOrNull()) },
                    onApplyCabTemplate = { viewModel.applyCabTemplate(it) },
                    onBadgeTap = { control ->
                        mappingControl = MappingControlState(control, section = "cab")
                    },
                    controlScale = controlScale
                )
                }
            }
        }

        // === EFFECTS SECTION ===
        item {
            SectionHeader(
                title = "Effects",
                enabled = boardState.effects.flatMap { it.controls }.hasAnyMapped(),
                onToggleEnabled = {
                    boardState.effects.forEach { viewModel.toggleEffectBlockEnabled(it.id) }
                },
                expanded = effectsExpanded,
                onToggleExpanded = { viewModel.toggleEffectsExpanded() },
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Column {
                    if (boardState.effects.isEmpty()) {
                        EmptySectionHint("No effects added yet")
                    } else {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(boardState.effects, key = { it.id }) { block ->
                                EffectCard(
                                    block = block,
                                    onToggleEnabled = { viewModel.toggleEffectBlockEnabled(block.id) },
                                    onKnobValueChange = { knob, value ->
                                        viewModel.onKnobValueChange(false, block.id, knob.id, knob, value)
                                    },
                                    onToggle = { toggle -> viewModel.onToggle(false, block.id, toggle.id, toggle) },
                                    onTap = { tap -> viewModel.onTap(tap) },
                                    onSelectorChange = { sel, idx ->
                                        viewModel.onSelectorChange(false, block.id, sel.id, sel, idx)
                                    },
                                    onFaderValueChange = { fader, value ->
                                        viewModel.onFaderValueChange(false, block.id, fader.id, fader, value)
                                    },
                                    onPresetPrev = { nav -> viewModel.onPresetPrev(false, block.id, nav.id, nav) },
                                    onPresetNext = { nav -> viewModel.onPresetNext(false, block.id, nav.id, nav) },
                                    onPadDown = { pad -> viewModel.sendPadOn(pad) },
                                    onPadUp = { pad -> viewModel.sendPadOff(pad) },
                                    onAddControl = {
                                        addControlTarget = AddControlTarget(false, block.id)
                                    },
                                    onEditControl = { control ->
                                        editControlState = EditControlState(false, block.id, control)
                                    },
                                    onReorder = { reordered ->
                                        viewModel.reorderControlsInBlock(false, block.id, reordered)
                                    },
                                    onRename = {
                                        renameTarget = RenameTarget(false, block.id, block.name)
                                    },
                                    onCustomize = {
                                        customizeTarget = CustomizeTarget.Block(false, block)
                                    },
                                    onRemove = { viewModel.removeEffectBlock(block.id) },
                                    onAbSwitch = { slot -> viewModel.switchBlockAbSlot(false, block.id, slot) },
                                    onBadgeTap = { control ->
                                        mappingControl = MappingControlState(control, isPedals = false, blockId = block.id, section = "effects")
                                    },
                                    controlScale = controlScale
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    AddButton("ADD EFFECT BLOCK") { viewModel.showAddEffectBlockDialog() }
                }
            }
        }
    }
    }

    // --- Dialogs & Bottom Sheets ---

    // Add pedal block sheet
    if (showAddPedalBlock) {
        AddPedalBlockSheet(
            onAddBlock = { viewModel.addPedalBlock(it) },
            onDismiss = { viewModel.hideAddPedalBlockDialog() }
        )
    }

    // Add effect block sheet
    if (showAddEffectBlock) {
        AddEffectBlockSheet(
            onAddBlock = { viewModel.addEffectBlock(it) },
            onDismiss = { viewModel.hideAddEffectBlockDialog() }
        )
    }

    // Add control to block dialog
    addControlTarget?.let { target ->
        AddEditControlDialog(
            onConfirm = { control: ControlType ->
                viewModel.addControlToBlock(target.isPedals, target.blockId, control)
                addControlTarget = null
            },
            onDismiss = { addControlTarget = null }
        )
    }

    // Edit control in block dialog
    editControlState?.let { state ->
        AddEditControlDialog(
            existingControl = state.control,
            onConfirm = { updated: ControlType ->
                viewModel.updateControlInBlock(state.isPedals, state.blockId, state.control.id, updated)
                editControlState = null
            },
            onDelete = {
                viewModel.removeControlFromBlock(state.isPedals, state.blockId, state.control.id)
                editControlState = null
            },
            onDismiss = { editControlState = null }
        )
    }

    // Add amp control
    if (showAddAmpControl) {
        AddEditControlDialog(
            onConfirm = { control ->
                viewModel.addAmpControl(control)
                showAddAmpControl = false
            },
            onDismiss = { showAddAmpControl = false }
        )
    }

    // Edit amp control
    editAmpControl?.let { control ->
        AddEditControlDialog(
            existingControl = control,
            onConfirm = { updated ->
                viewModel.updateAmpControl(control.id, updated)
                editAmpControl = null
            },
            onDelete = {
                viewModel.removeAmpControl(control.id)
                editAmpControl = null
            },
            onDismiss = { editAmpControl = null }
        )
    }

    // Add cab control
    if (showAddCabControl) {
        AddEditControlDialog(
            onConfirm = { control ->
                viewModel.addCabControl(control)
                showAddCabControl = false
            },
            onDismiss = { showAddCabControl = false }
        )
    }

    // Edit cab control
    editCabControl?.let { control ->
        AddEditControlDialog(
            existingControl = control,
            onConfirm = { updated ->
                viewModel.updateCabControl(control.id, updated)
                editCabControl = null
            },
            onDelete = {
                viewModel.removeCabControl(control.id)
                editCabControl = null
            },
            onDismiss = { editCabControl = null }
        )
    }

    // Rename block dialog
    renameTarget?.let { target ->
        RenameBlockDialog(
            currentName = target.currentName,
            onRename = { newName ->
                if (target.isPedals) {
                    viewModel.renamePedalBlock(target.blockId, newName)
                } else {
                    viewModel.renameEffectBlock(target.blockId, newName)
                }
                renameTarget = null
            },
            onDismiss = { renameTarget = null }
        )
    }

    // Customize block dialog
    customizeTarget?.let { target ->
        val name = when (target) {
            is CustomizeTarget.Block -> if (target.block.name == target.block.id || target.block.name.isBlank()) target.block.type else target.block.name
            is CustomizeTarget.Amp -> "Amplifier"
            is CustomizeTarget.Cab -> "Cabinet"
        }
        val app = when (target) {
            is CustomizeTarget.Block -> target.block.appearance
            is CustomizeTarget.Amp -> target.block?.appearance ?: BlockAppearance()
            is CustomizeTarget.Cab -> target.block?.appearance ?: BlockAppearance()
        }
        val lay = when (target) {
            is CustomizeTarget.Block -> target.block.layoutMode
            is CustomizeTarget.Amp -> target.block?.layoutMode ?: BlockLayout.COMPACT
            is CustomizeTarget.Cab -> target.block?.layoutMode ?: BlockLayout.COMPACT
        }

        CustomizeBlockDialog(
            blockName = name,
            initialAppearance = app,
            initialLayout = lay,
            onConfirm = { appearance, layout ->
                when (target) {
                    is CustomizeTarget.Block -> {
                        if (target.isPedals) viewModel.updatePedalBlockAppearance(target.block.id, appearance, layout)
                        else viewModel.updateEffectBlockAppearance(target.block.id, appearance, layout)
                    }
                    is CustomizeTarget.Amp -> viewModel.updateAmpAppearance(appearance, layout)
                    is CustomizeTarget.Cab -> viewModel.updateCabAppearance(appearance, layout)
                }
                customizeTarget = null
            },
            onDismiss = { customizeTarget = null }
        )
    }

    // Onboarding dialog
    if (showOnboarding) {
        OnboardingDialog(
            onSelectTemplate = { template ->
                applyOnboardingTemplate(template, viewModel)
                viewModel.hideOnboarding()
            },
            onStartEmpty = { viewModel.hideOnboarding() }
        )
    }

    // Mapping dialog (from "!" badge tap)
    mappingControl?.let { state ->
        MappingDialog(
            control = state.control,
            onSave = { ccNumber ->
                val updated = updateControlCcNumber(state.control, ccNumber)
                if (updated != null) {
                    when {
                        state.section == "amp" -> viewModel.updateAmpControl(state.control.id, updated)
                        state.section == "cab" -> viewModel.updateCabControl(state.control.id, updated)
                        state.isPedals -> viewModel.updateControlInBlock(true, state.blockId, state.control.id, updated)
                        else -> viewModel.updateControlInBlock(false, state.blockId, state.control.id, updated)
                    }
                }
                mappingControl = null
            },
            onDismiss = { mappingControl = null }
        )
    }
}

// --- Helper data classes for dialog state ---

private data class AddControlTarget(val isPedals: Boolean, val blockId: String)
private data class EditControlState(val isPedals: Boolean, val blockId: String, val control: ControlType)
private data class RenameTarget(val isPedals: Boolean, val blockId: String, val currentName: String)

private sealed class CustomizeTarget {
    data class Block(val isPedals: Boolean, val block: com.gearboard.domain.model.ControlBlock) : CustomizeTarget()
    data class Amp(val block: AmpBlock?) : CustomizeTarget()
    data class Cab(val block: CabBlock?) : CustomizeTarget()
}

private data class MappingControlState(
    val control: ControlType,
    val isPedals: Boolean = false,
    val blockId: String = "",
    val section: String = "" // "amp", "cab", "pedals", "effects"
)

// --- Helper composables ---

@Composable
private fun EmptySectionHint(text: String) {
    Text(
        text = text,
        color = GearBoardColors.TextDisabled,
        fontSize = 12.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun AddButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = GearBoardColors.SurfaceElevated,
            contentColor = GearBoardColors.Accent
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
        Spacer(Modifier.padding(start = 8.dp))
        Text(text, fontSize = 12.sp, letterSpacing = 1.sp)
    }
}

// --- Onboarding template application ---

private fun applyOnboardingTemplate(template: OnboardingTemplate, viewModel: BoardViewModel) {
    when (template) {
        OnboardingTemplate.GUITAR_AMP_SIM -> {
            // Pedals: Overdrive block
            viewModel.addPedalBlock(
                com.gearboard.domain.model.ControlBlock(
                    name = "Overdrive",
                    type = "Distortion",
                    controls = listOf(
                        ControlType.Knob(label = "Drive", ccNumber = 0),
                        ControlType.Knob(label = "Tone", ccNumber = 0),
                        ControlType.Knob(label = "Level", ccNumber = 0),
                        ControlType.Toggle(label = "Boost", ccNumber = 0),
                        ControlType.Toggle(label = "On/Off", ccNumber = 0)
                    )
                )
            )
            // Amp controls
            listOf(
                ControlType.Knob(label = "Gain", ccNumber = 0),
                ControlType.Knob(label = "Bass", ccNumber = 0),
                ControlType.Knob(label = "Mid", ccNumber = 0),
                ControlType.Knob(label = "Treble", ccNumber = 0),
                ControlType.Knob(label = "Presence", ccNumber = 0),
                ControlType.Knob(label = "Master", ccNumber = 0),
                ControlType.Toggle(label = "Channel", ccNumber = 0, pulseMode = false)
            ).forEach { viewModel.addAmpControl(it) }

            // Cab controls
            listOf(
                ControlType.Selector(label = "Model", ccNumber = 0, positions = listOf("4x12", "2x12")),
                ControlType.Selector(label = "Mic", ccNumber = 0, positions = listOf("SM57", "MD421")),
                ControlType.Fader(label = "Position", ccNumber = 0)
            ).forEach { viewModel.addCabControl(it) }

            // Effects: Delay block
            viewModel.addEffectBlock(
                com.gearboard.domain.model.ControlBlock(
                    name = "Delay",
                    type = "Time",
                    controls = listOf(
                        ControlType.Knob(label = "Time", ccNumber = 0),
                        ControlType.Knob(label = "Feedback", ccNumber = 0),
                        ControlType.Knob(label = "Mix", ccNumber = 0),
                        ControlType.Tap(label = "Tap Tempo", ccNumber = 0),
                        ControlType.Toggle(label = "On/Off", ccNumber = 0)
                    )
                )
            )
        }
        OnboardingTemplate.BASS_AMP_SIM -> {
            // Pedals: Overdrive block
            viewModel.addPedalBlock(
                com.gearboard.domain.model.ControlBlock(
                    name = "Overdrive",
                    type = "Bass OD",
                    controls = listOf(
                        ControlType.Knob(label = "Drive", ccNumber = 0),
                        ControlType.Knob(label = "Blend", ccNumber = 0),
                        ControlType.Knob(label = "Level", ccNumber = 0),
                        ControlType.Toggle(label = "On/Off", ccNumber = 0)
                    )
                )
            )
            // Amp controls
            listOf(
                ControlType.Knob(label = "Gain", ccNumber = 0),
                ControlType.Knob(label = "Bass", ccNumber = 0),
                ControlType.Knob(label = "Mid", ccNumber = 0),
                ControlType.Knob(label = "Treble", ccNumber = 0),
                ControlType.Knob(label = "Master", ccNumber = 0)
            ).forEach { viewModel.addAmpControl(it) }

            // Cab controls
            listOf(
                ControlType.Selector(label = "Model", ccNumber = 0, positions = listOf("8x10", "4x10")),
                ControlType.Selector(label = "Mic", ccNumber = 0, positions = listOf("RE20", "U47"))
            ).forEach { viewModel.addCabControl(it) }
        }
        OnboardingTemplate.MULTI_FX_BOARD -> {
            // Pedals: Compressor + Overdrive
            viewModel.addPedalBlock(
                com.gearboard.domain.model.ControlBlock(
                    name = "Compressor", type = "Dynamics",
                    controls = listOf(
                        ControlType.Knob(label = "Threshold", ccNumber = 0),
                        ControlType.Knob(label = "Ratio", ccNumber = 0),
                        ControlType.Knob(label = "Level", ccNumber = 0),
                        ControlType.Toggle(label = "On/Off", ccNumber = 0)
                    )
                )
            )
            viewModel.addPedalBlock(
                com.gearboard.domain.model.ControlBlock(
                    name = "Overdrive", type = "Distortion",
                    controls = listOf(
                        ControlType.Knob(label = "Drive", ccNumber = 0),
                        ControlType.Knob(label = "Tone", ccNumber = 0),
                        ControlType.Knob(label = "Level", ccNumber = 0),
                        ControlType.Toggle(label = "On/Off", ccNumber = 0)
                    )
                )
            )
            // Amp
            listOf(
                ControlType.Knob(label = "Gain", ccNumber = 0),
                ControlType.Knob(label = "Bass", ccNumber = 0),
                ControlType.Knob(label = "Mid", ccNumber = 0),
                ControlType.Knob(label = "Treble", ccNumber = 0),
                ControlType.Knob(label = "Master", ccNumber = 0)
            ).forEach { viewModel.addAmpControl(it) }
            // Effects: Chorus + Delay + Reverb
            viewModel.addEffectBlock(
                com.gearboard.domain.model.ControlBlock(
                    name = "Chorus", type = "Modulation",
                    controls = listOf(
                        ControlType.Knob(label = "Rate", ccNumber = 0),
                        ControlType.Knob(label = "Depth", ccNumber = 0),
                        ControlType.Knob(label = "Mix", ccNumber = 0),
                        ControlType.Toggle(label = "On/Off", ccNumber = 0)
                    )
                )
            )
            viewModel.addEffectBlock(
                com.gearboard.domain.model.ControlBlock(
                    name = "Delay", type = "Time",
                    controls = listOf(
                        ControlType.Knob(label = "Time", ccNumber = 0),
                        ControlType.Knob(label = "Feedback", ccNumber = 0),
                        ControlType.Knob(label = "Mix", ccNumber = 0),
                        ControlType.Tap(label = "Tap Tempo", ccNumber = 0),
                        ControlType.Toggle(label = "On/Off", ccNumber = 0)
                    )
                )
            )
            viewModel.addEffectBlock(
                com.gearboard.domain.model.ControlBlock(
                    name = "Reverb", type = "Reverb",
                    controls = listOf(
                        ControlType.Knob(label = "Size", ccNumber = 0),
                        ControlType.Knob(label = "Decay", ccNumber = 0),
                        ControlType.Knob(label = "Mix", ccNumber = 0),
                        ControlType.Toggle(label = "On/Off", ccNumber = 0)
                    )
                )
            )
        }
    }
}

/** Create a copy of a control with an updated ccNumber. */
private fun updateControlCcNumber(control: ControlType, ccNumber: Int): ControlType? {
    return when (control) {
        is ControlType.Knob -> control.copy(ccNumber = ccNumber)
        is ControlType.Toggle -> control.copy(ccNumber = ccNumber)
        is ControlType.Tap -> control.copy(ccNumber = ccNumber)
        is ControlType.Selector -> control.copy(ccNumber = ccNumber)
        is ControlType.Fader -> control.copy(ccNumber = ccNumber)
        else -> null
    }
}

private fun List<ControlType>.hasAnyMapped(): Boolean = any { control ->
    when (control) {
        is ControlType.Knob -> control.ccNumber != 0
        is ControlType.Toggle -> control.ccNumber != 0
        is ControlType.Tap -> control.ccNumber != 0
        is ControlType.Selector -> control.ccNumber != 0
        is ControlType.Fader -> control.ccNumber != 0
        is ControlType.PresetNav, is ControlType.Pad -> true
    }
}

@Composable
fun MidiChannelChip(channel: Int, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        color = GearBoardColors.SurfaceVariant,
        modifier = Modifier.height(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 6.dp)
        ) {
            Text(
                text = if (channel == 0) "OMNI" else "CH $channel",
                style = MaterialTheme.typography.labelSmall,
                color = GearBoardColors.TextSecondary
            )
        }
    }
}
