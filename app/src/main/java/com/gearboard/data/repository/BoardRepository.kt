package com.gearboard.data.repository

import com.gearboard.domain.model.AbSlot
import com.gearboard.domain.model.AmpBlock
import com.gearboard.domain.model.BoardState
import com.gearboard.domain.model.CabBlock
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

    // --- Amp blocks ---

    fun addAmpBlock(block: AmpBlock) {
        _boardState.value = _boardState.value.copy(
            ampBlocks = _boardState.value.ampBlocks + block
        )
    }

    fun removeAmpBlock(blockId: String) {
        _boardState.value = _boardState.value.copy(
            ampBlocks = _boardState.value.ampBlocks.filter { it.id != blockId }
        )
    }

    fun updateAmpBlock(block: AmpBlock) {
        _boardState.value = _boardState.value.copy(
            ampBlocks = _boardState.value.ampBlocks.map {
                if (it.id == block.id) block else it
            }
        )
    }

    fun renameAmpBlock(blockId: String, newName: String) {
        _boardState.value = _boardState.value.copy(
            ampBlocks = _boardState.value.ampBlocks.map {
                if (it.id == blockId) it.copy(name = newName) else it
            }
        )
    }

    fun switchAmpBlockAbSlot(blockId: String, targetSlot: AbSlot) {
        val blocks = _boardState.value.ampBlocks
        val idx = blocks.indexOfFirst { it.id == blockId }
        if (idx < 0) return
        val block = blocks[idx]
        if (block.currentSlot == targetSlot) return

        val currentValues = captureControlValues(block.controls)
        val updatedAbStates = block.abStates + mapOf(block.currentSlot to currentValues)
        val targetValues = updatedAbStates[targetSlot] ?: emptyMap()
        val newControls = applyControlValues(block.controls, targetValues)

        val updatedBlock = block.copy(currentSlot = targetSlot, controls = newControls, abStates = updatedAbStates)
        _boardState.value = _boardState.value.copy(
            ampBlocks = blocks.toMutableList().also { it[idx] = updatedBlock }
        )
    }

    fun addControlToAmpBlock(blockId: String, control: ControlType) {
        _boardState.value = _boardState.value.copy(
            ampBlocks = _boardState.value.ampBlocks.map { block ->
                if (block.id == blockId) block.copy(controls = block.controls + control) else block
            }
        )
    }

    fun removeControlFromAmpBlock(blockId: String, controlId: String) {
        _boardState.value = _boardState.value.copy(
            ampBlocks = _boardState.value.ampBlocks.map { block ->
                if (block.id == blockId) block.copy(controls = block.controls.filter { it.id != controlId })
                else block
            }
        )
    }

    fun updateControlInAmpBlock(blockId: String, controlId: String, updatedControl: ControlType) {
        _boardState.value = _boardState.value.copy(
            ampBlocks = _boardState.value.ampBlocks.map { block ->
                if (block.id == blockId) block.copy(controls = block.controls.map {
                    if (it.id == controlId) updatedControl else it
                }) else block
            }
        )
    }

    // Legacy single-block helpers — operate on first amp block, creating one if needed.

    fun addAmpControl(control: ControlType) {
        val blocks = _boardState.value.ampBlocks
        if (blocks.isEmpty()) {
            _boardState.value = _boardState.value.copy(
                ampBlocks = listOf(AmpBlock(id = "amp_main", name = "Amplifier", controls = listOf(control)))
            )
        } else {
            val updated = blocks[0].copy(controls = blocks[0].controls + control)
            _boardState.value = _boardState.value.copy(ampBlocks = listOf(updated) + blocks.drop(1))
        }
    }

    fun removeAmpControl(controlId: String) {
        val blocks = _boardState.value.ampBlocks
        if (blocks.isEmpty()) return
        val updated = blocks[0].copy(controls = blocks[0].controls.filter { it.id != controlId })
        _boardState.value = _boardState.value.copy(ampBlocks = listOf(updated) + blocks.drop(1))
    }

    fun updateAmpControl(controlId: String, updatedControl: ControlType) {
        val blocks = _boardState.value.ampBlocks
        if (blocks.isEmpty()) return
        val updated = blocks[0].copy(controls = blocks[0].controls.map {
            if (it.id == controlId) updatedControl else it
        })
        _boardState.value = _boardState.value.copy(ampBlocks = listOf(updated) + blocks.drop(1))
    }

    fun clearAmpControls() {
        val blocks = _boardState.value.ampBlocks
        if (blocks.isEmpty()) return
        val updated = blocks[0].copy(controls = emptyList())
        _boardState.value = _boardState.value.copy(ampBlocks = listOf(updated) + blocks.drop(1))
    }

    // --- Cab blocks ---

    fun addCabBlock(block: CabBlock) {
        _boardState.value = _boardState.value.copy(
            cabBlocks = _boardState.value.cabBlocks + block
        )
    }

    fun removeCabBlock(blockId: String) {
        _boardState.value = _boardState.value.copy(
            cabBlocks = _boardState.value.cabBlocks.filter { it.id != blockId }
        )
    }

    fun updateCabBlock(block: CabBlock) {
        _boardState.value = _boardState.value.copy(
            cabBlocks = _boardState.value.cabBlocks.map {
                if (it.id == block.id) block else it
            }
        )
    }

    fun renameCabBlock(blockId: String, newName: String) {
        _boardState.value = _boardState.value.copy(
            cabBlocks = _boardState.value.cabBlocks.map {
                if (it.id == blockId) it.copy(name = newName) else it
            }
        )
    }

    fun switchCabBlockAbSlot(blockId: String, targetSlot: AbSlot) {
        val blocks = _boardState.value.cabBlocks
        val idx = blocks.indexOfFirst { it.id == blockId }
        if (idx < 0) return
        val block = blocks[idx]
        if (block.currentSlot == targetSlot) return

        val currentValues = captureControlValues(block.controls)
        val updatedAbStates = block.abStates + mapOf(block.currentSlot to currentValues)
        val targetValues = updatedAbStates[targetSlot] ?: emptyMap()
        val newControls = applyControlValues(block.controls, targetValues)

        val updatedBlock = block.copy(currentSlot = targetSlot, controls = newControls, abStates = updatedAbStates)
        _boardState.value = _boardState.value.copy(
            cabBlocks = blocks.toMutableList().also { it[idx] = updatedBlock }
        )
    }

    fun addControlToCabBlock(blockId: String, control: ControlType) {
        _boardState.value = _boardState.value.copy(
            cabBlocks = _boardState.value.cabBlocks.map { block ->
                if (block.id == blockId) block.copy(controls = block.controls + control) else block
            }
        )
    }

    fun removeControlFromCabBlock(blockId: String, controlId: String) {
        _boardState.value = _boardState.value.copy(
            cabBlocks = _boardState.value.cabBlocks.map { block ->
                if (block.id == blockId) block.copy(controls = block.controls.filter { it.id != controlId })
                else block
            }
        )
    }

    fun updateControlInCabBlock(blockId: String, controlId: String, updatedControl: ControlType) {
        _boardState.value = _boardState.value.copy(
            cabBlocks = _boardState.value.cabBlocks.map { block ->
                if (block.id == blockId) block.copy(controls = block.controls.map {
                    if (it.id == controlId) updatedControl else it
                }) else block
            }
        )
    }

    // Legacy single-block helpers — operate on first cab block, creating one if needed.

    fun addCabControl(control: ControlType) {
        val blocks = _boardState.value.cabBlocks
        if (blocks.isEmpty()) {
            _boardState.value = _boardState.value.copy(
                cabBlocks = listOf(CabBlock(id = "cab_main", name = "Cabinet", controls = listOf(control)))
            )
        } else {
            val updated = blocks[0].copy(controls = blocks[0].controls + control)
            _boardState.value = _boardState.value.copy(cabBlocks = listOf(updated) + blocks.drop(1))
        }
    }

    fun removeCabControl(controlId: String) {
        val blocks = _boardState.value.cabBlocks
        if (blocks.isEmpty()) return
        val updated = blocks[0].copy(controls = blocks[0].controls.filter { it.id != controlId })
        _boardState.value = _boardState.value.copy(cabBlocks = listOf(updated) + blocks.drop(1))
    }

    fun updateCabControl(controlId: String, updatedControl: ControlType) {
        val blocks = _boardState.value.cabBlocks
        if (blocks.isEmpty()) return
        val updated = blocks[0].copy(controls = blocks[0].controls.map {
            if (it.id == controlId) updatedControl else it
        })
        _boardState.value = _boardState.value.copy(cabBlocks = listOf(updated) + blocks.drop(1))
    }

    fun clearCabControls() {
        val blocks = _boardState.value.cabBlocks
        if (blocks.isEmpty()) return
        val updated = blocks[0].copy(controls = emptyList())
        _boardState.value = _boardState.value.copy(cabBlocks = listOf(updated) + blocks.drop(1))
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

    // --- A/B switching ---

    /**
     * Switch A/B slot for a pedal or effect block.
     * Saves current control values into current slot, loads target slot's values.
     */
    fun switchBlockAbSlot(isPedals: Boolean, blockId: String, targetSlot: AbSlot) {
        val blocks = if (isPedals) _boardState.value.pedals else _boardState.value.effects
        val block = blocks.find { it.id == blockId } ?: return
        if (block.abSlot == targetSlot) return

        val currentValues = captureControlValues(block.controls)
        val updatedStateA = if (block.abSlot == AbSlot.A) currentValues else block.stateA
        val updatedStateB = if (block.abSlot == AbSlot.B) currentValues else block.stateB
        val targetValues = if (targetSlot == AbSlot.A) updatedStateA else updatedStateB
        val newControls = applyControlValues(block.controls, targetValues)

        val updatedBlock = block.copy(
            abSlot = targetSlot,
            controls = newControls,
            stateA = updatedStateA,
            stateB = updatedStateB
        )

        if (isPedals) {
            _boardState.value = _boardState.value.copy(
                pedals = _boardState.value.pedals.map { if (it.id == blockId) updatedBlock else it }
            )
        } else {
            _boardState.value = _boardState.value.copy(
                effects = _boardState.value.effects.map { if (it.id == blockId) updatedBlock else it }
            )
        }
    }

    /** Switch A/B for the first amp block. */
    fun switchAmpAbSlot(targetSlot: AbSlot) {
        val blocks = _boardState.value.ampBlocks
        if (blocks.isEmpty()) return
        val block = blocks[0]
        if (block.currentSlot == targetSlot) return

        val currentValues = captureControlValues(block.controls)
        val updatedAbStates = block.abStates + mapOf(block.currentSlot to currentValues)
        val targetValues = updatedAbStates[targetSlot] ?: emptyMap()
        val newControls = applyControlValues(block.controls, targetValues)

        val updatedBlock = block.copy(
            currentSlot = targetSlot,
            controls = newControls,
            abStates = updatedAbStates
        )
        _boardState.value = _boardState.value.copy(ampBlocks = listOf(updatedBlock) + blocks.drop(1))
    }

    /** Switch A/B for the first cab block. */
    fun switchCabAbSlot(targetSlot: AbSlot) {
        val blocks = _boardState.value.cabBlocks
        if (blocks.isEmpty()) return
        val block = blocks[0]
        if (block.currentSlot == targetSlot) return

        val currentValues = captureControlValues(block.controls)
        val updatedAbStates = block.abStates + mapOf(block.currentSlot to currentValues)
        val targetValues = updatedAbStates[targetSlot] ?: emptyMap()
        val newControls = applyControlValues(block.controls, targetValues)

        val updatedBlock = block.copy(
            currentSlot = targetSlot,
            controls = newControls,
            abStates = updatedAbStates
        )
        _boardState.value = _boardState.value.copy(cabBlocks = listOf(updatedBlock) + blocks.drop(1))
    }

    // --- A/B helpers ---

    private fun captureControlValues(controls: List<ControlType>): Map<String, Float> {
        return controls.mapNotNull { ctrl ->
            when (ctrl) {
                is ControlType.Knob -> ctrl.id to ctrl.value
                is ControlType.Fader -> ctrl.id to ctrl.value
                is ControlType.Toggle -> ctrl.id to (if (ctrl.isOn) 1f else 0f)
                is ControlType.Selector -> ctrl.id to (ctrl.selectedIndex.toFloat())
                else -> null
            }
        }.toMap()
    }

    private fun applyControlValues(controls: List<ControlType>, values: Map<String, Float>): List<ControlType> {
        if (values.isEmpty()) return controls
        return controls.map { ctrl ->
            val v = values[ctrl.id] ?: return@map ctrl
            when (ctrl) {
                is ControlType.Knob -> ctrl.copy(value = v)
                is ControlType.Fader -> ctrl.copy(value = v)
                is ControlType.Toggle -> ctrl.copy(isOn = v > 0.5f)
                is ControlType.Selector -> ctrl.copy(selectedIndex = v.toInt().coerceIn(0, ctrl.positions.size - 1))
                else -> ctrl
            }
        }
    }
}
