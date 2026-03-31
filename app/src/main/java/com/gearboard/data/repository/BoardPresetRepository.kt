package com.gearboard.data.repository

import android.content.Context
import android.net.Uri
import com.gearboard.data.presets.AmpliTube5Preset
import com.gearboard.data.presets.BiasFx2Preset
import com.gearboard.data.presets.BlankPreset
import com.gearboard.data.presets.GuitarRig7Preset
import com.gearboard.data.presets.HelixNativePreset
import com.gearboard.data.presets.NeuralDspGenericPreset
import com.gearboard.data.presets.NeuralDspPetrucciXPreset
import com.gearboard.domain.model.BoardPreset
import com.gearboard.domain.model.BoardState
import com.gearboard.domain.model.ControlType
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardPresetRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    /** Built-in presets compiled into the app (read-only). */
    val builtInPresets: List<BoardPreset> = listOf(
        NeuralDspPetrucciXPreset.preset,
        NeuralDspGenericPreset.preset,
        HelixNativePreset.preset,
        AmpliTube5Preset.preset,
        GuitarRig7Preset.preset,
        BiasFx2Preset.preset,
        BlankPreset.preset
    )

    private val userPresetsDir: File
        get() = File(context.filesDir, "board_presets").also { it.mkdirs() }

    /** Load all user-saved board presets from internal storage. */
    suspend fun loadUserPresets(): List<BoardPreset> = withContext(Dispatchers.IO) {
        userPresetsDir.listFiles { f -> f.extension == "gearboard" }
            ?.mapNotNull { file ->
                try {
                    gson.fromJson(file.readText(), BoardPreset::class.java)
                } catch (_: Exception) {
                    null
                }
            }
            ?: emptyList()
    }

    /** Save a user board preset to internal storage. */
    suspend fun saveUserPreset(preset: BoardPreset) = withContext(Dispatchers.IO) {
        val file = File(userPresetsDir, "${preset.id}.gearboard")
        file.writeText(gson.toJson(preset))
    }

    /** Delete a user board preset from internal storage. */
    suspend fun deleteUserPreset(presetId: String) = withContext(Dispatchers.IO) {
        File(userPresetsDir, "$presetId.gearboard").delete()
    }

    /** Export a preset to an external URI as a .gearboard file (JSON). */
    suspend fun exportPreset(preset: BoardPreset, uri: Uri) = withContext(Dispatchers.IO) {
        context.contentResolver.openOutputStream(uri)?.use { stream ->
            stream.write(gson.toJson(preset).toByteArray(Charsets.UTF_8))
        }
    }

    /** Sealed result type for [importPreset]. */
    sealed class PresetImportResult {
        /** Preset was successfully parsed. */
        data class Success(val preset: BoardPreset) : PresetImportResult()
        /** The JSON was malformed or missing required fields. */
        data class ParseError(val message: String) : PresetImportResult()
        /** The file could not be opened or read. */
        data class IoError(val message: String) : PresetImportResult()
    }

    /**
     * Import a preset from an external URI.
     *
     * Returns a [PresetImportResult] instead of throwing, so callers can handle
     * IO failures and JSON parse errors independently without try/catch.
     */
    suspend fun importPreset(uri: Uri): PresetImportResult = withContext(Dispatchers.IO) {
        return@withContext try {
            val json = context.contentResolver.openInputStream(uri)?.use { stream ->
                stream.readBytes().toString(Charsets.UTF_8)
            } ?: return@withContext PresetImportResult.IoError("Cannot open file")
            val preset = try {
                gson.fromJson(json, BoardPreset::class.java)
                    ?: return@withContext PresetImportResult.ParseError("Invalid preset format")
            } catch (e: com.google.gson.JsonSyntaxException) {
                return@withContext PresetImportResult.ParseError(e.message ?: "JSON parse error")
            }
            PresetImportResult.Success(preset)
        } catch (e: java.io.IOException) {
            PresetImportResult.IoError(e.message ?: "IO error")
        }
    }

    // --- CC conflict validation ---

    /**
     * The result of validating a preset import against the current board state.
     *
     * @property preset the preset being imported
     * @property conflicts list of CC number collisions between the preset and the existing board;
     *   empty if there are no conflicts
     */
    data class ImportResult(val preset: BoardPreset, val conflicts: List<CcConflict>)

    /**
     * Describes a CC number collision between an existing board control and an incoming preset control.
     *
     * @property ccNumber the conflicting CC number
     * @property existingLabel the name of the existing block/control that owns this CC
     * @property incomingLabel the label of the incoming preset control that would claim the same CC
     */
    data class CcConflict(val ccNumber: Int, val existingLabel: String, val incomingLabel: String)

    /**
     * Validate a preset-to-be-imported against the current board state.
     *
     * Scans all controls in [currentBoardState] to collect their CC numbers, then checks each
     * control in [preset] for collisions. Returns an [ImportResult] containing any [CcConflict]
     * entries found; an empty [ImportResult.conflicts] list means the preset is safe to apply directly.
     */
    fun validateImport(preset: BoardPreset, currentBoardState: BoardState): ImportResult {
        val existingCCs = mutableMapOf<Int, String>() // cc -> block/control label

        fun collectCCs(controls: List<ControlType>, blockLabel: String) {
            controls.forEach { ctrl ->
                if (ctrl.ccNumber > 0) existingCCs[ctrl.ccNumber] = blockLabel
            }
        }

        currentBoardState.pedals.forEach { collectCCs(it.controls, it.name) }
        currentBoardState.effects.forEach { collectCCs(it.controls, it.name) }
        currentBoardState.ampBlocks.forEach { collectCCs(it.controls, it.name) }
        currentBoardState.cabBlocks.forEach { collectCCs(it.controls, it.name) }

        val conflicts = mutableListOf<CcConflict>()
        preset.sections.forEach { section ->
            section.blocks.forEach { block ->
                block.controls.forEach { control ->
                    val cc = control.ccNumber
                    if (cc > 0 && existingCCs.containsKey(cc)) {
                        conflicts.add(CcConflict(cc, existingCCs[cc]!!, control.label))
                    }
                }
            }
        }
        return ImportResult(preset, conflicts)
    }
}
