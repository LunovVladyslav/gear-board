package com.gearboard.data.presets

import com.gearboard.domain.model.BoardPreset
import com.gearboard.domain.model.BoardSection
import com.gearboard.domain.model.ControlBlock
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.DisplayFormat
import com.gearboard.domain.model.PresetNavMode
import com.gearboard.domain.model.SectionType

object BiasFx2Preset {

    val preset = BoardPreset(
        id = "bias_fx_2",
        name = "BIAS FX 2",
        targetSoftware = "Positive Grid BIAS FX 2",
        description = "Layout for Positive Grid BIAS FX 2. Uses MIDI Learn for CC assignments. PC for preset navigation.",
        defaultMidiChannel = 1,
        setupInstructions = """
After loading this preset in GearBoard:
1. Open Positive Grid BIAS FX 2
2. Right-click / Ctrl-click each parameter you want to control
3. Select "MIDI Learn"
4. Move the corresponding control in GearBoard
5. BIAS FX 2 will automatically detect and assign the CC number

For preset navigation, this preset uses Program Change (PC) messages.
Make sure BIAS FX 2 is set to respond to PC messages on MIDI Channel 1.
Go to: Settings → MIDI → Enable Program Change
""".trimIndent(),
        sections = listOf(
            BoardSection(
                type = SectionType.PEDALS,
                blocks = listOf(
                    ControlBlock(
                        name = "DRIVE",
                        type = "Drive",
                        controls = listOf(
                            ControlType.Toggle(label = "ACTIVE", ccNumber = 52),
                            ControlType.Knob(label = "GAIN", ccNumber = 85, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "TONE", ccNumber = 86, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "LEVEL", ccNumber = 87, displayFormat = DisplayFormat.ZERO_TO_TEN)
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
                            ControlType.Knob(label = "GAIN", ccNumber = 88, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "BASS", ccNumber = 89, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "MID", ccNumber = 90, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "TREBLE", ccNumber = 91, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "PRESENCE", ccNumber = 92, displayFormat = DisplayFormat.ZERO_TO_TEN),
                            ControlType.Knob(label = "MASTER", ccNumber = 93, displayFormat = DisplayFormat.ZERO_TO_TEN)
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
                                ccNumber = 94,
                                positions = listOf("SM57", "MD421", "R121"),
                                ccValues = listOf(0, 63, 127)
                            ),
                            ControlType.Knob(label = "LEVEL", ccNumber = 95, displayFormat = DisplayFormat.DECIBELS_OUTPUT)
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
                            ControlType.Toggle(label = "ACTIVE", ccNumber = 53),
                            ControlType.Knob(label = "MIX", ccNumber = 20, displayFormat = DisplayFormat.PERCENTAGE),
                            ControlType.Knob(label = "TIME", ccNumber = 21, displayFormat = DisplayFormat.MILLISECONDS)
                        )
                    ),
                    ControlBlock(
                        name = "REVERB",
                        type = "Reverb",
                        controls = listOf(
                            ControlType.Toggle(label = "ACTIVE", ccNumber = 54),
                            ControlType.Knob(label = "MIX", ccNumber = 22, displayFormat = DisplayFormat.PERCENTAGE)
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
