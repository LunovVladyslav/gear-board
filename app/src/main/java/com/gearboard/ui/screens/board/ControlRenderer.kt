package com.gearboard.ui.screens.board

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.gearboard.ui.components.ReorderableColumn
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
    enabled: Boolean = true,
    controlScale: Float = 1.0f,
    modifier: Modifier = Modifier
) {
    when (control) {
        is ControlType.Knob -> {
            val knobSize = (GearBoardDimensions.KnobDefaultSize.value * controlScale).dp
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
                modifier = modifier
            )
        }
        is ControlType.Tap -> {
            GearTap(
                label = control.label,
                onTap = { onTap(control) },
                modifier = modifier
            )
        }
        is ControlType.Selector -> {
            GearSelector(
                label = control.label,
                positions = control.positions,
                selectedIndex = control.selectedIndex,
                onPositionSelected = { onSelectorChange(control, it) },
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
                    (GearBoardDimensions.FaderMinHeight.value * controlScale).dp
                } else {
                    (GearBoardDimensions.FaderMinHeight.value * controlScale * 1.5f).dp
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
            val padSize = (GearBoardDimensions.PadMinSize.value * controlScale).dp
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
}

/**
 * Renders a list of controls in a FlowRow layout.
 * If [onReorder] is provided, wraps controls in a ReorderableColumn for drag reorder.
 */
@OptIn(ExperimentalLayoutApi::class)
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
    onReorder: ((List<ControlType>) -> Unit)? = null,
    enabled: Boolean = true,
    controlScale: Float = 1.0f,
    modifier: Modifier = Modifier
) {
    if (controls.isEmpty()) return

    if (onReorder != null) {
        // Reorderable mode: vertical list with long-press drag
        ReorderableColumn(
            items = controls,
            onReorder = onReorder,
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                enabled = enabled,
                controlScale = controlScale
            )
        }
    } else {
        // Standard FlowRow layout
        FlowRow(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            controls.forEach { control ->
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
                    controlScale = controlScale
                )
            }
        }
    }
}

