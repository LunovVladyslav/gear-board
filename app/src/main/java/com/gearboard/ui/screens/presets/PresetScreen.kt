package com.gearboard.ui.screens.presets

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gearboard.domain.model.Preset
import com.gearboard.ui.components.GearBoardTopBar
import com.gearboard.ui.theme.GearBoardColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PresetScreen(
    viewModel: PresetViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val presets by viewModel.presets.collectAsStateWithLifecycle()
    val selectedPreset by viewModel.selectedPreset.collectAsStateWithLifecycle()
    val showSaveDialog by viewModel.showSaveDialog.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

    // SAF: export preset to JSON file
    var presetToExport by remember { mutableStateOf<Preset?>(null) }
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null && presetToExport != null) {
            try {
                val json = viewModel.exportPresetJson(presetToExport!!)
                context.contentResolver.openOutputStream(uri)?.use { it.write(json.toByteArray()) }
                Toast.makeText(context, "Exported successfully", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Export failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
        presetToExport = null
    }

    // SAF: import preset from JSON file
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                val json = context.contentResolver.openInputStream(uri)?.bufferedReader()?.readText()
                if (json != null) {
                    viewModel.importPresetFromJson(json)
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Import failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Show toast messages
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    Scaffold(
        topBar = {
            GearBoardTopBar(actions = {
                IconButton(onClick = { viewModel.showSaveDialog() }) {
                    Icon(Icons.Default.Add, "New Preset", tint = GearBoardColors.Accent)
                }
                IconButton(onClick = { importLauncher.launch(arrayOf("application/json")) }) {
                    Icon(Icons.Default.Download, "Import", tint = GearBoardColors.TextSecondary)
                }
            })
        }
    ) { innerPadding ->
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GearBoardColors.Background)
            .padding(innerPadding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Preset count (first content item — large "PRESETS" title removed)
        item {
            Text(
                "${presets.size} preset${if (presets.size != 1) "s" else ""}",
                color = GearBoardColors.TextDisabled,
                fontSize = 12.sp
            )
        }

        if (presets.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Save, null, tint = GearBoardColors.TextDisabled, modifier = Modifier.size(40.dp))
                    Text("No presets saved yet", color = GearBoardColors.TextSecondary, fontSize = 14.sp)
                    Text("Configure your board and save a preset", color = GearBoardColors.TextDisabled, fontSize = 12.sp, textAlign = TextAlign.Center)
                }
            }
        } else {
            items(presets, key = { it.id }) { preset ->
                PresetCard(
                    preset = preset,
                    isSelected = selectedPreset?.id == preset.id,
                    onLoad = { viewModel.loadPreset(preset) },
                    onOverwrite = { viewModel.overwritePreset(preset) },
                    onExport = {
                        presetToExport = preset
                        exportLauncher.launch("${preset.name}.json")
                    },
                    onDelete = { viewModel.deletePreset(preset) }
                )
            }
        }
    }

    // Save dialog
    if (showSaveDialog) {
        SavePresetDialog(
            onSave = { name, bank, program -> viewModel.saveNewPreset(name, bank, program) },
            onDismiss = { viewModel.hideSaveDialog() }
        )
    }
    }
}

@Composable
private fun PresetCard(
    preset: Preset,
    isSelected: Boolean,
    onLoad: () -> Unit,
    onOverwrite: () -> Unit,
    onExport: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) GearBoardColors.SurfaceVariant else GearBoardColors.Surface)
            .border(
                width = 1.dp,
                color = if (isSelected) GearBoardColors.Accent else GearBoardColors.BorderDefault,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onLoad)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Selection indicator
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(GearBoardColors.Accent)
            )
            Spacer(Modifier.width(12.dp))
        }

        // Preset info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = preset.name,
                color = if (isSelected) GearBoardColors.Accent else GearBoardColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Bank ${preset.bank} / PC ${preset.program}",
                    color = GearBoardColors.TextDisabled,
                    fontSize = 11.sp
                )
                Text(
                    dateFormat.format(Date(preset.updatedAt)),
                    color = GearBoardColors.TextDisabled,
                    fontSize = 11.sp
                )
            }
        }

        // Actions
        Row {
            if (isSelected) {
                IconButton(onClick = onOverwrite, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Save, "Overwrite", tint = GearBoardColors.Accent, modifier = Modifier.size(18.dp))
                }
            }
            IconButton(onClick = onExport, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Upload, "Export", tint = GearBoardColors.TextSecondary, modifier = Modifier.size(18.dp))
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Delete, "Delete", tint = GearBoardColors.DangerText, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun SavePresetDialog(
    onSave: (name: String, bank: Int, program: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var bank by remember { mutableIntStateOf(0) }
    var program by remember { mutableIntStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GearBoardColors.Surface,
        titleContentColor = GearBoardColors.Accent,
        textContentColor = GearBoardColors.TextPrimary,
        title = {
            Text("SAVE PRESET", letterSpacing = 2.sp, fontSize = 16.sp)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Preset Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GearBoardColors.Accent,
                        unfocusedBorderColor = GearBoardColors.BorderDefault,
                        focusedLabelColor = GearBoardColors.Accent,
                        cursorColor = GearBoardColors.Accent
                    )
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = bank.toString(),
                        onValueChange = { bank = it.toIntOrNull() ?: 0 },
                        label = { Text("Bank") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GearBoardColors.Accent,
                            unfocusedBorderColor = GearBoardColors.BorderDefault,
                            focusedLabelColor = GearBoardColors.Accent,
                            cursorColor = GearBoardColors.Accent
                        )
                    )
                    OutlinedTextField(
                        value = program.toString(),
                        onValueChange = { program = it.toIntOrNull() ?: 0 },
                        label = { Text("Program") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GearBoardColors.Accent,
                            unfocusedBorderColor = GearBoardColors.BorderDefault,
                            focusedLabelColor = GearBoardColors.Accent,
                            cursorColor = GearBoardColors.Accent
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank()) onSave(name.trim(), bank, program) },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GearBoardColors.Accent,
                    contentColor = GearBoardColors.TextOnAccent
                )
            ) {
                Text("SAVE")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = GearBoardColors.TextSecondary)
            }
        }
    )
}
