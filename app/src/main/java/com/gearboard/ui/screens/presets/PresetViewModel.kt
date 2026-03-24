package com.gearboard.ui.screens.presets

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gearboard.data.repository.BoardRepository
import com.gearboard.data.repository.PresetRepository
import com.gearboard.data.repository.SettingsRepository
import com.gearboard.domain.model.BoardState
import com.gearboard.domain.model.Preset
import com.gearboard.midi.GearBoardMidiManager
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PresetViewModel @Inject constructor(
    private val presetRepository: PresetRepository,
    private val boardRepository: BoardRepository,
    private val midiManager: GearBoardMidiManager,
    private val settingsRepository: SettingsRepository,
    private val gson: Gson
) : ViewModel() {

    val presets = presetRepository.getAllPresets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isPremium = settingsRepository.isPremium
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _selectedPreset = MutableStateFlow<Preset?>(null)
    val selectedPreset: StateFlow<Preset?> = _selectedPreset.asStateFlow()

    private val _showSaveDialog = MutableStateFlow(false)
    val showSaveDialog: StateFlow<Boolean> = _showSaveDialog.asStateFlow()

    private val _showPremiumSheet = MutableStateFlow(false)
    val showPremiumSheet: StateFlow<Boolean> = _showPremiumSheet.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    companion object {
        const val FREE_PRESET_LIMIT = 3
    }

    /**
     * Load a preset into the board and send Program Change.
     */
    fun loadPreset(preset: Preset) {
        boardRepository.loadBoardState(preset.boardState)
        _selectedPreset.value = preset
        midiManager.sendProgramChange(preset.program)
        _toastMessage.value = "Loaded: ${preset.name}"
    }

    /**
     * Save the current board state as a new preset.
     */
    fun saveNewPreset(name: String, bank: Int = 0, program: Int = 0) {
        viewModelScope.launch {
            val count = presetRepository.getPresetCount()
            val premium = settingsRepository.isPremium.first()

            if (!premium && count >= FREE_PRESET_LIMIT) {
                _showPremiumSheet.value = true
                return@launch
            }

            val preset = Preset(
                name = name,
                bank = bank,
                program = program,
                boardState = boardRepository.getCurrentState()
            )
            val id = presetRepository.savePreset(preset)
            _toastMessage.value = "Saved: $name"
            _showSaveDialog.value = false
        }
    }

    /**
     * Overwrite an existing preset with the current board state.
     */
    fun overwritePreset(preset: Preset) {
        viewModelScope.launch {
            val updated = preset.copy(
                boardState = boardRepository.getCurrentState(),
                updatedAt = System.currentTimeMillis()
            )
            presetRepository.savePreset(updated)
            _selectedPreset.value = updated
            _toastMessage.value = "Updated: ${preset.name}"
        }
    }

    /**
     * Delete a preset.
     */
    fun deletePreset(preset: Preset) {
        viewModelScope.launch {
            presetRepository.deletePreset(preset)
            if (_selectedPreset.value?.id == preset.id) {
                _selectedPreset.value = null
            }
            _toastMessage.value = "Deleted: ${preset.name}"
        }
    }

    /**
     * Export a preset as JSON string (for SAF file writing).
     */
    fun exportPresetJson(preset: Preset): String {
        return gson.toJson(
            mapOf(
                "name" to preset.name,
                "bank" to preset.bank,
                "program" to preset.program,
                "boardState" to preset.boardState,
                "exportedAt" to System.currentTimeMillis()
            )
        )
    }

    /**
     * Import a preset from JSON string (from SAF file reading).
     */
    fun importPresetFromJson(json: String) {
        viewModelScope.launch {
            try {
                val count = presetRepository.getPresetCount()
                val premium = settingsRepository.isPremium.first()

                if (!premium && count >= FREE_PRESET_LIMIT) {
                    _showPremiumSheet.value = true
                    return@launch
                }

                val map = gson.fromJson(json, Map::class.java) as Map<String, Any>
                val name = map["name"] as? String ?: "Imported Preset"
                val bank = (map["bank"] as? Double)?.toInt() ?: 0
                val program = (map["program"] as? Double)?.toInt() ?: 0
                val boardStateJson = gson.toJson(map["boardState"])
                val boardState = gson.fromJson(boardStateJson, BoardState::class.java)

                val preset = Preset(
                    name = name,
                    bank = bank,
                    program = program,
                    boardState = boardState
                )
                presetRepository.savePreset(preset)
                _toastMessage.value = "Imported: $name"
            } catch (e: Exception) {
                _toastMessage.value = "Import failed: ${e.message}"
            }
        }
    }

    fun showSaveDialog() { _showSaveDialog.value = true }
    fun hideSaveDialog() { _showSaveDialog.value = false }
    fun hidePremiumSheet() { _showPremiumSheet.value = false }
    fun clearToast() { _toastMessage.value = null }
}
