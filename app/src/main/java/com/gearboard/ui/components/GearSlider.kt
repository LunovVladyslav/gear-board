package com.gearboard.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.ui.theme.GearBoardColors

/**
 * GearSlider — horizontal slider with amber styling.
 * Shows a numeric value indicator next to the thumb.
 *
 * @param value Normalized value 0f..1f
 * @param onValueChange Callback with new normalized value
 * @param label Text label above the slider
 * @param displayValue Formatted display string
 * @param modifier Modifier
 * @param enabled Whether the slider is interactive
 */
@Composable
fun GearSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    label: String,
    displayValue: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val view = LocalView.current
    var lastHapticStep by remember { mutableIntStateOf((value * 20).toInt()) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Label + value row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label.uppercase(),
                color = GearBoardColors.TextSecondary,
                fontSize = 10.sp,
                letterSpacing = 1.5.sp
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(GearBoardColors.SurfaceElevated)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = displayValue,
                    color = if (enabled) GearBoardColors.TextPrimary else GearBoardColors.TextDisabled,
                    fontSize = 12.sp
                )
            }
        }

        // Slider
        Slider(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
                val currentStep = (newValue * 20).toInt()
                if (currentStep != lastHapticStep) {
                    lastHapticStep = currentStep
                    view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp),
            enabled = enabled,
            colors = SliderDefaults.colors(
                thumbColor = GearBoardColors.Accent,
                activeTrackColor = GearBoardColors.Accent,
                inactiveTrackColor = GearBoardColors.SurfaceElevated,
                disabledThumbColor = GearBoardColors.TextDisabled,
                disabledActiveTrackColor = GearBoardColors.TextDisabled,
                disabledInactiveTrackColor = GearBoardColors.SurfaceElevated
            )
        )
    }
}
