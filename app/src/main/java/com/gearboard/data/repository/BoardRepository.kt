package com.gearboard.data.repository

import com.gearboard.domain.model.AmpSettings
import com.gearboard.domain.model.BoardState
import com.gearboard.domain.model.CabinetSettings
import com.gearboard.domain.model.ControlBlock
import com.gearboard.domain.model.ControlType
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

    // --- Pedals (ControlBlock) ---

    fun addPedalBlock(block: ControlBlock) {
        _boardState.value = _boardState.value.copy(
            pedals = _boardState.value.pedals + block
        )
    }

    fun removePedalBlock(blockId: String) {
        _boardState.value = _boardState.value.copy(
            pedals = _boardState.value.pedals.filter { it.id != blockId }
        )
    }

    fun updatePedalBlock(block: ControlBlock) {
        _boardState.value = _boardState.value.copy(
            pedals = _boardState.value.pedals.map {
                if (it.id == block.id) block else it
            }
        )
    }

    fun renamePedalBlock(blockId: String, newName: String) {
        _boardState.value = _boardState.value.copy(
            pedals = _boardState.value.pedals.map {
                if (it.id == blockId) it.copy(name = newName) else it
            }
        )
    }

    // --- Amp ---

    fun updateAmp(amp: AmpSettings) {
        _boardState.value = _boardState.value.copy(amp = amp)
    }

    fun addAmpControl(control: ControlType) {
        val amp = _boardState.value.amp
        _boardState.value = _boardState.value.copy(
            amp = amp.copy(controls = amp.controls + control)
        )
    }

    fun removeAmpControl(controlId: String) {
        val amp = _boardState.value.amp
        _boardState.value = _boardState.value.copy(
            amp = amp.copy(controls = amp.controls.filter { it.id != controlId })
        )
    }

    fun updateAmpControl(controlId: String, updatedControl: ControlType) {
        val amp = _boardState.value.amp
        _boardState.value = _boardState.value.copy(
            amp = amp.copy(controls = amp.controls.map {
                if (it.id == controlId) updatedControl else it
            })
        )
    }

    fun clearAmpControls() {
        val amp = _boardState.value.amp
        _boardState.value = _boardState.value.copy(
            amp = amp.copy(controls = emptyList())
        )
    }

    // --- Cabinet ---

    fun updateCabinet(cabinet: CabinetSettings) {
        _boardState.value = _boardState.value.copy(cabinet = cabinet)
    }

    fun addCabControl(control: ControlType) {
        val cab = _boardState.value.cabinet
        _boardState.value = _boardState.value.copy(
            cabinet = cab.copy(controls = cab.controls + control)
        )
    }

    fun removeCabControl(controlId: String) {
        val cab = _boardState.value.cabinet
        _boardState.value = _boardState.value.copy(
            cabinet = cab.copy(controls = cab.controls.filter { it.id != controlId })
        )
    }

    fun updateCabControl(controlId: String, updatedControl: ControlType) {
        val cab = _boardState.value.cabinet
        _boardState.value = _boardState.value.copy(
            cabinet = cab.copy(controls = cab.controls.map {
                if (it.id == controlId) updatedControl else it
            })
        )
    }

    fun clearCabControls() {
        val cab = _boardState.value.cabinet
        _boardState.value = _boardState.value.copy(
            cabinet = cab.copy(controls = emptyList())
        )
    }

    // --- Effects (ControlBlock) ---

    fun addEffectBlock(block: ControlBlock) {
        _boardState.value = _boardState.value.copy(
            effects = _boardState.value.effects + block
        )
    }

    fun removeEffectBlock(blockId: String) {
        _boardState.value = _boardState.value.copy(
            effects = _boardState.value.effects.filter { it.id != blockId }
        )
    }

    fun updateEffectBlock(block: ControlBlock) {
        _boardState.value = _boardState.value.copy(
            effects = _boardState.value.effects.map {
                if (it.id == block.id) block else it
            }
        )
    }

    fun renameEffectBlock(blockId: String, newName: String) {
        _boardState.value = _boardState.value.copy(
            effects = _boardState.value.effects.map {
                if (it.id == blockId) it.copy(name = newName) else it
            }
        )
    }

    // --- Block-level control operations ---

    fun addControlToBlock(sectionIsPedals: Boolean, blockId: String, control: ControlType) {
        if (sectionIsPedals) {
            _boardState.value = _boardState.value.copy(
                pedals = _boardState.value.pedals.map { block ->
                    if (block.id == blockId) block.copy(controls = block.controls + control)
                    else block
                }
            )
        } else {
            _boardState.value = _boardState.value.copy(
                effects = _boardState.value.effects.map { block ->
                    if (block.id == blockId) block.copy(controls = block.controls + control)
                    else block
                }
            )
        }
    }

    fun removeControlFromBlock(sectionIsPedals: Boolean, blockId: String, controlId: String) {
        if (sectionIsPedals) {
            _boardState.value = _boardState.value.copy(
                pedals = _boardState.value.pedals.map { block ->
                    if (block.id == blockId) block.copy(controls = block.controls.filter { it.id != controlId })
                    else block
                }
            )
        } else {
            _boardState.value = _boardState.value.copy(
                effects = _boardState.value.effects.map { block ->
                    if (block.id == blockId) block.copy(controls = block.controls.filter { it.id != controlId })
                    else block
                }
            )
        }
    }

    fun updateControlInBlock(sectionIsPedals: Boolean, blockId: String, controlId: String, updatedControl: ControlType) {
        if (sectionIsPedals) {
            _boardState.value = _boardState.value.copy(
                pedals = _boardState.value.pedals.map { block ->
                    if (block.id == blockId) block.copy(controls = block.controls.map {
                        if (it.id == controlId) updatedControl else it
                    }) else block
                }
            )
        } else {
            _boardState.value = _boardState.value.copy(
                effects = _boardState.value.effects.map { block ->
                    if (block.id == blockId) block.copy(controls = block.controls.map {
                        if (it.id == controlId) updatedControl else it
                    }) else block
                }
            )
        }
    }

    fun reorderControlsInBlock(sectionIsPedals: Boolean, blockId: String, reorderedControls: List<ControlType>) {
        if (sectionIsPedals) {
            _boardState.value = _boardState.value.copy(
                pedals = _boardState.value.pedals.map { block ->
                    if (block.id == blockId) block.copy(controls = reorderedControls)
                    else block
                }
            )
        } else {
            _boardState.value = _boardState.value.copy(
                effects = _boardState.value.effects.map { block ->
                    if (block.id == blockId) block.copy(controls = reorderedControls)
                    else block
                }
            )
        }
    }

    // --- Full state ---

    fun loadBoardState(state: BoardState) {
        _boardState.value = state
    }

    fun getCurrentState(): BoardState = _boardState.value
}
