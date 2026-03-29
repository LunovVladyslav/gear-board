package com.gearboard.domain.model

import java.util.UUID

data class TimeSignature(
    val numerator: Int = 4,
    val denominator: Int = 4
) {
    fun ticksPerBar(): Int {
        val ticksPerQuarter = 24
        val ticksPerDenominator = ticksPerQuarter * (4f / denominator)
        return (ticksPerDenominator * numerator).toInt()
    }
}

data class LiveSession(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val totalBars: Int,
    val initialBpm: Float,
    val initialTimeSignature: TimeSignature,
    val createdAt: Long = System.currentTimeMillis()
)

data class BarEvent(
    val id: String = UUID.randomUUID().toString(),
    val liveSessionId: String,
    val barNumber: Int,
    val presetId: Long? = null,
    val bpm: Float? = null,
    val timeSignature: TimeSignature? = null
)

data class LiveState(
    val isPlaying: Boolean = false,
    val syncMode: SyncMode = SyncMode.STANDALONE,
    val currentBar: Int = 0,
    val currentBeat: Int = 0,
    val currentTick: Int = 0,
    val currentBpm: Float = 120f,
    val detectedBpm: Float? = null,
    val currentTimeSignature: TimeSignature = TimeSignature(4, 4),
    val currentPresetId: Long? = null,
    val nextEventBar: Int? = null,
    val clockSource: ClockSource = ClockSource.NONE
)

enum class SyncMode { STANDALONE, SYNCED }
enum class ClockSource { NONE, BLE_MIDI, USB_MIDI }
