package com.gearboard.data.repository

import com.gearboard.data.local.dao.ControlItemDao
import com.gearboard.data.local.entity.ControlItemEntity
import com.gearboard.domain.model.ControlSize
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.DisplayFormat
import com.gearboard.domain.model.FaderOrientation
import com.gearboard.domain.model.SectionType
import com.google.common.truth.Truth.assertThat
import com.google.gson.GsonBuilder
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ControlRepositoryTest {

    private lateinit var dao: ControlItemDao
    private lateinit var repo: ControlRepository

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repo = ControlRepository(dao, GsonBuilder().create())
    }

    // --- toEntity ---

    @Test
    fun `toEntity maps Knob fields correctly`() {
        val knob = ControlType.Knob(
            id = "knob-1",
            label = "Drive",
            ccNumber = 5,
            midiChannel = 2,
            size = ControlSize.LARGE,
            value = 0.75f,
            displayFormat = DisplayFormat.ZERO_TO_TEN
        )
        val entity = repo.toEntity(knob, SectionType.PEDALS, "block-1", 0, blockName = "Overdrive")

        assertThat(entity.stableId).isEqualTo("knob-1")
        assertThat(entity.label).isEqualTo("Drive")
        assertThat(entity.ccNumber).isEqualTo(5)
        assertThat(entity.midiChannel).isEqualTo(2)
        assertThat(entity.sectionType).isEqualTo("PEDALS")
        assertThat(entity.blockId).isEqualTo("block-1")
        assertThat(entity.blockName).isEqualTo("Overdrive")
        assertThat(entity.controlType).isEqualTo("KNOB")
        assertThat(entity.sortOrder).isEqualTo(0)
    }

    @Test
    fun `toEntity maps Toggle fields correctly`() {
        val toggle = ControlType.Toggle(
            id = "toggle-1",
            label = "Bypass",
            ccNumber = 10,
            momentaryMode = true,
            isOn = true
        )
        val entity = repo.toEntity(toggle, SectionType.EFFECTS, "block-2", 1, blockName = "Delay")

        assertThat(entity.controlType).isEqualTo("TOGGLE")
        assertThat(entity.sectionType).isEqualTo("EFFECTS")
        assertThat(entity.blockName).isEqualTo("Delay")
        assertThat(entity.ccNumber).isEqualTo(10)
        assertThat(entity.sortOrder).isEqualTo(1)
    }

    @Test
    fun `toEntity maps Fader fields correctly`() {
        val fader = ControlType.Fader(
            id = "fader-1",
            label = "Volume",
            ccNumber = 7,
            midiChannel = 1,
            orientation = FaderOrientation.HORIZONTAL
        )
        val entity = repo.toEntity(fader, SectionType.AMP, "amp-block", 2)

        assertThat(entity.controlType).isEqualTo("FADER")
        assertThat(entity.sectionType).isEqualTo("AMP")
        assertThat(entity.blockName).isEqualTo("") // default
    }

    @Test
    fun `toEntity preserves blockName default as empty string`() {
        val knob = ControlType.Knob(label = "Gain", ccNumber = 1)
        val entity = repo.toEntity(knob, SectionType.PEDALS, "blk", 0)
        assertThat(entity.blockName).isEqualTo("")
    }

    @Test
    fun `toEntity Pad always has ccNumber 0`() {
        val pad = ControlType.Pad(label = "Kick", noteNumber = 36)
        val entity = repo.toEntity(pad, SectionType.PEDALS, "blk", 0)
        assertThat(entity.ccNumber).isEqualTo(0)
        assertThat(entity.controlType).isEqualTo("PAD")
    }

    // --- toDomain ---

    @Test
    fun `toDomain KNOB restores fields from entity`() {
        val knob = ControlType.Knob(label = "Drive", ccNumber = 5, midiChannel = 3, value = 0.8f)
        val entity = repo.toEntity(knob, SectionType.PEDALS, "blk", 0)
        val restored = repo.toDomain(entity) as ControlType.Knob

        assertThat(restored.label).isEqualTo("Drive")
        assertThat(restored.ccNumber).isEqualTo(5)
        assertThat(restored.midiChannel).isEqualTo(3)
        assertThat(restored.value).isWithin(0.001f).of(0.8f)
    }

    @Test
    fun `toDomain TOGGLE restores momentaryMode and isOn`() {
        val toggle = ControlType.Toggle(label = "FX", ccNumber = 22, momentaryMode = true, isOn = true)
        val entity = repo.toEntity(toggle, SectionType.EFFECTS, "blk", 0)
        val restored = repo.toDomain(entity) as ControlType.Toggle

        assertThat(restored.momentaryMode).isTrue()
        assertThat(restored.isOn).isTrue()
    }

    @Test
    fun `toDomain SELECTOR restores positions and selectedIndex`() {
        val selector = ControlType.Selector(
            label = "Mode",
            ccNumber = 44,
            positions = listOf("Clean", "Crunch", "Lead"),
            selectedIndex = 2
        )
        val entity = repo.toEntity(selector, SectionType.AMP, "blk", 0)
        val restored = repo.toDomain(entity) as ControlType.Selector

        assertThat(restored.positions).containsExactly("Clean", "Crunch", "Lead")
        assertThat(restored.selectedIndex).isEqualTo(2)
    }

    @Test
    fun `toDomain FADER restores orientation and displayFormat`() {
        val fader = ControlType.Fader(
            label = "Vol",
            ccNumber = 7,
            orientation = FaderOrientation.HORIZONTAL,
            displayFormat = DisplayFormat.DECIBELS_OUTPUT
        )
        val entity = repo.toEntity(fader, SectionType.AMP, "blk", 0)
        val restored = repo.toDomain(entity) as ControlType.Fader

        assertThat(restored.orientation).isEqualTo(FaderOrientation.HORIZONTAL)
        assertThat(restored.displayFormat).isEqualTo(DisplayFormat.DECIBELS_OUTPUT)
    }

    @Test
    fun `toDomain unknown type returns default Knob`() {
        val entity = ControlItemEntity(
            stableId = "x",
            sectionType = "PEDALS",
            blockId = "blk",
            blockName = "",
            controlType = "UNKNOWN_TYPE",
            label = "Test",
            ccNumber = 1,
            midiChannel = 1
        )
        val result = repo.toDomain(entity)
        assertThat(result).isInstanceOf(ControlType.Knob::class.java)
    }

    @Test
    fun `toDomain PAD restores noteNumber and velocity`() {
        val pad = ControlType.Pad(label = "Snare", noteNumber = 38, velocity = 80)
        val entity = repo.toEntity(pad, SectionType.PEDALS, "blk", 0)
        val restored = repo.toDomain(entity) as ControlType.Pad

        assertThat(restored.noteNumber).isEqualTo(38)
        assertThat(restored.velocity).isEqualTo(80)
    }

    // --- upsertAll / deleteOrphans delegation ---

    @Test
    fun `upsertAll delegates to dao`() = runTest {
        val items = listOf<ControlItemEntity>()
        coEvery { dao.upsertAll(items) } returns Unit
        repo.upsertAll(items)
        coVerify { dao.upsertAll(items) }
    }

    @Test
    fun `deleteOrphans delegates to dao`() = runTest {
        val ids = listOf("id1", "id2")
        coEvery { dao.deleteOrphans(ids) } returns Unit
        repo.deleteOrphans(ids)
        coVerify { dao.deleteOrphans(ids) }
    }

    // --- Round-trip (toEntity -> toDomain) ---

    @Test
    fun `round-trip Knob preserves stableId`() {
        val knob = ControlType.Knob(id = "stable-uuid", label = "Gain", ccNumber = 8)
        val entity = repo.toEntity(knob, SectionType.PEDALS, "blk", 0)
        val restored = repo.toDomain(entity)
        assertThat(restored.id).isEqualTo("stable-uuid")
    }

    @Test
    fun `round-trip Fader preserves value`() {
        val fader = ControlType.Fader(label = "Mix", ccNumber = 15, value = 0.33f)
        val entity = repo.toEntity(fader, SectionType.EFFECTS, "blk", 0)
        val restored = repo.toDomain(entity) as ControlType.Fader
        assertThat(restored.value).isWithin(0.001f).of(0.33f)
    }
}
