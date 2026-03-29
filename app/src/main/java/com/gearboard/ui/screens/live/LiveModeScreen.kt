package com.gearboard.ui.screens.live

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gearboard.domain.model.BarEvent
import com.gearboard.domain.model.LiveSession
import com.gearboard.domain.model.LiveState
import com.gearboard.domain.model.Preset
import com.gearboard.domain.model.SyncMode
import com.gearboard.domain.model.TimeSignature
import com.gearboard.ui.components.GearBoardTopBar
import com.gearboard.ui.theme.GearBoardColors

@Composable
fun LiveModeScreen(
    viewModel: LiveModeViewModel = hiltViewModel()
) {
    val sessions by viewModel.sessions.collectAsStateWithLifecycle()
    val currentSession by viewModel.currentSession.collectAsStateWithLifecycle()
    val barEvents by viewModel.barEvents.collectAsStateWithLifecycle()
    val liveState by viewModel.liveState.collectAsStateWithLifecycle()
    val syncMode by viewModel.syncMode.collectAsStateWithLifecycle()
    val showBleWarning by viewModel.showBleWarning.collectAsStateWithLifecycle()
    val presets by viewModel.presets.collectAsStateWithLifecycle()

    var showNewSessionDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            GearBoardTopBar(actions = {
                IconButton(onClick = { showNewSessionDialog = true }) {
                    Icon(Icons.Default.Add, "New Session", tint = GearBoardColors.Accent)
                }
            })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GearBoardColors.Background)
                .padding(innerPadding)
        ) {
            if (currentSession == null) {
                // Session list
                SessionListContent(
                    sessions = sessions,
                    barEvents = barEvents,
                    onSelectSession = { viewModel.selectSession(it) },
                    onDeleteSession = { viewModel.deleteSession(it) },
                    onNewSession = { showNewSessionDialog = true }
                )
            } else {
                // Timeline
                TimelineContent(
                    session = currentSession!!,
                    barEvents = barEvents,
                    liveState = liveState,
                    syncMode = syncMode,
                    presets = presets,
                    onBack = { viewModel.clearCurrentSession() },
                    onSetSyncMode = { viewModel.setSyncMode(it) },
                    onStart = { viewModel.startStandalone() },
                    onStop = { viewModel.stopPlayback() },
                    onReset = { viewModel.resetPlayback() },
                    onBarTap = { /* handled inside */ },
                    onSetPresetAtBar = { bar, presetId -> viewModel.setPresetAtBar(bar, presetId) },
                    onSetBpmAtBar = { bar, bpm -> viewModel.setBpmAtBar(bar, bpm) },
                    onSetTimeSigAtBar = { bar, ts -> viewModel.setTimeSignatureAtBar(bar, ts) },
                    onClearBar = { viewModel.clearBar(it) }
                )
            }

            // BLE warning banner
            AnimatedVisibility(
                visible = showBleWarning,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 8.dp)
            ) {
                BleLatencyWarning(onDismiss = { viewModel.dismissBleWarning() })
            }
        }
    }

    if (showNewSessionDialog) {
        NewSessionDialog(
            onDismiss = { showNewSessionDialog = false },
            onCreate = { name, bpm, ts, bars ->
                viewModel.createSession(name, bpm, ts, bars)
                showNewSessionDialog = false
            }
        )
    }
}

// ─── Session List ──────────────────────────────────────────────────────────────

@Composable
private fun SessionListContent(
    sessions: List<LiveSession>,
    barEvents: List<BarEvent>,
    onSelectSession: (LiveSession) -> Unit,
    onDeleteSession: (LiveSession) -> Unit,
    onNewSession: () -> Unit
) {
    if (sessions.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("No live sessions yet", color = GearBoardColors.TextSecondary, fontSize = 14.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                "Create a session to assign presets to bars",
                color = GearBoardColors.TextDisabled,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onNewSession,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GearBoardColors.Accent,
                    contentColor = GearBoardColors.TextOnAccent
                )
            ) {
                Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("NEW SESSION", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sessions, key = { it.id }) { session ->
                SessionCard(
                    session = session,
                    eventCount = 0, // simplified — full count would need separate state
                    onTap = { onSelectSession(session) },
                    onDelete = { onDeleteSession(session) }
                )
            }
        }
    }
}

@Composable
private fun SessionCard(
    session: LiveSession,
    eventCount: Int,
    onTap: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(GearBoardColors.Surface)
            .border(1.dp, GearBoardColors.BorderDefault, RoundedCornerShape(8.dp))
            .clickable(onClick = onTap)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = session.name,
                color = GearBoardColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${session.initialBpm.toInt()} BPM · " +
                        "${session.initialTimeSignature.numerator}/${session.initialTimeSignature.denominator} · " +
                        "${session.totalBars} bars",
                color = GearBoardColors.TextDisabled,
                fontSize = 11.sp
            )
        }
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                Icons.Default.Delete,
                "Delete",
                tint = GearBoardColors.DangerText,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

// ─── Timeline ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimelineContent(
    session: LiveSession,
    barEvents: List<BarEvent>,
    liveState: LiveState,
    syncMode: SyncMode,
    presets: List<Preset>,
    onBack: () -> Unit,
    onSetSyncMode: (SyncMode) -> Unit,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit,
    onBarTap: (Int) -> Unit,
    onSetPresetAtBar: (Int, Long?) -> Unit,
    onSetBpmAtBar: (Int, Float?) -> Unit,
    onSetTimeSigAtBar: (Int, TimeSignature?) -> Unit,
    onClearBar: (Int) -> Unit
) {
    var selectedBar by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header row: back + session name + sync toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text("← Back", color = GearBoardColors.TextSecondary, fontSize = 12.sp)
            }
            Text(
                text = session.name,
                color = GearBoardColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            SyncModeToggle(
                mode = syncMode,
                onToggle = {
                    onSetSyncMode(if (syncMode == SyncMode.STANDALONE) SyncMode.SYNCED else SyncMode.STANDALONE)
                }
            )
        }

        // Horizontal bar timeline
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                for (bar in 1..session.totalBars) {
                    val event = barEvents.firstOrNull { it.barNumber == bar }
                    val preset = presets.firstOrNull { it.id == event?.presetId }
                    BarCell(
                        barNumber = bar,
                        event = event,
                        preset = preset,
                        isActive = liveState.isPlaying && liveState.currentBar == bar,
                        onTap = { selectedBar = bar }
                    )
                }
            }
        }

        // Status row: bar / beat indicator / BPM / time sig
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            val barLabel = if (liveState.currentBar > 0)
                "Bar ${liveState.currentBar} / ${session.totalBars}"
            else "—"
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(barLabel, color = GearBoardColors.TextSecondary, fontSize = 12.sp)
                BeatDots(
                    beat = liveState.currentBeat,
                    total = liveState.currentTimeSignature.numerator
                )
                Text(
                    "${liveState.currentBpm.toInt()} BPM",
                    color = GearBoardColors.TextSecondary,
                    fontSize = 12.sp
                )
                Text(
                    "${liveState.currentTimeSignature.numerator}/${liveState.currentTimeSignature.denominator}",
                    color = GearBoardColors.TextSecondary,
                    fontSize = 12.sp
                )
            }

            val activePreset = presets.firstOrNull { it.id == liveState.currentPresetId }
            if (activePreset != null) {
                Text(
                    "Preset: ${activePreset.name}",
                    color = GearBoardColors.Accent,
                    fontSize = 12.sp
                )
            }

            // Synced mode status
            if (syncMode == SyncMode.SYNCED) {
                val statusText = when {
                    liveState.isPlaying -> "Synced · ${liveState.detectedBpm?.toInt()} BPM"
                    else -> "Waiting for DAW clock..."
                }
                Text(statusText, color = GearBoardColors.TextDisabled, fontSize = 11.sp)
            }
        }

        // Transport controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Reset
            Button(
                onClick = onReset,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GearBoardColors.SurfaceVariant,
                    contentColor = GearBoardColors.TextPrimary
                ),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Replay, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("RESET", fontSize = 11.sp)
            }
            // Play / Stop
            Button(
                onClick = if (liveState.isPlaying) onStop else onStart,
                enabled = syncMode == SyncMode.STANDALONE,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (liveState.isPlaying) GearBoardColors.DangerText else GearBoardColors.Accent,
                    contentColor = if (liveState.isPlaying) GearBoardColors.TextPrimary else GearBoardColors.TextOnAccent
                ),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    if (liveState.isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                    null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    if (liveState.isPlaying) "STOP" else "START",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // Bar edit bottom sheet
    if (selectedBar != null) {
        val bar = selectedBar!!
        val event = barEvents.firstOrNull { it.barNumber == bar }
        BarEditSheet(
            barNumber = bar,
            event = event,
            presets = presets,
            syncMode = syncMode,
            session = session,
            onSetPreset = { presetId -> onSetPresetAtBar(bar, presetId) },
            onSetBpm = { bpm -> onSetBpmAtBar(bar, bpm) },
            onSetTimeSig = { ts -> onSetTimeSigAtBar(bar, ts) },
            onClear = { onClearBar(bar) },
            onDismiss = { selectedBar = null }
        )
    }
}

// ─── Bar Cell ──────────────────────────────────────────────────────────────────

@Composable
fun BarCell(
    barNumber: Int,
    event: BarEvent?,
    preset: Preset?,
    isActive: Boolean,
    onTap: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(56.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(
                if (isActive) GearBoardColors.Accent.copy(alpha = 0.12f)
                else GearBoardColors.Surface
            )
            .border(
                width = if (isActive) 1.5.dp else 0.5.dp,
                color = if (isActive) GearBoardColors.Accent else GearBoardColors.BorderDefault,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(onClick = onTap)
    ) {
        // Amber top accent bar for bars with events
        if (event != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(GearBoardColors.Accent)
                    .align(Alignment.TopCenter)
            )
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Bar number
            Text(
                text = barNumber.toString(),
                color = if (isActive) GearBoardColors.Accent else GearBoardColors.TextDisabled,
                fontSize = 10.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
            )

            // Preset abbreviation
            if (preset != null) {
                Text(
                    text = preset.name.take(3).uppercase(),
                    color = GearBoardColors.Accent,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }

            // BPM change indicator
            if (event?.bpm != null) {
                Text(
                    text = "${event.bpm.toInt()}",
                    color = GearBoardColors.TextSecondary,
                    fontSize = 8.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ─── Beat dots ─────────────────────────────────────────────────────────────────

@Composable
private fun BeatDots(beat: Int, total: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        for (i in 1..total.coerceAtMost(8)) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(
                        if (i <= beat) GearBoardColors.Accent
                        else GearBoardColors.SurfaceVariant
                    )
            )
        }
    }
}

// ─── Sync mode toggle ──────────────────────────────────────────────────────────

@Composable
private fun SyncModeToggle(mode: SyncMode, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(GearBoardColors.SurfaceVariant)
            .clickable(onClick = onToggle)
            .padding(horizontal = 2.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        listOf(SyncMode.STANDALONE, SyncMode.SYNCED).forEach { m ->
            Text(
                text = if (m == SyncMode.STANDALONE) "STANDALONE" else "SYNCED",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = if (mode == m) GearBoardColors.TextOnAccent else GearBoardColors.TextDisabled,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (mode == m) GearBoardColors.Accent else GearBoardColors.SurfaceVariant)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

// ─── Bar Edit Bottom Sheet ────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BarEditSheet(
    barNumber: Int,
    event: BarEvent?,
    presets: List<Preset>,
    syncMode: SyncMode,
    session: LiveSession,
    onSetPreset: (Long?) -> Unit,
    onSetBpm: (Float?) -> Unit,
    onSetTimeSig: (TimeSignature?) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedPresetId by remember(event) { mutableStateOf(event?.presetId) }
    var bpmText by remember(event) { mutableStateOf(event?.bpm?.toInt()?.toString() ?: "") }
    var selectedTs by remember(event) { mutableStateOf(event?.timeSignature ?: session.initialTimeSignature) }
    var showPresetDropdown by remember { mutableStateOf(false) }
    var showTsDropdown by remember { mutableStateOf(false) }

    val commonTimeSigs = listOf(
        TimeSignature(4, 4), TimeSignature(3, 4), TimeSignature(6, 8),
        TimeSignature(7, 8), TimeSignature(5, 4), TimeSignature(5, 8)
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = GearBoardColors.Surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Bar $barNumber",
                color = GearBoardColors.Accent,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                letterSpacing = 2.sp
            )

            // Preset picker
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Preset", color = GearBoardColors.TextSecondary, fontSize = 13.sp, modifier = Modifier.width(80.dp))
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(GearBoardColors.SurfaceVariant)
                            .clickable { showPresetDropdown = true }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .weight(1f, fill = false),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val label = presets.firstOrNull { it.id == selectedPresetId }?.name ?: "None"
                        Text(label, color = GearBoardColors.TextPrimary, fontSize = 13.sp)
                        Text("▾", color = GearBoardColors.TextSecondary, fontSize = 13.sp)
                    }
                    DropdownMenu(
                        expanded = showPresetDropdown,
                        onDismissRequest = { showPresetDropdown = false },
                        modifier = Modifier.background(GearBoardColors.Surface)
                    ) {
                        DropdownMenuItem(
                            text = { Text("None", color = GearBoardColors.TextSecondary) },
                            onClick = { selectedPresetId = null; showPresetDropdown = false }
                        )
                        presets.forEach { preset ->
                            DropdownMenuItem(
                                text = { Text(preset.name, color = GearBoardColors.TextPrimary) },
                                onClick = { selectedPresetId = preset.id; showPresetDropdown = false }
                            )
                        }
                    }
                }
            }

            // BPM field
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("BPM", color = GearBoardColors.TextSecondary, fontSize = 13.sp, modifier = Modifier.width(80.dp))
                OutlinedTextField(
                    value = if (syncMode == SyncMode.SYNCED) "" else bpmText,
                    onValueChange = { bpmText = it },
                    placeholder = {
                        Text(
                            if (syncMode == SyncMode.SYNCED) "Controlled by DAW"
                            else "${session.initialBpm.toInt()} (inherited)",
                            fontSize = 12.sp,
                            color = GearBoardColors.TextDisabled
                        )
                    },
                    enabled = syncMode == SyncMode.STANDALONE,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GearBoardColors.Accent,
                        unfocusedBorderColor = GearBoardColors.BorderDefault,
                        focusedLabelColor = GearBoardColors.Accent,
                        cursorColor = GearBoardColors.Accent,
                        disabledBorderColor = GearBoardColors.BorderDefault,
                        disabledTextColor = GearBoardColors.TextDisabled
                    )
                )
            }

            // Time signature picker
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Time sig", color = GearBoardColors.TextSecondary, fontSize = 13.sp, modifier = Modifier.width(80.dp))
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(GearBoardColors.SurfaceVariant)
                            .clickable { showTsDropdown = true }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("${selectedTs.numerator}/${selectedTs.denominator}", color = GearBoardColors.TextPrimary, fontSize = 13.sp)
                        Text("▾", color = GearBoardColors.TextSecondary, fontSize = 13.sp)
                    }
                    DropdownMenu(
                        expanded = showTsDropdown,
                        onDismissRequest = { showTsDropdown = false },
                        modifier = Modifier.background(GearBoardColors.Surface)
                    ) {
                        commonTimeSigs.forEach { ts ->
                            DropdownMenuItem(
                                text = { Text("${ts.numerator}/${ts.denominator}", color = GearBoardColors.TextPrimary) },
                                onClick = { selectedTs = ts; showTsDropdown = false }
                            )
                        }
                    }
                }
            }

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { onClear(); onDismiss() },
                    colors = ButtonDefaults.textButtonColors(contentColor = GearBoardColors.DangerText)
                ) {
                    Text("Clear bar")
                }
                Button(
                    onClick = {
                        onSetPreset(selectedPresetId)
                        onSetBpm(bpmText.toFloatOrNull())
                        onSetTimeSig(selectedTs)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GearBoardColors.Accent,
                        contentColor = GearBoardColors.TextOnAccent
                    )
                ) {
                    Text("Done")
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

// ─── New Session Dialog ────────────────────────────────────────────────────────

@Composable
private fun NewSessionDialog(
    onDismiss: () -> Unit,
    onCreate: (String, Float, TimeSignature, Int) -> Unit
) {
    var name by remember { mutableStateOf("My Song") }
    var bpmText by remember { mutableStateOf("120") }
    var totalBarsText by remember { mutableStateOf("32") }
    var selectedTs by remember { mutableStateOf(TimeSignature(4, 4)) }
    var showTsDropdown by remember { mutableStateOf(false) }

    val commonTimeSigs = listOf(
        TimeSignature(4, 4), TimeSignature(3, 4), TimeSignature(6, 8),
        TimeSignature(7, 8), TimeSignature(5, 4), TimeSignature(5, 8)
    )

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = GearBoardColors.Accent,
        unfocusedBorderColor = GearBoardColors.BorderDefault,
        focusedLabelColor = GearBoardColors.Accent,
        cursorColor = GearBoardColors.Accent
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GearBoardColors.Surface,
        titleContentColor = GearBoardColors.Accent,
        textContentColor = GearBoardColors.TextPrimary,
        title = { Text("New Live Session", fontSize = 16.sp, letterSpacing = 1.sp) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
                OutlinedTextField(
                    value = bpmText,
                    onValueChange = { bpmText = it },
                    label = { Text("BPM") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
                // Time sig row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Time sig:", color = GearBoardColors.TextSecondary, fontSize = 13.sp, modifier = Modifier.width(80.dp))
                    Box {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(GearBoardColors.SurfaceVariant)
                                .clickable { showTsDropdown = true }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("${selectedTs.numerator}/${selectedTs.denominator}", color = GearBoardColors.TextPrimary, fontSize = 13.sp)
                            Text("▾", color = GearBoardColors.TextSecondary)
                        }
                        DropdownMenu(
                            expanded = showTsDropdown,
                            onDismissRequest = { showTsDropdown = false },
                            modifier = Modifier.background(GearBoardColors.Surface)
                        ) {
                            commonTimeSigs.forEach { ts ->
                                DropdownMenuItem(
                                    text = { Text("${ts.numerator}/${ts.denominator}", color = GearBoardColors.TextPrimary) },
                                    onClick = { selectedTs = ts; showTsDropdown = false }
                                )
                            }
                        }
                    }
                }
                OutlinedTextField(
                    value = totalBarsText,
                    onValueChange = { totalBarsText = it },
                    label = { Text("Total bars") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val bpm = bpmText.toFloatOrNull() ?: 120f
                    val bars = totalBarsText.toIntOrNull()?.coerceAtLeast(1) ?: 32
                    if (name.isNotBlank()) onCreate(name.trim(), bpm, selectedTs, bars)
                },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GearBoardColors.Accent,
                    contentColor = GearBoardColors.TextOnAccent
                )
            ) { Text("Create") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = GearBoardColors.TextSecondary)
            }
        }
    )
}

// ─── Block 5: BLE Latency Warning Banner ─────────────────────────────────────

@Composable
private fun BleLatencyWarning(onDismiss: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(GearBoardColors.SurfaceVariant)
            .border(1.dp, GearBoardColors.Accent.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            Icons.Default.Warning,
            null,
            tint = GearBoardColors.Accent,
            modifier = Modifier.size(18.dp)
        )
        Text(
            "BLE MIDI has ~10–20ms jitter. For tighter sync, use USB MIDI.",
            color = GearBoardColors.TextPrimary,
            fontSize = 11.sp,
            modifier = Modifier.weight(1f),
            lineHeight = 14.sp
        )
        TextButton(
            onClick = onDismiss,
            colors = ButtonDefaults.textButtonColors(contentColor = GearBoardColors.Accent)
        ) {
            Text("Got it", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}
