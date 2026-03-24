package com.gearboard.ui.screens.board

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gearboard.domain.model.AmpSettings
import com.gearboard.ui.components.GearKnob
import com.gearboard.ui.theme.GearBoardDimensions

/**
 * AmpSection — displays amp controls as a row of knobs.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AmpSection(
    amp: AmpSettings,
    onControlChange: (controlId: String, value: Float) -> Unit,
    modifier: Modifier = Modifier,
    controlScale: Float = 1.0f
) {
    val knobSize = (GearBoardDimensions.KnobDefaultSize.value * controlScale).dp

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        amp.controls.forEach { control ->
            GearKnob(
                value = control.value,
                onValueChange = { onControlChange(control.id, it) },
                label = control.name,
                displayValue = formatControlValue(control),
                enabled = amp.enabled,
                size = knobSize
            )
        }
    }
}

/**
 * CabSection — displays cabinet controls as a row of knobs.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CabSection(
    cabinet: com.gearboard.domain.model.CabinetSettings,
    onControlChange: (controlId: String, value: Float) -> Unit,
    modifier: Modifier = Modifier,
    controlScale: Float = 1.0f
) {
    val knobSize = (GearBoardDimensions.KnobDefaultSize.value * controlScale).dp

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        cabinet.controls.forEach { control ->
            GearKnob(
                value = control.value,
                onValueChange = { onControlChange(control.id, it) },
                label = control.name,
                displayValue = formatControlValue(control),
                enabled = cabinet.enabled,
                size = knobSize
            )
        }
    }
}

/** Format control value for display */
private fun formatControlValue(control: com.gearboard.domain.model.ControlParam): String {
    val rawValue = control.minValue + (control.value * (control.maxValue - control.minValue))
    return when {
        control.unit.isNotEmpty() -> "${rawValue.toInt()} ${control.unit}"
        control.maxValue <= 10f -> "%.1f".format(rawValue)
        else -> rawValue.toInt().toString()
    }
}
