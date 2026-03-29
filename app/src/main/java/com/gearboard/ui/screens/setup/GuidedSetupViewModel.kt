package com.gearboard.ui.screens.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gearboard.data.repository.BoardRepository
import com.gearboard.data.repository.SettingsRepository
import com.gearboard.domain.model.AmpTemplates
import com.gearboard.domain.model.CabTemplates
import com.gearboard.domain.model.ControlBlock
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.DisplayFormat
import com.gearboard.midi.GearBoardMidiManager
import com.gearboard.ui.screens.board.OnboardingTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Data class for a single control that needs CC mapping during guided setup.
 */
data class MappingItem(
    val label: String,
    val controlId: String,
    val section: String,      // "pedals", "amp", "cab", "effects"
    val blockId: String,      // block UUID or "amp_main"/"cab_main"
    val isMapped: Boolean = false
)

/**
 * GuidedSetupViewModel — manages the multi-step wizard state.
 *
 * Steps:
 * 0 = Plugin selection
 * 1 = Layout preview
 * 2 = Guided mapping (step-by-step)
 * 3 = Completion
 */
@HiltViewModel
class GuidedSetupViewModel @Inject constructor(
    private val boardRepository: BoardRepository,
    private val settingsRepository: SettingsRepository,
    private val midiManager: GearBoardMidiManager
) : ViewModel() {

    private val _currentStep = MutableStateFlow(0)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    private val _selectedTemplate = MutableStateFlow<OnboardingTemplate?>(null)
    val selectedTemplate: StateFlow<OnboardingTemplate?> = _selectedTemplate.asStateFlow()

    private val _mappingControls = MutableStateFlow<List<MappingItem>>(emptyList())
    val mappingControls: StateFlow<List<MappingItem>> = _mappingControls.asStateFlow()

    private val _currentMappingIndex = MutableStateFlow(0)
    val currentMappingIndex: StateFlow<Int> = _currentMappingIndex.asStateFlow()

    private val _mappingCountdown = MutableStateFlow(0)
    val mappingCountdown: StateFlow<Int> = _mappingCountdown.asStateFlow()

    private val _isMappingActive = MutableStateFlow(false)
    val isMappingActive: StateFlow<Boolean> = _isMappingActive.asStateFlow()

    // Signals navigation to Board
    private val _navigateToBoard = MutableStateFlow(false)
    val navigateToBoard: StateFlow<Boolean> = _navigateToBoard.asStateFlow()

    private var countdownJob: Job? = null

    val mappedCount: Int
        get() = _mappingControls.value.count { it.isMapped }

    val skippedCount: Int
        get() = _mappingControls.value.count { !it.isMapped }

    /**
     * Select a template and apply it.
     * @param template null = "Build from Scratch" → skip to Board.
     */
    fun selectTemplate(template: OnboardingTemplate?) {
        if (template == null) {
            // Build from scratch: mark setup done, go to Board
            viewModelScope.launch {
                settingsRepository.setGuidedSetupComplete(true)
                _navigateToBoard.value = true
            }
            return
        }

        _selectedTemplate.value = template
        applyTemplate(template)
        _currentStep.value = 1
    }

    /** Advance from preview to guided mapping. */
    fun startMapping() {
        _currentMappingIndex.value = 0
        _currentStep.value = 2
    }

    /** Start the mapping process for the current control — sends CC wiggle. */
    fun startMappingControl() {
        val idx = _currentMappingIndex.value
        if (idx >= _mappingControls.value.size) return

        _isMappingActive.value = true
        _mappingCountdown.value = 15


        // Send CC "wiggle" to trigger MIDI Learn in the plugin
        viewModelScope.launch {
            // Send CC 64 value 64, then 0
            midiManager.sendControlChange(64, 64)
            delay(100)
            midiManager.sendControlChange(64, 0)
        }

        // Start countdown timer
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (t in 15 downTo 0) {
                _mappingCountdown.value = t
                if (t > 0) delay(1000)
            }
            _isMappingActive.value = false
        }
    }

    /** Mark the current control as mapped and move to next. */
    fun markCurrentMapped() {
        countdownJob?.cancel()
        _isMappingActive.value = false

        val items = _mappingControls.value.toMutableList()
        val idx = _currentMappingIndex.value
        if (idx < items.size) {
            items[idx] = items[idx].copy(isMapped = true)
            _mappingControls.value = items
        }
        advanceMapping()
    }

    /** Skip the current control without mapping. */
    fun skipControl() {
        countdownJob?.cancel()
        _isMappingActive.value = false
        advanceMapping()
    }

    private fun advanceMapping() {
        val nextIdx = _currentMappingIndex.value + 1
        if (nextIdx >= _mappingControls.value.size) {
            _currentStep.value = 3
        } else {
            _currentMappingIndex.value = nextIdx
        }
    }

    /** Complete the setup → navigate to Board. */
    fun completeSetup() {
        viewModelScope.launch {
            settingsRepository.setGuidedSetupComplete(true)
            _navigateToBoard.value = true
        }
    }

    /** Go back one step (for step indicator navigation). */
    fun goBack() {
        countdownJob?.cancel()
        _isMappingActive.value = false
        if (_currentStep.value > 0) {
            _currentStep.value = _currentStep.value - 1
        }
    }

    // --- Template application ---

    private fun applyTemplate(template: OnboardingTemplate) {
        when (template) {
            OnboardingTemplate.GUITAR_AMP_SIM -> applyGuitarAmpSim()
            OnboardingTemplate.BASS_AMP_SIM -> applyBassAmpSim()
            OnboardingTemplate.MULTI_FX_BOARD -> applyMultiFxBoard()
        }
    }

    private fun applyGuitarAmpSim() {
        val odBlock = ControlBlock(
            name = "Overdrive", type = "Distortion",
            controls = listOf(
                ControlType.Knob(label = "Drive", ccNumber = 0),
                ControlType.Knob(label = "Tone", ccNumber = 0),
                ControlType.Knob(label = "Level", ccNumber = 0),
                ControlType.Toggle(label = "Boost", ccNumber = 0),
                ControlType.Toggle(label = "On/Off", ccNumber = 0)
            )
        )
        boardRepository.addPedalBlock(odBlock)

        val ampBlock = AmpTemplates.HIGH_GAIN.copy(id = "amp_main")
        boardRepository.addAmpBlock(ampBlock)

        val cabBlock = CabTemplates.CLOSED_BACK_412.copy(id = "cab_main")
        boardRepository.addCabBlock(cabBlock)

        val delayBlock = ControlBlock(
            name = "Delay", type = "Time",
            controls = listOf(
                ControlType.Knob(label = "Time", ccNumber = 0, displayFormat = DisplayFormat.MILLISECONDS),
                ControlType.Knob(label = "Feedback", ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                ControlType.Knob(label = "Mix", ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                ControlType.Tap(label = "Tap Tempo", ccNumber = 0),
                ControlType.Toggle(label = "On/Off", ccNumber = 0)
            )
        )
        boardRepository.addEffectBlock(delayBlock)

        _mappingControls.value = listOf(
            MappingItem("Gain", ampBlock.controls[0].id, "amp", ampBlock.id),
            MappingItem("Master", ampBlock.controls[5].id, "amp", ampBlock.id),
            MappingItem("Bass", ampBlock.controls[1].id, "amp", ampBlock.id),
            MappingItem("Treble", ampBlock.controls[3].id, "amp", ampBlock.id),
            MappingItem("Drive", odBlock.controls[0].id, "pedals", odBlock.id),
            MappingItem("OD On/Off", odBlock.controls[4].id, "pedals", odBlock.id),
            MappingItem("Delay Mix", delayBlock.controls[2].id, "effects", delayBlock.id),
            MappingItem("Delay On/Off", delayBlock.controls[4].id, "effects", delayBlock.id)
        )
    }

    private fun applyBassAmpSim() {
        val odBlock = ControlBlock(
            name = "Overdrive", type = "Bass OD",
            controls = listOf(
                ControlType.Knob(label = "Drive", ccNumber = 0),
                ControlType.Knob(label = "Blend", ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                ControlType.Knob(label = "Level", ccNumber = 0),
                ControlType.Toggle(label = "On/Off", ccNumber = 0)
            )
        )
        boardRepository.addPedalBlock(odBlock)

        val ampBlock = AmpTemplates.BASS_AMP.copy(id = "amp_main")
        boardRepository.addAmpBlock(ampBlock)

        val cabBlock = CabTemplates.BASS_410.copy(id = "cab_main")
        boardRepository.addCabBlock(cabBlock)

        _mappingControls.value = listOf(
            MappingItem("Gain", ampBlock.controls[0].id, "amp", ampBlock.id),
            MappingItem("Master", ampBlock.controls[5].id, "amp", ampBlock.id),
            MappingItem("Bass", ampBlock.controls[1].id, "amp", ampBlock.id),
            MappingItem("Treble", ampBlock.controls[4].id, "amp", ampBlock.id),
            MappingItem("Drive", odBlock.controls[0].id, "pedals", odBlock.id),
            MappingItem("OD On/Off", odBlock.controls[3].id, "pedals", odBlock.id)
        )
    }

    private fun applyMultiFxBoard() {
        val compBlock = ControlBlock(
            name = "Compressor", type = "Dynamics",
            controls = listOf(
                ControlType.Knob(label = "Threshold", ccNumber = 0, displayFormat = DisplayFormat.DECIBELS),
                ControlType.Knob(label = "Ratio", ccNumber = 0),
                ControlType.Knob(label = "Level", ccNumber = 0),
                ControlType.Toggle(label = "On/Off", ccNumber = 0)
            )
        )
        boardRepository.addPedalBlock(compBlock)

        val odBlock = ControlBlock(
            name = "Overdrive", type = "Distortion",
            controls = listOf(
                ControlType.Knob(label = "Drive", ccNumber = 0),
                ControlType.Knob(label = "Tone", ccNumber = 0),
                ControlType.Knob(label = "Level", ccNumber = 0),
                ControlType.Toggle(label = "On/Off", ccNumber = 0)
            )
        )
        boardRepository.addPedalBlock(odBlock)

        val ampBlock = AmpTemplates.MODERN_HIGH_GAIN.copy(id = "amp_main")
        boardRepository.addAmpBlock(ampBlock)

        val chorusBlock = ControlBlock(
            name = "Chorus", type = "Modulation",
            controls = listOf(
                ControlType.Knob(label = "Rate", ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                ControlType.Knob(label = "Depth", ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                ControlType.Knob(label = "Mix", ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                ControlType.Toggle(label = "On/Off", ccNumber = 0)
            )
        )
        boardRepository.addEffectBlock(chorusBlock)

        val delayBlock = ControlBlock(
            name = "Delay", type = "Time",
            controls = listOf(
                ControlType.Knob(label = "Time", ccNumber = 0, displayFormat = DisplayFormat.MILLISECONDS),
                ControlType.Knob(label = "Feedback", ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                ControlType.Knob(label = "Mix", ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                ControlType.Tap(label = "Tap Tempo", ccNumber = 0),
                ControlType.Toggle(label = "On/Off", ccNumber = 0)
            )
        )
        boardRepository.addEffectBlock(delayBlock)

        val reverbBlock = ControlBlock(
            name = "Reverb", type = "Reverb",
            controls = listOf(
                ControlType.Knob(label = "Size", ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                ControlType.Knob(label = "Decay", ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                ControlType.Knob(label = "Mix", ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                ControlType.Toggle(label = "On/Off", ccNumber = 0)
            )
        )
        boardRepository.addEffectBlock(reverbBlock)

        val cabBlock = CabTemplates.CLOSED_BACK_212.copy(id = "cab_main")
        boardRepository.addCabBlock(cabBlock)

        _mappingControls.value = listOf(
            MappingItem("Gain", ampBlock.controls[0].id, "amp", ampBlock.id),
            MappingItem("Master", ampBlock.controls[5].id, "amp", ampBlock.id),
            MappingItem("Drive", odBlock.controls[0].id, "pedals", odBlock.id),
            MappingItem("OD On/Off", odBlock.controls[3].id, "pedals", odBlock.id),
            MappingItem("Delay Mix", delayBlock.controls[2].id, "effects", delayBlock.id),
            MappingItem("Delay On/Off", delayBlock.controls[4].id, "effects", delayBlock.id),
            MappingItem("Reverb Mix", reverbBlock.controls[2].id, "effects", reverbBlock.id),
            MappingItem("Reverb On/Off", reverbBlock.controls[3].id, "effects", reverbBlock.id)
        )
    }
}
