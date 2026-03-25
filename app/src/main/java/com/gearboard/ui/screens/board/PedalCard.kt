package com.gearboard.ui.screens.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.TouchApp
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
import com.gearboard.domain.model.Pedal
import com.gearboard.ui.components.GearKnob
import com.gearboard.ui.components.GearToggle
import com.gearboard.ui.components.ToggleVariant
import com.gearboard.ui.theme.GearBoardColors
import com.gearboard.ui.theme.GearBoardDimensions

/**
 * PedalCard — displays a single pedal with its controls.
 * Fixed width (160dp), used inside a horizontal LazyRow.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PedalCard(
    pedal: Pedal,
    onToggleEnabled: () -> Unit,
    onControlChange: (controlId: String, value: Float) -> Unit,
    onToggleButton: (buttonId: String) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    controlScale: Float = 1.0f
) {
    val knobSize = (GearBoardDimensions.KnobDefaultSize.value * controlScale).dp

    Column(
        modifier = modifier
            .width(GearBoardDimensions.PedalCardWidth)
            .clip(RoundedCornerShape(GearBoardDimensions.RadiusM))
            .background(GearBoardColors.Surface)
            .border(
                width = GearBoardDimensions.BorderThin,
                color = if (pedal.enabled) GearBoardColors.BorderAccent.copy(alpha = 0.3f) else GearBoardColors.BorderDefault,
                shape = RoundedCornerShape(GearBoardDimensions.RadiusM)
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Header: name + close button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pedal.name.uppercase(),
                    color = if (pedal.enabled) GearBoardColors.Accent else GearBoardColors.TextDisabled,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = pedal.type.uppercase(),
                    color = GearBoardColors.TextDisabled,
                    fontSize = 9.sp,
                    letterSpacing = 1.sp
                )
            }
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(Icons.Default.Close, "Remove", tint = GearBoardColors.TextDisabled, modifier = Modifier.size(14.dp))
            }
        }

        // Knobs
        if (pedal.controls.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                pedal.controls.forEach { control ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        GearKnob(
                            value = control.value,
                            onValueChange = { onControlChange(control.id, it) },
                            label = control.name,
                            displayValue = formatValue(control),
                            enabled = pedal.enabled,
                            size = knobSize
                        )
                        // Show CC number
                        if (control.ccNumber >= 0) {
                            Text(
                                text = "CC ${control.ccNumber}",
                                color = GearBoardColors.Accent.copy(alpha = 0.5f),
                                fontSize = 8.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }

        // Toggle buttons
        if (pedal.toggleButtons.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                modifier = Modifier.fillMaxWidth()
            ) {
                pedal.toggleButtons.forEach { btn ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        GearToggle(
                            label = btn.label,
                            enabled = btn.enabled,
                            onToggle = { onToggleButton(btn.id) },
                            variant = ToggleVariant.STOMP,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        if (btn.ccNumber >= 0) {
                            Text(
                                text = "CC ${btn.ccNumber}",
                                color = GearBoardColors.Accent.copy(alpha = 0.5f),
                                fontSize = 8.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }

        // Tap buttons
        if (pedal.tapButtons.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                modifier = Modifier.fillMaxWidth()
            ) {
                pedal.tapButtons.forEach { tap ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(GearBoardColors.SurfaceElevated)
                            .border(1.dp, GearBoardColors.BorderDefault, RoundedCornerShape(6.dp))
                            .clickable { /* TODO: tap tempo */ }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.TouchApp, null, tint = GearBoardColors.Accent, modifier = Modifier.size(14.dp))
                            Text(tap.label.uppercase(), color = GearBoardColors.TextPrimary, fontSize = 10.sp, letterSpacing = 1.sp)
                        }
                    }
                }
            }
        }

        // Enable/disable stomp
        Spacer(Modifier.height(4.dp))
        GearToggle(
            label = if (pedal.enabled) "ON" else "OFF",
            enabled = pedal.enabled,
            onToggle = onToggleEnabled,
            variant = ToggleVariant.STOMP,
            modifier = Modifier
        )
    }
}

/** Format control value for display */
private fun formatValue(control: com.gearboard.domain.model.ControlParam): String {
    val rawValue = control.minValue + (control.value * (control.maxValue - control.minValue))
    return when {
        control.unit.isNotEmpty() -> "${rawValue.toInt()} ${control.unit}"
        control.maxValue <= 10f -> "%.1f".format(rawValue)
        else -> rawValue.toInt().toString()
    }
}
