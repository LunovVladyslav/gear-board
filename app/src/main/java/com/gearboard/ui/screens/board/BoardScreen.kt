package com.gearboard.ui.screens.board

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gearboard.ui.components.SectionHeader
import com.gearboard.ui.theme.GearBoardColors

/**
 * BoardScreen — the main performance screen.
 *
 * Displays 4 collapsible sections:
 * 1. Pedals (horizontal scroll)
 * 2. Amp (knob row)
 * 3. Cabinet (knob row)
 * 4. Effects (vertical list)
 *
 * Full-sensor orientation recommended for this screen.
 */
@Composable
fun BoardScreen(
    viewModel: BoardViewModel = hiltViewModel()
) {
    val boardState by viewModel.boardState.collectAsStateWithLifecycle()
    val controlScale by viewModel.controlSize.collectAsStateWithLifecycle()
    val pedalsExpanded by viewModel.pedalsExpanded.collectAsStateWithLifecycle()
    val ampExpanded by viewModel.ampExpanded.collectAsStateWithLifecycle()
    val cabExpanded by viewModel.cabExpanded.collectAsStateWithLifecycle()
    val effectsExpanded by viewModel.effectsExpanded.collectAsStateWithLifecycle()
    val showAddPedalSheet by viewModel.showAddPedalSheet.collectAsStateWithLifecycle()
    val showAddEffectSheet by viewModel.showAddEffectSheet.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GearBoardColors.Background),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // === PEDALS SECTION ===
        item {
            SectionHeader(
                title = "Pedals",
                enabled = boardState.pedals.any { it.enabled },
                onToggleEnabled = {
                    // Toggle all pedals
                    boardState.pedals.forEach { viewModel.togglePedalEnabled(it.id) }
                },
                expanded = pedalsExpanded,
                onToggleExpanded = { viewModel.togglePedalsExpanded() },
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Column {
                    if (boardState.pedals.isEmpty()) {
                        EmptySectionHint("No pedals added yet")
                    } else {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(boardState.pedals, key = { it.id }) { pedal ->
                                PedalCard(
                                    pedal = pedal,
                                    onToggleEnabled = { viewModel.togglePedalEnabled(pedal.id) },
                                    onControlChange = { ctrlId, value -> viewModel.updatePedalControl(pedal.id, ctrlId, value) },
                                    onToggleButton = { btnId -> viewModel.togglePedalButton(pedal.id, btnId) },
                                    onRemove = { viewModel.removePedal(pedal.id) },
                                    controlScale = controlScale
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    AddButton("ADD PEDAL") { viewModel.showAddPedalSheet() }
                }
            }
        }

        // === AMP SECTION ===
        item {
            SectionHeader(
                title = "Amplifier",
                enabled = boardState.amp.enabled,
                onToggleEnabled = { viewModel.toggleAmpEnabled() },
                expanded = ampExpanded,
                onToggleExpanded = { viewModel.toggleAmpExpanded() },
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                AmpSection(
                    amp = boardState.amp,
                    onControlChange = { ctrlId, value -> viewModel.updateAmpControl(ctrlId, value) },
                    controlScale = controlScale
                )
            }
        }

        // === CABINET SECTION ===
        item {
            SectionHeader(
                title = "Cabinet",
                enabled = boardState.cabinet.enabled,
                onToggleEnabled = { viewModel.toggleCabEnabled() },
                expanded = cabExpanded,
                onToggleExpanded = { viewModel.toggleCabExpanded() },
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                CabSection(
                    cabinet = boardState.cabinet,
                    onControlChange = { ctrlId, value -> viewModel.updateCabControl(ctrlId, value) },
                    controlScale = controlScale
                )
            }
        }

        // === EFFECTS SECTION ===
        item {
            SectionHeader(
                title = "Effects",
                enabled = boardState.effects.any { it.enabled },
                onToggleEnabled = {
                    boardState.effects.forEach { viewModel.toggleEffectEnabled(it.id) }
                },
                expanded = effectsExpanded,
                onToggleExpanded = { viewModel.toggleEffectsExpanded() },
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (boardState.effects.isEmpty()) {
                        EmptySectionHint("No effects added yet")
                    } else {
                        boardState.effects.forEach { effect ->
                            EffectCard(
                                effect = effect,
                                onToggleEnabled = { viewModel.toggleEffectEnabled(effect.id) },
                                onControlChange = { ctrlId, value -> viewModel.updateEffectControl(effect.id, ctrlId, value) },
                                onRemove = { viewModel.removeEffect(effect.id) },
                                controlScale = controlScale
                            )
                        }
                    }

                    AddButton("ADD EFFECT") { viewModel.showAddEffectSheet() }
                }
            }
        }
    }

    // Bottom sheets
    if (showAddPedalSheet) {
        AddPedalSheet(
            onAddPedal = { viewModel.addPedal(it) },
            onDismiss = { viewModel.hideAddPedalSheet() }
        )
    }

    if (showAddEffectSheet) {
        AddEffectSheet(
            onAddEffect = { viewModel.addEffect(it) },
            onDismiss = { viewModel.hideAddEffectSheet() }
        )
    }
}

@Composable
private fun EmptySectionHint(text: String) {
    Text(
        text = text,
        color = GearBoardColors.TextDisabled,
        fontSize = 12.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun AddButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = GearBoardColors.SurfaceElevated,
            contentColor = GearBoardColors.Accent
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
        Spacer(Modifier.padding(start = 8.dp))
        Text(text, fontSize = 12.sp, letterSpacing = 1.sp)
    }
}
