package com.gearboard.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.domain.model.FaderOrientation
import com.gearboard.ui.theme.GearBoardColors

/**
 * GearFader — vertical or horizontal fader/slider drawn with Canvas.
 * Amber thumb, drag gesture for value changes.
 *
 * @param value Normalized value 0f..1f
 * @param onValueChange Callback with new normalized value
 * @param label Text label below/beside the fader
 * @param orientation VERTICAL or HORIZONTAL
 * @param faderLength Length of the fader track
 * @param onLongPress Optional callback for edit dialog
 */
@Composable
fun GearFader(
    value: Float,
    onValueChange: (Float) -> Unit,
    label: String,
    displayValue: String = "%.0f".format(value * 127),
    modifier: Modifier = Modifier,
    orientation: FaderOrientation = FaderOrientation.VERTICAL,
    faderLength: Dp = 120.dp,
    onLongPress: (() -> Unit)? = null
) {
    val view = LocalView.current
    var dragAccumulator by remember { mutableFloatStateOf(0f) }
    var lastHapticStep by remember { mutableIntStateOf((value * 20).toInt()) }

    val isVertical = orientation == FaderOrientation.VERTICAL
    val trackWidth = 40.dp
    val thumbHeight = 16f
    val thumbWidth = 32f
    val trackLineWidth = 4f

    if (isVertical) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Value display
            Text(
                text = displayValue,
                color = GearBoardColors.TextPrimary,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )

            Canvas(
                modifier = Modifier
                    .width(trackWidth)
                    .height(faderLength)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                onValueChange(0.5f)
                                view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                            },
                            onLongPress = {
                                onLongPress?.invoke()
                                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                            }
                        )
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { dragAccumulator = value },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                // For vertical: up = increase (negative y)
                                val delta = -dragAmount.y / (size.height * 0.8f)
                                dragAccumulator = (dragAccumulator + delta).coerceIn(0f, 1f)
                                onValueChange(dragAccumulator)

                                val currentStep = (dragAccumulator * 20).toInt()
                                if (currentStep != lastHapticStep) {
                                    lastHapticStep = currentStep
                                    view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                                }
                            }
                        )
                    }
            ) {
                val trackX = size.width / 2f
                val padding = thumbHeight
                val trackTop = padding
                val trackBottom = size.height - padding

                // Track background
                drawLine(
                    color = GearBoardColors.SurfaceElevated,
                    start = Offset(trackX, trackTop),
                    end = Offset(trackX, trackBottom),
                    strokeWidth = trackLineWidth,
                    cap = StrokeCap.Round
                )

                // Active track (from bottom to thumb)
                val thumbY = trackBottom - (value * (trackBottom - trackTop))
                drawLine(
                    color = GearBoardColors.Accent.copy(alpha = 0.4f),
                    start = Offset(trackX, trackBottom),
                    end = Offset(trackX, thumbY),
                    strokeWidth = trackLineWidth,
                    cap = StrokeCap.Round
                )

                // Thumb
                drawRoundRect(
                    color = GearBoardColors.Accent,
                    topLeft = Offset(trackX - thumbWidth / 2f, thumbY - thumbHeight / 2f),
                    size = Size(thumbWidth, thumbHeight),
                    cornerRadius = CornerRadius(4f, 4f)
                )

                // Thumb center line
                drawLine(
                    color = GearBoardColors.TextOnAccent,
                    start = Offset(trackX - thumbWidth / 4f, thumbY),
                    end = Offset(trackX + thumbWidth / 4f, thumbY),
                    strokeWidth = 2f,
                    cap = StrokeCap.Round
                )
            }

            // Label
            if (label.isNotEmpty()) {
                Text(
                    text = label.uppercase(),
                    color = GearBoardColors.TextSecondary,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        // Horizontal orientation
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (label.isNotEmpty()) {
                Text(
                    text = label.uppercase(),
                    color = GearBoardColors.TextSecondary,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )
            }

            Canvas(
                modifier = Modifier
                    .width(faderLength)
                    .height(trackWidth)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                onValueChange(0.5f)
                                view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                            },
                            onLongPress = {
                                onLongPress?.invoke()
                                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                            }
                        )
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { dragAccumulator = value },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                val delta = dragAmount.x / (size.width * 0.8f)
                                dragAccumulator = (dragAccumulator + delta).coerceIn(0f, 1f)
                                onValueChange(dragAccumulator)

                                val currentStep = (dragAccumulator * 20).toInt()
                                if (currentStep != lastHapticStep) {
                                    lastHapticStep = currentStep
                                    view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                                }
                            }
                        )
                    }
            ) {
                val trackY = size.height / 2f
                val padding = thumbWidth / 2f
                val trackLeft = padding
                val trackRight = size.width - padding

                // Track background
                drawLine(
                    color = GearBoardColors.SurfaceElevated,
                    start = Offset(trackLeft, trackY),
                    end = Offset(trackRight, trackY),
                    strokeWidth = trackLineWidth,
                    cap = StrokeCap.Round
                )

                // Active track
                val thumbX = trackLeft + (value * (trackRight - trackLeft))
                drawLine(
                    color = GearBoardColors.Accent.copy(alpha = 0.4f),
                    start = Offset(trackLeft, trackY),
                    end = Offset(thumbX, trackY),
                    strokeWidth = trackLineWidth,
                    cap = StrokeCap.Round
                )

                // Thumb (vertical rectangle for horizontal fader)
                drawRoundRect(
                    color = GearBoardColors.Accent,
                    topLeft = Offset(thumbX - thumbHeight / 2f, trackY - thumbWidth / 2f),
                    size = Size(thumbHeight, thumbWidth),
                    cornerRadius = CornerRadius(4f, 4f)
                )

                // Thumb center line
                drawLine(
                    color = GearBoardColors.TextOnAccent,
                    start = Offset(thumbX, trackY - thumbWidth / 4f),
                    end = Offset(thumbX, trackY + thumbWidth / 4f),
                    strokeWidth = 2f,
                    cap = StrokeCap.Round
                )
            }

            Text(
                text = displayValue,
                color = GearBoardColors.TextPrimary,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
