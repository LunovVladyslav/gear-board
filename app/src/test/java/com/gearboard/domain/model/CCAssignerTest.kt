package com.gearboard.domain.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CCAssignerTest {

    private fun assertInRange(cc: Int, min: Int, max: Int) {
        assertThat(cc).isAtLeast(min)
        assertThat(cc).isAtMost(max)
    }

    @Test
    fun `assignBlock pedals produces CCs only in 1 to 31`() {
        val controls = List(5) { ControlType.Knob(label = "Knob $it", ccNumber = 0) }
        val result = CCAssigner.assign(controls, "pedals", mutableSetOf())
        result.forEach { assertInRange(it.ccNumber, 1, 31) }
    }

    @Test
    fun `assignBlock amp produces CCs only in 32 to 63`() {
        val controls = List(5) { ControlType.Knob(label = "Knob $it", ccNumber = 0) }
        val result = CCAssigner.assign(controls, "amp", mutableSetOf())
        result.forEach { assertInRange(it.ccNumber, 32, 63) }
    }

    @Test
    fun `assignBlock cab produces CCs only in 64 to 79`() {
        val controls = List(5) { ControlType.Knob(label = "Knob $it", ccNumber = 0) }
        val result = CCAssigner.assign(controls, "cab", mutableSetOf())
        result.forEach { assertInRange(it.ccNumber, 64, 79) }
    }

    @Test
    fun `assignBlock effects produces CCs only in 80 to 110`() {
        val controls = List(5) { ControlType.Knob(label = "Knob $it", ccNumber = 0) }
        val result = CCAssigner.assign(controls, "effects", mutableSetOf())
        result.forEach { assertInRange(it.ccNumber, 80, 110) }
    }

    @Test
    fun `no duplicate CCs when multiple blocks share usedCCs`() {
        val usedCCs = mutableSetOf<Int>()
        val block1 = List(10) { ControlType.Knob(label = "Knob $it", ccNumber = 0) }
        val result1 = CCAssigner.assign(block1, "pedals", usedCCs)
        val block2 = List(5) { ControlType.Knob(label = "Knob2 $it", ccNumber = 0) }
        val result2 = CCAssigner.assign(block2, "pedals", usedCCs)
        val allCCs = (result1 + result2).map { it.ccNumber }
        assertThat(allCCs.size).isEqualTo(allCCs.distinct().size)
    }

    @Test
    fun `pre-assigned controls are left unchanged`() {
        val controls = listOf(
            ControlType.Knob(label = "Pre", ccNumber = 15),
            ControlType.Knob(label = "Unassigned", ccNumber = 0)
        )
        val result = CCAssigner.assign(controls, "pedals", mutableSetOf())
        assertThat(result[0].ccNumber).isEqualTo(15)
        assertThat(result[1].ccNumber).isNotEqualTo(0)
    }

    @Test
    fun `PresetNav controls are skipped - CC stays 0`() {
        val controls = listOf(ControlType.PresetNav(label = "Nav", ccNumber = 0))
        val result = CCAssigner.assign(controls, "pedals", mutableSetOf())
        // PresetNav is returned as-is (not assigned) per CCAssigner.assign logic
        assertThat(result[0].ccNumber).isEqualTo(0)
    }

    @Test
    fun `assignAmp uses amp range`() {
        val block = AmpBlock(name = "Amp", controls = List(3) { ControlType.Knob(label = "K$it", ccNumber = 0) })
        val result = CCAssigner.assignAmp(block, mutableSetOf())
        result.controls.forEach { assertInRange(it.ccNumber, 32, 63) }
    }

    @Test
    fun `assignCab uses cab range`() {
        val block = CabBlock(name = "Cab", controls = List(3) { ControlType.Knob(label = "K$it", ccNumber = 0) })
        val result = CCAssigner.assignCab(block, mutableSetOf())
        result.controls.forEach { assertInRange(it.ccNumber, 64, 79) }
    }

    @Test
    fun `usedCCs is updated in place after assignment`() {
        val usedCCs = mutableSetOf<Int>()
        val controls = List(3) { ControlType.Knob(label = "K$it", ccNumber = 0) }
        CCAssigner.assign(controls, "pedals", usedCCs)
        assertThat(usedCCs).isNotEmpty()
        assertThat(usedCCs.all { it in 1..31 }).isTrue()
    }

    @Test
    fun `sections do not interfere - all assigned CCs are unique`() {
        val usedCCs = mutableSetOf<Int>()
        val pedals = List(5) { ControlType.Knob(label = "P$it", ccNumber = 0) }
        val amp = List(5) { ControlType.Knob(label = "A$it", ccNumber = 0) }
        val cab = List(3) { ControlType.Knob(label = "C$it", ccNumber = 0) }
        val effects = List(5) { ControlType.Knob(label = "E$it", ccNumber = 0) }

        val rPedals = CCAssigner.assign(pedals, "pedals", usedCCs)
        val rAmp = CCAssigner.assign(amp, "amp", usedCCs)
        val rCab = CCAssigner.assign(cab, "cab", usedCCs)
        val rEffects = CCAssigner.assign(effects, "effects", usedCCs)

        val allCCs = (rPedals + rAmp + rCab + rEffects).map { it.ccNumber }.filter { it > 0 }
        assertThat(allCCs.size).isEqualTo(allCCs.distinct().size)
    }

    @Test
    fun `assign with unknown section falls back to pedals range`() {
        val controls = listOf(ControlType.Knob(label = "K", ccNumber = 0))
        val result = CCAssigner.assign(controls, "unknown_section", mutableSetOf())
        // Falls back to 1..31 (pedals range is default for unknown section)
        assertInRange(result.first().ccNumber, 1, 31)
    }

    @Test
    fun `assignBlock returns same size list`() {
        val controls = List(7) { ControlType.Knob(label = "K$it", ccNumber = 0) }
        val result = CCAssigner.assign(controls, "pedals", mutableSetOf())
        assertThat(result).hasSize(7)
    }

    @Test
    fun `Toggle and Tap and Selector are also assigned CCs`() {
        val controls = listOf(
            ControlType.Toggle(label = "Bypass", ccNumber = 0),
            ControlType.Tap(label = "Tempo", ccNumber = 0),
            ControlType.Selector(label = "Mode", ccNumber = 0, positions = listOf("A", "B"))
        )
        val result = CCAssigner.assign(controls, "pedals", mutableSetOf())
        result.forEach { assertThat(it.ccNumber).isGreaterThan(0) }
    }
}
