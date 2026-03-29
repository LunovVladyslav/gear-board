package com.gearboard.midi

import com.gearboard.data.repository.PresetRepository
import com.gearboard.domain.model.BarEvent
import com.gearboard.domain.model.ClockSource
import com.gearboard.domain.model.LiveSession
import com.gearboard.domain.model.LiveState
import com.gearboard.domain.model.SyncMode
import com.gearboard.domain.model.TimeSignature
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MidiClockReceiver(
    private val presetRepository: PresetRepository,
    private val midiManager: GearBoardMidiManager,
    private val onStateUpdate: (LiveState) -> Unit
) {
    var session: LiveSession? = null
    var events: List<BarEvent> = emptyList()
    var clockSource: ClockSource = ClockSource.NONE

    // Runtime state
    private var tickCount = 0
    private var currentBar = 1
    private var currentBeat = 0
    private var currentPresetId: Long? = null
    private var currentTs = TimeSignature(4, 4)
    private var currentBpm = 120f

    // BPM detection: rolling window of last 8 tick timestamps
    private val tickTimestamps = ArrayDeque<Long>(8)

    // SPP pending bytes
    private var sppByte1: Int? = null

    fun onStart() {
        tickCount = 0
        currentBar = 1
        currentBeat = 0
        currentPresetId = null
        currentTs = session?.initialTimeSignature ?: TimeSignature(4, 4)
        currentBpm = session?.initialBpm ?: 120f
        tickTimestamps.clear()
        sppByte1 = null
        applyEventsAtBar(1)
        emitState(isPlaying = true)
    }

    fun onContinue() {
        emitState(isPlaying = true)
    }

    fun onStop() {
        emitState(isPlaying = false)
    }

    fun onTick(timestampMs: Long) {
        // BPM detection
        tickTimestamps.addLast(timestampMs)
        if (tickTimestamps.size > 8) tickTimestamps.removeFirst()
        if (tickTimestamps.size >= 2) {
            val avgIntervalMs = (tickTimestamps.last() - tickTimestamps.first()) /
                    (tickTimestamps.size - 1).toFloat()
            if (avgIntervalMs > 0) {
                currentBpm = 60000f / (avgIntervalMs * 24f)
            }
        }

        tickCount++
        val ticksPerBar = currentTs.ticksPerBar()
        val ticksPerBeat = (ticksPerBar / currentTs.numerator).coerceAtLeast(1)
        currentBeat = (tickCount / ticksPerBeat) + 1

        if (tickCount >= ticksPerBar) {
            tickCount = 0
            currentBar++
            val sess = session ?: run { emitState(isPlaying = true); return }
            if (currentBar > sess.totalBars) {
                onStop()
                return
            }
            applyEventsAtBar(currentBar)
        }

        emitState(isPlaying = true)
    }

    fun onSongPositionPointer(lsb: Int, msb: Int) {
        val beats = (msb shl 7) or lsb   // 14-bit value: MIDI beats (6 ticks each)
        val totalTicks = beats * 6
        val ticksPerBar = currentTs.ticksPerBar()
        currentBar = (totalTicks / ticksPerBar) + 1
        tickCount = totalTicks % ticksPerBar
        applyEventsAtBar(currentBar)
        emitState(isPlaying = false)
    }

    private fun applyEventsAtBar(bar: Int) {
        val event = events.firstOrNull { it.barNumber == bar } ?: return
        event.timeSignature?.let { currentTs = it }
        event.presetId?.let { presetId ->
            if (presetId != currentPresetId) {
                currentPresetId = presetId
                CoroutineScope(Dispatchers.IO).launch {
                    val preset = presetRepository.getPresetById(presetId)
                    if (preset != null) {
                        midiManager.sendPreset(preset.bank, preset.program)
                    }
                }
            }
        }
    }

    private fun emitState(isPlaying: Boolean) {
        onStateUpdate(
            LiveState(
                isPlaying = isPlaying,
                syncMode = SyncMode.SYNCED,
                currentBar = currentBar,
                currentBeat = currentBeat,
                currentTick = tickCount,
                currentBpm = currentBpm,
                detectedBpm = currentBpm,
                currentTimeSignature = currentTs,
                currentPresetId = currentPresetId,
                clockSource = clockSource
            )
        )
    }
}
