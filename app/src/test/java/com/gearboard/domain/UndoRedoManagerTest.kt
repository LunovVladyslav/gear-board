package com.gearboard.domain

import com.gearboard.domain.model.BoardCommand
import com.gearboard.domain.model.SectionType
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class UndoRedoManagerTest {

    private lateinit var manager: UndoRedoManager

    @Before fun setup() {
        manager = UndoRedoManager()
    }

    @Test fun `canUndo is false when empty`() =
        assertThat(manager.canUndo.value).isFalse()

    @Test fun `canRedo is false when empty`() =
        assertThat(manager.canRedo.value).isFalse()

    @Test fun `canUndo is true after push`() {
        manager.push(fakeCommand())
        assertThat(manager.canUndo.value).isTrue()
    }

    @Test fun `undo returns last pushed command`() {
        val cmd = fakeCommand(oldValue = 0.3f, newValue = 0.7f)
        manager.push(cmd)
        assertThat(manager.undo()).isEqualTo(cmd)
    }

    @Test fun `undo moves command to redo stack`() {
        manager.push(fakeCommand())
        manager.undo()
        assertThat(manager.canRedo.value).isTrue()
    }

    @Test fun `redo returns undone command`() {
        val cmd = fakeCommand()
        manager.push(cmd)
        manager.undo()
        assertThat(manager.redo()).isEqualTo(cmd)
    }

    @Test fun `new push clears redo stack`() {
        manager.push(fakeCommand())
        manager.undo()
        manager.push(fakeCommand())
        assertThat(manager.canRedo.value).isFalse()
    }

    @Test fun `history is capped at 50`() {
        repeat(60) { manager.push(fakeCommand()) }
        var undoCount = 0
        while (manager.canUndo.value) {
            manager.undo()
            undoCount++
        }
        assertThat(undoCount).isEqualTo(50)
    }

    @Test fun `clear resets both stacks`() {
        manager.push(fakeCommand())
        manager.push(fakeCommand())
        manager.clear()
        assertThat(manager.canUndo.value).isFalse()
        assertThat(manager.canRedo.value).isFalse()
    }

    @Test fun `undo on empty returns null`() =
        assertThat(manager.undo()).isNull()

    @Test fun `redo on empty returns null`() =
        assertThat(manager.redo()).isNull()

    @Test fun `multiple undo and redo cycle correctly`() {
        val cmd1 = fakeCommand(oldValue = 0.1f, newValue = 0.2f)
        val cmd2 = fakeCommand(oldValue = 0.2f, newValue = 0.5f)
        manager.push(cmd1)
        manager.push(cmd2)

        assertThat(manager.undo()).isEqualTo(cmd2)
        assertThat(manager.undo()).isEqualTo(cmd1)
        assertThat(manager.canUndo.value).isFalse()

        assertThat(manager.redo()).isEqualTo(cmd1)
        assertThat(manager.redo()).isEqualTo(cmd2)
        assertThat(manager.canRedo.value).isFalse()
    }

    private fun fakeCommand(oldValue: Float = 0f, newValue: Float = 1f) =
        BoardCommand.ControlValueChanged(
            blockId = "block1",
            section = SectionType.PEDALS,
            controlId = "ctrl1",
            oldValue = oldValue,
            newValue = newValue
        )
}
