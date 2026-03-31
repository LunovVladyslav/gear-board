package com.gearboard.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gearboard.data.local.dao.MidiMappingDao
import com.gearboard.data.local.dao.PresetDao
import com.gearboard.data.local.entity.MidiMappingEntity
import com.gearboard.data.local.entity.PresetEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PresetDaoTest {

    private lateinit var db: GearBoardDatabase
    private lateinit var dao: PresetDao
    private lateinit var mappingDao: MidiMappingDao

    @Before fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GearBoardDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.presetDao()
        mappingDao = db.midiMappingDao()
    }

    @After fun teardown() = db.close()

    // --- Preset CRUD ---

    @Test fun `insert and retrieve preset`() = runBlocking {
        val preset = PresetEntity(bank = 1, program = 1, name = "Lead", boardStateJson = "{}")
        dao.insertPreset(preset)
        val result = dao.getAllPresets().first()
        assertThat(result).hasSize(1)
        assertThat(result.first().name).isEqualTo("Lead")
    }

    @Test fun `findByBankAndProgram returns correct preset`() = runBlocking {
        dao.insertPreset(PresetEntity(bank = 1, program = 1, name = "Lead", boardStateJson = "{}"))
        dao.insertPreset(PresetEntity(bank = 1, program = 2, name = "Rhythm", boardStateJson = "{}"))
        val result = dao.getPresetByBankProgram(bank = 1, program = 2)
        assertThat(result?.name).isEqualTo("Rhythm")
    }

    @Test fun `delete removes preset`() = runBlocking {
        val preset = PresetEntity(bank = 1, program = 1, name = "Test", boardStateJson = "{}")
        dao.insertPreset(preset)
        val inserted = dao.getAllPresets().first().first()
        dao.deletePreset(inserted)
        assertThat(dao.getAllPresets().first()).isEmpty()
    }

    @Test fun `update modifies existing preset`() = runBlocking {
        dao.insertPreset(PresetEntity(bank = 1, program = 1, name = "Old", boardStateJson = "{}"))
        val inserted = dao.getAllPresets().first().first()
        dao.updatePreset(inserted.copy(name = "New"))
        assertThat(dao.getAllPresets().first().first().name).isEqualTo("New")
    }

    @Test fun `deleteAllPresets clears table`() = runBlocking {
        dao.insertPreset(PresetEntity(bank = 1, program = 1, name = "A", boardStateJson = "{}"))
        dao.insertPreset(PresetEntity(bank = 1, program = 2, name = "B", boardStateJson = "{}"))
        dao.deleteAllPresets()
        assertThat(dao.getAllPresets().first()).isEmpty()
    }

    @Test fun `getPresetByBankProgram returns null when not found`() = runBlocking {
        val result = dao.getPresetByBankProgram(bank = 99, program = 99)
        assertThat(result).isNull()
    }

    @Test fun `presets are sorted by bank then program`() = runBlocking {
        dao.insertPreset(PresetEntity(bank = 2, program = 1, name = "B2P1", boardStateJson = "{}"))
        dao.insertPreset(PresetEntity(bank = 1, program = 2, name = "B1P2", boardStateJson = "{}"))
        dao.insertPreset(PresetEntity(bank = 1, program = 1, name = "B1P1", boardStateJson = "{}"))
        val result = dao.getAllPresets().first()
        assertThat(result[0].name).isEqualTo("B1P1")
        assertThat(result[1].name).isEqualTo("B1P2")
        assertThat(result[2].name).isEqualTo("B2P1")
    }

    // --- MidiMappingDao ---

    @Test fun `MidiMappingDao stores and retrieves CC mapping`() = runBlocking {
        val mapping = MidiMappingEntity(
            controlId = "pedal.0.drive",
            controlName = "Drive",
            ccNumber = 5,
            channel = 1
        )
        mappingDao.insertMapping(mapping)
        val result = mappingDao.getMappingByControlId("pedal.0.drive")
        assertThat(result?.ccNumber).isEqualTo(5)
        assertThat(result?.controlName).isEqualTo("Drive")
    }

    @Test fun `MidiMappingDao getMappingByCcNumber finds by CC`() = runBlocking {
        mappingDao.insertMapping(
            MidiMappingEntity(controlId = "ctrl1", controlName = "Drive", ccNumber = 7, channel = 0)
        )
        val result = mappingDao.getMappingByCcNumber(7)
        assertThat(result?.controlId).isEqualTo("ctrl1")
    }

    @Test fun `MidiMappingDao deleteByControlId removes only that mapping`() = runBlocking {
        mappingDao.insertMapping(
            MidiMappingEntity(controlId = "ctrl1", controlName = "Drive", ccNumber = 1, channel = 0)
        )
        mappingDao.insertMapping(
            MidiMappingEntity(controlId = "ctrl2", controlName = "Tone", ccNumber = 2, channel = 0)
        )
        mappingDao.deleteMappingByControlId("ctrl1")
        assertThat(mappingDao.getMappingByControlId("ctrl1")).isNull()
        assertThat(mappingDao.getMappingByControlId("ctrl2")).isNotNull()
    }

    @Test fun `MidiMappingDao deleteAll clears all mappings`() = runBlocking {
        mappingDao.insertMapping(
            MidiMappingEntity(controlId = "ctrl1", controlName = "Drive", ccNumber = 1, channel = 0)
        )
        mappingDao.deleteAllMappings()
        assertThat(mappingDao.getMappingByControlId("ctrl1")).isNull()
    }
}
