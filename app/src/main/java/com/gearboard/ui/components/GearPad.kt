package com.gearboard.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.ui.theme.GearBoardColors
import com.gearboard.ui.theme.GearBoardDimensions

/**
 * GearPad — MIDI note pad.
 * Sends Note On (press) and Note Off (release).
 * Min size 80×80dp. Amber flash on press.
 */
@Composable
fun GearPad(
    label: String,
    onPressDown: () -> Unit,
    onPressUp: () -> Unit,
    modifier: Modifier = Modifier,
    padSize: Dp = 80.dp,
    onLongPress: (() -> Unit)? = null
) {
    val view = LocalView.current
    var isPressed by remember { mutableStateOf(false) }

    val bgColor by animateColorAsState(
        targetValue = if (isPressed) GearBoardColors.Accent else GearBoardColors.SurfaceElevated,
        animationSpec = tween(50),
        label = "padPress"
    )
    val textColor by animateColorAsState(
        targetValue = if (isPressed) GearBoardColors.TextOnAccent else GearBoardColors.TextPrimary,
        animationSpec = tween(50),
        label = "padPressText"
    )

    val effectiveSize = maxOf(padSize, 80.dp)

    Box(
        modifier = modifier
            .size(effectiveSize)
            .clip(RoundedCornerShape(GearBoardDimensions.RadiusL))
            .background(bgColor)
            .border(
                width = GearBoardDimensions.BorderMedium,
                color = if (isPressed) GearBoardColors.Accent else GearBoardColors.BorderDefault,
                shape = RoundedCornerShape(GearBoardDimensions.RadiusL)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        onPressDown()
                        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                        tryAwaitRelease()
                        isPressed = false
                        onPressUp()
                    },
                    onLongPress = {
                        onLongPress?.invoke()
                        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label.uppercase(),
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )
    }
}
