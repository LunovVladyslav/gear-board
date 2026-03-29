package com.gearboard.domain.model

/**
 * Core domain models for GearBoard.
 * These represent the UI/business layer data structures,
 * separate from Room entities for clean architecture.
 */

/** A/B slot for dual-state switching. */
enum class AbSlot { A, B }

enum class HeaderStyle { DEFAULT, MINIMAL, BOLD }
enum class BlockLayout { GRID, ROW, COMPACT }

data class BlockAppearance(
    val accentColor: Long = 0xFFE8A020, // GearBoardColors.Accent default (Amber)
    val headerStyle: HeaderStyle = HeaderStyle.MINIMAL
)

/**
 * A block (pedal, effect unit, etc.) containing arbitrary controls.
 * Used in Pedals and Effects sections which support multiple blocks.
 */
data class ControlBlock(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val type: String = "Custom",        // e.g. "Overdrive", "Delay", "EQ"
    val enabled: Boolean = false,
    val controls: List<ControlType> = emptyList(),
    val abSlot: AbSlot = AbSlot.A,
    val stateA: Map<String, Float> = emptyMap(),  // controlId -> normalized value
    val stateB: Map<String, Float> = emptyMap(),
    val appearance: BlockAppearance = BlockAppearance(),
    val layoutMode: BlockLayout = BlockLayout.COMPACT
)

/** Amp block — mirrors ControlBlock for the Amplifier section. */
data class AmpBlock(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val controls: List<ControlType> = emptyList(),
    val appearance: BlockAppearance = BlockAppearance(),
    val layoutMode: BlockLayout = BlockLayout.GRID,
    val currentSlot: AbSlot = AbSlot.A,
    val abStates: Map<AbSlot, Map<String, Float>> = emptyMap()
)

/** Cabinet block — mirrors ControlBlock for the Cabinet section. */
data class CabBlock(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val controls: List<ControlType> = emptyList(),
    val appearance: BlockAppearance = BlockAppearance(),
    val layoutMode: BlockLayout = BlockLayout.GRID,
    val currentSlot: AbSlot = AbSlot.A,
    val abStates: Map<AbSlot, Map<String, Float>> = emptyMap()
)

/** Complete board state (all sections) */
data class BoardState(
    val pedals: List<ControlBlock> = emptyList(),
    val ampBlocks: List<AmpBlock> = emptyList(),
    val cabBlocks: List<CabBlock> = emptyList(),
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
