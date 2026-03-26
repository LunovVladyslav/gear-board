package com.gearboard.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for persisted control items.
 * Each row represents one control primitive within a section block.
 * Type-specific parameters are stored as JSON in [extraConfig].
 */
@Entity(tableName = "control_items")
data class ControlItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sectionType: String,     // "PEDALS", "AMP", "CAB", "EFFECTS"
    val blockId: String,         // UUID of the block within the section
    val controlType: String,     // "KNOB", "TOGGLE", "TAP", "SELECTOR", "FADER", "PRESET_NAV", "PAD"
    val label: String,
    val ccNumber: Int = 0,
    val midiChannel: Int = 1,
    val extraConfig: String = "", // JSON for type-specific params
    val sortOrder: Int = 0
)
