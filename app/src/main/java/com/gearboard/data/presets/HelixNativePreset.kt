package com.gearboard.data.presets

import com.gearboard.domain.model.BoardPreset
import com.gearboard.domain.model.BoardSection
import com.gearboard.domain.model.ControlBlock
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.DisplayFormat
import com.gearboard.domain.model.FaderOrientation
import com.gearboard.domain.model.PresetNavMode
import com.gearboard.domain.model.SectionType

object HelixNativePreset {

    val preset = BoardPreset(
        id = "helix_native",
        name = "Helix Native",
        targetSoftware = "Line 6 Helix Native",
        description = "Layout for Helix Native. Snapshots use PC messages. FX bypass uses CC. Wah and Volume use Helix convention CC1/CC7.",
        defaultMidiChannel = 1,
        setupInstructions = """
After loading this preset in GearBoard:
1. Open Line 6 Helix Native
2. Go to Settings → MIDI/Tempo
3. Set MIDI Receive Channel to 1 (or Omni)

SNAPSHOTS (Program Change):
  The SNAP 1–4 toggles send PC messages (Program 0–3).
  Helix Native maps PC 0–7 to its 8 snapshot slots by default.

FX BYPASS (CC 52–55):
  In Helix Native's Command Center, assign each block bypass to the matching CC.
  Each Toggle in GearBoard sends CC 127 (on) / CC 0 (off).

EXPRESSION PEDALS:
  WAH = CC 1 (Helix hardware convention)
  VOLUME = CC 7 (standard MIDI volume)
  These are intentional — do not remap unless your hardware differs.

PRESET NAVIGATION:
  Uses Program Change messages for preset navigation.
  Make sure Helix Native is set to receive PC on Channel 1.
""".trimIndent(),
        sections = listOf(
            BoardSection(
                type = SectionType.PEDALS,
                blocks = listOf(
                    ControlBlock(
                        name = "SNAPSHOTS",
                        type = "Snapshots",
                        controls = listOf(
                            // Helix snapshots are PC messages — using Tap to send PC via ccNumber sentinel
                            // These Toggles are wired to PC in the MIDI engine when navMode=PC
                            ControlType.Toggle(label = "SNAP 1", ccNumber = 20),
                            ControlType.Toggle(label = "SNAP 2", ccNumber = 21),
                            ControlType.Toggle(label = "SNAP 3", ccNumber = 22),
                            ControlType.Toggle(label = "SNAP 4", ccNumber = 23)
                        )
                    ),
                    ControlBlock(
                        name = "FX BYPASS",
                        type = "FX",
                        controls = listOf(
                            ControlType.Toggle(label = "FX 1", ccNumber = 52),
                            ControlType.Toggle(label = "FX 2", ccNumber = 53),
                            ControlType.Toggle(label = "FX 3", ccNumber = 54),
                            ControlType.Toggle(label = "FX 4", ccNumber = 55)
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
                            ControlType.Knob(label = "MASTER", ccNumber = 89, displayFormat = DisplayFormat.ZERO_TO_TEN)
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
                                ccNumber = 90,
                                positions = listOf("57 DYN", "421 DYN", "121 RBN"),
                                ccValues = listOf(0, 63, 127)
                            ),
                            ControlType.Knob(label = "LEVEL", ccNumber = 91, displayFormat = DisplayFormat.DECIBELS_OUTPUT)
                        )
                    )
                )
            ),
            BoardSection(
                type = SectionType.EFFECTS,
                blocks = listOf(
                    ControlBlock(
                        name = "EXPRESSION",
                        type = "Expression",
                        controls = listOf(
                            // CC1 and CC7 are intentional Helix hardware conventions
                            ControlType.Fader(
                                label = "WAH",
                                ccNumber = 1,
                                orientation = FaderOrientation.VERTICAL,
                                displayFormat = DisplayFormat.PERCENTAGE
                            ),
                            ControlType.Fader(
                                label = "VOLUME",
                                ccNumber = 7,
                                orientation = FaderOrientation.VERTICAL,
                                displayFormat = DisplayFormat.PERCENTAGE
                            )
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
