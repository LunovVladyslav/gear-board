package com.gearboard.ui.screens.monitor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gearboard.domain.model.MidiEvent
import com.gearboard.midi.GearBoardMidiManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MonitorViewModel @Inject constructor(
    private val midiManager: GearBoardMidiManager
) : ViewModel() {

    companion object {
        const val MAX_EVENTS = 200
    }

    private val _events = MutableStateFlow<List<MidiEvent>>(emptyList())
    val events: StateFlow<List<MidiEvent>> = _events.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> = _isPaused.asStateFlow()

    private val _filterType = MutableStateFlow<String?>(null)
    val filterType: StateFlow<String?> = _filterType.asStateFlow()

    val connectionState = midiManager.connectionState

    init {
        viewModelScope.launch {
            midiManager.midiEvents.collect { event ->
                if (!_isPaused.value) {
                    val current = _events.value.toMutableList()
                    current.add(0, event) // Newest first
                    if (current.size > MAX_EVENTS) {
                        _events.value = current.take(MAX_EVENTS)
                    } else {
                        _events.value = current
                    }
                }
            }
        }
    }

    fun togglePause() {
        _isPaused.value = !_isPaused.value
    }

    fun clearEvents() {
        _events.value = emptyList()
    }

    fun setFilter(type: String?) {
        _filterType.value = type
    }
}
