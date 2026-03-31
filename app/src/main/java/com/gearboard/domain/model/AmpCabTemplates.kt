package com.gearboard.domain.model

import com.gearboard.domain.model.DisplayFormat.DECIBELS_OUTPUT
import com.gearboard.domain.model.DisplayFormat.PERCENTAGE
import com.gearboard.domain.model.DisplayFormat.ZERO_TO_TEN
import com.gearboard.domain.model.FaderOrientation.HORIZONTAL

/**
 * Predefined amp block templates.
 * Each entry is a ready-to-add AmpBlock with typical controls for the amp style.
 */
object AmpTemplates {

    val CLEAN_AMERICAN = AmpBlock(
        name = "Clean American",
        // Fender-style: loud clean headroom, bright
        controls = listOf(
            ControlType.Knob(label = "Volume", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Bass", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Mid", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Treble", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Reverb", ccNumber = 0, displayFormat = PERCENTAGE),
            ControlType.Toggle(label = "Bright", ccNumber = 0)
        )
    )

    val BRITISH_CRUNCH = AmpBlock(
        name = "British Crunch",
        // Marshall-style: shared EQ, two channels blended
        controls = listOf(
            ControlType.Knob(label = "Gain", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Bass", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Mid", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Treble", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Presence", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Master", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Toggle(label = "Channel", ccNumber = 0)   // Ch1/Ch2
        )
    )

    val BRITISH_CLASS_A = AmpBlock(
        name = "British Class A",
        // Vox-style: no mid control, tone cut, Top Boost
        controls = listOf(
            ControlType.Knob(label = "Volume", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Bass", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Treble", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Cut", ccNumber = 0, displayFormat = ZERO_TO_TEN),  // tone cut (inverse)
            ControlType.Toggle(label = "Top Boost", ccNumber = 0)
        )
    )

    val HIGH_GAIN = AmpBlock(
        name = "High Gain",
        // Mesa-style: multi-channel, tight response
        controls = listOf(
            ControlType.Knob(label = "Gain", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Bass", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Mid", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Treble", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Presence", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Master", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Selector(label = "Channel", ccNumber = 0, positions = listOf("Clean", "Crunch", "Lead")),
            ControlType.Toggle(label = "Bright", ccNumber = 0)
        )
    )

    val MODERN_HIGH_GAIN = AmpBlock(
        name = "Modern High Gain",
        // Bogner/Diezel-style: boutique, tight + loose switch
        controls = listOf(
            ControlType.Knob(label = "Gain", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Bass", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Mid", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Treble", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Presence", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Master", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Depth", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Selector(label = "Channel", ccNumber = 0, positions = listOf("A", "B")),
            ControlType.Toggle(label = "Tight", ccNumber = 0),
            ControlType.Toggle(label = "Bright", ccNumber = 0)
        )
    )

    val BASS_AMP = AmpBlock(
        name = "Bass Amp",
        controls = listOf(
            ControlType.Knob(label = "Gain", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Bass", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Low Mid", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "High Mid", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Treble", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Master", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Blend", ccNumber = 0, displayFormat = PERCENTAGE),  // dry/wet
            ControlType.Toggle(label = "Bright", ccNumber = 0)
        )
    )

    val all: List<AmpBlock> = listOf(
        CLEAN_AMERICAN, BRITISH_CRUNCH, BRITISH_CLASS_A, HIGH_GAIN, MODERN_HIGH_GAIN, BASS_AMP
    )
}

/**
 * Predefined cabinet block templates.
 */
object CabTemplates {

    val OPEN_BACK_112 = CabBlock(
        name = "1×12 Open",
        // warm, airy — typical combo
        controls = listOf(
            ControlType.Selector(label = "Mic Type", ccNumber = 0, positions = listOf("SM57", "MD421", "R121")),
            ControlType.Fader(label = "Mic Position", ccNumber = 0, orientation = HORIZONTAL, displayFormat = PERCENTAGE),
            ControlType.Knob(label = "Distance", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Level", ccNumber = 0, displayFormat = DECIBELS_OUTPUT)
        )
    )

    val CLOSED_BACK_212 = CabBlock(
        name = "2×12 Closed",
        controls = listOf(
            ControlType.Selector(label = "Mic Type", ccNumber = 0, positions = listOf("SM57", "MD421", "R121", "U87")),
            ControlType.Fader(label = "Mic Position", ccNumber = 0, orientation = HORIZONTAL, displayFormat = PERCENTAGE),
            ControlType.Knob(label = "Distance", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Level", ccNumber = 0, displayFormat = DECIBELS_OUTPUT),
            ControlType.Knob(label = "Room", ccNumber = 0, displayFormat = PERCENTAGE)
        )
    )

    val CLOSED_BACK_412 = CabBlock(
        name = "4×12 Closed",
        // typical for British and high-gain styles
        controls = listOf(
            ControlType.Selector(label = "Mic Type", ccNumber = 0, positions = listOf("SM57", "MD421", "R121", "U87")),
            ControlType.Fader(label = "Mic Position", ccNumber = 0, orientation = HORIZONTAL, displayFormat = PERCENTAGE),
            ControlType.Knob(label = "Distance", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Level", ccNumber = 0, displayFormat = DECIBELS_OUTPUT),
            ControlType.Knob(label = "Room", ccNumber = 0, displayFormat = PERCENTAGE),
            ControlType.Toggle(label = "Phase", ccNumber = 0)
        )
    )

    val BASS_115 = CabBlock(
        name = "Bass 1×15",
        controls = listOf(
            ControlType.Selector(label = "Mic Type", ccNumber = 0, positions = listOf("MD421", "D112", "SM7B")),
            ControlType.Fader(label = "Mic Position", ccNumber = 0, orientation = HORIZONTAL, displayFormat = PERCENTAGE),
            ControlType.Knob(label = "Distance", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Level", ccNumber = 0, displayFormat = DECIBELS_OUTPUT)
        )
    )

    val BASS_410 = CabBlock(
        name = "Bass 4×10",
        controls = listOf(
            ControlType.Selector(label = "Mic Type", ccNumber = 0, positions = listOf("MD421", "D112", "SM7B")),
            ControlType.Fader(label = "Mic Position", ccNumber = 0, orientation = HORIZONTAL, displayFormat = PERCENTAGE),
            ControlType.Knob(label = "Distance", ccNumber = 0, displayFormat = ZERO_TO_TEN),
            ControlType.Knob(label = "Level", ccNumber = 0, displayFormat = DECIBELS_OUTPUT),
            ControlType.Toggle(label = "Tweeter", ccNumber = 0)
        )
    )

    val all: List<CabBlock> = listOf(
        OPEN_BACK_112, CLOSED_BACK_212, CLOSED_BACK_412, BASS_115, BASS_410
    )
}
