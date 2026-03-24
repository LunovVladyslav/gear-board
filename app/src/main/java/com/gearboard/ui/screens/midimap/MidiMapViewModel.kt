package com.gearboard.ui.screens.midimap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gearboard.data.repository.MidiMappingRepository
import com.gearboard.domain.model.MidiDirection
import com.gearboard.domain.model.MidiEvent
import com.gearboard.domain.model.MidiEventType
import com.gearboard.domain.model.MidiMapping
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
     * Listens for incoming CC messages for 10 seconds.
     */
    fun startLearn(controlId: String, controlName: String) {
        // Cancel any existing learn session
        cancelLearn()

        _learnState.value = LearnState(
            isActive = true,
            controlId = controlId,
            controlName = controlName,
            remainingSeconds = 10
        )

        // Countdown timer
        learnJob = viewModelScope.launch {
            for (i in 10 downTo 1) {
                _learnState.value = _learnState.value.copy(remainingSeconds = i)
                delay(1000)
            }
            // Timeout — no CC detected
            _learnState.value = LearnState()
            _toastMessage.value = "Learn timed out"
        }

        // Listen for incoming CC events
        listenJob = viewModelScope.launch {
            midiManager.midiEvents.collect { event ->
                if (_learnState.value.isActive &&
                    event.type == MidiEventType.CONTROL_CHANGE &&
                    event.direction == MidiDirection.INCOMING
                ) {
                    val ccNumber = event.data1
                    _learnState.value = _learnState.value.copy(detectedCc = ccNumber)

                    // Auto-assign after brief delay for UI feedback
                    delay(500)
                    assignMapping(controlId, controlName, ccNumber, event.channel)
                    cancelLearn()
                }
            }
        }
    }

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
