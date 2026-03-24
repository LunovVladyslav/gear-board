package com.gearboard.ui.screens.midimap

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gearboard.domain.model.MidiMapping
import com.gearboard.ui.theme.GearBoardColors

@Composable
fun MidiMapScreen(
    viewModel: MidiMapViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val mappings by viewModel.mappings.collectAsStateWithLifecycle()
    val learnState by viewModel.learnState.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(GearBoardColors.Background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "MIDI MAPPING",
                        color = GearBoardColors.Accent,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 3.sp
                    )
                    if (mappings.isNotEmpty()) {
                        IconButton(onClick = { viewModel.deleteAllMappings() }) {
                            Icon(Icons.Default.DeleteSweep, "Clear All", tint = GearBoardColors.DangerText)
                        }
                    }
                }
            }

            // Info text
            item {
                Text(
                    "Assign MIDI CC numbers to controls. Use Learn mode to auto-detect from incoming MIDI.",
                    color = GearBoardColors.TextDisabled,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }

            item { Spacer(Modifier.height(4.dp)) }

            if (mappings.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Sensors, null, tint = GearBoardColors.TextDisabled, modifier = Modifier.size(40.dp))
                        Text("No MIDI mappings", color = GearBoardColors.TextSecondary, fontSize = 14.sp)
                        Text(
                            "Long-press any knob on the Board to start mapping",
                            color = GearBoardColors.TextDisabled,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(mappings, key = { it.id }) { mapping ->
                    MappingCard(
                        mapping = mapping,
                        onLearn = { viewModel.startLearn(mapping.controlId, mapping.controlName) },
                        onDelete = { viewModel.deleteMapping(mapping.controlId) }
                    )
                }
            }
        }

        // Learn mode overlay
        AnimatedVisibility(
            visible = learnState.isActive,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LearnOverlay(
                learnState = learnState,
                onCancel = { viewModel.cancelLearn() }
            )
        }
    }
}

@Composable
private fun MappingCard(
    mapping: MidiMapping,
    onLearn: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(GearBoardColors.Surface)
            .border(1.dp, GearBoardColors.BorderDefault, RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Control name
        Column(modifier = Modifier.weight(1f)) {
            Text(
                mapping.controlName,
                color = GearBoardColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                "Ch ${mapping.channel + 1}",
                color = GearBoardColors.TextDisabled,
                fontSize = 11.sp
            )
        }

        // CC Number badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(GearBoardColors.Accent.copy(alpha = 0.15f))
                .border(1.dp, GearBoardColors.Accent.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "CC ${mapping.ccNumber}",
                color = GearBoardColors.Accent,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        Spacer(Modifier.width(8.dp))

        // Learn button
        IconButton(onClick = onLearn, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Sensors, "Re-learn", tint = GearBoardColors.Accent, modifier = Modifier.size(18.dp))
        }

        // Delete
        IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Delete, "Remove", tint = GearBoardColors.DangerText, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun LearnOverlay(
    learnState: LearnState,
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GearBoardColors.Background.copy(alpha = 0.9f))
            .clickable(enabled = false) { },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            // Countdown ring
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { learnState.remainingSeconds / 10f },
                    modifier = Modifier.size(100.dp),
                    color = if (learnState.detectedCc != null) GearBoardColors.ConnectedUsb else GearBoardColors.Accent,
                    strokeWidth = 4.dp,
                    trackColor = GearBoardColors.SurfaceElevated
                )
                Text(
                    text = if (learnState.detectedCc != null) "CC ${learnState.detectedCc}" else "${learnState.remainingSeconds}",
                    color = if (learnState.detectedCc != null) GearBoardColors.ConnectedUsb else GearBoardColors.Accent,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Title
            Text(
                "MIDI LEARN",
                color = GearBoardColors.Accent,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            // Control name
            Text(
                learnState.controlName,
                color = GearBoardColors.TextPrimary,
                fontSize = 14.sp
            )

            // Instruction
            Text(
                if (learnState.detectedCc != null) "CC ${learnState.detectedCc} detected!" else "Move a knob on your MIDI controller...",
                color = if (learnState.detectedCc != null) GearBoardColors.ConnectedUsb else GearBoardColors.TextSecondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            // Cancel button
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GearBoardColors.SurfaceElevated,
                    contentColor = GearBoardColors.TextPrimary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("CANCEL", fontSize = 12.sp, letterSpacing = 1.sp)
            }
        }
    }
}
