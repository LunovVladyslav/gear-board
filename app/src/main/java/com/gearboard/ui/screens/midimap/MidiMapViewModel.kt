package com.gearboard.ui.screens.midimap

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gearboard.data.repository.MidiMappingRepository
import com.gearboard.domain.model.MidiDirection
import com.gearboard.domain.model.MidiEvent
import com.gearboard.domain.model.MidiEventType
import com.gearboard.domain.model.MidiMapping
import com.gearboard.domain.model.SectionType
import com.gearboard.midi.GearBoardMidiManager
import com.gearboard.midi.autoAssignCC
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

data class LearnState(
    val isActive: Boolean = false,
    val controlId: String = "",
    val controlName: String = "",
    val remainingSeconds: Int = 10,
    val detectedCc: Int? = null
)

@HiltViewModel
class MidiMapViewModel @Inject constructor(
    private val mappingRepository: MidiMappingRepository,
    private val midiManager: GearBoardMidiManager
) : ViewModel() {

    val mappings = mappingRepository.getAllMappings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _learnState = MutableStateFlow(LearnState())
    val learnState: StateFlow<LearnState> = _learnState.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private var learnJob: Job? = null
    private var listenJob: Job? = null

    /**
     * Start MIDI Learn mode for a control.
     *
     * 1. Auto-assigns a unique CC if the control has none (so Neural DSP can capture it).
     * 2. Sends a wiggle on that CC immediately (Neural DSP detects GearBoard's CC number).
     * 3. Listens for 10s for an incoming CC from a physical controller (optional override).
     *
     * @param section Used to group CC numbers by section (PEDALS 1-31, AMP 32-63, CAB 64-79, EFFECTS 80-110).
     * @param isToggle True for Toggle controls — sends a sustained 127 wiggle so plugins don't miss it.
     */
    fun startLearn(
        controlId: String,
        controlName: String,
        section: SectionType? = null,
        isToggle: Boolean = false
    ) {
        cancelLearn()

        // learnJob handles setup, wiggle, and countdown
        learnJob = viewModelScope.launch {
            // Look up any existing mapping for this control
            val existing = mappingRepository.getMappingByControlId(controlId)
            val existingCc = existing?.ccNumber ?: 0
            val existingChannel = existing?.channel ?: 0

            // Auto-assign a unique CC if none is assigned yet
            val learnCc = if (existingCc > 0) {
                existingCc
            } else {
                val usedCCs = mappings.value.map { it.ccNumber }.filter { it > 0 }.toSet()
                autoAssignCC(section, usedCCs)
            }

            Log.d("MIDI_LEARN", "Starting learn for '$controlName' on CC $learnCc")

            // Persist the assigned CC immediately — Neural DSP can now capture it
            val mapping = MidiMapping(
                controlId = controlId,
                controlName = controlName,
                ccNumber = learnCc,
                channel = existingChannel
            )
            if (existing != null) {
                mappingRepository.saveMapping(mapping.copy(id = existing.id))
            } else {
                mappingRepository.saveMapping(mapping)
            }

            // Send wiggle on the assigned CC so Neural DSP Learn mode detects it
            val midiCh = existingChannel + 1  // stored 0-15 → send 1-16
            if (isToggle) {
                // Hold 127 for 200ms so plugin doesn't miss the edge
                midiManager.sendControlChange(learnCc, 127, midiCh)
                delay(200)
                midiManager.sendControlChange(learnCc, 0, midiCh)
            } else {
                // Standard mid-range wiggle for knobs/faders
                midiManager.sendControlChange(learnCc, 64, midiCh)
                delay(50)
                midiManager.sendControlChange(learnCc, 0, midiCh)
            }

            _learnState.value = LearnState(
                isActive = true,
                controlId = controlId,
                controlName = controlName,
                remainingSeconds = 10
            )

            // Countdown
            for (i in 10 downTo 1) {
                _learnState.value = _learnState.value.copy(remainingSeconds = i)
                delay(1000)
            }
            // Timeout — auto-assigned CC remains; no physical override received
            _learnState.value = LearnState()
            _toastMessage.value = "Learn timed out"
        }

        // listenJob watches for an incoming CC from a physical controller (optional override)
        listenJob = viewModelScope.launch {
            midiManager.midiEvents.collect { event ->
                if (_learnState.value.isActive &&
                    event.type == MidiEventType.CONTROL_CHANGE &&
                    event.direction == MidiDirection.INCOMING
                ) {
                    val ccNumber = event.data1
                    _learnState.value = _learnState.value.copy(detectedCc = ccNumber)

                    // Brief pause for UI feedback, then override with the physical CC
                    delay(500)
                    assignMapping(controlId, controlName, ccNumber, event.channel)
                    cancelLearn()
                }
            }
        }
    }

    // autoAssignCC is a top-level function in MidiCcAssigner.kt (testable independently)

    /**
     * Cancel learn mode.
     */
    fun cancelLearn() {
        learnJob?.cancel()
        listenJob?.cancel()
        _learnState.value = LearnState()
    }

    /**
     * Manually assign a CC number to a control.
     */
    fun assignMapping(controlId: String, controlName: String, ccNumber: Int, channel: Int = 0) {
        viewModelScope.launch {
            val mapping = MidiMapping(
                controlId = controlId,
                controlName = controlName,
                ccNumber = ccNumber,
                channel = channel
            )
            // Check if mapping already exists for this control
            val existing = mappingRepository.getMappingByControlId(controlId)
            if (existing != null) {
                mappingRepository.saveMapping(mapping.copy(id = existing.id))
            } else {
                mappingRepository.saveMapping(mapping)
            }
            _toastMessage.value = "$controlName → CC $ccNumber"
        }
    }

    /**
     * Delete a mapping.
     */
    fun deleteMapping(controlId: String) {
        viewModelScope.launch {
            mappingRepository.deleteMapping(controlId)
            _toastMessage.value = "Mapping removed"
        }
    }

    /**
     * Delete all mappings.
     */
    fun deleteAllMappings() {
        viewModelScope.launch {
            mappingRepository.deleteAllMappings()
            _toastMessage.value = "All mappings cleared"
        }
    }

    fun clearToast() { _toastMessage.value = null }
}
