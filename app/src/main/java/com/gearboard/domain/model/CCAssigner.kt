package com.gearboard.domain.model

/**
 * Assigns unique sequential CC numbers to controls in a block.
 * Templates use ccNumber=0 as placeholder — call this before adding to board.
 */
object CCAssigner {

    // Section CC ranges
    private val RANGES = mapOf(
        "pedals"  to 1..31,
        "amp"     to 32..63,
        "cab"     to 64..79,
        "effects" to 80..110
    )

    /**
     * Assigns unique CC numbers to all controls with ccNumber == 0.
     * Already-assigned controls (ccNumber > 0) are left unchanged.
     *
     * @param controls list of controls to assign
     * @param section  "pedals" | "amp" | "cab" | "effects"
     * @param usedCCs  set of CC numbers already in use across the whole board — updated in place
     * @return new list with CC numbers assigned
     */
    fun assign(
        controls: List<ControlType>,
        section: String,
        usedCCs: MutableSet<Int>
    ): List<ControlType> {
        val range = RANGES[section] ?: 1..31
        return controls.map { control ->
            if (control.ccNumber > 0) {
                usedCCs.add(control.ccNumber)
                control
            } else {
                val cc = range.firstOrNull { it !in usedCCs } ?: range.first
                usedCCs.add(cc)
                when (control) {
                    is ControlType.Knob      -> control.copy(ccNumber = cc)
                    is ControlType.Toggle    -> control.copy(ccNumber = cc)
                    is ControlType.Tap       -> control.copy(ccNumber = cc)
                    is ControlType.Selector  -> control.copy(ccNumber = cc)
                    is ControlType.Fader     -> control.copy(ccNumber = cc)
                    is ControlType.Pad       -> control.copy(ccNumber = cc)
                    is ControlType.PresetNav -> control  // PC-based, no CC needed
                }
            }
        }
    }

    /** Assign CC numbers to an AmpBlock. */
    fun assignAmp(block: AmpBlock, usedCCs: MutableSet<Int>): AmpBlock =
        block.copy(controls = assign(block.controls, "amp", usedCCs))

    /** Assign CC numbers to a CabBlock. */
    fun assignCab(block: CabBlock, usedCCs: MutableSet<Int>): CabBlock =
        block.copy(controls = assign(block.controls, "cab", usedCCs))

    /** Assign CC numbers to a ControlBlock (pedal or effect). */
    fun assignBlock(
        block: ControlBlock,
        section: String,
        usedCCs: MutableSet<Int>
    ): ControlBlock =
        block.copy(controls = assign(block.controls, section, usedCCs))
}
