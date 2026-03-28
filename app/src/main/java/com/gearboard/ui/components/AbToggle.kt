package com.gearboard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.domain.model.AbSlot
import com.gearboard.ui.theme.GearBoardColors

/**
 * AbToggle — compact two-slot A/B toggle for block headers.
 *
 * ```
 * [A|B]
 * ```
 *
 * Selected slot is highlighted amber, the other is dimmed.
 */
@Composable
fun AbToggle(
    currentSlot: AbSlot,
    onSlotSelected: (AbSlot) -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = com.gearboard.ui.theme.LocalAccentColor.current
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(GearBoardColors.SurfaceElevated)
    ) {
        AbSlot.entries.forEach { slot ->
            val isSelected = slot == currentSlot
            Text(
                text = slot.name,
                color = if (isSelected) GearBoardColors.TextOnAccent else GearBoardColors.TextDisabled,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isSelected) accentColor else GearBoardColors.SurfaceElevated)
                    .clickable { onSlotSelected(slot) }
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
    }
}
