package com.gearboard.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.ui.theme.GearBoardColors
import com.gearboard.ui.theme.GearBoardDimensions

/**
 * GearPresetNav — preset navigation with ← / → buttons.
 * Sends Program Change +1 or -1.
 * Arrow buttons have 48dp minimum touch targets.
 */
@Composable
fun GearPresetNav(
    label: String,
    currentPreset: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current

    Row(
        modifier = modifier
            .height(GearBoardDimensions.ToggleMinHeight)
            .clip(RoundedCornerShape(GearBoardDimensions.RadiusM))
            .background(GearBoardColors.SurfaceElevated)
            .border(
                width = GearBoardDimensions.BorderThin,
                color = GearBoardColors.BorderDefault,
                shape = RoundedCornerShape(GearBoardDimensions.RadiusM)
            )
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Previous button
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "Previous preset",
            tint = GearBoardColors.Accent,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(GearBoardDimensions.RadiusS))
                .clickable {
                    onPrevious()
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                }
                .padding(8.dp)
        )

        // Preset display
        Text(
            text = "${label.uppercase()} ${currentPreset + 1}",
            color = GearBoardColors.TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp)
        )

        // Next button
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Next preset",
            tint = GearBoardColors.Accent,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(GearBoardDimensions.RadiusS))
                .clickable {
                    onNext()
                    view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                }
                .padding(8.dp)
        )
    }
}
