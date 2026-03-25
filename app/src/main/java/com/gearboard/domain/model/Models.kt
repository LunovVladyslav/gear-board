package com.gearboard.domain.model

/**
 * Core domain models for GearBoard.
 * These represent the UI/business layer data structures,
 * separate from Room entities for clean architecture.
 */

/** A single control (knob, slider) on a pedal/amp/effect */
data class ControlParam(
    val id: String,
    val name: String,
    val value: Float = 0.5f,      // 0f..1f normalized
    val minValue: Float = 0f,
    val maxValue: Float = 127f,   // MIDI CC range
    val unit: String = "",        // e.g. "dB", "Hz", "%"
    val ccNumber: Int = -1        // Assigned MIDI CC (-1 = unmapped)
)

/** Toggle button on a pedal (stomp-style on/off) */
data class ToggleButton(
    val id: String,
    val label: String,
    val enabled: Boolean = false,
    val ccNumber: Int = -1
)

/** Tap tempo button */
data class TapButton(
    val id: String,
    val label: String = "TAP",
    val ccNumber: Int = -1
)

/** A guitar pedal with its controls */
data class Pedal(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val type: String = "Custom",        // e.g. "Overdrive", "Delay", "Reverb"
    val enabled: Boolean = false,
    val controls: List<ControlParam> = emptyList(),
    val toggleButtons: List<ToggleButton> = emptyList(),
    val tapButtons: List<TapButton> = emptyList()
)

/** Amp section settings */
data class AmpSettings(
    val enabled: Boolean = true,
    val model: String = "Clean",
    val channel: String = "A",
    val controls: List<ControlParam> = listOf(
        ControlParam("gain", "Gain", ccNumber = 21),
        ControlParam("bass", "Bass", ccNumber = 22),
        ControlParam("mid", "Mid", ccNumber = 23),
        ControlParam("treble", "Treble", ccNumber = 24),
        ControlParam("presence", "Presence", ccNumber = 25),
        ControlParam("volume", "Volume", ccNumber = 26)
    )
)

/** Cabinet settings */
data class CabinetSettings(
    val enabled: Boolean = true,
    val model: String = "4x12",
    val micPosition: String = "Center",
    val controls: List<ControlParam> = listOf(
        ControlParam("mic_distance", "Mic Dist", unit = "cm", ccNumber = 27),
        ControlParam("low_cut", "Low Cut", unit = "Hz", ccNumber = 28),
        ControlParam("high_cut", "High Cut", unit = "Hz", ccNumber = 29)
    )
)

/** An effect in the effects chain */
data class Effect(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val type: String = "Custom",  // e.g. "EQ", "Compressor", "Chorus"
    val enabled: Boolean = true,
    val controls: List<ControlParam> = emptyList()
)

/** Complete board state (all sections) */
data class BoardState(
    val pedals: List<Pedal> = emptyList(),
    val amp: AmpSettings = AmpSettings(),
    val cabinet: CabinetSettings = CabinetSettings(),
    val effects: List<Effect> = emptyList()
)

/** User preset */
data class Preset(
    val id: Long = 0,
    val name: String,
    val bank: Int = 0,          // 0-based bank number
    val program: Int = 0,       // 0-based program number
    val boardState: BoardState = BoardState(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/** MIDI CC mapping for a control */
data class MidiMapping(
    val id: Long = 0,
    val controlId: String,       // Reference to ControlParam.id
    val controlName: String,
    val ccNumber: Int,           // 0-127
    val channel: Int = 0         // 0-15 (MIDI channels 1-16)
)

/** MIDI event for the monitor screen */
data class MidiEvent(
    val timestamp: Long = System.currentTimeMillis(),
    val type: MidiEventType,
    val channel: Int,
    val data1: Int,             // CC number or note
    val data2: Int,             // Value
    val direction: MidiDirection,
    val rawBytes: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MidiEvent) return false
        return timestamp == other.timestamp && type == other.type &&
                channel == other.channel && data1 == other.data1 && data2 == other.data2
    }

    override fun hashCode(): Int {
        var result = timestamp.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + channel
        result = 31 * result + data1
        result = 31 * result + data2
        return result
    }
}

enum class MidiEventType {
    NOTE_ON, NOTE_OFF, CONTROL_CHANGE, PROGRAM_CHANGE, PITCH_BEND, SYSEX, UNKNOWN
}

enum class MidiDirection {
    INCOMING, OUTGOING
}
