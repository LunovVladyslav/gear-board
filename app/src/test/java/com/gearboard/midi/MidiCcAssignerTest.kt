package com.gearboard.midi

import com.gearboard.domain.model.SectionType
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Unit tests for [autoAssignCC].
 * Verifies section-based CC range assignment and duplicate avoidance.
 */
class MidiCcAssignerTest {

    @Test fun `returns 1 for PEDALS when no CCs used`() {
        assertThat(autoAssignCC(SectionType.PEDALS, emptySet())).isEqualTo(1)
    }

    @Test fun `returns 32 for AMP when no CCs used`() {
        assertThat(autoAssignCC(SectionType.AMP, emptySet())).isEqualTo(32)
    }

    @Test fun `returns 64 for CAB when no CCs used`() {
        assertThat(autoAssignCC(SectionType.CAB, emptySet())).isEqualTo(64)
    }

    @Test fun `returns 80 for EFFECTS when no CCs used`() {
        assertThat(autoAssignCC(SectionType.EFFECTS, emptySet())).isEqualTo(80)
    }

    @Test fun `returns 1 for null section when no CCs used`() {
        assertThat(autoAssignCC(null, emptySet())).isEqualTo(1)
    }

    @Test fun `skips used CC numbers`() {
        val used = setOf(1, 2)
        assertThat(autoAssignCC(SectionType.PEDALS, used)).isEqualTo(3)
    }

    @Test fun `skips multiple used CCs`() {
        val used = setOf(1, 2, 3, 4, 5)
        assertThat(autoAssignCC(SectionType.PEDALS, used)).isEqualTo(6)
    }

    @Test fun `assigned CC is always in section range for PEDALS`() {
        repeat(31) { i ->
            val used = (1..i).toSet()
            val cc = autoAssignCC(SectionType.PEDALS, used)
            assertThat(cc).isIn(1..31)
        }
    }

    @Test fun `assigned CC is always in section range for AMP`() {
        repeat(32) { i ->
            val used = (32 until 32 + i).toSet()
            val cc = autoAssignCC(SectionType.AMP, used)
            assertThat(cc).isIn(32..63)
        }
    }

    @Test fun `assigned CC is always in section range for CAB`() {
        repeat(16) { i ->
            val used = (64 until 64 + i).toSet()
            val cc = autoAssignCC(SectionType.CAB, used)
            assertThat(cc).isIn(64..79)
        }
    }

    @Test fun `assigned CC is always in section range for EFFECTS`() {
        repeat(31) { i ->
            val used = (80 until 80 + i).toSet()
            val cc = autoAssignCC(SectionType.EFFECTS, used)
            assertThat(cc).isIn(80..110)
        }
    }

    @Test fun `never returns a duplicate CC`() {
        val assigned = mutableSetOf<Int>()
        repeat(31) {
            val cc = autoAssignCC(SectionType.PEDALS, assigned)
            assertThat(assigned).doesNotContain(cc)
            assigned.add(cc)
        }
    }

    @Test fun `falls back to rangeStart when all CCs in range are used`() {
        val allPedalCCs = (1..31).toSet()
        // When full, falls back to rangeStart (1)
        assertThat(autoAssignCC(SectionType.PEDALS, allPedalCCs)).isEqualTo(1)
    }

    @Test fun `CCs used in other sections do not block PEDALS range`() {
        // Only AMP and EFFECTS CCs are used — PEDALS range should still start at 1
        val usedElsewhere = (32..110).toSet()
        assertThat(autoAssignCC(SectionType.PEDALS, usedElsewhere)).isEqualTo(1)
    }
}
