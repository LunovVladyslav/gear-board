package com.gearboard.ui.screens.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gearboard.data.local.entity.ControlItemEntity
import com.gearboard.data.repository.BoardRepository
import com.gearboard.data.repository.ControlRepository
import com.gearboard.data.repository.MidiMappingRepository
import com.gearboard.data.repository.SettingsRepository
import com.gearboard.domain.UndoRedoManager
import com.gearboard.domain.model.AbSlot
import com.gearboard.domain.model.AmpBlock
import com.gearboard.domain.model.BlockAppearance
import com.gearboard.domain.model.BlockLayout
import com.gearboard.domain.model.BoardCommand
import com.gearboard.domain.model.BoardState
import com.gearboard.domain.model.CabBlock
import com.gearboard.domain.model.CCAssigner
import com.gearboard.domain.model.ControlBlock
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.SectionType
import com.gearboard.domain.model.selectorCCValue
import com.gearboard.midi.GearBoardMidiManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val boardRepository: BoardRepository,
    private val controlRepository: ControlRepository,
    private val midiMappingRepository: MidiMappingRepository,
    private val midiManager: GearBoardMidiManager,
    private val settingsRepository: SettingsRepository,
    private val undoRedoManager: UndoRedoManager
) : ViewModel() {

    val boardState: StateFlow<BoardState> = boardRepository.boardState

    /**
     * In-memory cache of CC assignments from the midi_mappings table.
     * key = controlId (String), value = ccNumber
     * Kept current by observing getAllMappings(). Used in sendControlMidi to
     * resolve the correct CC even when ControlType.ccNumber is still 0.
     */
    private val ccAssignments = MutableStateFlow<Map<String, Int>>(emptyMap())

    val controlSize = settingsRepository.controlSize
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1.0f)

    val globalMidiChannel: StateFlow<Int> = settingsRepository.midiChannel
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun setGlobalMidiChannel(channel: Int) {
        viewModelScope.launch {
            settingsRepository.setMidiChannel(channel)
        }
    }

    // Section expanded states
    private val _pedalsExpanded = MutableStateFlow(true)
    val pedalsExpanded: StateFlow<Boolean> = _pedalsExpanded.asStateFlow()

    private val _ampExpanded = MutableStateFlow(true)
    val ampExpanded: StateFlow<Boolean> = _ampExpanded.asStateFlow()

    private val _cabExpanded = MutableStateFlow(true)
    val cabExpanded: StateFlow<Boolean> = _cabExpanded.asStateFlow()

    private val _effectsExpanded = MutableStateFlow(true)
    val effectsExpanded: StateFlow<Boolean> = _effectsExpanded.asStateFlow()

    // Dialog states
    private val _showAddPedalBlockDialog = MutableStateFlow(false)
    val showAddPedalBlockDialog: StateFlow<Boolean> = _showAddPedalBlockDialog.asStateFlow()

    private val _showAddEffectBlockDialog = MutableStateFlow(false)
    val showAddEffectBlockDialog: StateFlow<Boolean> = _showAddEffectBlockDialog.asStateFlow()

    private val _showAddAmpBlockDialog = MutableStateFlow(false)
    val showAddAmpBlockDialog: StateFlow<Boolean> = _showAddAmpBlockDialog.asStateFlow()

    private val _showAddCabBlockDialog = MutableStateFlow(false)
    val showAddCabBlockDialog: StateFlow<Boolean> = _showAddCabBlockDialog.asStateFlow()

    private val _showOnboarding = MutableStateFlow(false)
    val showOnboarding: StateFlow<Boolean> = _showOnboarding.asStateFlow()

    // Undo/redo
    val canUndo: StateFlow<Boolean> = undoRedoManager.canUndo
    val canRedo: StateFlow<Boolean> = undoRedoManager.canRedo

    private val _lastUndoDescription = MutableStateFlow<String?>(null)
    val lastUndoDescription: StateFlow<String?> = _lastUndoDescription.asStateFlow()

    fun clearUndoDescription() { _lastUndoDescription.value = null }

    // Auto-save debounce
    private var autoSaveJob: Job? = null

    // Debounced undo push for knob/fader (avoids flooding undo stack during drag)
    private val undoPendingJobs = java.util.concurrent.ConcurrentHashMap<String, Job>()
    private val undoDebounceMs = 500L
    private val undoCapturedOldValues = java.util.concurrent.ConcurrentHashMap<String, Float>()

    init {
        loadPersistedState()
        // Keep ccAssignments current so sendControlMidi always uses the right CC
        viewModelScope.launch {
            midiMappingRepository.getAllMappings().collect { mappings ->
                ccAssignments.value = mappings
                    .filter { it.ccNumber > 0 }
                    .associate { it.controlId to it.ccNumber }
            }
        }
    }

    // --- Section expand/collapse ---
    fun togglePedalsExpanded() { _pedalsExpanded.value = !_pedalsExpanded.value }
    fun toggleAmpExpanded() { _ampExpanded.value = !_ampExpanded.value }
    fun toggleCabExpanded() { _cabExpanded.value = !_cabExpanded.value }
    fun toggleEffectsExpanded() { _effectsExpanded.value = !_effectsExpanded.value }

    // --- Pedal Blocks ---
    fun addPedalBlock(block: ControlBlock) {
        val usedCCs = collectUsedCCs()
        val assigned = CCAssigner.assignBlock(migratePedalBlock(block), "pedals", usedCCs)
        undoRedoManager.push(BoardCommand.BlockAdded(SectionType.PEDALS, assigned))
        boardRepository.addPedalBlock(assigned)
        _showAddPedalBlockDialog.value = false
        triggerAutoSave()
    }

    fun removePedalBlock(blockId: String) {
        val state = boardRepository.getCurrentState()
        val block = state.pedals.find { it.id == blockId }
        if (block != null) {
            val position = state.pedals.indexOf(block)
            undoRedoManager.push(BoardCommand.BlockRemoved(SectionType.PEDALS, block, position))
        }
        boardRepository.removePedalBlock(blockId)
        triggerAutoSave()
    }

    fun renamePedalBlock(blockId: String, newName: String) {
        boardRepository.renamePedalBlock(blockId, newName)
        triggerAutoSave()
    }

    fun togglePedalBlockEnabled(blockId: String) {
        val state = boardRepository.getCurrentState()
        state.pedals.find { it.id == blockId }?.let { block ->
            boardRepository.updatePedalBlock(block.copy(enabled = !block.enabled))
        }
    }

    fun updatePedalBlockAppearance(blockId: String, appearance: BlockAppearance, layout: BlockLayout) {
        val state = boardRepository.getCurrentState()
        state.pedals.find { it.id == blockId }?.let { block ->
            boardRepository.updatePedalBlock(block.copy(appearance = appearance, layoutMode = layout))
            triggerAutoSave()
        }
    }

    // --- Effect Blocks ---
    fun addEffectBlock(block: ControlBlock) {
        val usedCCs = collectUsedCCs()
        val assigned = CCAssigner.assignBlock(migratePedalBlock(block), "effects", usedCCs)
        undoRedoManager.push(BoardCommand.BlockAdded(SectionType.EFFECTS, assigned))
        boardRepository.addEffectBlock(assigned)
        _showAddEffectBlockDialog.value = false
        triggerAutoSave()
    }

    fun removeEffectBlock(blockId: String) {
        val state = boardRepository.getCurrentState()
        val block = state.effects.find { it.id == blockId }
        if (block != null) {
            val position = state.effects.indexOf(block)
            undoRedoManager.push(BoardCommand.BlockRemoved(SectionType.EFFECTS, block, position))
        }
        boardRepository.removeEffectBlock(blockId)
        triggerAutoSave()
    }

    fun renameEffectBlock(blockId: String, newName: String) {
        boardRepository.renameEffectBlock(blockId, newName)
        triggerAutoSave()
    }

    fun toggleEffectBlockEnabled(blockId: String) {
        val state = boardRepository.getCurrentState()
        state.effects.find { it.id == blockId }?.let { block ->
            boardRepository.updateEffectBlock(block.copy(enabled = !block.enabled))
        }
    }

    fun updateEffectBlockAppearance(blockId: String, appearance: BlockAppearance, layout: BlockLayout) {
        val state = boardRepository.getCurrentState()
        state.effects.find { it.id == blockId }?.let { block ->
            boardRepository.updateEffectBlock(block.copy(appearance = appearance, layoutMode = layout))
            triggerAutoSave()
        }
    }

    // --- Amp blocks ---
    fun addAmpBlock(block: AmpBlock) {
        val assigned = CCAssigner.assignAmp(block, collectUsedCCs())
        undoRedoManager.push(BoardCommand.BlockAdded(SectionType.AMP, assigned))
        boardRepository.addAmpBlock(assigned)
        _showAddAmpBlockDialog.value = false
        triggerAutoSave()
    }

    fun removeAmpBlock(blockId: String) {
        val state = boardRepository.getCurrentState()
        val block = state.ampBlocks.find { it.id == blockId }
        if (block != null) {
            val position = state.ampBlocks.indexOf(block)
            undoRedoManager.push(BoardCommand.BlockRemoved(SectionType.AMP, block, position))
        }
        boardRepository.removeAmpBlock(blockId)
        triggerAutoSave()
    }

    fun renameAmpBlock(blockId: String, name: String) {
        boardRepository.renameAmpBlock(blockId, name)
        triggerAutoSave()
    }

    fun switchAmpBlockAbSlot(blockId: String, slot: AbSlot) {
        boardRepository.switchAmpBlockAbSlot(blockId, slot)
        boardRepository.boardState.value.ampBlocks.find { it.id == blockId }?.controls?.forEach { sendControlMidi(it) }
        triggerAutoSave()
    }

    fun addAmpBlockControl(blockId: String, control: ControlType) {
        boardRepository.addControlToAmpBlock(blockId, control)
        triggerAutoSave()
    }

    fun updateAmpBlockControl(blockId: String, controlId: String, updated: ControlType) {
        boardRepository.updateControlInAmpBlock(blockId, controlId, updated)
        triggerAutoSave()
    }

    fun removeAmpBlockControl(blockId: String, controlId: String) {
        boardRepository.removeControlFromAmpBlock(blockId, controlId)
        triggerAutoSave()
    }

    fun updateAmpBlockAppearance(blockId: String, appearance: BlockAppearance, layout: BlockLayout) {
        val block = boardRepository.getCurrentState().ampBlocks.find { it.id == blockId } ?: return
        boardRepository.updateAmpBlock(block.copy(appearance = appearance, layoutMode = layout))
        triggerAutoSave()
    }

    // --- Cab blocks ---
    fun addCabBlock(block: CabBlock) {
        val assigned = CCAssigner.assignCab(block, collectUsedCCs())
        undoRedoManager.push(BoardCommand.BlockAdded(SectionType.CAB, assigned))
        boardRepository.addCabBlock(assigned)
        _showAddCabBlockDialog.value = false
        triggerAutoSave()
    }

    fun removeCabBlock(blockId: String) {
        val state = boardRepository.getCurrentState()
        val block = state.cabBlocks.find { it.id == blockId }
        if (block != null) {
            val position = state.cabBlocks.indexOf(block)
            undoRedoManager.push(BoardCommand.BlockRemoved(SectionType.CAB, block, position))
        }
        boardRepository.removeCabBlock(blockId)
        triggerAutoSave()
    }

    fun renameCabBlock(blockId: String, name: String) {
        boardRepository.renameCabBlock(blockId, name)
        triggerAutoSave()
    }

    fun switchCabBlockAbSlot(blockId: String, slot: AbSlot) {
        boardRepository.switchCabBlockAbSlot(blockId, slot)
        boardRepository.boardState.value.cabBlocks.find { it.id == blockId }?.controls?.forEach { sendControlMidi(it) }
        triggerAutoSave()
    }

    fun addCabBlockControl(blockId: String, control: ControlType) {
        boardRepository.addControlToCabBlock(blockId, control)
        triggerAutoSave()
    }

    fun updateCabBlockControl(blockId: String, controlId: String, updated: ControlType) {
        boardRepository.updateControlInCabBlock(blockId, controlId, updated)
        triggerAutoSave()
    }

    fun removeCabBlockControl(blockId: String, controlId: String) {
        boardRepository.removeControlFromCabBlock(blockId, controlId)
        triggerAutoSave()
    }

    fun updateCabBlockAppearance(blockId: String, appearance: BlockAppearance, layout: BlockLayout) {
        val block = boardRepository.getCurrentState().cabBlocks.find { it.id == blockId } ?: return
        boardRepository.updateCabBlock(block.copy(appearance = appearance, layoutMode = layout))
        triggerAutoSave()
    }

    // --- Control CRUD (block-level) ---
    fun addControlToBlock(isPedals: Boolean, blockId: String, control: ControlType) {
        boardRepository.addControlToBlock(isPedals, blockId, control)
        triggerAutoSave()
    }

    fun removeControlFromBlock(isPedals: Boolean, blockId: String, controlId: String) {
        boardRepository.removeControlFromBlock(isPedals, blockId, controlId)
        triggerAutoSave()
    }

    fun updateControlInBlock(isPedals: Boolean, blockId: String, controlId: String, updatedControl: ControlType) {
        boardRepository.updateControlInBlock(isPedals, blockId, controlId, updatedControl)
        triggerAutoSave()
    }

    fun reorderControlsInBlock(isPedals: Boolean, blockId: String, reordered: List<ControlType>) {
        boardRepository.reorderControlsInBlock(isPedals, blockId, reordered)
        triggerAutoSave()
    }

    // --- MIDI Sending ---

    /**
     * Unified MIDI send for any ControlType interaction.
     *
     * NOTE: ccNumberA and ccNumberB are intentionally not used for routing here.
     * A/B bank uses ONE CC number per control with TWO saved values (not two CC numbers).
     * BoardRepository.switchBlockAbSlot() restores control.value from stateA/stateB,
     * so sendControlMidi always reads the correct post-switch value automatically.
     */
    fun sendControlMidi(control: ControlType) {
        when (control) {
            is ControlType.Knob -> {
                val cc = ccAssignments.value[control.id] ?: control.ccNumber
                if (cc <= 0) {
                    android.util.Log.w("BoardViewModel", "sendControlMidi: skipping Knob ${control.id} with cc=$cc")
                    return
                }
                val midiValue = (control.value * 127f).toInt().coerceIn(0, 127)
                midiManager.sendControlChange(cc, midiValue, control.midiChannel)
            }
            is ControlType.Toggle -> {
                val cc = ccAssignments.value[control.id] ?: control.ccNumber
                if (cc <= 0) {
                    android.util.Log.w("BoardViewModel", "sendControlMidi: skipping Toggle ${control.id} with cc=$cc")
                    return
                }
                // Momentary mode: pulse 127 → 0 after 50 ms (hold-style bypass trigger).
                // Stomp and regular toggles: latching — send 127 when ON, 0 when OFF.
                // (Plugins like Neural DSP use threshold: ≥64 = active, <64 = bypass)
                if (control.momentaryMode) {
                    midiManager.sendControlChange(cc, 127, control.midiChannel)
                    viewModelScope.launch {
                        delay(50)
                        midiManager.sendControlChange(cc, 0, control.midiChannel)
                    }
                } else {
                    midiManager.sendControlChange(
                        cc,
                        if (control.isOn) 127 else 0,
                        control.midiChannel
                    )
                }
            }
            is ControlType.Tap -> {
                val cc = ccAssignments.value[control.id] ?: control.ccNumber
                if (cc <= 0) {
                    android.util.Log.w("BoardViewModel", "sendControlMidi: skipping Tap ${control.id} with cc=$cc")
                    return
                }
                midiManager.sendControlChange(cc, 127, control.midiChannel)
            }
            is ControlType.Selector -> {
                val cc = ccAssignments.value[control.id] ?: control.ccNumber
                if (cc <= 0) {
                    android.util.Log.w("BoardViewModel", "sendControlMidi: skipping Selector ${control.id} with cc=$cc")
                    return
                }
                val value = selectorCCValue(control.selectedIndex, control.positions.size, control.ccValues)
                midiManager.sendControlChange(cc, value, control.midiChannel)
            }
            is ControlType.Fader -> {
                val cc = ccAssignments.value[control.id] ?: control.ccNumber
                if (cc <= 0) {
                    android.util.Log.w("BoardViewModel", "sendControlMidi: skipping Fader ${control.id} with cc=$cc")
                    return
                }
                val midiValue = (control.value * 127f).toInt().coerceIn(0, 127)
                midiManager.sendControlChange(cc, midiValue, control.midiChannel)
            }
            is ControlType.PresetNav -> {
                midiManager.sendProgramChange(control.currentPreset, control.midiChannel)
            }
            is ControlType.Pad -> {
                // Note On handled in sendPadOn/sendPadOff
            }
        }
    }

    fun sendPadOn(pad: ControlType.Pad) {
        midiManager.sendNoteOn(pad.noteNumber, pad.velocity, pad.midiChannel)
    }

    fun sendPadOff(pad: ControlType.Pad) {
        midiManager.sendNoteOff(pad.noteNumber, pad.midiChannel)
    }

    // --- Knob/Fader/Toggle/Selector value updates (with MIDI send) ---

    fun onKnobValueChange(isPedals: Boolean, blockId: String, controlId: String, knob: ControlType.Knob, newValue: Float) {
        val updated = knob.copy(value = newValue)
        if (blockId.isNotEmpty()) {
            updateControlInBlock(isPedals, blockId, controlId, updated)
        }
        sendControlMidi(updated)

        // Capture old value only on first move (before debounce fires)
        if (!undoCapturedOldValues.containsKey(controlId)) {
            undoCapturedOldValues[controlId] = knob.value
        }

        // Cancel any pending undo push for this control and schedule a new one
        undoPendingJobs[controlId]?.cancel()
        undoPendingJobs[controlId] = viewModelScope.launch {
            delay(undoDebounceMs)
            val capturedOld = undoCapturedOldValues.remove(controlId) ?: return@launch
            if (kotlin.math.abs(newValue - capturedOld) > 0.005f) {
                undoRedoManager.push(BoardCommand.ControlValueChanged(
                    blockId = blockId,
                    section = if (isPedals) SectionType.PEDALS else SectionType.EFFECTS,
                    controlId = controlId,
                    oldValue = capturedOld,
                    newValue = newValue
                ))
            }
            undoPendingJobs.remove(controlId)
        }
        triggerAutoSave()
    }

    fun onFaderValueChange(isPedals: Boolean, blockId: String, controlId: String, fader: ControlType.Fader, newValue: Float) {
        val updated = fader.copy(value = newValue)
        if (blockId.isNotEmpty()) {
            updateControlInBlock(isPedals, blockId, controlId, updated)
        }
        sendControlMidi(updated)

        if (!undoCapturedOldValues.containsKey(controlId)) {
            undoCapturedOldValues[controlId] = fader.value
        }

        undoPendingJobs[controlId]?.cancel()
        undoPendingJobs[controlId] = viewModelScope.launch {
            delay(undoDebounceMs)
            val capturedOld = undoCapturedOldValues.remove(controlId) ?: return@launch
            if (kotlin.math.abs(newValue - capturedOld) > 0.005f) {
                undoRedoManager.push(BoardCommand.ControlValueChanged(
                    blockId = blockId,
                    section = if (isPedals) SectionType.PEDALS else SectionType.EFFECTS,
                    controlId = controlId,
                    oldValue = capturedOld,
                    newValue = newValue
                ))
            }
            undoPendingJobs.remove(controlId)
        }
        triggerAutoSave()
    }

    fun onToggle(isPedals: Boolean, blockId: String, controlId: String, toggle: ControlType.Toggle) {
        val updated = toggle.copy(isOn = !toggle.isOn)
        if (blockId.isNotEmpty()) {
            updateControlInBlock(isPedals, blockId, controlId, updated)
        }
        sendControlMidi(updated)
    }

    fun onSelectorChange(isPedals: Boolean, blockId: String, controlId: String, selector: ControlType.Selector, newIndex: Int) {
        val updated = selector.copy(selectedIndex = newIndex)
        if (blockId.isNotEmpty()) {
            updateControlInBlock(isPedals, blockId, controlId, updated)
        }
        sendControlMidi(updated)
    }

    fun onTap(control: ControlType.Tap) {
        sendControlMidi(control)
    }

    fun onPresetNext(isPedals: Boolean, blockId: String, controlId: String, presetNav: ControlType.PresetNav) {
        val next = (presetNav.currentPreset + 1).coerceAtMost(127)
        val updated = presetNav.copy(currentPreset = next)
        if (blockId.isNotEmpty()) {
            updateControlInBlock(isPedals, blockId, controlId, updated)
        }
        sendControlMidi(updated)
    }

    fun onPresetPrev(isPedals: Boolean, blockId: String, controlId: String, presetNav: ControlType.PresetNav) {
        val prev = (presetNav.currentPreset - 1).coerceAtLeast(0)
        val updated = presetNav.copy(currentPreset = prev)
        if (blockId.isNotEmpty()) {
            updateControlInBlock(isPedals, blockId, controlId, updated)
        }
        sendControlMidi(updated)
    }

    // --- Undo / Redo ---

    fun undo() {
        when (val cmd = undoRedoManager.undo()) {
            is BoardCommand.ControlValueChanged -> {
                _lastUndoDescription.value = "Undo: value change on control"
                val isPedals = cmd.section == SectionType.PEDALS
                val state = boardRepository.getCurrentState()
                val blocks = if (isPedals) state.pedals else state.effects
                blocks.find { it.id == cmd.blockId }?.controls?.find { it.id == cmd.controlId }?.let { control ->
                    val restored = when (control) {
                        is ControlType.Knob -> control.copy(value = cmd.oldValue)
                        is ControlType.Fader -> control.copy(value = cmd.oldValue)
                        else -> control
                    }
                    boardRepository.updateControlInBlock(isPedals, cmd.blockId, cmd.controlId, restored)
                    sendControlMidi(restored)
                }
            }
            is BoardCommand.BlockAdded -> {
                _lastUndoDescription.value = "Undo: block added"
                when (cmd.section) {
                    SectionType.PEDALS -> boardRepository.removePedalBlock((cmd.block as ControlBlock).id)
                    SectionType.EFFECTS -> boardRepository.removeEffectBlock((cmd.block as ControlBlock).id)
                    SectionType.AMP -> boardRepository.removeAmpBlock((cmd.block as AmpBlock).id)
                    SectionType.CAB -> boardRepository.removeCabBlock((cmd.block as CabBlock).id)
                }
            }
            is BoardCommand.BlockRemoved -> {
                _lastUndoDescription.value = "Undo: block removed"
                when (cmd.section) {
                    SectionType.PEDALS -> boardRepository.addPedalBlock(cmd.block as ControlBlock)
                    SectionType.EFFECTS -> boardRepository.addEffectBlock(cmd.block as ControlBlock)
                    SectionType.AMP -> boardRepository.addAmpBlock(cmd.block as AmpBlock)
                    SectionType.CAB -> boardRepository.addCabBlock(cmd.block as CabBlock)
                }
            }
            else -> {}
        }
        triggerAutoSave()
    }

    fun redo() {
        when (val cmd = undoRedoManager.redo()) {
            is BoardCommand.ControlValueChanged -> {
                val isPedals = cmd.section == SectionType.PEDALS
                val state = boardRepository.getCurrentState()
                val blocks = if (isPedals) state.pedals else state.effects
                blocks.find { it.id == cmd.blockId }?.controls?.find { it.id == cmd.controlId }?.let { control ->
                    val restored = when (control) {
                        is ControlType.Knob -> control.copy(value = cmd.newValue)
                        is ControlType.Fader -> control.copy(value = cmd.newValue)
                        else -> control
                    }
                    boardRepository.updateControlInBlock(isPedals, cmd.blockId, cmd.controlId, restored)
                    sendControlMidi(restored)
                }
            }
            is BoardCommand.BlockAdded -> {
                when (cmd.section) {
                    SectionType.PEDALS -> boardRepository.addPedalBlock(cmd.block as ControlBlock)
                    SectionType.EFFECTS -> boardRepository.addEffectBlock(cmd.block as ControlBlock)
                    SectionType.AMP -> boardRepository.addAmpBlock(cmd.block as AmpBlock)
                    SectionType.CAB -> boardRepository.addCabBlock(cmd.block as CabBlock)
                }
            }
            is BoardCommand.BlockRemoved -> {
                when (cmd.section) {
                    SectionType.PEDALS -> boardRepository.removePedalBlock((cmd.block as ControlBlock).id)
                    SectionType.EFFECTS -> boardRepository.removeEffectBlock((cmd.block as ControlBlock).id)
                    SectionType.AMP -> boardRepository.removeAmpBlock((cmd.block as AmpBlock).id)
                    SectionType.CAB -> boardRepository.removeCabBlock((cmd.block as CabBlock).id)
                }
            }
            else -> {}
        }
        triggerAutoSave()
    }

    // --- Dialog visibility ---
    fun showAddPedalBlockDialog() { _showAddPedalBlockDialog.value = true }
    fun hideAddPedalBlockDialog() { _showAddPedalBlockDialog.value = false }
    fun showAddEffectBlockDialog() { _showAddEffectBlockDialog.value = true }
    fun hideAddEffectBlockDialog() { _showAddEffectBlockDialog.value = false }
    fun showAddAmpBlockDialog() { _showAddAmpBlockDialog.value = true }
    fun hideAddAmpBlockDialog() { _showAddAmpBlockDialog.value = false }
    fun showAddCabBlockDialog() { _showAddCabBlockDialog.value = true }
    fun hideAddCabBlockDialog() { _showAddCabBlockDialog.value = false }
    fun hideOnboarding() { _showOnboarding.value = false }

    // --- Persistence ---

    private fun triggerAutoSave() {
        autoSaveJob?.cancel()
        autoSaveJob = viewModelScope.launch {
            delay(300)
            persistCurrentState()
        }
    }

    private suspend fun persistCurrentState() {
        val state = boardRepository.getCurrentState()
        val entities = mutableListOf<ControlItemEntity>()

        state.pedals.forEach { block ->
            block.controls.forEachIndexed { idx, control ->
                entities.add(controlRepository.toEntity(control, SectionType.PEDALS, block.id, idx, blockName = block.name))
            }
        }
        state.ampBlocks.forEach { block ->
            block.controls.forEachIndexed { idx, control ->
                entities.add(controlRepository.toEntity(control, SectionType.AMP, block.id, idx, blockName = block.name))
            }
        }
        state.cabBlocks.forEach { block ->
            block.controls.forEachIndexed { idx, control ->
                entities.add(controlRepository.toEntity(control, SectionType.CAB, block.id, idx, blockName = block.name))
            }
        }
        state.effects.forEach { block ->
            block.controls.forEachIndexed { idx, control ->
                entities.add(controlRepository.toEntity(control, SectionType.EFFECTS, block.id, idx, blockName = block.name))
            }
        }

        val activeIds = entities.map { it.stableId }
        controlRepository.upsertAll(entities)
        if (activeIds.isNotEmpty()) {
            controlRepository.deleteOrphans(activeIds)
        } else {
            controlRepository.deleteAll()
        }
    }

    private fun loadPersistedState() {
        viewModelScope.launch {
            if (controlRepository.isEmpty()) {
                _showOnboarding.value = true
                return@launch
            }

            val allEntities = controlRepository.getAllControlsOnce()
            if (allEntities.isEmpty()) {
                _showOnboarding.value = true
                return@launch
            }

            val bySection = allEntities.groupBy { it.sectionType }

            val pedalEntities = bySection[SectionType.PEDALS.name] ?: emptyList()
            val pedalBlocks = pedalEntities.groupBy { it.blockId }.map { (blockId, entities) ->
                val rawBlock = ControlBlock(
                    id = blockId,
                    name = entities.first().blockName.ifEmpty { blockId },
                    controls = entities.sortedBy { it.sortOrder }.map { controlRepository.toDomain(it) }
                )
                migratePedalBlock(rawBlock)
            }

            val ampEntities = bySection[SectionType.AMP.name] ?: emptyList()
            val ampBlocks = ampEntities.groupBy { it.blockId }.map { (blockId, entities) ->
                AmpBlock(
                    id = blockId,
                    name = entities.first().blockName.ifEmpty { "Amplifier" },
                    controls = entities.sortedBy { it.sortOrder }.map { controlRepository.toDomain(it) }
                )
            }

            val cabEntities = bySection[SectionType.CAB.name] ?: emptyList()
            val cabBlocks = cabEntities.groupBy { it.blockId }.map { (blockId, entities) ->
                CabBlock(
                    id = blockId,
                    name = entities.first().blockName.ifEmpty { "Cabinet" },
                    controls = entities.sortedBy { it.sortOrder }.map { controlRepository.toDomain(it) }
                )
            }

            val effectEntities = bySection[SectionType.EFFECTS.name] ?: emptyList()
            val effectBlocks = effectEntities.groupBy { it.blockId }.map { (blockId, entities) ->
                val rawBlock = ControlBlock(
                    id = blockId,
                    name = entities.first().blockName.ifEmpty { blockId },
                    controls = entities.sortedBy { it.sortOrder }.map { controlRepository.toDomain(it) }
                )
                migratePedalBlock(rawBlock)
            }

            boardRepository.loadBoardState(BoardState(
                pedals = pedalBlocks,
                ampBlocks = ampBlocks,
                cabBlocks = cabBlocks,
                effects = effectBlocks
            ))
        }
    }

    // --- Migration ---

    /**
     * Ensures every ControlBlock has an ON/OFF stomp control.
     * If one already exists, syncs block.enabled from its isOn state.
     */
    private fun migratePedalBlock(block: ControlBlock): ControlBlock {
        val stompControl = block.controls.filterIsInstance<ControlType.Toggle>()
            .firstOrNull { it.isStompButton }
        return if (stompControl != null) {
            block.copy(enabled = stompControl.isOn)
        } else {
            block.copy(
                controls = block.controls + ControlType.Toggle(
                    label = "ON/OFF",
                    ccNumber = 0,  // CCAssigner will assign after migration
                    isStompButton = true,
                    isOn = block.enabled
                )
            )
        }
    }

    /** Collects all CC numbers currently in use across the whole board. */
    private fun collectUsedCCs(): MutableSet<Int> {
        val state = boardRepository.getCurrentState()
        val used = mutableSetOf<Int>()

        fun com.gearboard.domain.model.ControlType.collectCC() { if (ccNumber > 0) used.add(ccNumber) }

        state.pedals.flatMap { it.controls }.forEach { it.collectCC() }
        state.effects.flatMap { it.controls }.forEach { it.collectCC() }
        state.ampBlocks.flatMap { it.controls }.forEach { it.collectCC() }
        state.cabBlocks.flatMap { it.controls }.forEach { it.collectCC() }

        // Include MIDI Learn overrides so new blocks don't reuse them
        ccAssignments.value.values.forEach { used.add(it) }

        return used
    }

    // --- A/B switching ---

    fun switchBlockAbSlot(isPedals: Boolean, blockId: String, slot: AbSlot) {
        boardRepository.switchBlockAbSlot(isPedals, blockId, slot)
        val blocks = if (isPedals) boardRepository.boardState.value.pedals
                     else boardRepository.boardState.value.effects
        blocks.find { it.id == blockId }?.controls?.forEach { sendControlMidi(it) }
        triggerAutoSave()
    }
}
