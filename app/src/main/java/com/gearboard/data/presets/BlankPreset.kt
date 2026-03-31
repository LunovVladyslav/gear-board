package com.gearboard.data.presets

import com.gearboard.domain.model.BoardPreset
import com.gearboard.domain.model.BoardSection
import com.gearboard.domain.model.ControlBlock
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.DisplayFormat
import com.gearboard.domain.model.PresetNavMode
import com.gearboard.domain.model.SectionType

object BlankPreset {

    val preset = BoardPreset(
        id = "blank",
        name = "Blank (Custom)",
        targetSoftware = "Any VST",
        description = "Empty preset with placeholder controls. Assign CC numbers in GearBoard's MIDI MAP screen to match your software.",
        defaultMidiChannel = 1,
        setupInstructions = """
This is a blank preset. To use it:
1. Load this preset in GearBoard
2. Open GearBoard's MIDI MAP screen (tap the MIDI icon)
3. For each control, assign a CC number matching your VST's MIDI mapping
4. Use MIDI Learn in your VST to assign the same CC numbers

All controls start with placeholder CC numbers. Edit them to match your setup.
""".trimIndent(),
        sections = listOf(
            BoardSection(
                type = SectionType.PEDALS,
                blocks = listOf(
                    ControlBlock(
                        name = "PEDAL 1",
                        type = "Custom",
                        controls = listOf(
                            ControlType.Toggle(label = "ON/OFF", ccNumber = 52),
                            ControlType.Knob(label = "PARAM 1", ccNumber = 20, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "PARAM 2", ccNumber = 21, displayFormat = DisplayFormat.ZERO_TO_TEN)
                        )
                    )
                )
            ),
            BoardSection(
                type = SectionType.AMP,
                blocks = listOf(
                    ControlBlock(
                        name = "AMP",
                        type = "Custom",
                        controls = listOf(
                            ControlType.Knob(label = "PARAM 1", ccNumber = 85, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "PARAM 2", ccNumber = 86, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "PARAM 3", ccNumber = 87, displayFormat = DisplayFormat.ZERO_TO_TEN)
                        )
                    )
                )
            ),
            BoardSection(
                type = SectionType.CAB,
                isExpanded = false,
                blocks = emptyList()
            ),
            BoardSection(
                type = SectionType.EFFECTS,
                blocks = listOf(
                    ControlBlock(
                        name = "FX 1",
                        type = "Custom",
                        controls = listOf(
                            ControlType.Toggle(label = "ACTIVE", ccNumber = 53),
                            ControlType.Knob(label = "MIX", ccNumber = 22, displayFormat = DisplayFormat.PERCENTAGE)
                        )
                    ),
                    ControlBlock(
                        name = "PRESET NAV",
                        type = "Navigation",
                        controls = listOf(
                            ControlType.PresetNav(
                                label = "PRESET",
                                ccNumber = 80,
                                navMode = PresetNavMode.CC_INC_DEC
                            )
                        )
                    )
                )
            )
        )
    )
}
