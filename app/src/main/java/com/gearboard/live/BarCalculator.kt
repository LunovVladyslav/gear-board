package com.gearboard.live

import com.gearboard.domain.model.TimeSignature

object BarCalculator {
    fun beatDurationMs(bpm: Float, denominator: Int): Long {
        val quarterNoteMs = 60000f / bpm
        return (quarterNoteMs * (4f / denominator)).toLong()
    }

    fun barDurationMs(bpm: Float, ts: TimeSignature): Long {
        return beatDurationMs(bpm, ts.denominator) * ts.numerator
    }
}
