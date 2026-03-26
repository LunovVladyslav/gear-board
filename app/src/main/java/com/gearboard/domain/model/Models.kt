package com.gearboard.domain.model

/**
 * Core domain models for GearBoard.
 * These represent the UI/business layer data structures,
 * separate from Room entities for clean architecture.
 */

/**
 * A block (pedal, effect unit, etc.) containing arbitrary controls.
 * Used in Pedals and Effects sections which support multiple blocks.
 */
data class ControlBlock(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val type: String = "Custom",        // e.g. "Overdrive", "Delay", "EQ"
    val enabled: Boolean = false,
    val controls: List<ControlType> = emptyList()
)

/** Amp section settings */
data class AmpSettings(
    val enabled: Boolean = true,
    val model: String = "Clean",
    val channel: String = "A",
    val controls: List<ControlType> = emptyList()
)

/** Cabinet settings */
data class CabinetSettings(
    val enabled: Boolean = true,
    val model: String = "4x12",
    val micPosition: String = "Center",
    val controls: List<ControlType> = emptyList()
)

/** Complete board state (all sections) */
data class BoardState(
    val pedals: List<ControlBlock> = emptyList(),
    val amp: AmpSettings = AmpSettings(),
    val cabinet: CabinetSettings = CabinetSettings(),
    val effects: List<ControlBlock> = emptyList()
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
    val controlId: String,       // Reference to ControlType.id
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
