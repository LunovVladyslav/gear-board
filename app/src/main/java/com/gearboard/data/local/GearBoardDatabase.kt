package com.gearboard.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GearBoardDatabase : RoomDatabase() {
    abstract fun presetDao(): PresetDao
    abstract fun midiMappingDao(): MidiMappingDao
    abstract fun controlItemDao(): ControlItemDao
    abstract fun liveModeDao(): LiveModeDao

    companion object {
        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE control_items ADD COLUMN blockName TEXT NOT NULL DEFAULT ''"
                )
                // Assign new random IDs to rows with empty stableId (SQLite default for TEXT)
                database.execSQL(
                    "UPDATE control_items SET stableId = lower(hex(randomblob(16))) WHERE stableId = '' OR stableId IS NULL"
                )
                // Reassign duplicate stableIds: keep the lowest-id row, update all others
                database.execSQL(
                    "UPDATE control_items SET stableId = lower(hex(randomblob(16))) WHERE id NOT IN (SELECT MIN(id) FROM control_items GROUP BY stableId)"
                )
                // Create unique index on stableId for UPSERT identity
                database.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS index_control_items_stableId ON control_items(stableId)"
                )
            }
        }
    }
}
