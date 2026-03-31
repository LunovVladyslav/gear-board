package com.gearboard.data.presets

import com.gearboard.domain.model.BoardPreset
import com.gearboard.domain.model.BoardSection
import com.gearboard.domain.model.ControlBlock
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.DisplayFormat
import com.gearboard.domain.model.PresetNavMode
import com.gearboard.domain.model.SectionType

object AmpliTube5Preset {

    val preset = BoardPreset(
        id = "amplitube_5",
        name = "AmpliTube 5",
        targetSoftware = "IK Multimedia AmpliTube 5",
        description = "Layout for IK Multimedia AmpliTube 5. Uses MIDI Learn for all CC assignments. PC for preset navigation.",
        defaultMidiChannel = 1,
        setupInstructions = """
After loading this preset in GearBoard:
1. Open IK Multimedia AmpliTube 5
2. Right-click each parameter you want to control
3. Select "MIDI Learn"
4. Move the corresponding control in GearBoard
5. AmpliTube will automatically detect and assign the CC number

For preset navigation, this preset uses Program Change (PC) messages.
Make sure AmpliTube 5 is set to respond to PC messages on MIDI Channel 1.
Go to: Edit → Preferences → MIDI → Enable Program Change
""".trimIndent(),
        sections = listOf(
            BoardSection(
                type = SectionType.PEDALS,
                blocks = listOf(
                    ControlBlock(
                        name = "STOMPBOX 1",
                        type = "Stompbox",
                        controls = listOf(
                            ControlType.Toggle(label = "ACTIVE", ccNumber = 52),
                            ControlType.Knob(label = "PARAM 1", ccNumber = 20, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "PARAM 2", ccNumber = 21, displayFormat = DisplayFormat.ZERO_TO_TEN)
                        )
                    ),
                    ControlBlock(
                        name = "STOMPBOX 2",
                        type = "Stompbox",
                        controls = listOf(
                            ControlType.Toggle(label = "ACTIVE", ccNumber = 53),
                            ControlType.Knob(label = "PARAM 1", ccNumber = 22, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "PARAM 2", ccNumber = 23, displayFormat = DisplayFormat.ZERO_TO_TEN)
                        )
                    )
                )
            ),
            BoardSection(
                type = SectionType.AMP,
                blocks = listOf(
                    ControlBlock(
                        name = "AMP",
                        type = "Amp",
                        controls = listOf(
                            ControlType.Knob(label = "GAIN", ccNumber = 85, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "BASS", ccNumber = 86, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "MID", ccNumber = 87, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "TREBLE", ccNumber = 88, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "PRESENCE", ccNumber = 89, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "MASTER", ccNumber = 90, displayFormat = DisplayFormat.ZERO_TO_TEN)
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
                                ccNumber = 91,
                                positions = listOf("SM57", "MD421", "U87"),
                                ccValues = listOf(0, 63, 127)
                            ),
                            ControlType.Knob(label = "LEVEL", ccNumber = 92, displayFormat = DisplayFormat.DECIBELS_OUTPUT)
                        )
                    )
                )
            ),
            BoardSection(
                type = SectionType.EFFECTS,
                blocks = listOf(
                    ControlBlock(
                        name = "RACK FX",
                        type = "Rack",
                        controls = listOf(
                            ControlType.Toggle(label = "DELAY", ccNumber = 54),
                            ControlType.Knob(label = "D.MIX", ccNumber = 24, displayFormat = DisplayFormat.PERCENTAGE),
                            ControlType.Toggle(label = "REVERB", ccNumber = 55),
                            ControlType.Knob(label = "R.MIX", ccNumber = 25, displayFormat = DisplayFormat.PERCENTAGE)
                        )
                    ),
                    ControlBlock(
                        name = "PRESET NAV",
                        type = "Navigation",
                        controls = listOf(
                            ControlType.PresetNav(
                                label = "PRESET",
                                ccNumber = -1,
                                navMode = PresetNavMode.PC,
                                pcChannel = 1
                            )
                        )
                    )
                )
            )
        )
    )
}
