package com.gearboard.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity for persisted control items.
 * Each row represents one control primitive within a section block.
 * Type-specific parameters are stored as JSON in [extraConfig].
 */
@Entity(
    tableName = "control_items",
    indices = [Index(value = ["stableId"], unique = true)]
)
data class ControlItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    /** Stable UUID carried from ControlType.id — survives delete-and-reinsert save cycles. */
    val stableId: String = java.util.UUID.randomUUID().toString(),
    val sectionType: String,     // "PEDALS", "AMP", "CAB", "EFFECTS"
    val blockId: String,         // UUID of the block within the section
    val blockName: String = "",  // Human-readable block name (persisted for restore)
    val controlType: String,     // "KNOB", "TOGGLE", "TAP", "SELECTOR", "FADER", "PRESET_NAV", "PAD"
    val label: String,
    val ccNumber: Int = 0,
    val midiChannel: Int = 1,
    val extraConfig: String = "", // JSON for type-specific params
    val sortOrder: Int = 0
)
