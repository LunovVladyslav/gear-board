package com.gearboard.ui.screens.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.domain.model.BlockAppearance
import com.gearboard.domain.model.BlockLayout
import com.gearboard.domain.model.HeaderStyle
import com.gearboard.ui.theme.GearBoardColors

private val PREDEFINED_COLORS = listOf(
    0xFFE8A020, // GearBoard Amber (Default)
    0xFFF44336, // Red
    0xFF4CAF50, // Green
    0xFF2196F3, // Blue
    0xFF9C27B0, // Purple
    0xFF00BCD4, // Cyan
    0xFFFF9800, // Orange
    0xFF8BC34A  // Light Green
)

@Composable
fun CustomizeBlockDialog(
    blockName: String,
    initialAppearance: BlockAppearance,
    initialLayout: BlockLayout,
    onConfirm: (BlockAppearance, BlockLayout) -> Unit,
    onDismiss: () -> Unit
) {
    var appearance by remember { mutableStateOf(initialAppearance) }
    var layoutMode by remember { mutableStateOf(initialLayout) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GearBoardColors.Surface,
        titleContentColor = GearBoardColors.Accent,
        title = {
            Text(
                "CUSTOMIZE: ${blockName.uppercase()}",
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                fontSize = 16.sp
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Color Palette
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "ACCENT COLOR",
                        color = GearBoardColors.TextSecondary,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PREDEFINED_COLORS.take(4).forEach { colorVal ->
                            ColorCircle(
                                color = colorVal,
                                isSelected = appearance.accentColor == colorVal,
                                onClick = { appearance = appearance.copy(accentColor = colorVal) }
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PREDEFINED_COLORS.takeLast(4).forEach { colorVal ->
                            ColorCircle(
                                color = colorVal,
                                isSelected = appearance.accentColor == colorVal,
                                onClick = { appearance = appearance.copy(accentColor = colorVal) }
                            )
                        }
                    }
                }

                // Header Style
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "HEADER STYLE",
                        color = GearBoardColors.TextSecondary,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        HeaderStyle.entries.forEach { style ->
                            val isSel = appearance.headerStyle == style
                            Text(
                                text = style.name,
                                color = if (isSel) GearBoardColors.TextOnAccent else GearBoardColors.TextPrimary,
                                fontSize = 11.sp,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSel) GearBoardColors.Accent else GearBoardColors.SurfaceElevated)
                                    .clickable { appearance = appearance.copy(headerStyle = style) }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                // Layout Mode
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "LAYOUT MODE",
                        color = GearBoardColors.TextSecondary,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        BlockLayout.entries.forEach { mode ->
                            val isSel = layoutMode == mode
                            Text(
                                text = mode.name,
                                color = if (isSel) GearBoardColors.TextOnAccent else GearBoardColors.TextPrimary,
                                fontSize = 11.sp,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSel) GearBoardColors.Accent else GearBoardColors.SurfaceElevated)
                                    .clickable { layoutMode = mode }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(appearance, layoutMode) }
            ) {
                Text(
                    "SAVE",
                    color = GearBoardColors.Accent,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = GearBoardColors.TextSecondary)
            }
        }
    )
}

@Composable
private fun ColorCircle(
    color: Long,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(color))
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) Color.White else Color.Transparent,
                shape = CircleShape
            )
            .clickable { onClick() }
    )
}
