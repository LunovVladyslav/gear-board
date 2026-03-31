
package com.gearboard.midi

import com.gearboard.domain.model.SectionType

/**
 * Returns the first available CC number in the section's reserved range
 * that isn't already used by another mapping.
 *
 * Section ranges:
 *   PEDALS  → 1–31
 *   AMP     → 32–63
 *   CAB     → 64–79
 *   EFFECTS → 80–110
 *   null    → 1–110 (global scan)
 *
 * Falls back to rangeStart if the entire range is occupied.
 */
fun autoAssignCC(section: SectionType?, usedCCs: Set<Int>): Int {
    val rangeStart = when (section) {
        SectionType.PEDALS  -> 1
        SectionType.AMP     -> 32
        SectionType.CAB     -> 64
        SectionType.EFFECTS -> 80
        null                -> 1
    }
    val rangeEnd = when (section) {
        SectionType.PEDALS  -> 31
        SectionType.AMP     -> 63
        SectionType.CAB     -> 79
        SectionType.EFFECTS -> 110
        null                -> 110
    }
    return (rangeStart..rangeEnd).firstOrNull { it !in usedCCs } ?: rangeStart
}
