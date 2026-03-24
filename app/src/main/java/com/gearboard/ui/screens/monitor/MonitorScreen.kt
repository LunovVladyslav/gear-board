package com.gearboard.ui.screens.monitor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gearboard.domain.model.MidiDirection
import com.gearboard.domain.model.MidiEvent
import com.gearboard.domain.model.MidiEventType
import com.gearboard.ui.theme.GearBoardColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MonitorScreen(
    viewModel: MonitorViewModel = hiltViewModel()
) {
    val events by viewModel.events.collectAsStateWithLifecycle()
    val isPaused by viewModel.isPaused.collectAsStateWithLifecycle()
    val filterType by viewModel.filterType.collectAsStateWithLifecycle()

    val filteredEvents = remember(events, filterType) {
        if (filterType == null) events
        else events.filter { it.type.name == filterType }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GearBoardColors.Background)
    ) {
        // Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(GearBoardColors.Surface)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "MIDI MONITOR",
                color = GearBoardColors.Accent,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = { viewModel.togglePause() }, modifier = Modifier.size(36.dp)) {
                    Icon(
                        if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                        if (isPaused) "Resume" else "Pause",
                        tint = if (isPaused) GearBoardColors.Accent else GearBoardColors.TextSecondary
                    )
                }
                IconButton(onClick = { viewModel.clearEvents() }, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.Delete, "Clear", tint = GearBoardColors.TextSecondary)
                }
            }
        }

        // Filter chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = filterType == null,
                onClick = { viewModel.setFilter(null) },
                label = { Text("ALL", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = GearBoardColors.Accent,
                    selectedLabelColor = GearBoardColors.TextOnAccent,
                    containerColor = GearBoardColors.SurfaceElevated,
                    labelColor = GearBoardColors.TextSecondary
                )
            )
            listOf("CONTROL_CHANGE", "NOTE_ON", "NOTE_OFF", "PROGRAM_CHANGE", "PITCH_BEND").forEach { type ->
                FilterChip(
                    selected = filterType == type,
                    onClick = { viewModel.setFilter(if (filterType == type) null else type) },
                    label = { Text(type.replace("_", " ").take(8), fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GearBoardColors.Accent,
                        selectedLabelColor = GearBoardColors.TextOnAccent,
                        containerColor = GearBoardColors.SurfaceElevated,
                        labelColor = GearBoardColors.TextSecondary
                    )
                )
            }
        }

        // Event count
        Text(
            "${filteredEvents.size} events${if (isPaused) " (PAUSED)" else ""}",
            color = GearBoardColors.TextDisabled,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
        )

        // Event list
        if (filteredEvents.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.Sensors, null, tint = GearBoardColors.TextDisabled, modifier = Modifier.size(40.dp))
                Spacer(Modifier.height(8.dp))
                Text("No MIDI events", color = GearBoardColors.TextSecondary, fontSize = 14.sp)
                Text("Events will appear here in real-time", color = GearBoardColors.TextDisabled, fontSize = 12.sp, textAlign = TextAlign.Center)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(filteredEvents, key = { "${it.timestamp}_${it.data1}_${it.data2}_${it.direction}" }) { event ->
                    EventRow(event)
                }
            }
        }
    }
}

@Composable
private fun EventRow(event: MidiEvent) {
    val timeFormat = remember { SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()) }

    val typeColor = when (event.type) {
        MidiEventType.CONTROL_CHANGE -> GearBoardColors.Accent
        MidiEventType.NOTE_ON -> GearBoardColors.ConnectedUsb
        MidiEventType.NOTE_OFF -> GearBoardColors.TextDisabled
        MidiEventType.PROGRAM_CHANGE -> GearBoardColors.ConnectedBle
        MidiEventType.PITCH_BEND -> GearBoardColors.AccentHover
        MidiEventType.SYSEX -> GearBoardColors.DangerText
        MidiEventType.UNKNOWN -> GearBoardColors.TextDisabled
    }

    val typeLabel = when (event.type) {
        MidiEventType.CONTROL_CHANGE -> "CC"
        MidiEventType.NOTE_ON -> "ON"
        MidiEventType.NOTE_OFF -> "OFF"
        MidiEventType.PROGRAM_CHANGE -> "PC"
        MidiEventType.PITCH_BEND -> "PB"
        MidiEventType.SYSEX -> "SYS"
        MidiEventType.UNKNOWN -> "???"
    }

    val detail = when (event.type) {
        MidiEventType.CONTROL_CHANGE -> "CC${event.data1} = ${event.data2}"
        MidiEventType.NOTE_ON, MidiEventType.NOTE_OFF -> "Note ${event.data1} Vel ${event.data2}"
        MidiEventType.PROGRAM_CHANGE -> "PC ${event.data1}"
        MidiEventType.PITCH_BEND -> "${(event.data2 shl 7) or event.data1}"
        else -> "${event.data1} ${event.data2}"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(GearBoardColors.Surface)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Direction arrow
        Icon(
            imageVector = if (event.direction == MidiDirection.INCOMING) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
            contentDescription = null,
            tint = if (event.direction == MidiDirection.INCOMING) GearBoardColors.ConnectedBle else GearBoardColors.Accent,
            modifier = Modifier.size(12.dp)
        )

        // Timestamp
        Text(
            timeFormat.format(Date(event.timestamp)),
            color = GearBoardColors.TextDisabled,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
        )

        // Channel
        Text(
            "Ch${event.channel + 1}",
            color = GearBoardColors.TextSecondary,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.width(28.dp)
        )

        // Type badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(3.dp))
                .background(typeColor.copy(alpha = 0.15f))
                .border(0.5.dp, typeColor.copy(alpha = 0.3f), RoundedCornerShape(3.dp))
                .padding(horizontal = 6.dp, vertical = 1.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(typeLabel, color = typeColor, fontSize = 9.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        }

        // Detail
        Text(
            detail,
            color = GearBoardColors.TextPrimary,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.weight(1f)
        )
    }
}
