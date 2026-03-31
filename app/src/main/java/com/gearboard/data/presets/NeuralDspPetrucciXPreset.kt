package com.gearboard.data.presets

import com.gearboard.domain.model.BoardPreset
import com.gearboard.domain.model.BoardSection
import com.gearboard.domain.model.ControlBlock
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.DisplayFormat
import com.gearboard.domain.model.FaderOrientation
import com.gearboard.domain.model.PresetNavMode
import com.gearboard.domain.model.SectionType

object NeuralDspPetrucciXPreset {

    val preset = BoardPreset(
        id = "neural_dsp_petrucci_x",
        name = "Neural DSP Petrucci X",
        targetSoftware = "Neural DSP Archetype Petrucci X",
        description = "Full MIDI layout for Neural DSP Archetype Petrucci X. Uses CC Absolute for amp knobs, CC Toggle for bypass, CC_INC_DEC for preset navigation.",
        defaultMidiChannel = 1,
        setupInstructions = """
After loading this preset in GearBoard:
1. Open Neural DSP Archetype Petrucci X
2. Go to MIDI → MIDI Mappings
3. For each control below, add a mapping:
   - Type: CC Absolute (for knobs/faders), CC Toggle (for on/off buttons), CC Preset Dec/Inc (for preset navigation)
   - Channel: 1
   - CC Number: must match the CC number shown in GearBoard's MIDI MAP screen

CHANNEL SELECTOR (CC 90) — amp type with 3 positions:
  GearBoard sends: Clean=0, Rhythm=63, Lead=127
  Neural DSP maps the incoming CC value proportionally to the 3-channel range.

PRESET NAVIGATION (CC 80):
  Value 0 = Previous preset, Value 127 = Next preset
  Use type: CC Preset Dec/Inc
""".trimIndent(),
        sections = listOf(
            BoardSection(
                type = SectionType.PEDALS,
                blocks = listOf(
                    ControlBlock(
                        name = "PRE FX",
                        type = "Effects",
                        controls = listOf(
                            ControlType.Toggle(label = "OVERDRIVE", ccNumber = 85),
                            ControlType.Toggle(label = "PHASER", ccNumber = 86),
                            ControlType.Toggle(label = "CHORUS", ccNumber = 87),
                            ControlType.Toggle(label = "FLANGER", ccNumber = 88)
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
                            ControlType.Selector(
                                label = "CHANNEL",
                                ccNumber = 90,
                                positions = listOf("Clean", "Rhythm", "Lead"),
                                ccValues = listOf(0, 63, 127)
                            ),
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
                                positions = listOf("SM57", "MD421", "R121", "U87"),
                                ccValues = listOf(0, 42, 85, 127)
                            ),
                            ControlType.Fader(
                                label = "MIC POS",
                                ccNumber = 53,
                                orientation = FaderOrientation.HORIZONTAL,
                                displayFormat = DisplayFormat.PERCENTAGE
                            ),
                            ControlType.Knob(
                                label = "DISTANCE",
                                ccNumber = 54,
                                displayFormat = DisplayFormat.ZERO_TO_TEN
                            ),
                            ControlType.Knob(
                                label = "LEVEL",
                                ccNumber = 55,
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
                        name = "DELAY",
                        type = "Delay",
                        controls = listOf(
                            ControlType.Toggle(label = "ACTIVE", ccNumber = 106),
                            ControlType.Knob(label = "MIX", ccNumber = 107, displayFormat = DisplayFormat.PERCENTAGE),
                            ControlType.Knob(label = "TIME", ccNumber = 108, displayFormat = DisplayFormat.MILLISECONDS),
                            ControlType.Knob(label = "FEEDBACK", ccNumber = 109, displayFormat = DisplayFormat.PERCENTAGE)
                        )
                    ),
                    ControlBlock(
                        name = "REVERB",
                        type = "Reverb",
                        controls = listOf(
                            ControlType.Toggle(label = "ACTIVE", ccNumber = 110),
                            ControlType.Knob(label = "MIX", ccNumber = 111, displayFormat = DisplayFormat.PERCENTAGE),
                            ControlType.Knob(label = "DECAY", ccNumber = 112, displayFormat = DisplayFormat.ZERO_TO_TEN)
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
