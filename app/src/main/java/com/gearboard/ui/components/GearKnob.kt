package com.gearboard.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gearboard.ui.theme.GearBoardColors
import com.gearboard.ui.theme.GearBoardDimensions
import com.gearboard.ui.theme.LocalAccentColor
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * GearKnob — the most important UI component.
 *
 * A custom Canvas composable that looks like a physical knob on audio hardware.
 * Rotation range: -135° to +135° (270° total).
 * Minimum touch target: 64dp × 64dp.
 *
 * @param value Normalized value 0f..1f
 * @param onValueChange Callback with new normalized value
 * @param label Text label below the knob
 * @param displayValue Formatted display string (e.g. "6.0", "127")
 * @param modifier Modifier
 * @param enabled Whether the knob is interactive
 * @param size Diameter of the knob
 * @param onLongPress Optional callback for MIDI Learn activation
 */
@Composable
fun GearKnob(
    value: Float,
    onValueChange: (Float) -> Unit,
    label: String,
    displayValue: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Dp = GearBoardDimensions.KnobDefaultSize,
    onLongPress: (() -> Unit)? = null
) {
    val view = LocalView.current
    val accentColor = LocalAccentColor.current

    // Track haptic feedback step (fire every 5% change)
    var lastHapticStep by remember { mutableIntStateOf((value * 20).toInt()) }

    // Accumulator for drag
    var dragAccumulator by remember { mutableFloatStateOf(0f) }

    val effectiveSize = maxOf(size, GearBoardDimensions.KnobMinSize)

    Column(
        modifier = modifier.padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Canvas(
            modifier = Modifier
                .size(effectiveSize)
                .pointerInput(enabled) {
                    if (!enabled) return@pointerInput
                    detectTapGestures(
                        onDoubleTap = {
                            // Double-tap: reset to center (0.5f)
                            onValueChange(0.5f)
                            lastHapticStep = 10
                            view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                        },
                        onLongPress = {
                            onLongPress?.invoke()
                            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        }
                    )
                }
                .pointerInput(enabled) {
                    if (!enabled) return@pointerInput
                    detectDragGestures(
                        onDragStart = {
                            dragAccumulator = value
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            // Vertical drag: upward = increase, downward = decrease
                            // 1dp movement = 1/200 value change
                            val delta = -dragAmount.y / (GearBoardDimensions.KNOB_SENSITIVITY * density)
                            dragAccumulator = (dragAccumulator + delta).coerceIn(0f, 1f)
                            onValueChange(dragAccumulator)

                            // Haptic feedback every 5% (1/20)
                            val currentStep = (dragAccumulator * 20).toInt()
                            if (currentStep != lastHapticStep) {
                                lastHapticStep = currentStep
                                view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                            }
                        }
                    )
                }
        ) {
            val centerX = size.toPx() / 2f
            val centerY = size.toPx() / 2f
            val outerRadius = size.toPx() / 2f
            val innerRadius = outerRadius * 0.75f

            // --- Outer ring (body) ---
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        GearBoardColors.KnobSurfaceLight,
                        GearBoardColors.KnobBodyDark
                    ),
                    center = Offset(centerX * 0.8f, centerY * 0.8f),
                    radius = outerRadius
                ),
                radius = outerRadius,
                center = Offset(centerX, centerY)
            )

            // --- Inner circle (face) ---
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        GearBoardColors.KnobSurfaceLight,
                        GearBoardColors.KnobSurfaceDark
                    ),
                    center = Offset(centerX * 0.9f, centerY * 0.9f),
                    radius = innerRadius
                ),
                radius = innerRadius,
                center = Offset(centerX, centerY)
            )

            // --- Background arc (full range) ---
            val arcStroke = GearBoardDimensions.KnobArcStrokeWidth.toPx()
            val arcRadius = outerRadius * 0.88f
            drawArc(
                color = GearBoardColors.KnobArc,
                startAngle = 135f,  // -135° mapped to Canvas angles (0° = 3 o'clock)
                sweepAngle = GearBoardDimensions.KNOB_TOTAL_ANGLE,
                useCenter = false,
                topLeft = Offset(centerX - arcRadius, centerY - arcRadius),
                size = androidx.compose.ui.geometry.Size(arcRadius * 2, arcRadius * 2),
                style = Stroke(width = arcStroke, cap = StrokeCap.Round)
            )

            // --- Active arc (value indicator) ---
            val sweepAngle = value * GearBoardDimensions.KNOB_TOTAL_ANGLE
            if (sweepAngle > 0.5f) {
                drawArc(
                    color = accentColor,
                    startAngle = 135f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(centerX - arcRadius, centerY - arcRadius),
                    size = androidx.compose.ui.geometry.Size(arcRadius * 2, arcRadius * 2),
                    style = Stroke(width = arcStroke, cap = StrokeCap.Round)
                )
            }

            // --- Indicator line (amber needle) ---
            val angleDeg = GearBoardDimensions.KNOB_MIN_ANGLE + (value * GearBoardDimensions.KNOB_TOTAL_ANGLE)
            val angleRad = (angleDeg - 90f) * (PI / 180f).toFloat()
            val lineStartRadius = innerRadius * 0.3f
            val lineEndRadius = innerRadius * 0.85f

            val startX = centerX + lineStartRadius * cos(angleRad)
            val startY = centerY + lineStartRadius * sin(angleRad)
            val endX = centerX + lineEndRadius * cos(angleRad)
            val endY = centerY + lineEndRadius * sin(angleRad)

            drawLine(
                color = if (enabled) accentColor else GearBoardColors.TextDisabled,
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = GearBoardDimensions.KnobIndicatorWidth.toPx(),
                cap = StrokeCap.Round
            )

            // --- Center dot ---
            drawCircle(
                color = if (enabled) accentColor.copy(alpha = 0.3f) else GearBoardColors.TextDisabled.copy(alpha = 0.3f),
                radius = 3f,
                center = Offset(centerX, centerY)
            )
        }

        // Label
        if (label.isNotEmpty()) {
            Text(
                text = label.uppercase(),
                color = GearBoardColors.TextSecondary,
                fontSize = androidx.compose.ui.unit.TextUnit(10f, androidx.compose.ui.unit.TextUnitType.Sp),
                letterSpacing = androidx.compose.ui.unit.TextUnit(1.5f, androidx.compose.ui.unit.TextUnitType.Sp),
                textAlign = TextAlign.Center
            )
        }

        // Value display
        Text(
            text = displayValue,
            color = if (enabled) GearBoardColors.TextPrimary else GearBoardColors.TextDisabled,
            fontSize = androidx.compose.ui.unit.TextUnit(12f, androidx.compose.ui.unit.TextUnitType.Sp),
            textAlign = TextAlign.Center
        )
    }
}
