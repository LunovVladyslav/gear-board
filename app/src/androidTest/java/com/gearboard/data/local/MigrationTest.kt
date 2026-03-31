package com.gearboard.data.local

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    private val TEST_DB = "migration_test_db"

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        GearBoardDatabase::class.java
    )

    /**
     * Verifies that MIGRATION_4_5 correctly adds the blockName column to control_items
     * and creates the unique index on stableId, without losing existing data.
     */
    @Test
    fun migrate4To5_addsBlockNameColumn() {
        // Create version 4 database schema and insert a row
        helper.createDatabase(TEST_DB, 4).use { db ->
            db.execSQL(
                """INSERT INTO control_items
                   (stableId, sectionType, blockId, controlType, label, ccNumber, midiChannel, extraConfig, sortOrder)
                   VALUES ('stable-uuid-1', 'PEDALS', 'block-1', 'KNOB', 'Drive', 5, 1, '{}', 0)"""
            )
        }

        // Run migration 4 -> 5
        helper.runMigrationsAndValidate(
            TEST_DB,
            5,
            true,
            GearBoardDatabase.MIGRATION_4_5
        ).use { db ->
            // Verify the blockName column exists and existing row has default value ''
            val cursor = db.query("SELECT blockName FROM control_items WHERE stableId = 'stable-uuid-1'")
            assertThat(cursor.moveToFirst()).isTrue()
            val blockName = cursor.getString(cursor.getColumnIndexOrThrow("blockName"))
            assertThat(blockName).isEqualTo("")
            cursor.close()
        }
    }

    @Test
    fun migrate4To5_uniqueIndexOnStableId() {
        // Create version 4 schema
        helper.createDatabase(TEST_DB + "_idx", 4).use { db ->
            db.execSQL(
                """INSERT INTO control_items
                   (stableId, sectionType, blockId, controlType, label, ccNumber, midiChannel, extraConfig, sortOrder)
                   VALUES ('stable-1', 'PEDALS', 'block-1', 'KNOB', 'Drive', 5, 1, '{}', 0)"""
            )
        }

        // Run migration
        val db = helper.runMigrationsAndValidate(
            TEST_DB + "_idx",
            5,
            true,
            GearBoardDatabase.MIGRATION_4_5
        )

        // Confirm the unique index exists by querying sqlite_master
        val cursor = db.query(
            "SELECT name FROM sqlite_master WHERE type='index' AND name='index_control_items_stableId'"
        )
        assertThat(cursor.moveToFirst()).isTrue()
        assertThat(cursor.getString(0)).isEqualTo("index_control_items_stableId")
        cursor.close()
        db.close()
    }

    @Test
    fun migrate4To5_preservesExistingRows() {
        val dbName = TEST_DB + "_preserve"
        helper.createDatabase(dbName, 4).use { db ->
            db.execSQL(
                """INSERT INTO control_items
                   (stableId, sectionType, blockId, controlType, label, ccNumber, midiChannel, extraConfig, sortOrder)
                   VALUES ('uuid-a', 'AMP', 'amp-1', 'KNOB', 'Gain', 32, 1, '{}', 0)"""
            )
            db.execSQL(
                """INSERT INTO control_items
                   (stableId, sectionType, blockId, controlType, label, ccNumber, midiChannel, extraConfig, sortOrder)
                   VALUES ('uuid-b', 'CAB', 'cab-1', 'FADER', 'Mix', 64, 1, '{}', 1)"""
            )
        }

        helper.runMigrationsAndValidate(dbName, 5, true, GearBoardDatabase.MIGRATION_4_5).use { db ->
            val cursor = db.query("SELECT COUNT(*) FROM control_items")
            cursor.moveToFirst()
            val count = cursor.getInt(0)
            assertThat(count).isEqualTo(2)
            cursor.close()

            // Check the second row has default blockName
            val cur2 = db.query("SELECT label, blockName FROM control_items WHERE stableId = 'uuid-b'")
            cur2.moveToFirst()
            assertThat(cur2.getString(cur2.getColumnIndexOrThrow("label"))).isEqualTo("Mix")
            assertThat(cur2.getString(cur2.getColumnIndexOrThrow("blockName"))).isEqualTo("")
            cur2.close()
        }
    }

    @Test
    fun fullMigrationFromVersion4_databaseOpensWithDao() {
        val dbName = TEST_DB + "_full"
        helper.createDatabase(dbName, 4).use { db ->
            db.execSQL(
                """INSERT INTO control_items
                   (stableId, sectionType, blockId, controlType, label, ccNumber, midiChannel, extraConfig, sortOrder)
                   VALUES ('uuid-full', 'EFFECTS', 'fx-1', 'TOGGLE', 'Chorus', 80, 1, '{}', 0)"""
            )
        }

        // Open with full Room database (validates schema matches entities)
        val room = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GearBoardDatabase::class.java,
            dbName
        )
            .addMigrations(GearBoardDatabase.MIGRATION_4_5)
            .build()

        try {
            val items = kotlinx.coroutines.runBlocking {
                room.controlItemDao().getAllControlsOnce()
            }
            assertThat(items).hasSize(1)
            assertThat(items.first().label).isEqualTo("Chorus")
            assertThat(items.first().blockName).isEqualTo("")
        } finally {
            room.close()
        }
    }
}
