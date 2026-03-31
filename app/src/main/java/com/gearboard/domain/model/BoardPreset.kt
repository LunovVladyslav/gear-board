package com.gearboard.domain.model

/**
 * BoardPreset — a complete board layout shipped as a built-in preset.
 * Contains all sections, blocks, and controls with CC numbers pre-assigned.
 */
data class BoardPreset(
    val id: String,
    val name: String,
    val targetSoftware: String,
    val description: String,
    val defaultMidiChannel: Int = 1,
    val setupInstructions: String = "",
    val sections: List<BoardSection>
)

data class BoardSection(
    val type: SectionType,
    val isExpanded: Boolean = true,
    val blocks: List<ControlBlock>
)
