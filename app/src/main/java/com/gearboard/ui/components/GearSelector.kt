package com.gearboard.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.ui.theme.GearBoardColors
import com.gearboard.ui.theme.GearBoardDimensions

/**
 * GearSelector — horizontal row of position buttons.
 * Active position has amber background, others are dim.
 * If positions > 4, the row becomes horizontally scrollable.
 *
 * CC value distribution: value = (index * 127) / (positions.size - 1)
 */
@Composable
fun GearSelector(
    label: String,
    positions: List<String>,
    selectedIndex: Int,
    onPositionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Label
        if (label.isNotEmpty()) {
            Text(
                text = label.uppercase(),
                color = GearBoardColors.TextSecondary,
                fontSize = 10.sp,
                letterSpacing = 1.5.sp,
                textAlign = TextAlign.Center
            )
        }

        // Position buttons
        val scrollState = rememberScrollState()
        val rowModifier = if (positions.size > 4) {
            Modifier.horizontalScroll(scrollState)
        } else {
            Modifier
        }

        Row(
            modifier = rowModifier
                .clip(RoundedCornerShape(GearBoardDimensions.RadiusM))
                .border(
                    width = GearBoardDimensions.BorderThin,
                    color = GearBoardColors.BorderDefault,
                    shape = RoundedCornerShape(GearBoardDimensions.RadiusM)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            positions.forEachIndexed { index, posLabel ->
                val isSelected = index == selectedIndex
                val bgColor = if (isSelected) GearBoardColors.Accent else GearBoardColors.SurfaceElevated
                val txtColor = if (isSelected) GearBoardColors.TextOnAccent else GearBoardColors.TextPrimary

                Text(
                    text = posLabel.uppercase(),
                    color = txtColor,
                    fontSize = 10.sp,
                    letterSpacing = 0.5.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = if (index == 0) GearBoardDimensions.RadiusM else 0.dp,
                                bottomStart = if (index == 0) GearBoardDimensions.RadiusM else 0.dp,
                                topEnd = if (index == positions.lastIndex) GearBoardDimensions.RadiusM else 0.dp,
                                bottomEnd = if (index == positions.lastIndex) GearBoardDimensions.RadiusM else 0.dp
                            )
                        )
                        .background(bgColor)
                        .clickable {
                            onPositionSelected(index)
                            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                        }
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                )
            }
        }
    }
}
