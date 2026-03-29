package com.gearboard.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gearboard.data.local.converter.Converters
import com.gearboard.data.local.dao.ControlItemDao
import com.gearboard.data.local.dao.LiveModeDao
import com.gearboard.data.local.dao.MidiMappingDao
import com.gearboard.data.local.dao.PresetDao
import com.gearboard.data.local.entity.BarEventEntity
import com.gearboard.data.local.entity.ControlItemEntity
import com.gearboard.data.local.entity.LiveSessionEntity
import com.gearboard.data.local.entity.MidiMappingEntity
import com.gearboard.data.local.entity.PresetEntity

@Database(
    entities = [
        PresetEntity::class,
        MidiMappingEntity::class,
        ControlItemEntity::class,
        LiveSessionEntity::class,
        BarEventEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GearBoardDatabase : RoomDatabase() {
    abstract fun presetDao(): PresetDao
    abstract fun midiMappingDao(): MidiMappingDao
    abstract fun controlItemDao(): ControlItemDao
    abstract fun liveModeDao(): LiveModeDao
}
