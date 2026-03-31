package com.gearboard.data.presets

import com.gearboard.domain.model.BoardPreset
import com.gearboard.domain.model.BoardSection
import com.gearboard.domain.model.ControlBlock
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.DisplayFormat
import com.gearboard.domain.model.FaderOrientation
import com.gearboard.domain.model.PresetNavMode
import com.gearboard.domain.model.SectionType

object GuitarRig7Preset {

    val preset = BoardPreset(
        id = "guitar_rig_7",
        name = "Guitar Rig 7",
        targetSoftware = "Native Instruments Guitar Rig 7",
        description = "Layout for Native Instruments Guitar Rig 7. Full MIDI Learn for all CC assignments. PC for preset navigation.",
        defaultMidiChannel = 1,
        setupInstructions = """
After loading this preset in GearBoard:
1. Open Native Instruments Guitar Rig 7
2. Right-click / Ctrl-click each parameter you want to control
3. Select "MIDI Learn"
4. Move the corresponding control in GearBoard
5. Guitar Rig will automatically detect and assign the CC number

For preset navigation, this preset uses Program Change (PC) messages.
Make sure Guitar Rig 7 is set to respond to PC messages on MIDI Channel 1.
Go to: View → MIDI Settings → Enable Preset Change via MIDI
""".trimIndent(),
        sections = listOf(
            BoardSection(
                type = SectionType.PEDALS,
                blocks = listOf(
                    ControlBlock(
                        name = "PEDAL 1",
                        type = "Pedal",
                        controls = listOf(
                            ControlType.Toggle(label = "ACTIVE", ccNumber = 52),
                            ControlType.Knob(label = "MIX", ccNumber = 20, displayFormat = DisplayFormat.PERCENTAGE)
                        )
                    ),
                    ControlBlock(
                        name = "PEDAL 2",
                        type = "Pedal",
                        controls = listOf(
                            ControlType.Toggle(label = "ACTIVE", ccNumber = 53),
                            ControlType.Knob(label = "MIX", ccNumber = 21, displayFormat = DisplayFormat.PERCENTAGE)
                        )
                    )
                )
            ),
            BoardSection(
                type = SectionType.AMP,
                blocks = listOf(
                    ControlBlock(
                        name = "AMP HEAD",
                        type = "Amp",
                        controls = listOf(
                            ControlType.Knob(label = "GAIN", ccNumber = 85, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "BASS", ccNumber = 86, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "MID", ccNumber = 87, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "TREBLE", ccNumber = 88, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "MASTER", ccNumber = 89, displayFormat = DisplayFormat.ZERO_TO_TEN)
                        )
                    )
                )
            ),
            BoardSection(
                type = SectionType.CAB,
                blocks = listOf(
                    ControlBlock(
                        name = "CAB",
                        type = "Cab",
                        controls = listOf(
                            ControlType.Selector(
                                label = "MIC",
                                ccNumber = 90,
                                positions = listOf("SM57", "MD421", "U67", "R121"),
                                ccValues = listOf(0, 42, 85, 127)
                            ),
                            ControlType.Fader(
                                label = "POS",
                                ccNumber = 91,
                                orientation = FaderOrientation.HORIZONTAL,
                                displayFormat = DisplayFormat.PERCENTAGE
                            )
                        )
                    )
                )
            ),
            BoardSection(
                type = SectionType.EFFECTS,
                blocks = listOf(
                    ControlBlock(
                        name = "LOOP 1",
                        type = "Loop",
                        controls = listOf(
                            ControlType.Toggle(label = "BYPASS", ccNumber = 54),
                            ControlType.Knob(label = "MIX", ccNumber = 22, displayFormat = DisplayFormat.PERCENTAGE)
                        )
                    ),
                    ControlBlock(
                        name = "LOOP 2",
                        type = "Loop",
                        controls = listOf(
                            ControlType.Toggle(label = "BYPASS", ccNumber = 55),
                            ControlType.Knob(label = "MIX", ccNumber = 23, displayFormat = DisplayFormat.PERCENTAGE)
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
