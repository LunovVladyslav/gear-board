package com.gearboard.ui.screens.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.domain.model.Effect
import com.gearboard.ui.components.GearKnob
import com.gearboard.ui.components.GearToggle
import com.gearboard.ui.components.ToggleVariant
import com.gearboard.ui.theme.GearBoardColors
import com.gearboard.ui.theme.GearBoardDimensions

/**
 * EffectCard — displays an effect with controls.
 * Similar to PedalCard but simpler (no toggle/tap buttons).
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EffectCard(
    effect: Effect,
    onToggleEnabled: () -> Unit,
    onControlChange: (controlId: String, value: Float) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    controlScale: Float = 1.0f
) {
    val knobSize = (GearBoardDimensions.KnobDefaultSize.value * controlScale).dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(GearBoardDimensions.RadiusM))
            .background(GearBoardColors.Surface)
            .border(
                width = GearBoardDimensions.BorderThin,
                color = if (effect.enabled) GearBoardColors.BorderAccent.copy(alpha = 0.3f) else GearBoardColors.BorderDefault,
                shape = RoundedCornerShape(GearBoardDimensions.RadiusM)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GearToggle(
                    label = "",
                    enabled = effect.enabled,
                    onToggle = onToggleEnabled,
                    variant = ToggleVariant.SWITCH
                )
                Column {
                    Text(
                        text = effect.name.uppercase(),
                        color = if (effect.enabled) GearBoardColors.Accent else GearBoardColors.TextDisabled,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = effect.type.uppercase(),
                        color = GearBoardColors.TextDisabled,
                        fontSize = 9.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(Icons.Default.Close, "Remove", tint = GearBoardColors.TextDisabled, modifier = Modifier.size(14.dp))
            }
        }

        // Controls
        if (effect.controls.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                effect.controls.forEach { control ->
                    val rawValue = control.minValue + (control.value * (control.maxValue - control.minValue))
                    val displayValue = when {
                        control.unit.isNotEmpty() -> "${rawValue.toInt()} ${control.unit}"
                        control.maxValue <= 10f -> "%.1f".format(rawValue)
                        else -> rawValue.toInt().toString()
                    }
                    GearKnob(
                        value = control.value,
                        onValueChange = { onControlChange(control.id, it) },
                        label = control.name,
                        displayValue = displayValue,
                        enabled = effect.enabled,
                        size = knobSize
                    )
                }
            }
        }
    }
}
