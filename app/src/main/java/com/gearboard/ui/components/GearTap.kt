package com.gearboard.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.ui.theme.GearBoardColors
import com.gearboard.ui.theme.GearBoardDimensions
import kotlinx.coroutines.delay

/**
 * GearTap — momentary tap button.
 * Sends CC 127 on each press with a 100ms flash animation.
 * Stateless — always returns to dim.
 */
@Composable
fun GearTap(
    label: String,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    var isFlashing by remember { mutableStateOf(false) }

    val bgColor by animateColorAsState(
        targetValue = if (isFlashing) GearBoardColors.Accent else GearBoardColors.SurfaceElevated,
        animationSpec = tween(if (isFlashing) 30 else 100),
        label = "tapFlash"
    )
    val textColor by animateColorAsState(
        targetValue = if (isFlashing) GearBoardColors.TextOnAccent else GearBoardColors.TextPrimary,
        animationSpec = tween(if (isFlashing) 30 else 100),
        label = "tapFlashText"
    )

    LaunchedEffect(isFlashing) {
        if (isFlashing) {
            delay(100)
            isFlashing = false
        }
    }

    Box(
        modifier = modifier
            .height(GearBoardDimensions.ToggleMinHeight)
            .clip(RoundedCornerShape(GearBoardDimensions.RadiusM))
            .background(bgColor)
            .border(
                width = GearBoardDimensions.BorderThin,
                color = GearBoardColors.BorderDefault,
                shape = RoundedCornerShape(GearBoardDimensions.RadiusM)
            )
            .clickable {
                isFlashing = true
                onTap()
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                Icons.Default.TouchApp,
                contentDescription = null,
                tint = if (isFlashing) GearBoardColors.TextOnAccent else GearBoardColors.Accent,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = label.uppercase(),
                color = textColor,
                fontSize = 11.sp,
                letterSpacing = 1.5.sp
            )
        }
    }
}
