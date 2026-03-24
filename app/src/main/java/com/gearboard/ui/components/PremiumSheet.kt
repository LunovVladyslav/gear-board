package com.gearboard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.ui.theme.GearBoardColors

/**
 * Premium unlock BottomSheet.
 * Displayed when the user hits a freemium limit:
 * - 3 presets max
 * - 1 pedal max
 * - 2 effects max
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumSheet(
    onUpgrade: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = GearBoardColors.Surface,
        contentColor = GearBoardColors.TextPrimary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Default.Star,
                null,
                tint = GearBoardColors.Accent,
                modifier = Modifier.size(48.dp)
            )

            Text(
                "GEARBOARD PRO",
                color = GearBoardColors.Accent,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Text(
                "Unlock the full pedalboard experience",
                color = GearBoardColors.TextSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(4.dp))

            // Features list
            listOf(
                "✓ Unlimited presets",
                "✓ Unlimited pedals",
                "✓ Unlimited effects",
                "✓ Preset export/import",
                "✓ MIDI mapping",
                "✓ Priority support"
            ).forEach { feature ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        feature,
                        color = GearBoardColors.TextPrimary,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onUpgrade,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GearBoardColors.Accent,
                    contentColor = GearBoardColors.TextOnAccent
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "UPGRADE — \$4.99",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            TextButton(onClick = onDismiss) {
                Text("MAYBE LATER", color = GearBoardColors.TextDisabled, fontSize = 12.sp, letterSpacing = 1.sp)
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}
