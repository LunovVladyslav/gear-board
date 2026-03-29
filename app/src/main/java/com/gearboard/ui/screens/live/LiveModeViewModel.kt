package com.gearboard.ui.screens.live

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gearboard.data.repository.LiveModeRepository
import com.gearboard.data.repository.PresetRepository
import com.gearboard.domain.model.BarEvent
import com.gearboard.domain.model.ClockSource
import com.gearboard.domain.model.LiveSession
import com.gearboard.domain.model.LiveState
import com.gearboard.domain.model.Preset
import com.gearboard.domain.model.SyncMode
import com.gearboard.domain.model.TimeSignature
import com.gearboard.live.StandalonePlaybackEngine
import com.gearboard.midi.GearBoardMidiManager
import com.gearboard.midi.MidiClockReceiver
import com.gearboard.ui.components.ConnectionState
import com.gearboard.ui.components.ConnectionType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveModeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: LiveModeRepository,
    private val presetRepository: PresetRepository,
    private val midiManager: GearBoardMidiManager
) : ViewModel() {

    companion object {
        private const val PREFS_NAME = "gearboard_live"
        private const val KEY_BLE_WARNING_SHOWN = "live_ble_warning_shown"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // --- Sessions ---
    val sessions: StateFlow<List<LiveSession>> = repository.getAllSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentSession = MutableStateFlow<LiveSession?>(null)
    val currentSession: StateFlow<LiveSession?> = _currentSession.asStateFlow()

    private val _barEvents = MutableStateFlow<List<BarEvent>>(emptyList())
    val barEvents: StateFlow<List<BarEvent>> = _barEvents.asStateFlow()

    val presets: StateFlow<List<Preset>> = presetRepository.getAllPresets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Live state ---
    private val _liveState = MutableStateFlow(LiveState())
    val liveState: StateFlow<LiveState> = _liveState.asStateFlow()

    private val _syncMode = MutableStateFlow(SyncMode.STANDALONE)
    val syncMode: StateFlow<SyncMode> = _syncMode.asStateFlow()

    // --- BLE warning ---
    private val _showBleWarning = MutableStateFlow(false)
    val showBleWarning: StateFlow<Boolean> = _showBleWarning.asStateFlow()

    // --- Engines ---
    private val standaloneEngine = StandalonePlaybackEngine(midiManager, presetRepository)

    private val clockReceiver = MidiClockReceiver(
        presetRepository = presetRepository,
        midiManager = midiManager,
        onStateUpdate = { state ->
            _liveState.value = state
        }
    )

    init {
        // Wire clock receiver into MidiManager
        midiManager.clockReceiver = clockReceiver
    }

    override fun onCleared() {
        super.onCleared()
        standaloneEngine.stop()
        midiManager.clockReceiver = null
    }

    // --- Session management ---

    fun createSession(name: String, bpm: Float, ts: TimeSignature, totalBars: Int) {
        viewModelScope.launch {
            val session = LiveSession(
                name = name,
                totalBars = totalBars,
                initialBpm = bpm,
                initialTimeSignature = ts
            )
            repository.saveSession(session)
        }
    }

    fun selectSession(session: LiveSession) {
        _currentSession.value = session
        viewModelScope.launch {
            repository.getEventsForSession(session.id).collect { events ->
                _barEvents.value = events
                clockReceiver.session = session
                clockReceiver.events = events
            }
        }
    }

    fun clearCurrentSession() {
        stopPlayback()
        _currentSession.value = null
        _barEvents.value = emptyList()
    }

    fun deleteSession(session: LiveSession) {
        viewModelScope.launch {
            if (_currentSession.value?.id == session.id) clearCurrentSession()
            repository.deleteSession(session)
        }
    }

    // --- Bar editing ---

    fun setPresetAtBar(bar: Int, presetId: Long?) {
        val session = _currentSession.value ?: return
        viewModelScope.launch {
            val existing = _barEvents.value.firstOrNull { it.barNumber == bar }
            val event = (existing ?: BarEvent(liveSessionId = session.id, barNumber = bar))
                .copy(presetId = presetId)
            repository.upsertEvent(event)
        }
    }

    fun setBpmAtBar(bar: Int, bpm: Float?) {
        val session = _currentSession.value ?: return
        viewModelScope.launch {
            val existing = _barEvents.value.firstOrNull { it.barNumber == bar }
            val event = (existing ?: BarEvent(liveSessionId = session.id, barNumber = bar))
                .copy(bpm = bpm)
            repository.upsertEvent(event)
        }
    }

    fun setTimeSignatureAtBar(bar: Int, ts: TimeSignature?) {
        val session = _currentSession.value ?: return
        viewModelScope.launch {
            val existing = _barEvents.value.firstOrNull { it.barNumber == bar }
            val event = (existing ?: BarEvent(liveSessionId = session.id, barNumber = bar))
                .copy(timeSignature = ts)
            repository.upsertEvent(event)
        }
    }

    fun clearBar(bar: Int) {
        val session = _currentSession.value ?: return
        viewModelScope.launch {
            repository.clearBar(session.id, bar)
        }
    }

    // --- Playback ---

    fun setSyncMode(mode: SyncMode) {
        _syncMode.value = mode
        _liveState.value = _liveState.value.copy(syncMode = mode)

        if (mode == SyncMode.SYNCED) {
            standaloneEngine.stop()
            // Check BLE warning
            val connectionState = midiManager.connectionState.value
            if (connectionState is ConnectionState.Connected &&
                connectionState.type == ConnectionType.BLUETOOTH &&
                !prefs.getBoolean(KEY_BLE_WARNING_SHOWN, false)
            ) {
                _showBleWarning.value = true
            }
            // Detect clock source
            clockReceiver.clockSource = when {
                connectionState is ConnectionState.Connected &&
                        connectionState.type == ConnectionType.BLUETOOTH -> ClockSource.BLE_MIDI
                connectionState is ConnectionState.Connected -> ClockSource.USB_MIDI
                else -> ClockSource.NONE
            }
        }
    }

    fun dismissBleWarning() {
        prefs.edit().putBoolean(KEY_BLE_WARNING_SHOWN, true).apply()
        _showBleWarning.value = false
    }

    fun startStandalone() {
        val session = _currentSession.value ?: return
        standaloneEngine.start(session, _barEvents.value) { state ->
            _liveState.value = state
        }
    }

    fun stopPlayback() {
        standaloneEngine.stop()
        _liveState.value = _liveState.value.copy(isPlaying = false)
    }

    fun resetPlayback() {
        stopPlayback()
        val session = _currentSession.value
        _liveState.value = LiveState(
            currentBpm = session?.initialBpm ?: 120f,
            currentTimeSignature = session?.initialTimeSignature ?: TimeSignature(4, 4),
            syncMode = _syncMode.value
        )
    }
}
