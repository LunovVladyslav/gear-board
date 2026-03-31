package com.gearboard.data.presets

import com.gearboard.domain.model.BoardPreset
import com.gearboard.domain.model.BoardSection
import com.gearboard.domain.model.ControlBlock
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.DisplayFormat
import com.gearboard.domain.model.FaderOrientation
import com.gearboard.domain.model.PresetNavMode
import com.gearboard.domain.model.SectionType

object NeuralDspGenericPreset {

    val preset = BoardPreset(
        id = "neural_dsp_generic",
        name = "Neural DSP Generic",
        targetSoftware = "Neural DSP Archetype (any)",
        description = "Generic layout for any Neural DSP Archetype. Open MIDI Mappings in the plugin and assign each CC number to the matching parameter.",
        defaultMidiChannel = 1,
        setupInstructions = """
After loading this preset in GearBoard:
1. Open your Neural DSP Archetype plugin
2. Go to MIDI → MIDI Mappings
3. For each control, add a mapping:
   - Type: CC Absolute (knobs), CC Toggle (on/off), CC Preset Dec/Inc (navigation)
   - Channel: 1
   - CC Number: match the number shown in GearBoard's MIDI MAP screen

PRESET NAVIGATION (CC 80):
  Value 0 = Previous preset, Value 127 = Next preset
  Use type: CC Preset Dec/Inc

Note: CC numbers in this preset are generic starting points. Edit them in
GearBoard's MIDI MAP screen to match your specific Archetype's parameters.
""".trimIndent(),
        sections = listOf(
            BoardSection(
                type = SectionType.PEDALS,
                blocks = listOf(
                    ControlBlock(
                        name = "PRE FX",
                        type = "Effects",
                        controls = listOf(
                            ControlType.Toggle(label = "FX 1", ccNumber = 85),
                            ControlType.Toggle(label = "FX 2", ccNumber = 86),
                            ControlType.Toggle(label = "FX 3", ccNumber = 87),
                            ControlType.Toggle(label = "FX 4", ccNumber = 88)
                        )
                    )
                )
            ),
            BoardSection(
                type = SectionType.AMP,
                blocks = listOf(
                    ControlBlock(
                        name = "AMPLIFIER",
                        type = "Amp",
                        controls = listOf(
                            ControlType.Knob(
                                label = "GAIN",
                                ccNumber = 91,
                                ccNumberA = 91,
                                ccNumberB = 100,
                                displayFormat = DisplayFormat.ZERO_TO_TEN
                            ),
                            ControlType.Knob(
                                label = "BASS",
                                ccNumber = 92,
                                ccNumberA = 92,
                                ccNumberB = 101,
                                displayFormat = DisplayFormat.ZERO_TO_TEN
                            ),
                            ControlType.Knob(
                                label = "MID",
                                ccNumber = 93,
                                ccNumberA = 93,
                                ccNumberB = 102,
                                displayFormat = DisplayFormat.ZERO_TO_TEN
                            ),
                            ControlType.Knob(
                                label = "TREBLE",
                                ccNumber = 94,
                                ccNumberA = 94,
                                ccNumberB = 103,
                                displayFormat = DisplayFormat.ZERO_TO_TEN
                            ),
                            ControlType.Knob(
                                label = "PRESENCE",
                                ccNumber = 95,
                                ccNumberA = 95,
                                ccNumberB = 104,
                                displayFormat = DisplayFormat.ZERO_TO_TEN
                            ),
                            ControlType.Knob(
                                label = "MASTER",
                                ccNumber = 96,
                                ccNumberA = 96,
                                ccNumberB = 105,
                                displayFormat = DisplayFormat.ZERO_TO_TEN
                            )
                        )
                    )
                )
            ),
            BoardSection(
                type = SectionType.CAB,
                blocks = listOf(
                    ControlBlock(
                        name = "CABINET",
                        type = "Cab",
                        controls = listOf(
                            ControlType.Selector(
                                label = "MIC TYPE",
                                ccNumber = 52,
                                positions = listOf("SM57", "MD421", "R121"),
                                ccValues = listOf(0, 63, 127)
                            ),
                            ControlType.Fader(
                                label = "MIC POS",
                                ccNumber = 53,
                                orientation = FaderOrientation.HORIZONTAL,
                                displayFormat = DisplayFormat.PERCENTAGE
                            ),
                            ControlType.Knob(
                                label = "LEVEL",
                                ccNumber = 54,
                                displayFormat = DisplayFormat.DECIBELS_OUTPUT
                            )
                        )
                    )
                )
            ),
            BoardSection(
                type = SectionType.EFFECTS,
                blocks = listOf(
                    ControlBlock(
                        name = "FX 1",
                        type = "Effect",
                        controls = listOf(
                            ControlType.Toggle(label = "ACTIVE", ccNumber = 106),
                            ControlType.Knob(label = "MIX", ccNumber = 107, displayFormat = DisplayFormat.PERCENTAGE)
                        )
                    ),
                    ControlBlock(
                        name = "FX 2",
                        type = "Effect",
                        controls = listOf(
                            ControlType.Toggle(label = "ACTIVE", ccNumber = 108),
                            ControlType.Knob(label = "MIX", ccNumber = 109, displayFormat = DisplayFormat.PERCENTAGE)
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
