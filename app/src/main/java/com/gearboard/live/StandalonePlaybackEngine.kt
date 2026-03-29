package com.gearboard.live

import com.gearboard.data.repository.PresetRepository
import com.gearboard.domain.model.BarEvent
import com.gearboard.domain.model.ClockSource
import com.gearboard.domain.model.LiveSession
import com.gearboard.domain.model.LiveState
import com.gearboard.domain.model.SyncMode
import com.gearboard.midi.GearBoardMidiManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class StandalonePlaybackEngine(
    private val midiManager: GearBoardMidiManager,
    private val presetRepository: PresetRepository
) {
    private var playbackJob: Job? = null

    fun start(
        session: LiveSession,
        events: List<BarEvent>,
        onStateUpdate: (LiveState) -> Unit
    ) {
        playbackJob?.cancel()
        playbackJob = CoroutineScope(Dispatchers.Default).launch {
            var currentBpm = session.initialBpm
            var currentTs = session.initialTimeSignature
            var currentPresetId: Long? = null
            val eventsByBar = events.groupBy { it.barNumber }

            for (bar in 1..session.totalBars) {
                if (!isActive) break

                eventsByBar[bar]?.firstOrNull()?.let { event ->
                    event.bpm?.let { currentBpm = it }
                    event.timeSignature?.let { currentTs = it }
                    event.presetId?.let { presetId ->
                        if (presetId != currentPresetId) {
                            currentPresetId = presetId
                            val preset = presetRepository.getPresetById(presetId)
                            if (preset != null) {
                                midiManager.sendPreset(preset.bank, preset.program)
                            }
                        }
                    }
                }

                val beatMs = BarCalculator.beatDurationMs(currentBpm, currentTs.denominator)
                val nextEvent = findNextEventBar(eventsByBar, bar, session.totalBars)

                for (beat in 1..currentTs.numerator) {
                    if (!isActive) break
                    onStateUpdate(
                        LiveState(
                            isPlaying = true,
                            syncMode = SyncMode.STANDALONE,
                            currentBar = bar,
                            currentBeat = beat,
                            currentBpm = currentBpm,
                            currentTimeSignature = currentTs,
                            currentPresetId = currentPresetId,
                            nextEventBar = nextEvent,
                            clockSource = ClockSource.NONE
                        )
                    )
                    delay(beatMs)
                }
            }

            onStateUpdate(
                LiveState(
                    isPlaying = false,
                    currentBpm = session.initialBpm,
                    currentTimeSignature = session.initialTimeSignature
                )
            )
        }
    }

    fun stop() {
        playbackJob?.cancel()
    }

    private fun findNextEventBar(
        eventsByBar: Map<Int, List<BarEvent>>,
        currentBar: Int,
        totalBars: Int
    ): Int? {
        for (b in (currentBar + 1)..totalBars) {
            if (eventsByBar.containsKey(b)) return b
        }
        return null
    }
}
