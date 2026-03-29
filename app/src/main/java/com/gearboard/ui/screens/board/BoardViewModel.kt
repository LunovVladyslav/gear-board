package com.gearboard.ui.screens.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gearboard.data.repository.BoardRepository
import com.gearboard.data.repository.ControlRepository
import com.gearboard.data.repository.SettingsRepository
import com.gearboard.domain.model.AbSlot
import com.gearboard.domain.model.AmpBlock
import com.gearboard.domain.model.BoardState
import com.gearboard.domain.model.CabBlock
import com.gearboard.domain.model.ControlBlock
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.SectionType
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
    private val midiManager: GearBoardMidiManager,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val boardState: StateFlow<BoardState> = boardRepository.boardState

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

    private val _showOnboarding = MutableStateFlow(false)
    val showOnboarding: StateFlow<Boolean> = _showOnboarding.asStateFlow()

    // Auto-save debounce
    private var autoSaveJob: Job? = null

    init {
        loadPersistedState()
    }

    // --- Section expand/collapse ---
    fun togglePedalsExpanded() { _pedalsExpanded.value = !_pedalsExpanded.value }
    fun toggleAmpExpanded() { _ampExpanded.value = !_ampExpanded.value }
    fun toggleCabExpanded() { _cabExpanded.value = !_cabExpanded.value }
    fun toggleEffectsExpanded() { _effectsExpanded.value = !_effectsExpanded.value }

    // --- Pedal Blocks ---
    fun addPedalBlock(block: ControlBlock) {
        boardRepository.addPedalBlock(block)
        _showAddPedalBlockDialog.value = false
        triggerAutoSave()
    }

    fun removePedalBlock(blockId: String) {
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

    fun updatePedalBlockAppearance(blockId: String, appearance: com.gearboard.domain.model.BlockAppearance, layout: com.gearboard.domain.model.BlockLayout) {
        val state = boardRepository.getCurrentState()
        state.pedals.find { it.id == blockId }?.let { block ->
            boardRepository.updatePedalBlock(block.copy(appearance = appearance, layoutMode = layout))
            triggerAutoSave()
        }
    }

    // --- Effect Blocks ---
    fun addEffectBlock(block: ControlBlock) {
        boardRepository.addEffectBlock(block)
        _showAddEffectBlockDialog.value = false
        triggerAutoSave()
    }

    fun removeEffectBlock(blockId: String) {
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

    fun updateEffectBlockAppearance(blockId: String, appearance: com.gearboard.domain.model.BlockAppearance, layout: com.gearboard.domain.model.BlockLayout) {
        val state = boardRepository.getCurrentState()
        state.effects.find { it.id == blockId }?.let { block ->
            boardRepository.updateEffectBlock(block.copy(appearance = appearance, layoutMode = layout))
            triggerAutoSave()
        }
    }

    // --- Amp ---
    fun toggleAmpEnabled() {
        // AmpBlock has no enabled flag; no-op at section level.
    }

    fun updateAmpAppearance(appearance: com.gearboard.domain.model.BlockAppearance, layout: com.gearboard.domain.model.BlockLayout) {
        val block = boardRepository.getCurrentState().ampBlocks.firstOrNull() ?: return
        boardRepository.updateAmpBlock(block.copy(appearance = appearance, layoutMode = layout))
        triggerAutoSave()
    }

    // --- Cabinet ---
    fun toggleCabEnabled() {
        // CabBlock has no enabled flag; no-op at section level.
    }

    fun updateCabAppearance(appearance: com.gearboard.domain.model.BlockAppearance, layout: com.gearboard.domain.model.BlockLayout) {
        val block = boardRepository.getCurrentState().cabBlocks.firstOrNull() ?: return
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

    // --- Amp/Cab control CRUD ---
    fun addAmpControl(control: ControlType) {
        boardRepository.addAmpControl(control)
        triggerAutoSave()
    }

    fun removeAmpControl(controlId: String) {
        boardRepository.removeAmpControl(controlId)
        triggerAutoSave()
    }

    fun updateAmpControl(controlId: String, updatedControl: ControlType) {
        boardRepository.updateAmpControl(controlId, updatedControl)
        triggerAutoSave()
    }

    fun clearAmpControls() {
        boardRepository.clearAmpControls()
        triggerAutoSave()
    }

    fun addCabControl(control: ControlType) {
        boardRepository.addCabControl(control)
        triggerAutoSave()
    }

    fun removeCabControl(controlId: String) {
        boardRepository.removeCabControl(controlId)
        triggerAutoSave()
    }

    fun updateCabControl(controlId: String, updatedControl: ControlType) {
        boardRepository.updateCabControl(controlId, updatedControl)
        triggerAutoSave()
    }

    fun clearCabControls() {
        boardRepository.clearCabControls()
        triggerAutoSave()
    }

    fun applyAmpTemplate(template: AmpTemplate) {
        template.controls().forEach { boardRepository.addAmpControl(it) }
        triggerAutoSave()
    }

    fun applyCabTemplate(template: CabTemplate) {
        template.controls().forEach { boardRepository.addCabControl(it) }
        triggerAutoSave()
    }

    // --- MIDI Sending ---

    /** Unified MIDI send for any ControlType interaction. */
    fun sendControlMidi(control: ControlType) {
        when (control) {
            is ControlType.Knob -> {
                val midiValue = (control.value * 127f).toInt().coerceIn(0, 127)
                midiManager.sendControlChange(control.ccNumber, midiValue, control.midiChannel)
            }
            is ControlType.Toggle -> {
                if (control.pulseMode) {
                    midiManager.sendControlChange(control.ccNumber, 127, control.midiChannel)
                    viewModelScope.launch {
                        delay(50)
                        midiManager.sendControlChange(control.ccNumber, 0, control.midiChannel)
                    }
                } else {
                    midiManager.sendControlChange(
                        control.ccNumber,
                        if (control.isOn) 127 else 0,
                        control.midiChannel
                    )
                }
            }
            is ControlType.Tap -> {
                midiManager.sendControlChange(control.ccNumber, 127, control.midiChannel)
            }
            is ControlType.Selector -> {
                val total = control.positions.size
                val value = if (total <= 1) 0 else (control.selectedIndex * 127) / (total - 1)
                midiManager.sendControlChange(control.ccNumber, value, control.midiChannel)
            }
            is ControlType.Fader -> {
                val midiValue = (control.value * 127f).toInt().coerceIn(0, 127)
                midiManager.sendControlChange(control.ccNumber, midiValue, control.midiChannel)
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

    // --- Knob/Fader value updates (with MIDI send) ---

    fun onKnobValueChange(isPedals: Boolean, blockId: String, controlId: String, knob: ControlType.Knob, newValue: Float) {
        val updated = knob.copy(value = newValue)
        if (blockId.isEmpty()) {
            // Amp or Cab control
        } else {
            updateControlInBlock(isPedals, blockId, controlId, updated)
        }
        sendControlMidi(updated)
    }

    fun onAmpKnobValueChange(controlId: String, knob: ControlType.Knob, newValue: Float) {
        val updated = knob.copy(value = newValue)
        boardRepository.updateAmpControl(controlId, updated)
        sendControlMidi(updated)
    }

    fun onCabKnobValueChange(controlId: String, knob: ControlType.Knob, newValue: Float) {
        val updated = knob.copy(value = newValue)
        boardRepository.updateCabControl(controlId, updated)
        sendControlMidi(updated)
    }

    fun onFaderValueChange(isPedals: Boolean, blockId: String, controlId: String, fader: ControlType.Fader, newValue: Float) {
        val updated = fader.copy(value = newValue)
        if (blockId.isEmpty()) {
            // could be Amp or Cab
        } else {
            updateControlInBlock(isPedals, blockId, controlId, updated)
        }
        sendControlMidi(updated)
    }

    fun onAmpFaderValueChange(controlId: String, fader: ControlType.Fader, newValue: Float) {
        val updated = fader.copy(value = newValue)
        boardRepository.updateAmpControl(controlId, updated)
        sendControlMidi(updated)
    }

    fun onCabFaderValueChange(controlId: String, fader: ControlType.Fader, newValue: Float) {
        val updated = fader.copy(value = newValue)
        boardRepository.updateCabControl(controlId, updated)
        sendControlMidi(updated)
    }

    fun onToggle(isPedals: Boolean, blockId: String, controlId: String, toggle: ControlType.Toggle) {
        val updated = toggle.copy(isOn = !toggle.isOn)
        if (blockId.isNotEmpty()) {
            updateControlInBlock(isPedals, blockId, controlId, updated)
        }
        sendControlMidi(updated)
    }

    fun onAmpToggle(controlId: String, toggle: ControlType.Toggle) {
        val updated = toggle.copy(isOn = !toggle.isOn)
        boardRepository.updateAmpControl(controlId, updated)
        sendControlMidi(updated)
    }

    fun onCabToggle(controlId: String, toggle: ControlType.Toggle) {
        val updated = toggle.copy(isOn = !toggle.isOn)
        boardRepository.updateCabControl(controlId, updated)
        sendControlMidi(updated)
    }

    fun onSelectorChange(isPedals: Boolean, blockId: String, controlId: String, selector: ControlType.Selector, newIndex: Int) {
        val updated = selector.copy(selectedIndex = newIndex)
        if (blockId.isNotEmpty()) {
            updateControlInBlock(isPedals, blockId, controlId, updated)
        }
        sendControlMidi(updated)
    }

    fun onAmpSelectorChange(controlId: String, selector: ControlType.Selector, newIndex: Int) {
        val updated = selector.copy(selectedIndex = newIndex)
        boardRepository.updateAmpControl(controlId, updated)
        sendControlMidi(updated)
    }

    fun onCabSelectorChange(controlId: String, selector: ControlType.Selector, newIndex: Int) {
        val updated = selector.copy(selectedIndex = newIndex)
        boardRepository.updateCabControl(controlId, updated)
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

    // --- Dialog visibility ---
    fun showAddPedalBlockDialog() { _showAddPedalBlockDialog.value = true }
    fun hideAddPedalBlockDialog() { _showAddPedalBlockDialog.value = false }
    fun showAddEffectBlockDialog() { _showAddEffectBlockDialog.value = true }
    fun hideAddEffectBlockDialog() { _showAddEffectBlockDialog.value = false }
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
        controlRepository.deleteAll()

        // Persist pedal blocks
        state.pedals.forEachIndexed { blockIdx, block ->
            block.controls.forEachIndexed { ctrlIdx, control ->
                controlRepository.insert(
                    controlRepository.toEntity(control, SectionType.PEDALS, block.id, ctrlIdx)
                )
            }
        }

        // Persist amp blocks
        state.ampBlocks.forEach { block ->
            block.controls.forEachIndexed { idx, control ->
                controlRepository.insert(
                    controlRepository.toEntity(control, SectionType.AMP, block.id, idx)
                )
            }
        }

        // Persist cab blocks
        state.cabBlocks.forEach { block ->
            block.controls.forEachIndexed { idx, control ->
                controlRepository.insert(
                    controlRepository.toEntity(control, SectionType.CAB, block.id, idx)
                )
            }
        }

        // Persist effect blocks
        state.effects.forEachIndexed { blockIdx, block ->
            block.controls.forEachIndexed { ctrlIdx, control ->
                controlRepository.insert(
                    controlRepository.toEntity(control, SectionType.EFFECTS, block.id, ctrlIdx)
                )
            }
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

            // Group by section
            val bySection = allEntities.groupBy { it.sectionType }

            // Pedals: group by blockId
            val pedalEntities = bySection[SectionType.PEDALS.name] ?: emptyList()
            val pedalBlocks = pedalEntities.groupBy { it.blockId }.map { (blockId, entities) ->
                ControlBlock(
                    id = blockId,
                    name = blockId, // Will be overridden by stored block name later
                    controls = entities.sortedBy { it.sortOrder }.map { controlRepository.toDomain(it) }
                )
            }

            // Amp — group by blockId to support multiple amp blocks
            val ampEntities = bySection[SectionType.AMP.name] ?: emptyList()
            val ampBlocks = ampEntities.groupBy { it.blockId }.map { (blockId, entities) ->
                AmpBlock(
                    id = blockId,
                    name = "Amplifier",
                    controls = entities.sortedBy { it.sortOrder }.map { controlRepository.toDomain(it) }
                )
            }

            // Cab — group by blockId to support multiple cab blocks
            val cabEntities = bySection[SectionType.CAB.name] ?: emptyList()
            val cabBlocks = cabEntities.groupBy { it.blockId }.map { (blockId, entities) ->
                CabBlock(
                    id = blockId,
                    name = "Cabinet",
                    controls = entities.sortedBy { it.sortOrder }.map { controlRepository.toDomain(it) }
                )
            }

            // Effects: group by blockId
            val effectEntities = bySection[SectionType.EFFECTS.name] ?: emptyList()
            val effectBlocks = effectEntities.groupBy { it.blockId }.map { (blockId, entities) ->
                ControlBlock(
                    id = blockId,
                    name = blockId,
                    controls = entities.sortedBy { it.sortOrder }.map { controlRepository.toDomain(it) }
                )
            }

            val state = BoardState(
                pedals = pedalBlocks,
                ampBlocks = ampBlocks,
                cabBlocks = cabBlocks,
                effects = effectBlocks
            )
            boardRepository.loadBoardState(state)
        }
    }

    // --- A/B switching ---

    fun switchBlockAbSlot(isPedals: Boolean, blockId: String, slot: AbSlot) {
        boardRepository.switchBlockAbSlot(isPedals, blockId, slot)
        // Send MIDI CC for all controls in the block after switch
        val blocks = if (isPedals) boardRepository.boardState.value.pedals
                     else boardRepository.boardState.value.effects
        blocks.find { it.id == blockId }?.controls?.forEach { sendControlMidi(it) }
    }

    fun switchAmpAbSlot(slot: AbSlot) {
        boardRepository.switchAmpAbSlot(slot)
        boardRepository.boardState.value.ampBlocks.firstOrNull()?.controls?.forEach { sendControlMidi(it) }
    }

    fun switchCabAbSlot(slot: AbSlot) {
        boardRepository.switchCabAbSlot(slot)
        boardRepository.boardState.value.cabBlocks.firstOrNull()?.controls?.forEach { sendControlMidi(it) }
    }
}
