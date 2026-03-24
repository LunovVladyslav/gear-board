package com.gearboard.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.ui.theme.GearBoardColors
import com.gearboard.ui.theme.GearBoardDimensions

/**
 * GearToggle — on/off toggle with two visual variants.
 *
 * "stomp" variant: Looks like a guitar pedal footswitch with LED indicator.
 * "switch" variant: Standard toggle switch with sliding thumb.
 */
enum class ToggleVariant {
    STOMP, SWITCH
}

@Composable
fun GearToggle(
    label: String,
    enabled: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ToggleVariant = ToggleVariant.STOMP
) {
    val view = LocalView.current

    when (variant) {
        ToggleVariant.STOMP -> StompToggle(label, enabled, onToggle, modifier, view)
        ToggleVariant.SWITCH -> SwitchToggle(label, enabled, onToggle, modifier, view)
    }
}

@Composable
private fun StompToggle(
    label: String,
    enabled: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier,
    view: android.view.View
) {
    val bgColor by animateColorAsState(
        targetValue = if (enabled) GearBoardColors.Accent else GearBoardColors.SurfaceElevated,
        animationSpec = tween(150),
        label = "stompBg"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(GearBoardDimensions.ToggleStompSize)
                .clip(RoundedCornerShape(GearBoardDimensions.RadiusM))
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (enabled) {
                            listOf(GearBoardColors.Accent, GearBoardColors.AccentHover)
                        } else {
                            listOf(GearBoardColors.SurfaceElevated, GearBoardColors.Surface)
                        }
                    )
                )
                .then(
                    if (enabled) {
                        Modifier.shadow(8.dp, RoundedCornerShape(GearBoardDimensions.RadiusM), ambientColor = GearBoardColors.AccentGlow, spotColor = GearBoardColors.AccentGlow)
                    } else {
                        Modifier
                    }
                )
                .clickable {
                    onToggle()
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                },
            contentAlignment = Alignment.Center
        ) {
            if (enabled) {
                // LED dot indicator
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(GearBoardColors.TextOnAccent)
                )
            }
        }

        Text(
            text = label.uppercase(),
            color = GearBoardColors.TextSecondary,
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SwitchToggle(
    label: String,
    enabled: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier,
    view: android.view.View
) {
    val trackColor by animateColorAsState(
        targetValue = if (enabled) GearBoardColors.Accent else GearBoardColors.SurfaceElevated,
        animationSpec = tween(200),
        label = "switchTrack"
    )

    Row(
        modifier = modifier
            .height(GearBoardDimensions.ToggleMinHeight)
            .clip(RoundedCornerShape(GearBoardDimensions.RadiusM))
            .background(GearBoardColors.Surface)
            .border(
                width = GearBoardDimensions.BorderThin,
                color = GearBoardColors.BorderDefault,
                shape = RoundedCornerShape(GearBoardDimensions.RadiusM)
            )
            .clickable {
                onToggle()
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Switch track
        Box(
            modifier = Modifier
                .width(GearBoardDimensions.SwitchWidth)
                .height(GearBoardDimensions.SwitchHeight)
                .clip(RoundedCornerShape(12.dp))
                .background(trackColor)
        ) {
            // Thumb
            Box(
                modifier = Modifier
                    .size(GearBoardDimensions.SwitchThumbSize)
                    .offset(
                        x = if (enabled) (GearBoardDimensions.SwitchWidth - GearBoardDimensions.SwitchThumbSize - 4.dp) else 4.dp,
                        y = (GearBoardDimensions.SwitchHeight - GearBoardDimensions.SwitchThumbSize) / 2
                    )
                    .clip(CircleShape)
                    .background(GearBoardColors.TextPrimary)
                    .shadow(2.dp, CircleShape)
            )
        }

        // Label
        Text(
            text = label,
            color = GearBoardColors.TextPrimary,
            fontSize = 14.sp
        )
    }
}
