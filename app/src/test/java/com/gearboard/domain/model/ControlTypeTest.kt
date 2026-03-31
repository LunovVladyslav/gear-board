package com.gearboard.domain.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Unit tests for [midiToDisplay] and [selectorCCValue].
 * All tests use English locale formatting (dot decimal separator).
 */
class ControlTypeTest {

    // --- midiToDisplay: ZERO_TO_TEN ---

    @Test fun `ZERO_TO_TEN maps 0 to 0dot0`() =
        assertThat(midiToDisplay(0, DisplayFormat.ZERO_TO_TEN)).isEqualTo("0.0")

    @Test fun `ZERO_TO_TEN maps 127 to 10dot0`() =
        assertThat(midiToDisplay(127, DisplayFormat.ZERO_TO_TEN)).isEqualTo("10.0")

    @Test fun `ZERO_TO_TEN maps 64 to approximately 5dot0`() {
        // 64/127*10 = 5.039... → formatted "5.0"
        val result = midiToDisplay(64, DisplayFormat.ZERO_TO_TEN)
        assertThat(result.toFloat()).isWithin(0.1f).of(5.0f)
    }

    // --- midiToDisplay: PERCENTAGE ---

    @Test fun `PERCENTAGE maps 0 to 0 percent`() =
        assertThat(midiToDisplay(0, DisplayFormat.PERCENTAGE)).isEqualTo("0%")

    @Test fun `PERCENTAGE maps 127 to 100 percent`() =
        assertThat(midiToDisplay(127, DisplayFormat.PERCENTAGE)).isEqualTo("100%")

    // --- midiToDisplay: DECIBELS_OUTPUT ---

    @Test fun `DECIBELS_OUTPUT maps 0 to minus infinity`() =
        assertThat(midiToDisplay(0, DisplayFormat.DECIBELS_OUTPUT)).isEqualTo("-∞ dB")

    // --- midiToDisplay: MILLISECONDS ---

    @Test fun `MILLISECONDS maps 64 to roughly 1008ms`() {
        val result = midiToDisplay(64, DisplayFormat.MILLISECONDS)
        assertThat(result).endsWith("ms")
        val ms = result.removeSuffix("ms").trim().toInt()
        assertThat(ms).isIn(1000..1010)
    }

    // --- midiToDisplay: SEMITONES ---

    @Test fun `SEMITONES maps 0 to minus 12 st`() =
        assertThat(midiToDisplay(0, DisplayFormat.SEMITONES)).isEqualTo("-12 st")

    @Test fun `SEMITONES maps 64 to 0 st`() {
        val result = midiToDisplay(64, DisplayFormat.SEMITONES)
        // 64/127*24 = 12.09 → roundToInt() = 12 → 12-12 = 0
        assertThat(result).isEqualTo("0 st")
    }

    @Test fun `SEMITONES maps 127 to plus 12 st`() =
        assertThat(midiToDisplay(127, DisplayFormat.SEMITONES)).isEqualTo("+12 st")

    // --- midiToDisplay: MIDI_RAW ---

    @Test fun `MIDI_RAW passes value unchanged`() =
        assertThat(midiToDisplay(42, DisplayFormat.MIDI_RAW)).isEqualTo("42")

    // --- midiToDisplay: boundary coercion ---

    @Test fun `midiToDisplay coerces values below 0 to 0`() =
        assertThat(midiToDisplay(-1, DisplayFormat.MIDI_RAW)).isEqualTo("0")

    @Test fun `midiToDisplay coerces values above 127 to 127`() =
        assertThat(midiToDisplay(200, DisplayFormat.MIDI_RAW)).isEqualTo("127")

    // --- selectorCCValue ---

    @Test fun `Selector 2 positions maps index 0 to 0`() =
        assertThat(selectorCCValue(0, 2)).isEqualTo(0)

    @Test fun `Selector 2 positions maps index 1 to 127`() =
        assertThat(selectorCCValue(1, 2)).isEqualTo(127)

    @Test fun `Selector 3 positions maps index 1 to approximately 63`() =
        assertThat(selectorCCValue(1, 3)).isIn(62..64)

    @Test fun `Selector 4 positions maps index 0 to 0 and index 3 to 127`() {
        assertThat(selectorCCValue(0, 4)).isEqualTo(0)
        assertThat(selectorCCValue(3, 4)).isEqualTo(127)
    }

    @Test fun `Selector 1 position always returns 0`() =
        assertThat(selectorCCValue(0, 1)).isEqualTo(0)
}
