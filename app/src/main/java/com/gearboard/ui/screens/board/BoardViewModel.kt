package com.gearboard.ui.screens.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gearboard.data.repository.BoardRepository
import com.gearboard.data.repository.SettingsRepository
import com.gearboard.domain.model.AmpSettings
import com.gearboard.domain.model.BoardState
import com.gearboard.domain.model.CabinetSettings
import com.gearboard.domain.model.ControlParam
import com.gearboard.domain.model.Effect
import com.gearboard.domain.model.Pedal
import com.gearboard.domain.model.TapButton
import com.gearboard.domain.model.ToggleButton
import com.gearboard.midi.GearBoardMidiManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val boardRepository: BoardRepository,
    private val midiManager: GearBoardMidiManager,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val boardState: StateFlow<BoardState> = boardRepository.boardState

    val controlSize = settingsRepository.controlSize
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1.0f)

    // Section expanded states
    private val _pedalsExpanded = MutableStateFlow(true)
    val pedalsExpanded: StateFlow<Boolean> = _pedalsExpanded.asStateFlow()

    private val _ampExpanded = MutableStateFlow(true)
    val ampExpanded: StateFlow<Boolean> = _ampExpanded.asStateFlow()

    private val _cabExpanded = MutableStateFlow(true)
    val cabExpanded: StateFlow<Boolean> = _cabExpanded.asStateFlow()

    private val _effectsExpanded = MutableStateFlow(true)
    val effectsExpanded: StateFlow<Boolean> = _effectsExpanded.asStateFlow()

    // Add sheet visibility
    private val _showAddPedalSheet = MutableStateFlow(false)
    val showAddPedalSheet: StateFlow<Boolean> = _showAddPedalSheet.asStateFlow()

    private val _showAddEffectSheet = MutableStateFlow(false)
    val showAddEffectSheet: StateFlow<Boolean> = _showAddEffectSheet.asStateFlow()

    // --- Section expand/collapse ---
    fun togglePedalsExpanded() { _pedalsExpanded.value = !_pedalsExpanded.value }
    fun toggleAmpExpanded() { _ampExpanded.value = !_ampExpanded.value }
    fun toggleCabExpanded() { _cabExpanded.value = !_cabExpanded.value }
    fun toggleEffectsExpanded() { _effectsExpanded.value = !_effectsExpanded.value }

    // --- Pedals ---
    fun addPedal(pedal: Pedal) {
        boardRepository.addPedal(pedal)
        _showAddPedalSheet.value = false
    }

    fun removePedal(pedalId: String) {
        boardRepository.removePedal(pedalId)
    }

    fun togglePedalEnabled(pedalId: String) {
        val state = boardRepository.getCurrentState()
        state.pedals.find { it.id == pedalId }?.let { pedal ->
            val newEnabled = !pedal.enabled
            boardRepository.updatePedal(pedal.copy(enabled = newEnabled))
            // Send CC: ON=127, OFF=0 (CC 104-109 = unreserved range for pedal toggles)
            val pedalIndex = state.pedals.indexOf(pedal)
            val ccNum = 104 + pedalIndex
            if (ccNum in 0..127) {
                midiManager.sendControlChange(ccNum, if (newEnabled) 127 else 0)
            }
        }
    }

    fun updatePedalControl(pedalId: String, controlId: String, value: Float) {
        val state = boardRepository.getCurrentState()
        state.pedals.find { it.id == pedalId }?.let { pedal ->
            val updatedControls = pedal.controls.map { ctrl ->
                if (ctrl.id == controlId) {
                    val newCtrl = ctrl.copy(value = value)
                    // Send MIDI CC if mapped
                    if (newCtrl.ccNumber >= 0) {
                        val midiValue = (value * 127).toInt().coerceIn(0, 127)
                        midiManager.sendControlChange(newCtrl.ccNumber, midiValue)
                    }
                    newCtrl
                } else ctrl
            }
            boardRepository.updatePedal(pedal.copy(controls = updatedControls))
        }
    }

    fun togglePedalButton(pedalId: String, buttonId: String) {
        val state = boardRepository.getCurrentState()
        state.pedals.find { it.id == pedalId }?.let { pedal ->
            val updatedButtons = pedal.toggleButtons.map { btn ->
                if (btn.id == buttonId) {
                    val newBtn = btn.copy(enabled = !btn.enabled)
                    if (newBtn.ccNumber >= 0) {
                        midiManager.sendControlChange(newBtn.ccNumber, if (newBtn.enabled) 127 else 0)
                    }
                    newBtn
                } else btn
            }
            boardRepository.updatePedal(pedal.copy(toggleButtons = updatedButtons))
        }
    }

    // --- Amp ---
    fun toggleAmpEnabled() {
        val amp = boardRepository.getCurrentState().amp
        val newEnabled = !amp.enabled
        boardRepository.updateAmp(amp.copy(enabled = newEnabled))
        // Send CC 102: ON=127, OFF=0
        midiManager.sendControlChange(102, if (newEnabled) 127 else 0)
    }

    fun updateAmpControl(controlId: String, value: Float) {
        val amp = boardRepository.getCurrentState().amp
        val updatedControls = amp.controls.map { ctrl ->
            if (ctrl.id == controlId) {
                val newCtrl = ctrl.copy(value = value)
                if (newCtrl.ccNumber >= 0) {
                    midiManager.sendControlChange(newCtrl.ccNumber, (value * 127).toInt().coerceIn(0, 127))
                }
                newCtrl
            } else ctrl
        }
        boardRepository.updateAmp(amp.copy(controls = updatedControls))
    }

    // --- Cabinet ---
    fun toggleCabEnabled() {
        val cab = boardRepository.getCurrentState().cabinet
        val newEnabled = !cab.enabled
        boardRepository.updateCabinet(cab.copy(enabled = newEnabled))
        // Send CC 103: ON=127, OFF=0
        midiManager.sendControlChange(103, if (newEnabled) 127 else 0)
    }

    fun updateCabControl(controlId: String, value: Float) {
        val cab = boardRepository.getCurrentState().cabinet
        val updatedControls = cab.controls.map { ctrl ->
            if (ctrl.id == controlId) {
                val newCtrl = ctrl.copy(value = value)
                if (newCtrl.ccNumber >= 0) {
                    midiManager.sendControlChange(newCtrl.ccNumber, (value * 127).toInt().coerceIn(0, 127))
                }
                newCtrl
            } else ctrl
        }
        boardRepository.updateCabinet(cab.copy(controls = updatedControls))
    }

    // --- Effects ---
    fun addEffect(effect: Effect) {
        boardRepository.addEffect(effect)
        _showAddEffectSheet.value = false
    }

    fun removeEffect(effectId: String) {
        boardRepository.removeEffect(effectId)
    }

    fun toggleEffectEnabled(effectId: String) {
        val state = boardRepository.getCurrentState()
        state.effects.find { it.id == effectId }?.let { effect ->
            val newEnabled = !effect.enabled
            boardRepository.updateEffect(effect.copy(enabled = newEnabled))
            // Send CC: ON=127, OFF=0 (CC 110-115 = unreserved range for effect toggles)
            val effectIndex = state.effects.indexOf(effect)
            val ccNum = 110 + effectIndex
            if (ccNum in 0..127) {
                midiManager.sendControlChange(ccNum, if (newEnabled) 127 else 0)
            }
        }
    }

    fun updateEffectControl(effectId: String, controlId: String, value: Float) {
        val state = boardRepository.getCurrentState()
        state.effects.find { it.id == effectId }?.let { effect ->
            val updatedControls = effect.controls.map { ctrl ->
                if (ctrl.id == controlId) {
                    val newCtrl = ctrl.copy(value = value)
                    if (newCtrl.ccNumber >= 0) {
                        midiManager.sendControlChange(newCtrl.ccNumber, (value * 127).toInt().coerceIn(0, 127))
                    }
                    newCtrl
                } else ctrl
            }
            boardRepository.updateEffect(effect.copy(controls = updatedControls))
        }
    }

    // --- Sheets ---
    fun showAddPedalSheet() { _showAddPedalSheet.value = true }
    fun hideAddPedalSheet() { _showAddPedalSheet.value = false }
    fun showAddEffectSheet() { _showAddEffectSheet.value = true }
    fun hideAddEffectSheet() { _showAddEffectSheet.value = false }
}
