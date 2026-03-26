package com.gearboard.ui.screens.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.ui.theme.GearBoardColors

enum class OnboardingTemplate {
    GUITAR_AMP_SIM,
    BASS_AMP_SIM
}

/**
 * OnboardingDialog — first-launch dialog for template selection.
 * Offers a quick-start guitar or bass amp sim setup, or an empty board.
 */
@Composable
fun OnboardingDialog(
    onSelectTemplate: (OnboardingTemplate) -> Unit,
    onStartEmpty: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* cannot dismiss */ },
        containerColor = GearBoardColors.Surface,
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(
                    "WELCOME TO",
                    color = GearBoardColors.TextSecondary,
                    fontSize = 10.sp,
                    letterSpacing = 2.sp
                )
                Text(
                    "GEARBOARD",
                    color = GearBoardColors.Accent,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    "Choose a template to get started, or create an empty board.",
                    color = GearBoardColors.TextPrimary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                // Guitar template
                TemplateOption(
                    title = "Guitar Amp Sim",
                    description = "Pedals · Amp · Cab · Effects",
                    onClick = { onSelectTemplate(OnboardingTemplate.GUITAR_AMP_SIM) }
                )

                // Bass template
                TemplateOption(
                    title = "Bass Amp Sim",
                    description = "Pedals · Amp · Cab",
                    onClick = { onSelectTemplate(OnboardingTemplate.BASS_AMP_SIM) }
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onStartEmpty) {
                Text(
                    "START EMPTY",
                    color = GearBoardColors.TextSecondary,
                    letterSpacing = 1.sp
                )
            }
        }
    )
}

@Composable
private fun TemplateOption(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(GearBoardColors.SurfaceElevated)
            .border(1.dp, GearBoardColors.BorderAccent.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Text(
            text = title.uppercase(),
            color = GearBoardColors.Accent,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )
        Text(
            text = description,
            color = GearBoardColors.TextSecondary,
            fontSize = 11.sp
        )
    }
}
