package com.gearboard.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for persisted presets.
 * BoardState is serialized to JSON string via TypeConverter.
 */
@Entity(tableName = "presets")
data class PresetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val bank: Int = 0,
    val program: Int = 0,
    val boardStateJson: String,  // Serialized BoardState as JSON
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

/**
 * Room entity for MIDI CC mappings.
 */
@Entity(tableName = "midi_mappings")
data class MidiMappingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val controlId: String,
    val controlName: String,
    val ccNumber: Int,
    val channel: Int = 0
)
