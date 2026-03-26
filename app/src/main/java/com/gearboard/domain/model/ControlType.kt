package com.gearboard.domain.model

import kotlin.math.roundToInt

/**
 * Display format for continuous controls (Knob, Fader).
 * Converts raw MIDI 0-127 values to human-friendly units.
 */
enum class DisplayFormat(val display: String) {
    MIDI_RAW("MIDI Raw"),       // 0-127
    ZERO_TO_TEN("0 – 10"),     // 0.0 - 10.0 (amp params: Gain, Bass, Mid…)
    PERCENTAGE("Percent"),      // 0% - 100%  (Mix, Depth, Rate…)
    MILLISECONDS("Millisec"),   // 0ms - 2000ms (Delay Time)
    DECIBELS("Decibels"),       // -∞ to +6.0 dB (Volume, Level)
    SEMITONES("Semitones")      // -12 to +12 st (Transpose, Pitch)
}

/**
 * Convert a raw MIDI value (0-127) to a human-readable display string
 * based on the selected [DisplayFormat].
 */
fun midiToDisplay(midiValue: Int, format: DisplayFormat): String {
    return when (format) {
        DisplayFormat.ZERO_TO_TEN  -> "%.1f".format(midiValue / 127f * 10f)
        DisplayFormat.PERCENTAGE   -> "${(midiValue / 127f * 100).roundToInt()}%"
        DisplayFormat.MILLISECONDS -> "${(midiValue / 127f * 2000).roundToInt()}ms"
        DisplayFormat.DECIBELS     -> if (midiValue == 0) "-∞ dB"
                                      else "%.1f dB".format(midiValue / 127f * 12f - 6f)
        DisplayFormat.SEMITONES    -> "${(midiValue / 127f * 24).roundToInt() - 12} st"
        DisplayFormat.MIDI_RAW     -> midiValue.toString()
    }
}

/**
 * Unified sealed class for all control primitives in GearBoard.
 * Each subclass represents one type of MIDI control with its configuration.
 */
sealed class ControlType {
    abstract val id: String
    abstract val label: String
    abstract val midiChannel: Int

    data class Knob(
        override val id: String = java.util.UUID.randomUUID().toString(),
        override val label: String,
        val ccNumber: Int,
        override val midiChannel: Int = 1,
        val defaultValue: Float = 0.5f,  // 0f..1f normalized
        val value: Float = defaultValue,
        val displayFormat: DisplayFormat = DisplayFormat.ZERO_TO_TEN
    ) : ControlType()

    data class Toggle(
        override val id: String = java.util.UUID.randomUUID().toString(),
        override val label: String,
        val ccNumber: Int,
        override val midiChannel: Int = 1,
        val pulseMode: Boolean = true,   // pulse 127→0 with 50ms delay
        val isOn: Boolean = false
    ) : ControlType()

    data class Tap(
        override val id: String = java.util.UUID.randomUUID().toString(),
        override val label: String,
        val ccNumber: Int,
        override val midiChannel: Int = 1
        // Stateless — sends CC 127 on each press
    ) : ControlType()

    data class Selector(
        override val id: String = java.util.UUID.randomUUID().toString(),
        override val label: String,
        val ccNumber: Int,
        override val midiChannel: Int = 1,
        val positions: List<String>,       // e.g. ["Phase", "Vibe"]
        val selectedIndex: Int = 0
        // CC value = (index * 127) / (positions.size - 1)
    ) : ControlType()

    data class Fader(
        override val id: String = java.util.UUID.randomUUID().toString(),
        override val label: String,
        val ccNumber: Int,
        override val midiChannel: Int = 1,
        val defaultValue: Float = 0.5f,
        val value: Float = defaultValue,
        val orientation: FaderOrientation = FaderOrientation.VERTICAL,
        val displayFormat: DisplayFormat = DisplayFormat.ZERO_TO_TEN
    ) : ControlType()

    data class PresetNav(
        override val id: String = java.util.UUID.randomUUID().toString(),
        override val label: String = "PRESET",
        override val midiChannel: Int = 1,
        val currentPreset: Int = 0  // 0-127
    ) : ControlType()

    data class Pad(
        override val id: String = java.util.UUID.randomUUID().toString(),
        override val label: String,
        val noteNumber: Int,          // MIDI note 0-127
        override val midiChannel: Int = 10,  // channel 10 = drums
        val velocity: Int = 100       // fixed velocity
    ) : ControlType()
}

enum class FaderOrientation { VERTICAL, HORIZONTAL }

enum class SectionType { PEDALS, AMP, CAB, EFFECTS }
