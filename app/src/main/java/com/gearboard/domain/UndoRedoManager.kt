package com.gearboard.domain

import com.gearboard.domain.model.BoardCommand
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages undo/redo history for board state changes.
 *
 * Push a [BoardCommand] for every reversible user action.
 * Call [undo] to step back; [redo] to step forward.
 * History is capped at [maxHistory] entries — oldest entries are dropped.
 */
@Singleton
class UndoRedoManager @Inject constructor() {

    private val maxHistory: Int = 50

    private val undoStack = ArrayDeque<BoardCommand>()
    private val redoStack = ArrayDeque<BoardCommand>()

    private val _canUndo = MutableStateFlow(false)
    val canUndo: StateFlow<Boolean> = _canUndo.asStateFlow()

    private val _canRedo = MutableStateFlow(false)
    val canRedo: StateFlow<Boolean> = _canRedo.asStateFlow()

    /**
     * Push a new command, clearing the redo stack.
     * If history exceeds [maxHistory], the oldest entry is dropped.
     */
    fun push(command: BoardCommand) {
        undoStack.addLast(command)
        redoStack.clear()
        if (undoStack.size > maxHistory) undoStack.removeFirst()
        updateStates()
    }

    /**
     * Undo the most recent command. Returns the command so the caller
     * can apply its reverse effect, or null if history is empty.
     */
    fun undo(): BoardCommand? {
        val command = undoStack.removeLastOrNull() ?: return null
        redoStack.addLast(command)
        updateStates()
        return command
    }

    /**
     * Redo the most recently undone command. Returns the command so the
     * caller can re-apply it, or null if redo stack is empty.
     */
    fun redo(): BoardCommand? {
        val command = redoStack.removeLastOrNull() ?: return null
        undoStack.addLast(command)
        updateStates()
        return command
    }

    /**
     * Clear both stacks. Call when a preset is loaded (old history is invalid).
     */
    fun clear() {
        undoStack.clear()
        redoStack.clear()
        updateStates()
    }

    private fun updateStates() {
        _canUndo.value = undoStack.isNotEmpty()
        _canRedo.value = redoStack.isNotEmpty()
    }
}
