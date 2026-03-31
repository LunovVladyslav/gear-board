package com.gearboard.domain.model

import java.util.Locale
import kotlin.math.roundToInt

/**
 * Display format for continuous controls (Knob, Fader).
 * Converts raw MIDI 0-127 values to human-friendly units.
 *
 * Bug 5 fix: DECIBELS split into DECIBELS_OUTPUT (-∞ to 0 dB) and DECIBELS_BOOST (0 to +12 dB).
 */
enum class DisplayFormat(val display: String) {
    MIDI_RAW("MIDI Raw"),           // 0-127
    ZERO_TO_TEN("0 – 10"),         // 0.0 - 10.0 (amp params: Gain, Bass, Mid…)
    PERCENTAGE("Percent"),          // 0% - 100%  (Mix, Depth, Rate…)
    MILLISECONDS("Millisec"),       // 0ms - 2000ms (Delay Time)
    DECIBELS_OUTPUT("dB Out"),      // -∞ to 0 dB (output/level faders)
    DECIBELS_BOOST("dB Boost"),     // 0 to +12 dB (boost/gain add)
    SEMITONES("Semitones")          // -12 to +12 st (Transpose, Pitch)
}

/**
 * Convert a raw MIDI value (0-127) to a human-readable display string.
 * Values outside 0-127 are clamped.
 */
fun midiToDisplay(midiValue: Int, format: DisplayFormat): String {
    val v = midiValue.coerceIn(0, 127)
    return when (format) {
        DisplayFormat.ZERO_TO_TEN    -> String.format(Locale.US, "%.1f", v / 127f * 10f)
        DisplayFormat.PERCENTAGE     -> "${(v / 127f * 100).roundToInt()}%"
        DisplayFormat.MILLISECONDS   -> "${(v / 127f * 2000).roundToInt()}ms"
        DisplayFormat.DECIBELS_OUTPUT -> if (v == 0) "-∞ dB"
                                         else String.format(Locale.US, "%.1f dB", (v / 127f * 60f) - 60f)
        DisplayFormat.DECIBELS_BOOST -> String.format(Locale.US, "+%.1f dB", v / 127f * 12f)
        DisplayFormat.SEMITONES      -> {
            val st = (v / 127f * 24).roundToInt() - 12
            if (st > 0) "+$st st" else "$st st"
        }
        DisplayFormat.MIDI_RAW       -> v.toString()
    }
}

/**
 * Returns the MIDI CC value (0-127) for a selector position.
 *
 * Bug 3 fix: If [ccValues] is provided and valid, returns the explicit value for [index].
 * Otherwise distributes positions evenly using integer division.
 *
 * Even distribution reference:
 *   2 positions → 0, 127
 *   3 positions → 0, 63, 127
 *   4 positions → 0, 42, 85, 127
 *   5 positions → 0, 32, 64, 96, 127
 */
fun selectorCCValue(index: Int, positions: Int, ccValues: List<Int>? = null): Int {
    if (ccValues != null && ccValues.size == positions) {
        return ccValues[index].coerceIn(0, 127)
    }
    if (positions <= 1) return 0
    return (index * 127) / (positions - 1)
}

enum class ControlSize(val scaleFactor: Float) {
    SMALL(0.75f),
    MEDIUM(1.0f),
    LARGE(1.25f)
}

/** Navigation mode for PresetNav controls. Bug 1 fix. */
enum class PresetNavMode {
    CC_INC_DEC,  // Neural DSP: one CC, value=0 dec, value=127 inc
    CC_SPLIT,    // Two separate CCs for dec and inc
    PC           // Standard Program Change messages
}

/**
 * Unified sealed class for all control primitives in GearBoard.
 * Each subclass represents one type of MIDI control with its configuration.
 */
sealed class ControlType {
    abstract val id: String
    abstract val label: String
    abstract val midiChannel: Int
    abstract val size: ControlSize
    abstract val ccNumber: Int

    /**
     * Bug 6 fix: A/B bank CC numbers added to Knob, Toggle, Fader.
     * ccNumberA = CC sent in Bank A (default = ccNumber)
     * ccNumberB = CC sent in Bank B (default = same as A)
     */
    data class Knob(
        override val id: String = java.util.UUID.randomUUID().toString(),
        override val label: String,
        override val ccNumber: Int,
        override val midiChannel: Int = 1,
        override val size: ControlSize = ControlSize.MEDIUM,
        val defaultValue: Float = 0.5f,  // 0f..1f normalized
        val value: Float = defaultValue,
        val displayFormat: DisplayFormat = DisplayFormat.ZERO_TO_TEN,
        val ccNumberA: Int = ccNumber,
        val ccNumberB: Int = ccNumber
    ) : ControlType()

    /**
     * Bug 4 fix: pulseMode renamed to momentaryMode with inverted default.
     * momentaryMode=false → latching on/off (default).
     * momentaryMode=true  → sends 127 then 0 after 50ms (momentary bypass trigger).
     */
    data class Toggle(
        override val id: String = java.util.UUID.randomUUID().toString(),
        override val label: String,
        override val ccNumber: Int,
        override val midiChannel: Int = 1,
        override val size: ControlSize = ControlSize.MEDIUM,
        val momentaryMode: Boolean = false,
        val isOn: Boolean = false,
        val isStompButton: Boolean = false,
        val ccNumberA: Int = ccNumber,
        val ccNumberB: Int = ccNumber
    ) : ControlType()

    data class Tap(
        override val id: String = java.util.UUID.randomUUID().toString(),
        override val label: String,
        override val ccNumber: Int,
        override val midiChannel: Int = 1,
        override val size: ControlSize = ControlSize.MEDIUM
        // Stateless — sends CC 127 on each press
    ) : ControlType()

    /**
     * Bug 3 fix: explicit ccValues list added for per-position CC override.
     * If null, CC values are distributed evenly via selectorCCValue().
     * If provided, must be the same size as positions.
     */
    data class Selector(
        override val id: String = java.util.UUID.randomUUID().toString(),
        override val label: String,
        override val ccNumber: Int,
        override val midiChannel: Int = 1,
        override val size: ControlSize = ControlSize.MEDIUM,
        val positions: List<String>,
        val selectedIndex: Int = 0,
        val ccValues: List<Int>? = null
    ) : ControlType()

    data class Fader(
        override val id: String = java.util.UUID.randomUUID().toString(),
        override val label: String,
        override val ccNumber: Int,
        override val midiChannel: Int = 1,
        override val size: ControlSize = ControlSize.MEDIUM,
        val defaultValue: Float = 0.5f,
        val value: Float = defaultValue,
        val orientation: FaderOrientation = FaderOrientation.VERTICAL,
        val displayFormat: DisplayFormat = DisplayFormat.ZERO_TO_TEN,
        val ccNumberA: Int = ccNumber,
        val ccNumberB: Int = ccNumber
    ) : ControlType()

    /**
     * Bug 1 fix: PresetNav now implements ccNumber and adds navMode.
     * CC_INC_DEC: sends value 0 (prev) or 127 (next) on the same CC.
     * PC: sends Program Change messages.
     */
    data class PresetNav(
        override val id: String = java.util.UUID.randomUUID().toString(),
        override val label: String = "PRESET",
        override val ccNumber: Int = 80,
        override val midiChannel: Int = 1,
        override val size: ControlSize = ControlSize.MEDIUM,
        val currentPreset: Int = 0,
        val navMode: PresetNavMode = PresetNavMode.CC_INC_DEC,
        val pcChannel: Int = 1
    ) : ControlType()

    /**
     * Bug 2 fix: Pad now implements ccNumber (sentinel -1 — pads use noteNumber, not CC).
     */
    data class Pad(
        override val id: String = java.util.UUID.randomUUID().toString(),
        override val label: String,
        val noteNumber: Int,
        override val ccNumber: Int = -1,   // Pads use noteNumber, not CC
        override val midiChannel: Int = 10,
        override val size: ControlSize = ControlSize.MEDIUM,
        val velocity: Int = 100
    ) : ControlType()
}

enum class FaderOrientation { VERTICAL, HORIZONTAL }

enum class SectionType { PEDALS, AMP, CAB, EFFECTS }
