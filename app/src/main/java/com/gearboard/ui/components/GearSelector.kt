package com.gearboard.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.ui.theme.GearBoardColors
import com.gearboard.ui.theme.LocalAccentColor

/**
 * GearSelector — collapsed button showing only the selected option.
 * Tap to open a DropdownMenu overlay; other UI does not shift.
 * Chevron animates on open/close.
 */
@Composable
fun GearSelector(
    label: String,
    positions: List<String>,
    selectedIndex: Int,
    onPositionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onLongPress: (() -> Unit)? = null
) {
    val accentColor = LocalAccentColor.current
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = positions.getOrNull(selectedIndex) ?: positions.firstOrNull() ?: ""

    Column(modifier = modifier) {

        // Section label above button
        if (label.isNotEmpty()) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = GearBoardColors.TextSecondary,
                maxLines = 1,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        if (positions.isEmpty()) return@Column

        // Collapsed button — shows only selected value
        Box {
            Surface(
                onClick = { expanded = true },
                shape = RoundedCornerShape(6.dp),
                color = GearBoardColors.SurfaceVariant,
                border = BorderStroke(
                    width = if (expanded) 1.5.dp else 1.dp,
                    color = if (expanded) accentColor else GearBoardColors.BorderDefault
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selectedLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = GearBoardColors.TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    // Chevron — rotates 180° when expanded
                    val rotation by animateFloatAsState(
                        targetValue = if (expanded) 180f else 0f,
                        label = "chevron"
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = GearBoardColors.TextSecondary,
                        modifier = Modifier
                            .size(16.dp)
                            .rotate(rotation)
                    )
                }
            }

            // Dropdown — renders as overlay, nothing shifts
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(GearBoardColors.Surface)
                    .border(1.dp, GearBoardColors.BorderDefault, RoundedCornerShape(6.dp))
                    .widthIn(min = 160.dp)
            ) {
                positions.forEachIndexed { index, optionLabel ->
                    val isSelected = index == selectedIndex
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = optionLabel,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) accentColor
                                        else GearBoardColors.TextPrimary,
                                maxLines = 1
                            )
                        },
                        onClick = {
                            onPositionSelected(index)
                            expanded = false
                        },
                        leadingIcon = if (isSelected) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = accentColor,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        } else null,
                        modifier = Modifier.background(
                            if (isSelected) accentColor.copy(alpha = 0.1f)
                            else Color.Transparent
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Hint label below button
        Text(
            text = "Tap to change",
            style = MaterialTheme.typography.labelSmall,
            color = GearBoardColors.TextDisabled,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
