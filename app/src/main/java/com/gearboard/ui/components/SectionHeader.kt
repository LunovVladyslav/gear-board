package com.gearboard.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.ui.theme.GearBoardColors
import com.gearboard.ui.theme.GearBoardDimensions

/**
 * SectionHeader — collapsible section with on/off indicator.
 *
 * From the mock: amber dot (on/off), section name, chevron for collapse/expand.
 * Tap on dot = toggle section on/off.
 * Tap on chevron = collapse/expand content.
 *
 * @param title Section title (displayed uppercase)
 * @param enabled Whether the section is active (amber dot)
 * @param onToggleEnabled Toggle section on/off
 * @param expanded Whether section content is visible
 * @param onToggleExpanded Toggle collapse/expand
 * @param content Section body content
 */
@Composable
fun SectionHeader(
    title: String,
    enabled: Boolean,
    onToggleEnabled: () -> Unit,
    expanded: Boolean,
    onToggleExpanded: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(GearBoardDimensions.RadiusM))
            .background(GearBoardColors.Surface)
            .border(
                width = GearBoardDimensions.BorderThin,
                color = GearBoardColors.BorderDefault,
                shape = RoundedCornerShape(GearBoardDimensions.RadiusM)
            )
    ) {
        // Header bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(GearBoardColors.Surface, GearBoardColors.SurfaceVariant)
                    )
                )
                .border(
                    width = GearBoardDimensions.BorderThin,
                    color = GearBoardColors.BorderDefault,
                    shape = RoundedCornerShape(
                        topStart = GearBoardDimensions.RadiusM,
                        topEnd = GearBoardDimensions.RadiusM,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // On/off indicator dot
                Box(
                    modifier = Modifier
                        .size(GearBoardDimensions.SectionDotSize)
                        .clip(CircleShape)
                        .background(
                            if (enabled) GearBoardColors.OnIndicator else GearBoardColors.OffIndicator
                        )
                        .then(
                            if (enabled) {
                                Modifier.shadow(
                                    elevation = 4.dp,
                                    shape = CircleShape,
                                    ambientColor = GearBoardColors.AccentGlow,
                                    spotColor = GearBoardColors.AccentGlow
                                )
                            } else Modifier
                        )
                        .clickable { onToggleEnabled() }
                )

                // Section title
                Text(
                    text = title.uppercase(),
                    color = GearBoardColors.TextPrimary,
                    fontSize = 14.sp,
                    letterSpacing = 3.sp
                )
            }

            // Expand/collapse chevron
            IconButton(onClick = onToggleExpanded) {
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = GearBoardColors.TextSecondary
                )
            }
        }

        // Collapsible content
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                content()
            }
        }
    }
}
