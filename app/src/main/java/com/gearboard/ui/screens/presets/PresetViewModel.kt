package com.gearboard.ui.screens.presets

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gearboard.data.repository.BoardPresetRepository
import com.gearboard.data.repository.BoardRepository
import com.gearboard.data.repository.PresetRepository
import com.gearboard.data.repository.SettingsRepository
import com.gearboard.domain.model.AmpBlock
import com.gearboard.domain.model.BoardPreset
import com.gearboard.domain.model.BoardSection
import com.gearboard.domain.model.BoardState
import com.gearboard.domain.model.CabBlock
import com.gearboard.domain.model.CCAssigner
import com.gearboard.domain.model.ControlBlock
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.Preset
import com.gearboard.domain.model.SectionType
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
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PresetViewModel @Inject constructor(
    private val presetRepository: PresetRepository,
    private val boardPresetRepository: BoardPresetRepository,
    private val boardRepository: BoardRepository,
    private val midiManager: GearBoardMidiManager,
    private val settingsRepository: SettingsRepository,
    private val gson: Gson
) : ViewModel() {

    // --- Existing preset functionality (Room-stored presets) ---
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

    // --- Board Preset functionality ---
    val builtInPresets: List<BoardPreset> = boardPresetRepository.builtInPresets

    private val _userPresets = MutableStateFlow<List<BoardPreset>>(emptyList())
    val userPresets: StateFlow<List<BoardPreset>> = _userPresets.asStateFlow()

    private val _isLoadingUserPresets = MutableStateFlow(true)
    val isLoadingUserPresets: StateFlow<Boolean> = _isLoadingUserPresets.asStateFlow()

    private val _showApplyConfirmDialog = MutableStateFlow<BoardPreset?>(null)
    val showApplyConfirmDialog: StateFlow<BoardPreset?> = _showApplyConfirmDialog.asStateFlow()

    private val _showSaveBoardPresetDialog = MutableStateFlow(false)
    val showSaveBoardPresetDialog: StateFlow<Boolean> = _showSaveBoardPresetDialog.asStateFlow()

    private val _showCcConflictDialog = MutableStateFlow<CcConflictDialogState?>(null)
    val showCcConflictDialog: StateFlow<CcConflictDialogState?> = _showCcConflictDialog.asStateFlow()

    data class CcConflictDialogState(
        val conflicts: List<BoardPresetRepository.CcConflict>,
        val preset: BoardPreset
    )

    companion object {
        const val FREE_PRESET_LIMIT = 3
    }

    init {
        loadUserPresets()
    }

    fun loadUserPresets() {
        viewModelScope.launch {
            _isLoadingUserPresets.value = true
            _userPresets.value = boardPresetRepository.loadUserPresets()
            _isLoadingUserPresets.value = false
        }
    }

    fun showApplyConfirmDialog(preset: BoardPreset) { _showApplyConfirmDialog.value = preset }
    fun dismissApplyConfirmDialog() { _showApplyConfirmDialog.value = null }
    fun showSaveBoardPresetDialog() { _showSaveBoardPresetDialog.value = true }
    fun dismissSaveBoardPresetDialog() { _showSaveBoardPresetDialog.value = false }
    fun dismissCcConflictDialog() { _showCcConflictDialog.value = null }

    fun applyBoardPreset(preset: BoardPreset) {
        viewModelScope.launch {
            val boardState = reconstructBoardState(preset)
            boardRepository.loadBoardState(boardState)
            _showApplyConfirmDialog.value = null
            _toastMessage.value = "Applied: ${preset.name}"
        }
    }

    fun saveCurrentAsBoardPreset(name: String) {
        viewModelScope.launch {
            val state = boardRepository.getCurrentState()
            val preset = BoardPreset(
                id = UUID.randomUUID().toString(),
                name = name,
                targetSoftware = "Custom",
                description = "Saved from current board",
                sections = listOf(
                    BoardSection(SectionType.PEDALS, blocks = state.pedals),
                    BoardSection(SectionType.AMP, blocks = state.ampBlocks.map { it.toControlBlock() }),
                    BoardSection(SectionType.CAB, blocks = state.cabBlocks.map { it.toControlBlock() }),
                    BoardSection(SectionType.EFFECTS, blocks = state.effects)
                )
            )
            boardPresetRepository.saveUserPreset(preset)
            loadUserPresets()
            _showSaveBoardPresetDialog.value = false
            _toastMessage.value = "Saved: $name"
        }
    }

    fun deleteBoardUserPreset(presetId: String) {
        viewModelScope.launch {
            boardPresetRepository.deleteUserPreset(presetId)
            loadUserPresets()
        }
    }

    fun exportBoardPreset(preset: BoardPreset, uri: Uri) {
        viewModelScope.launch {
            try {
                boardPresetRepository.exportPreset(preset, uri)
                _toastMessage.value = "Exported: ${preset.name}"
            } catch (e: Exception) {
                _toastMessage.value = "Export failed: ${e.message}"
            }
        }
    }

    fun importBoardPreset(uri: Uri) {
        viewModelScope.launch {
            when (val result = boardPresetRepository.importPreset(uri)) {
                is BoardPresetRepository.PresetImportResult.IoError -> {
                    _toastMessage.value = "Import failed: ${result.message}"
                }
                is BoardPresetRepository.PresetImportResult.ParseError -> {
                    _toastMessage.value = "Invalid preset file: ${result.message}"
                }
                is BoardPresetRepository.PresetImportResult.Success -> {
                    val preset = result.preset
                    val currentState = boardRepository.getCurrentState()
                    val validation = boardPresetRepository.validateImport(preset, currentState)
                    if (validation.conflicts.isEmpty()) {
                        applyBoardPreset(preset)
                        boardPresetRepository.saveUserPreset(preset)
                        loadUserPresets()
                        _toastMessage.value = "Imported: ${preset.name}"
                    } else {
                        _showCcConflictDialog.value = CcConflictDialogState(validation.conflicts, preset)
                    }
                }
            }
        }
    }

    fun importBoardPresetForceReassign(preset: BoardPreset) {
        viewModelScope.launch {
            val currentState = boardRepository.getCurrentState()
            val usedCCs = collectUsedCCsFromState(currentState)
            val reassigned = reassignPresetCCs(preset, usedCCs)
            applyBoardPreset(reassigned)
            boardPresetRepository.saveUserPreset(reassigned)
            loadUserPresets()
            _showCcConflictDialog.value = null
            _toastMessage.value = "Imported with reassigned CCs: ${preset.name}"
        }
    }

    private fun collectUsedCCsFromState(state: BoardState): MutableSet<Int> {
        return (state.pedals + state.effects)
            .flatMap { it.controls }
            .plus(state.ampBlocks.flatMap { it.controls })
            .plus(state.cabBlocks.flatMap { it.controls })
            .map { it.ccNumber }
            .filter { it > 0 }
            .toMutableSet()
    }

    private fun reassignPresetCCs(preset: BoardPreset, usedCCs: MutableSet<Int>): BoardPreset {
        val newSections = preset.sections.map { section ->
            val sectionName = when (section.type) {
                SectionType.PEDALS -> "pedals"
                SectionType.AMP -> "amp"
                SectionType.CAB -> "cab"
                SectionType.EFFECTS -> "effects"
            }
            val newBlocks = section.blocks.map { block ->
                CCAssigner.assignBlock(block, sectionName, usedCCs)
            }
            section.copy(blocks = newBlocks)
        }
        return preset.copy(id = UUID.randomUUID().toString(), sections = newSections)
    }

    private fun reconstructBoardState(preset: BoardPreset): BoardState {
        val usedCCs = mutableSetOf<Int>()
        var pedals = emptyList<ControlBlock>()
        var ampBlocks = emptyList<AmpBlock>()
        var cabBlocks = emptyList<CabBlock>()
        var effects = emptyList<ControlBlock>()

        preset.sections.forEach { section ->
            when (section.type) {
                SectionType.PEDALS -> {
                    pedals = section.blocks.map { block ->
                        CCAssigner.assignBlock(block, "pedals", usedCCs)
                    }
                }
                SectionType.AMP -> {
                    ampBlocks = section.blocks.map { block ->
                        CCAssigner.assignAmp(
                            AmpBlock(id = block.id, name = block.name, controls = block.controls),
                            usedCCs
                        )
                    }
                }
                SectionType.CAB -> {
                    cabBlocks = section.blocks.map { block ->
                        CCAssigner.assignCab(
                            CabBlock(id = block.id, name = block.name, controls = block.controls),
                            usedCCs
                        )
                    }
                }
                SectionType.EFFECTS -> {
                    effects = section.blocks.map { block ->
                        CCAssigner.assignBlock(block, "effects", usedCCs)
                    }
                }
            }
        }
        return BoardState(pedals = pedals, ampBlocks = ampBlocks, cabBlocks = cabBlocks, effects = effects)
    }

    // --- Existing preset methods ---

    fun loadPreset(preset: Preset) {
        boardRepository.loadBoardState(preset.boardState)
        _selectedPreset.value = preset
        midiManager.sendProgramChange(preset.program)
        _toastMessage.value = "Loaded: ${preset.name}"
    }

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
            presetRepository.savePreset(preset)
            _toastMessage.value = "Saved: $name"
            _showSaveDialog.value = false
        }
    }

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

    fun deletePreset(preset: Preset) {
        viewModelScope.launch {
            presetRepository.deletePreset(preset)
            if (_selectedPreset.value?.id == preset.id) {
                _selectedPreset.value = null
            }
            _toastMessage.value = "Deleted: ${preset.name}"
        }
    }

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

// Extension helpers for converting Amp/Cab blocks to ControlBlock for BoardSection
private fun AmpBlock.toControlBlock() = ControlBlock(id = id, name = name, controls = controls)
private fun CabBlock.toControlBlock() = ControlBlock(id = id, name = name, controls = controls)
