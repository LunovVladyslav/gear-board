package com.gearboard.ui.screens.board

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.FaderOrientation
import com.gearboard.domain.model.midiToDisplay
import com.gearboard.ui.components.GearFader
import com.gearboard.ui.components.GearKnob
import com.gearboard.ui.components.GearPad
import com.gearboard.ui.components.GearPresetNav
import com.gearboard.ui.components.GearSelector
import com.gearboard.ui.components.GearTap
import com.gearboard.ui.components.GearToggle
import com.gearboard.ui.components.ReorderableGrid
import com.gearboard.ui.components.ToggleVariant
import com.gearboard.ui.theme.GearBoardDimensions
import kotlin.math.roundToInt

/**
 * ControlRenderer — renders any ControlType as the appropriate composable.
 * Centralized mapping from domain model → UI component.
 */
@Composable
fun RenderControl(
    control: ControlType,
    onKnobValueChange: (ControlType.Knob, Float) -> Unit = { _, _ -> },
    onToggle: (ControlType.Toggle) -> Unit = {},
    onTap: (ControlType.Tap) -> Unit = {},
    onSelectorChange: (ControlType.Selector, Int) -> Unit = { _, _ -> },
    onFaderValueChange: (ControlType.Fader, Float) -> Unit = { _, _ -> },
    onPresetPrev: (ControlType.PresetNav) -> Unit = {},
    onPresetNext: (ControlType.PresetNav) -> Unit = {},
    onPadDown: (ControlType.Pad) -> Unit = {},
    onPadUp: (ControlType.Pad) -> Unit = {},
    onLongPress: (ControlType) -> Unit = {},
    onBadgeTap: (ControlType) -> Unit = {},
    enabled: Boolean = true,
    controlScale: Float = 1.0f,
    modifier: Modifier = Modifier
) {
    val finalScale = controlScale * control.size.scaleFactor
    UnmappedBadge(control = control, onBadgeTap = { onBadgeTap(control) }) {
    when (control) {
        is ControlType.Knob -> {
            val knobSize = (GearBoardDimensions.KnobDefaultSize.value * finalScale).dp
            val midiValue = (control.value * 127).roundToInt()
            GearKnob(
                value = control.value,
                onValueChange = { onKnobValueChange(control, it) },
                label = control.label,
                displayValue = midiToDisplay(midiValue, control.displayFormat),
                enabled = enabled,
                size = knobSize,
                onLongPress = { onLongPress(control) },
                modifier = modifier
            )
        }
        is ControlType.Toggle -> {
            GearToggle(
                label = control.label,
                enabled = control.isOn,
                onToggle = { onToggle(control) },
                variant = ToggleVariant.STOMP,
                onLongPress = { onLongPress(control) },
                modifier = modifier
            )
        }
        is ControlType.Tap -> {
            GearTap(
                label = control.label,
                onTap = { onTap(control) },
                onLongPress = { onLongPress(control) },
                modifier = modifier
            )
        }
        is ControlType.Selector -> {
            GearSelector(
                label = control.label,
                positions = control.positions,
                selectedIndex = control.selectedIndex,
                onPositionSelected = { onSelectorChange(control, it) },
                onLongPress = { onLongPress(control) },
                modifier = modifier
            )
        }
        is ControlType.Fader -> {
            val midiValue = (control.value * 127).roundToInt()
            GearFader(
                value = control.value,
                onValueChange = { onFaderValueChange(control, it) },
                label = control.label,
                displayValue = midiToDisplay(midiValue, control.displayFormat),
                orientation = control.orientation,
                faderLength = if (control.orientation == FaderOrientation.VERTICAL) {
                    (GearBoardDimensions.FaderMinHeight.value * finalScale).dp
                } else {
                    (GearBoardDimensions.FaderMinHeight.value * finalScale * 1.5f).dp
                },
                onLongPress = { onLongPress(control) },
                modifier = modifier
            )
        }
        is ControlType.PresetNav -> {
            GearPresetNav(
                label = control.label,
                currentPreset = control.currentPreset,
                onPrevious = { onPresetPrev(control) },
                onNext = { onPresetNext(control) },
                modifier = modifier
            )
        }
        is ControlType.Pad -> {
            val padSize = (GearBoardDimensions.PadMinSize.value * finalScale).dp
            GearPad(
                label = control.label,
                onPressDown = { onPadDown(control) },
                onPressUp = { onPadUp(control) },
                padSize = padSize,
                onLongPress = { onLongPress(control) },
                modifier = modifier
            )
        }
    }
    } // end UnmappedBadge
}

/**
 * Renders a list of controls in a grid layout with max 3 rows.
 * If [onReorder] is provided, wraps controls in a ReorderableGrid for 2D drag reorder.
 */
@Composable
fun RenderControlList(
    controls: List<ControlType>,
    onKnobValueChange: (ControlType.Knob, Float) -> Unit = { _, _ -> },
    onToggle: (ControlType.Toggle) -> Unit = {},
    onTap: (ControlType.Tap) -> Unit = {},
    onSelectorChange: (ControlType.Selector, Int) -> Unit = { _, _ -> },
    onFaderValueChange: (ControlType.Fader, Float) -> Unit = { _, _ -> },
    onPresetPrev: (ControlType.PresetNav) -> Unit = {},
    onPresetNext: (ControlType.PresetNav) -> Unit = {},
    onPadDown: (ControlType.Pad) -> Unit = {},
    onPadUp: (ControlType.Pad) -> Unit = {},
    onLongPress: (ControlType) -> Unit = {},
    onBadgeTap: (ControlType) -> Unit = {},
    onReorder: ((List<ControlType>) -> Unit)? = null,
    enabled: Boolean = true,
    controlScale: Float = 1.0f,
    layoutMode: com.gearboard.domain.model.BlockLayout = com.gearboard.domain.model.BlockLayout.GRID,
    modifier: Modifier = Modifier
) {
    if (controls.isEmpty()) return

    val maxRows = when (layoutMode) {
        com.gearboard.domain.model.BlockLayout.GRID -> 3
        com.gearboard.domain.model.BlockLayout.ROW -> 1
        com.gearboard.domain.model.BlockLayout.COMPACT -> 3
    }

    val horizontalSpacing = when (layoutMode) {
        com.gearboard.domain.model.BlockLayout.COMPACT -> 2.dp
        else -> 4.dp
    }

    val finalScale = if (layoutMode == com.gearboard.domain.model.BlockLayout.COMPACT) controlScale * 0.8f else controlScale

    if (onReorder != null) {
        // Reorderable mode: 2D grid with long-press drag
        ReorderableGrid(
            items = controls,
            onReorder = onReorder,
            modifier = modifier,
            maxRows = maxRows,
            horizontalArrangement = Arrangement.spacedBy(horizontalSpacing, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) { _, control ->
            RenderControl(
                control = control,
                onKnobValueChange = onKnobValueChange,
                onToggle = onToggle,
                onTap = onTap,
                onSelectorChange = onSelectorChange,
                onFaderValueChange = onFaderValueChange,
                onPresetPrev = onPresetPrev,
                onPresetNext = onPresetNext,
                onPadDown = onPadDown,
                onPadUp = onPadUp,
                onLongPress = onLongPress,
                onBadgeTap = onBadgeTap,
                enabled = enabled,
                controlScale = finalScale
            )
        }
    } else {
        when (layoutMode) {
            com.gearboard.domain.model.BlockLayout.GRID -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 80.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = modifier.heightIn(max = 400.dp)
                ) {
                    items(controls) { control ->
                        RenderControl(
                            control = control,
                            onKnobValueChange = onKnobValueChange,
                            onToggle = onToggle,
                            onTap = onTap,
                            onSelectorChange = onSelectorChange,
                            onFaderValueChange = onFaderValueChange,
                            onPresetPrev = onPresetPrev,
                            onPresetNext = onPresetNext,
                            onPadDown = onPadDown,
                            onPadUp = onPadUp,
                            onLongPress = onLongPress,
                            enabled = enabled,
                            controlScale = finalScale
                        )
                    }
                }
            }
            com.gearboard.domain.model.BlockLayout.ROW -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
                    modifier = modifier
                ) {
                    items(controls) { control ->
                        RenderControl(
                            control = control,
                            onKnobValueChange = onKnobValueChange,
                            onToggle = onToggle,
                            onTap = onTap,
                            onSelectorChange = onSelectorChange,
                            onFaderValueChange = onFaderValueChange,
                            onPresetPrev = onPresetPrev,
                            onPresetNext = onPresetNext,
                            onPadDown = onPadDown,
                            onPadUp = onPadUp,
                            onLongPress = onLongPress,
                            enabled = enabled,
                            controlScale = finalScale
                        )
                    }
                }
            }
            com.gearboard.domain.model.BlockLayout.COMPACT -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = modifier.heightIn(max = 200.dp)
                ) {
                    items(controls) { control ->
                        RenderControl(
                            control = control,
                            onKnobValueChange = onKnobValueChange,
                            onToggle = onToggle,
                            onTap = onTap,
                            onSelectorChange = onSelectorChange,
                            onFaderValueChange = onFaderValueChange,
                            onPresetPrev = onPresetPrev,
                            onPresetNext = onPresetNext,
                            onPadDown = onPadDown,
                            onPadUp = onPadUp,
                            onLongPress = onLongPress,
                            enabled = enabled,
                            controlScale = finalScale
                        )
                    }
                }
            }
        }
    }
}

