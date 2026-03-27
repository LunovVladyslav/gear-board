package com.gearboard.ui.screens.setup

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gearboard.ui.screens.board.OnboardingTemplate
import com.gearboard.ui.theme.GearBoardColors

/**
 * GuidedSetupScreen — full-screen wizard for first-time users.
 *
 * Step 0: Plugin selection
 * Step 1: Layout preview
 * Step 2: Guided mapping
 * Step 3: Completion
 */
@Composable
fun GuidedSetupScreen(
    onNavigateToBoard: () -> Unit,
    viewModel: GuidedSetupViewModel = hiltViewModel()
) {
    val currentStep by viewModel.currentStep.collectAsStateWithLifecycle()
    val mappingControls by viewModel.mappingControls.collectAsStateWithLifecycle()
    val currentMappingIndex by viewModel.currentMappingIndex.collectAsStateWithLifecycle()
    val mappingCountdown by viewModel.mappingCountdown.collectAsStateWithLifecycle()
    val isMappingActive by viewModel.isMappingActive.collectAsStateWithLifecycle()
    val navigateToBoard by viewModel.navigateToBoard.collectAsStateWithLifecycle()

    // Navigate when signaled
    LaunchedEffect(navigateToBoard) {
        if (navigateToBoard) {
            onNavigateToBoard()
        }
    }

    // BackHandler: show confirmation instead of leaving
    var showExitConfirm by remember { mutableStateOf(false) }
    BackHandler {
        if (currentStep > 0) {
            viewModel.goBack()
        } else {
            showExitConfirm = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GearBoardColors.Background)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            "GEARBOARD",
            color = GearBoardColors.Accent,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 4.sp
        )
        Spacer(Modifier.height(4.dp))

        // Step indicator dots
        StepIndicator(currentStep = currentStep, totalSteps = 4)

        Spacer(Modifier.height(16.dp))

        // Step content
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                (fadeIn() + slideInHorizontally { it / 3 }) togetherWith
                        (fadeOut() + slideOutHorizontally { -it / 3 })
            },
            modifier = Modifier.weight(1f),
            label = "wizard_step"
        ) { step ->
            when (step) {
                0 -> PluginSelectionStep(
                    onSelectTemplate = { viewModel.selectTemplate(it) },
                    onBuildFromScratch = { viewModel.selectTemplate(null) }
                )
                1 -> LayoutPreviewStep(
                    templateName = viewModel.selectedTemplate.value?.displayName() ?: "",
                    mappingControls = mappingControls,
                    onStartMapping = { viewModel.startMapping() },
                    onBack = { viewModel.goBack() }
                )
                2 -> GuidedMappingStep(
                    mappingControls = mappingControls,
                    currentIndex = currentMappingIndex,
                    countdown = mappingCountdown,
                    isActive = isMappingActive,
                    onStartMapping = { viewModel.startMappingControl() },
                    onNext = { viewModel.markCurrentMapped() },
                    onSkip = { viewModel.skipControl() }
                )
                3 -> CompletionStep(
                    totalControls = mappingControls.size,
                    mappedCount = viewModel.mappedCount,
                    skippedCount = viewModel.skippedCount,
                    onStartPlaying = { viewModel.completeSetup() }
                )
            }
        }
    }

    // Exit confirmation dialog
    if (showExitConfirm) {
        AlertDialog(
            onDismissRequest = { showExitConfirm = false },
            containerColor = GearBoardColors.Surface,
            title = {
                Text("Leave Setup?", color = GearBoardColors.Accent, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Your board will be empty. You can always run setup again from Settings.",
                    color = GearBoardColors.TextPrimary
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showExitConfirm = false
                    viewModel.selectTemplate(null) // exits to empty Board
                }) {
                    Text("LEAVE", color = GearBoardColors.DangerText, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitConfirm = false }) {
                    Text("STAY", color = GearBoardColors.Accent)
                }
            }
        )
    }
}

// --- Step Indicator ---

@Composable
private fun StepIndicator(currentStep: Int, totalSteps: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until totalSteps) {
            val isActive = i <= currentStep
            Box(
                modifier = Modifier
                    .size(if (i == currentStep) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(
                        if (isActive) GearBoardColors.Accent
                        else GearBoardColors.SurfaceElevated
                    )
            )
        }
    }
}

// --- Step 0: Plugin Selection ---

@Composable
private fun PluginSelectionStep(
    onSelectTemplate: (OnboardingTemplate) -> Unit,
    onBuildFromScratch: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            "Which plugin are you using?",
            color = GearBoardColors.TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            "Choose a template to get started quickly",
            color = GearBoardColors.TextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        TemplateCard(
            emoji = "🎸",
            title = "Guitar Amp Sim",
            description = "Overdrive · Amp · Cab · Delay\n8 key controls for guided mapping",
            onClick = { onSelectTemplate(OnboardingTemplate.GUITAR_AMP_SIM) }
        )

        TemplateCard(
            emoji = "🎸",
            title = "Bass Amp Sim",
            description = "Overdrive · Amp · Cab\n6 key controls for guided mapping",
            onClick = { onSelectTemplate(OnboardingTemplate.BASS_AMP_SIM) }
        )

        TemplateCard(
            emoji = "🎸",
            title = "Multi-FX Board",
            description = "Comp · Drive · Chorus · Delay · Reverb · Amp\n8 key controls for guided mapping",
            onClick = { onSelectTemplate(OnboardingTemplate.MULTI_FX_BOARD) }
        )

        TemplateCard(
            emoji = "⚙️",
            title = "Build from Scratch",
            description = "Start with an empty board and add controls manually",
            isOutlined = true,
            onClick = onBuildFromScratch
        )
    }
}

@Composable
private fun TemplateCard(
    emoji: String,
    title: String,
    description: String,
    isOutlined: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .then(
                if (isOutlined)
                    Modifier.border(1.dp, GearBoardColors.BorderDefault, RoundedCornerShape(12.dp))
                        .background(GearBoardColors.Background)
                else
                    Modifier.background(GearBoardColors.SurfaceElevated)
                        .border(1.dp, GearBoardColors.BorderAccent.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(emoji, fontSize = 28.sp)
        Spacer(Modifier.width(14.dp))
        Column {
            Text(
                title.uppercase(),
                color = if (isOutlined) GearBoardColors.TextPrimary else GearBoardColors.Accent,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
            Text(
                description,
                color = GearBoardColors.TextSecondary,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}

// --- Step 1: Layout Preview ---

@Composable
private fun LayoutPreviewStep(
    templateName: String,
    mappingControls: List<MappingItem>,
    onStartMapping: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(Modifier.height(8.dp))

        Text(
            "Your Board is Ready",
            color = GearBoardColors.TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Template: $templateName",
            color = GearBoardColors.Accent,
            fontSize = 13.sp,
            letterSpacing = 1.sp
        )

        Spacer(Modifier.height(4.dp))

        // Show controls that need mapping
        Text(
            "CONTROLS TO MAP",
            color = GearBoardColors.TextSecondary,
            fontSize = 10.sp,
            letterSpacing = 1.5.sp,
            modifier = Modifier.fillMaxWidth()
        )

        mappingControls.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(GearBoardColors.SurfaceElevated)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "!",
                        color = GearBoardColors.Accent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(GearBoardColors.Accent.copy(alpha = 0.15f)),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        item.label,
                        color = GearBoardColors.TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    "CC: —",
                    color = GearBoardColors.TextDisabled,
                    fontSize = 11.sp
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            "All CC numbers are unassigned.\nThe next step will guide you through mapping each control.",
            color = GearBoardColors.TextSecondary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onStartMapping,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = GearBoardColors.Accent,
                contentColor = GearBoardColors.TextOnAccent
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                "START GUIDED MAPPING →",
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                "← CHOOSE DIFFERENT TEMPLATE",
                color = GearBoardColors.TextSecondary,
                fontSize = 12.sp
            )
        }
    }
}

// --- Step 2: Guided Mapping ---

@Composable
private fun GuidedMappingStep(
    mappingControls: List<MappingItem>,
    currentIndex: Int,
    countdown: Int,
    isActive: Boolean,
    onStartMapping: () -> Unit,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    if (currentIndex >= mappingControls.size) return

    val currentItem = mappingControls[currentIndex]
    val progress = (currentIndex + 1).toFloat() / mappingControls.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Progress
        Text(
            "Step ${currentIndex + 1} of ${mappingControls.size}",
            color = GearBoardColors.TextSecondary,
            fontSize = 12.sp,
            letterSpacing = 1.sp
        )

        val animatedProgress by animateFloatAsState(
            targetValue = progress,
            label = "mapping_progress"
        )
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = GearBoardColors.Accent,
            trackColor = GearBoardColors.SurfaceElevated
        )

        Spacer(Modifier.height(8.dp))

        // Control name with highlight
        Text(
            "Map: ${currentItem.label}",
            color = GearBoardColors.Accent,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        // Instructions box
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(GearBoardColors.SurfaceElevated)
                .border(1.dp, GearBoardColors.BorderAccent.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "In your plugin:",
                color = GearBoardColors.TextSecondary,
                fontSize = 12.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("→ ", color = GearBoardColors.Accent, fontWeight = FontWeight.Bold)
                Text(
                    "Click MIDI Learn on ${currentItem.label}",
                    color = GearBoardColors.TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(
                "Then tap the button below to send a CC signal:",
                color = GearBoardColors.TextSecondary,
                fontSize = 12.sp
            )

            // Large highlighted control preview
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(GearBoardColors.Accent.copy(alpha = 0.15f))
                    .border(2.dp, GearBoardColors.Accent, CircleShape)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    currentItem.label.take(4).uppercase(),
                    color = GearBoardColors.Accent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        // Mapping button / countdown
        if (!isActive) {
            Button(
                onClick = onStartMapping,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GearBoardColors.Accent,
                    contentColor = GearBoardColors.TextOnAccent
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.MusicNote, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    "SEND CC SIGNAL",
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        } else {
            // Active: countdown
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Timer,
                        null,
                        tint = GearBoardColors.Accent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "Waiting for MIDI Learn…  ${countdown}s",
                        color = GearBoardColors.Accent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    "Turn the knob in your plugin now",
                    color = GearBoardColors.TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        // Next / Skip buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onSkip,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("SKIP", color = GearBoardColors.TextSecondary, letterSpacing = 1.sp)
            }

            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GearBoardColors.SurfaceElevated,
                    contentColor = GearBoardColors.Accent
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("NEXT →", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }
        }

        // Previously mapped controls
        AnimatedVisibility(visible = currentIndex > 0) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Spacer(Modifier.height(4.dp))
                Text(
                    "MAPPED",
                    color = GearBoardColors.TextDisabled,
                    fontSize = 9.sp,
                    letterSpacing = 1.5.sp
                )
                mappingControls.take(currentIndex).forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(0.6f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (item.isMapped) Icons.Default.Check else Icons.Default.Settings,
                            null,
                            tint = if (item.isMapped) GearBoardColors.Accent else GearBoardColors.TextDisabled,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            item.label,
                            color = if (item.isMapped) GearBoardColors.TextPrimary else GearBoardColors.TextDisabled,
                            fontSize = 12.sp
                        )
                        if (!item.isMapped) {
                            Text(
                                " — skipped",
                                color = GearBoardColors.TextDisabled,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- Step 3: Completion ---

@Composable
private fun CompletionStep(
    totalControls: Int,
    mappedCount: Int,
    skippedCount: Int,
    onStartPlaying: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Success icon
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(GearBoardColors.Accent.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Complete",
                tint = GearBoardColors.Accent,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            "Setup Complete!",
            color = GearBoardColors.TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "$mappedCount/$totalControls controls mapped" +
                    if (skippedCount > 0) "\n$skippedCount skipped" else "",
            color = GearBoardColors.TextSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "You can always remap controls\nby tapping the ! badge on any unmapped control",
            color = GearBoardColors.TextDisabled,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onStartPlaying,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GearBoardColors.Accent,
                contentColor = GearBoardColors.TextOnAccent
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "START PLAYING →",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }
    }
}

// --- Helpers ---

private fun OnboardingTemplate.displayName(): String = when (this) {
    OnboardingTemplate.GUITAR_AMP_SIM -> "Guitar Amp Sim"
    OnboardingTemplate.BASS_AMP_SIM -> "Bass Amp Sim"
    OnboardingTemplate.MULTI_FX_BOARD -> "Multi-FX Board"
}
