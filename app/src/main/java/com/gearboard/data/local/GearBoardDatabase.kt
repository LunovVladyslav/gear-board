package com.gearboard.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gearboard.data.local.converter.Converters
import com.gearboard.data.local.dao.MidiMappingDao
import com.gearboard.data.local.dao.PresetDao
import com.gearboard.data.local.entity.MidiMappingEntity
import com.gearboard.data.local.entity.PresetEntity

@Database(
    entities = [
        PresetEntity::class,
        MidiMappingEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GearBoardDatabase : RoomDatabase() {
    abstract fun presetDao(): PresetDao
    abstract fun midiMappingDao(): MidiMappingDao
}
