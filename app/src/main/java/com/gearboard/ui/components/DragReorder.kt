package com.gearboard.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.gearboard.ui.theme.GearBoardColors
import kotlin.math.roundToInt

/**
 * ReorderableColumn — a Column whose children can be reordered via long-press drag.
 *
 * @param items The list of items to display
 * @param onReorder Callback with the reordered list after a drag completes
 * @param itemContent Composable for each item
 */
@Composable
fun <T> ReorderableColumn(
    items: List<T>,
    onReorder: (List<T>) -> Unit,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(6.dp),
    itemContent: @Composable (index: Int, item: T) -> Unit
) {
    if (items.isEmpty()) return

    val view = LocalView.current
    val density = LocalDensity.current

    // Track the currently dragged item
    var draggedIndex by remember { mutableIntStateOf(-1) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }
    var itemHeights by remember { mutableStateOf(listOf<Int>()) }
    var currentOrder by remember(items) { mutableStateOf(items.toList()) }

    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement
    ) {
        currentOrder.forEachIndexed { index, item ->
            val isDragged = draggedIndex == index
            val elevation by animateDpAsState(
                targetValue = if (isDragged) 8.dp else 0.dp,
                label = "dragElevation"
            )

            Surface(
                shadowElevation = elevation,
                shape = RoundedCornerShape(8.dp),
                color = if (isDragged) GearBoardColors.SurfaceElevated else GearBoardColors.Surface,
                modifier = Modifier
                    .zIndex(if (isDragged) 1f else 0f)
                    .offset {
                        IntOffset(0, if (isDragged) dragOffsetY.roundToInt() else 0)
                    }
                    .onGloballyPositioned { coords ->
                        val heights = itemHeights.toMutableList()
                        while (heights.size <= index) heights.add(0)
                        heights[index] = coords.size.height
                        itemHeights = heights
                    }
                    .pointerInput(index) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = {
                                draggedIndex = index
                                dragOffsetY = 0f
                                currentOrder = items.toList()
                                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                dragOffsetY += dragAmount.y

                                // Calculate the target index based on drag offset
                                if (itemHeights.isNotEmpty() && draggedIndex >= 0) {
                                    val avgHeight = itemHeights
                                        .filter { it > 0 }
                                        .let { if (it.isEmpty()) 100 else it.average().toInt() }
                                    val spacingPx = with(density) { 6.dp.toPx() }.toInt()
                                    val step = avgHeight + spacingPx

                                    val rawTargetIndex = draggedIndex + (dragOffsetY / step).roundToInt()
                                    val targetIndex = rawTargetIndex.coerceIn(0, currentOrder.size - 1)

                                    if (targetIndex != draggedIndex) {
                                        val mutableList = currentOrder.toMutableList()
                                        val movedItem = mutableList.removeAt(draggedIndex)
                                        mutableList.add(targetIndex, movedItem)
                                        currentOrder = mutableList

                                        // Adjust offset for swap
                                        dragOffsetY -= (targetIndex - draggedIndex) * step
                                        draggedIndex = targetIndex
                                        view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                                    }
                                }
                            },
                            onDragEnd = {
                                onReorder(currentOrder)
                                draggedIndex = -1
                                dragOffsetY = 0f
                            },
                            onDragCancel = {
                                currentOrder = items.toList()
                                draggedIndex = -1
                                dragOffsetY = 0f
                            }
                        )
                    }
            ) {
                itemContent(index, item)
            }
        }
    }
}
