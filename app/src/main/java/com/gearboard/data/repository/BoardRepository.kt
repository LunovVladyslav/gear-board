package com.gearboard.data.repository

import com.gearboard.domain.model.AmpSettings
import com.gearboard.domain.model.BoardState
import com.gearboard.domain.model.CabinetSettings
import com.gearboard.domain.model.Effect
import com.gearboard.domain.model.Pedal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * BoardRepository — manages the current Board state in memory.
 * This is the live state of the board, separate from persisted presets.
 */
@Singleton
class BoardRepository @Inject constructor() {

    private val _boardState = MutableStateFlow(BoardState())
    val boardState: StateFlow<BoardState> = _boardState.asStateFlow()

    // --- Pedals ---

    fun addPedal(pedal: Pedal) {
        _boardState.value = _boardState.value.copy(
            pedals = _boardState.value.pedals + pedal
        )
    }

    fun removePedal(pedalId: String) {
        _boardState.value = _boardState.value.copy(
            pedals = _boardState.value.pedals.filter { it.id != pedalId }
        )
    }

    fun updatePedal(pedal: Pedal) {
        _boardState.value = _boardState.value.copy(
            pedals = _boardState.value.pedals.map {
                if (it.id == pedal.id) pedal else it
            }
        )
    }

    // --- Amp ---

    fun updateAmp(amp: AmpSettings) {
        _boardState.value = _boardState.value.copy(amp = amp)
    }

    // --- Cabinet ---

    fun updateCabinet(cabinet: CabinetSettings) {
        _boardState.value = _boardState.value.copy(cabinet = cabinet)
    }

    // --- Effects ---

    fun addEffect(effect: Effect) {
        _boardState.value = _boardState.value.copy(
            effects = _boardState.value.effects + effect
        )
    }

    fun removeEffect(effectId: String) {
        _boardState.value = _boardState.value.copy(
            effects = _boardState.value.effects.filter { it.id != effectId }
        )
    }

    fun updateEffect(effect: Effect) {
        _boardState.value = _boardState.value.copy(
            effects = _boardState.value.effects.map {
                if (it.id == effect.id) effect else it
            }
        )
    }

    // --- Full state ---

    fun loadBoardState(state: BoardState) {
        _boardState.value = state
    }

    fun getCurrentState(): BoardState = _boardState.value
}
