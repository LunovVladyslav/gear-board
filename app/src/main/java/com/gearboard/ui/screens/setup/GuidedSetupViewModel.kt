
package com.gearboard.ui.screens.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gearboard.data.repository.BoardRepository
import com.gearboard.data.repository.SettingsRepository
import com.gearboard.domain.model.AmpTemplates
import com.gearboard.domain.model.CCAssigner
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

data class MappingItem(
    val label: String,
    val controlId: String,
    val section: String,
    val blockId: String,
    val isMapped: Boolean = false
)

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

    private val _navigateToBoard = MutableStateFlow(false)
    val navigateToBoard: StateFlow<Boolean> = _navigateToBoard.asStateFlow()

    private var countdownJob: Job? = null

    // Tracks all assigned CCs to prevent duplicates across sections
    private val usedCCs = mutableSetOf<Int>()

    val mappedCount: Int
        get() = _mappingControls.value.count { it.isMapped }

    val skippedCount: Int
        get() = _mappingControls.value.count { !it.isMapped }

    fun selectTemplate(template: OnboardingTemplate?) {
        if (template == null) {
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

    fun startMapping() {
        _currentMappingIndex.value = 0
        _currentStep.value = 2
    }

    fun startMappingControl() {
        val idx = _currentMappingIndex.value
        val item = _mappingControls.value.getOrNull(idx) ?: return

        _isMappingActive.value = true
        _mappingCountdown.value = 15

        val control = boardRepository.findControl(item.controlId, item.blockId, item.section)
        val learnCC = control?.ccNumber ?: 0

        viewModelScope.launch {
            if (learnCC > 0) {
                when (control) {
                    is ControlType.Toggle -> {
                        midiManager.sendControlChange(learnCC, 127)
                        delay(200)
                        midiManager.sendControlChange(learnCC, 0)
                    }
                    else -> {
                        midiManager.sendControlChange(learnCC, 64)
                        delay(100)
                        midiManager.sendControlChange(learnCC, 0)
                    }
                }
            }
        }

        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            for (t in 15 downTo 0) {
                _mappingCountdown.value = t
                if (t > 0) delay(1000)
            }
            _isMappingActive.value = false
        }
    }

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

    fun completeSetup() {
        viewModelScope.launch {
            settingsRepository.setGuidedSetupComplete(true)
            _navigateToBoard.value = true
        }
    }

    fun goBack() {
        countdownJob?.cancel()
        _isMappingActive.value = false
        if (_currentStep.value > 0) _currentStep.value = _currentStep.value - 1
    }

    private fun applyTemplate(template: OnboardingTemplate) {
        when (template) {
            OnboardingTemplate.GUITAR_AMP_SIM  -> applyGuitarAmpSim()
            OnboardingTemplate.BASS_AMP_SIM    -> applyBassAmpSim()
            OnboardingTemplate.MULTI_FX_BOARD  -> applyMultiFxBoard()
        }
    }

    private fun applyGuitarAmpSim() {
        usedCCs.clear()

        val odBlock = CCAssigner.assignBlock(
            ControlBlock(
                name = "Overdrive", type = "Distortion",
                controls = listOf(
                    ControlType.Knob(label = "Drive",    ccNumber = 0),
                    ControlType.Knob(label = "Tone",     ccNumber = 0),
                    ControlType.Knob(label = "Level",    ccNumber = 0),
                    ControlType.Toggle(label = "Boost",  ccNumber = 0),
                    ControlType.Toggle(label = "On/Off", ccNumber = 0)
                )
            ),
            section = "pedals",
            usedCCs = usedCCs
        )
        boardRepository.addPedalBlock(odBlock)

        val ampBlock = CCAssigner.assignAmp(
            AmpTemplates.HIGH_GAIN.copy(id = "amp_main"),
            usedCCs
        )
        boardRepository.addAmpBlock(ampBlock)

        val cabBlock = CCAssigner.assignCab(
            CabTemplates.CLOSED_BACK_412.copy(id = "cab_main"),
            usedCCs
        )
        boardRepository.addCabBlock(cabBlock)

        val delayBlock = CCAssigner.assignBlock(
            ControlBlock(
                name = "Delay", type = "Time",
                controls = listOf(
                    ControlType.Knob(label = "Time",     ccNumber = 0, displayFormat = DisplayFormat.MILLISECONDS),
                    ControlType.Knob(label = "Feedback", ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                    ControlType.Knob(label = "Mix",      ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                    ControlType.Tap(label = "Tap Tempo", ccNumber = 0),
                    ControlType.Toggle(label = "On/Off", ccNumber = 0)
                )
            ),
            section = "effects",
            usedCCs = usedCCs
        )
        boardRepository.addEffectBlock(delayBlock)

        _mappingControls.value = listOf(
            MappingItem("Gain",         ampBlock.controls[0].id,   "amp",     ampBlock.id),
            MappingItem("Master",       ampBlock.controls[5].id,   "amp",     ampBlock.id),
            MappingItem("Bass",         ampBlock.controls[1].id,   "amp",     ampBlock.id),
            MappingItem("Treble",       ampBlock.controls[3].id,   "amp",     ampBlock.id),
            MappingItem("Drive",        odBlock.controls[0].id,    "pedals",  odBlock.id),
            MappingItem("OD On/Off",    odBlock.controls[4].id,    "pedals",  odBlock.id),
            MappingItem("Delay Mix",    delayBlock.controls[2].id, "effects", delayBlock.id),
            MappingItem("Delay On/Off", delayBlock.controls[4].id, "effects", delayBlock.id)
        )
    }

    private fun applyBassAmpSim() {
        usedCCs.clear()

        val odBlock = CCAssigner.assignBlock(
            ControlBlock(
                name = "Overdrive", type = "Bass OD",
                controls = listOf(
                    ControlType.Knob(label = "Drive",    ccNumber = 0),
                    ControlType.Knob(label = "Blend",    ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                    ControlType.Knob(label = "Level",    ccNumber = 0),
                    ControlType.Toggle(label = "On/Off", ccNumber = 0)
                )
            ),
            section = "pedals",
            usedCCs = usedCCs
        )
        boardRepository.addPedalBlock(odBlock)

        val ampBlock = CCAssigner.assignAmp(
            AmpTemplates.BASS_AMP.copy(id = "amp_main"),
            usedCCs
        )
        boardRepository.addAmpBlock(ampBlock)

        val cabBlock = CCAssigner.assignCab(
            CabTemplates.BASS_410.copy(id = "cab_main"),
            usedCCs
        )
        boardRepository.addCabBlock(cabBlock)

        _mappingControls.value = listOf(
            MappingItem("Gain",      ampBlock.controls[0].id, "amp",    ampBlock.id),
            MappingItem("Master",    ampBlock.controls[5].id, "amp",    ampBlock.id),
            MappingItem("Bass",      ampBlock.controls[1].id, "amp",    ampBlock.id),
            MappingItem("Treble",    ampBlock.controls[4].id, "amp",    ampBlock.id),
            MappingItem("Drive",     odBlock.controls[0].id,  "pedals", odBlock.id),
            MappingItem("OD On/Off", odBlock.controls[3].id,  "pedals", odBlock.id)
        )
    }

    private fun applyMultiFxBoard() {
        usedCCs.clear()

        val compBlock = CCAssigner.assignBlock(
            ControlBlock(
                name = "Compressor", type = "Dynamics",
                controls = listOf(
                    ControlType.Knob(label = "Threshold", ccNumber = 0, displayFormat = DisplayFormat.DECIBELS_OUTPUT),
                    ControlType.Knob(label = "Ratio",     ccNumber = 0),
                    ControlType.Knob(label = "Level",     ccNumber = 0),
                    ControlType.Toggle(label = "On/Off",  ccNumber = 0)
                )
            ),
            section = "pedals",
            usedCCs = usedCCs
        )
        boardRepository.addPedalBlock(compBlock)

        val odBlock = CCAssigner.assignBlock(
            ControlBlock(
                name = "Overdrive", type = "Distortion",
                controls = listOf(
                    ControlType.Knob(label = "Drive",    ccNumber = 0),
                    ControlType.Knob(label = "Tone",     ccNumber = 0),
                    ControlType.Knob(label = "Level",    ccNumber = 0),
                    ControlType.Toggle(label = "On/Off", ccNumber = 0)
                )
            ),
            section = "pedals",
            usedCCs = usedCCs
        )
        boardRepository.addPedalBlock(odBlock)

        val ampBlock = CCAssigner.assignAmp(
            AmpTemplates.MODERN_HIGH_GAIN.copy(id = "amp_main"),
            usedCCs
        )
        boardRepository.addAmpBlock(ampBlock)

        val cabBlock = CCAssigner.assignCab(
            CabTemplates.CLOSED_BACK_212.copy(id = "cab_main"),
            usedCCs
        )
        boardRepository.addCabBlock(cabBlock)

        val chorusBlock = CCAssigner.assignBlock(
            ControlBlock(
                name = "Chorus", type = "Modulation",
                controls = listOf(
                    ControlType.Knob(label = "Rate",     ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                    ControlType.Knob(label = "Depth",    ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                    ControlType.Knob(label = "Mix",      ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                    ControlType.Toggle(label = "On/Off", ccNumber = 0)
                )
            ),
            section = "effects",
            usedCCs = usedCCs
        )
        boardRepository.addEffectBlock(chorusBlock)

        val delayBlock = CCAssigner.assignBlock(
            ControlBlock(
                name = "Delay", type = "Time",
                controls = listOf(
                    ControlType.Knob(label = "Time",     ccNumber = 0, displayFormat = DisplayFormat.MILLISECONDS),
                    ControlType.Knob(label = "Feedback", ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                    ControlType.Knob(label = "Mix",      ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                    ControlType.Tap(label = "Tap Tempo", ccNumber = 0),
                    ControlType.Toggle(label = "On/Off", ccNumber = 0)
                )
            ),
            section = "effects",
            usedCCs = usedCCs
        )
        boardRepository.addEffectBlock(delayBlock)

        val reverbBlock = CCAssigner.assignBlock(
            ControlBlock(
                name = "Reverb", type = "Reverb",
                controls = listOf(
                    ControlType.Knob(label = "Size",     ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                    ControlType.Knob(label = "Decay",    ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                    ControlType.Knob(label = "Mix",      ccNumber = 0, displayFormat = DisplayFormat.PERCENTAGE),
                    ControlType.Toggle(label = "On/Off", ccNumber = 0)
                )
            ),
            section = "effects",
            usedCCs = usedCCs
        )
        boardRepository.addEffectBlock(reverbBlock)

        _mappingControls.value = listOf(
            MappingItem("Gain",          ampBlock.controls[0].id,    "amp",     ampBlock.id),
            MappingItem("Master",        ampBlock.controls[5].id,    "amp",     ampBlock.id),
            MappingItem("Drive",         odBlock.controls[0].id,     "pedals",  odBlock.id),
            MappingItem("OD On/Off",     odBlock.controls[3].id,     "pedals",  odBlock.id),
            MappingItem("Delay Mix",     delayBlock.controls[2].id,  "effects", delayBlock.id),
            MappingItem("Delay On/Off",  delayBlock.controls[4].id,  "effects", delayBlock.id),
            MappingItem("Reverb Mix",    reverbBlock.controls[2].id, "effects", reverbBlock.id),
            MappingItem("Reverb On/Off", reverbBlock.controls[3].id, "effects", reverbBlock.id)
        )
    }
}
