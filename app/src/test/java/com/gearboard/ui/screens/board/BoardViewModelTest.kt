package com.gearboard.ui.screens.board

import com.gearboard.data.repository.BoardRepository
import com.gearboard.data.repository.ControlRepository
import com.gearboard.data.repository.MidiMappingRepository
import com.gearboard.data.repository.SettingsRepository
import com.gearboard.domain.UndoRedoManager
import com.gearboard.domain.model.BoardCommand
import com.gearboard.domain.model.ControlBlock
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.SectionType
import com.gearboard.midi.GearBoardMidiManager
import com.gearboard.util.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BoardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var boardRepository: BoardRepository
    private lateinit var controlRepository: ControlRepository
    private lateinit var midiMappingRepository: MidiMappingRepository
    private lateinit var midiManager: GearBoardMidiManager
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var undoRedoManager: UndoRedoManager
    private lateinit var viewModel: BoardViewModel

    @Before
    fun setup() {
        boardRepository = mockk(relaxed = true)
        controlRepository = mockk(relaxed = true)
        midiMappingRepository = mockk(relaxed = true)
        midiManager = mockk(relaxed = true)
        settingsRepository = mockk(relaxed = true)
        undoRedoManager = UndoRedoManager()

        every { boardRepository.boardState } returns MutableStateFlow(
            com.gearboard.domain.model.BoardState()
        )
        every { midiMappingRepository.getAllMappings() } returns emptyFlow()
        every { settingsRepository.controlSize } returns MutableStateFlow(1.0f)
        every { settingsRepository.midiChannel } returns MutableStateFlow(0)
        every { settingsRepository.isPremium } returns MutableStateFlow(false)
        coEvery { controlRepository.isEmpty() } returns true // skip load, show onboarding

        viewModel = BoardViewModel(
            boardRepository = boardRepository,
            controlRepository = controlRepository,
            midiMappingRepository = midiMappingRepository,
            midiManager = midiManager,
            settingsRepository = settingsRepository,
            undoRedoManager = undoRedoManager
        )
    }

    // --- sendControlMidi CC guard ---

    @Test
    fun `sendControlMidi skips Knob with cc 0`() {
        val knob = ControlType.Knob(label = "Drive", ccNumber = 0)
        viewModel.sendControlMidi(knob)
        verify(exactly = 0) { midiManager.sendControlChange(any(), any(), any()) }
    }

    @Test
    fun `sendControlMidi sends Knob with valid cc`() {
        val knob = ControlType.Knob(label = "Drive", ccNumber = 5, value = 0.5f)
        viewModel.sendControlMidi(knob)
        verify(exactly = 1) { midiManager.sendControlChange(5, 63, 1) }
    }

    @Test
    fun `sendControlMidi skips Toggle with cc 0`() {
        val toggle = ControlType.Toggle(label = "FX", ccNumber = 0)
        viewModel.sendControlMidi(toggle)
        verify(exactly = 0) { midiManager.sendControlChange(any(), any(), any()) }
    }

    @Test
    fun `sendControlMidi sends Toggle on with valid cc`() {
        val toggle = ControlType.Toggle(label = "FX", ccNumber = 10, isOn = true)
        viewModel.sendControlMidi(toggle)
        verify(exactly = 1) { midiManager.sendControlChange(10, 127, 1) }
    }

    @Test
    fun `sendControlMidi skips Fader with cc 0`() {
        val fader = ControlType.Fader(label = "Vol", ccNumber = 0)
        viewModel.sendControlMidi(fader)
        verify(exactly = 0) { midiManager.sendControlChange(any(), any(), any()) }
    }

    @Test
    fun `sendControlMidi skips Tap with cc 0`() {
        val tap = ControlType.Tap(label = "Tap", ccNumber = 0)
        viewModel.sendControlMidi(tap)
        verify(exactly = 0) { midiManager.sendControlChange(any(), any(), any()) }
    }

    @Test
    fun `sendControlMidi skips Selector with cc 0`() {
        val selector = ControlType.Selector(
            label = "Mode", ccNumber = 0,
            positions = listOf("A", "B")
        )
        viewModel.sendControlMidi(selector)
        verify(exactly = 0) { midiManager.sendControlChange(any(), any(), any()) }
    }

    @Test
    fun `sendControlMidi PresetNav sends program change`() {
        val nav = ControlType.PresetNav(label = "Nav", currentPreset = 3, midiChannel = 1)
        viewModel.sendControlMidi(nav)
        verify(exactly = 1) { midiManager.sendProgramChange(3, 1) }
    }

    // --- Undo/Redo ---

    @Test
    fun `canUndo is false initially`() {
        assertThat(undoRedoManager.canUndo.value).isFalse()
    }

    @Test
    fun `canRedo is false initially`() {
        assertThat(undoRedoManager.canRedo.value).isFalse()
    }

    @Test
    fun `onKnobValueChange pushes undo command`() = runTest {
        val knob = ControlType.Knob(id = "k1", label = "Drive", ccNumber = 5, value = 0.3f)
        viewModel.onKnobValueChange(true, "blk1", "k1", knob, 0.7f)
        advanceTimeBy(600) // past the 500ms debounce
        assertThat(undoRedoManager.canUndo.value).isTrue()
    }

    @Test
    fun `onKnobValueChange undo command has correct old and new values`() = runTest {
        val knob = ControlType.Knob(id = "k1", label = "Drive", ccNumber = 5, value = 0.3f)
        viewModel.onKnobValueChange(true, "blk1", "k1", knob, 0.7f)
        advanceTimeBy(600)
        val cmd = undoRedoManager.undo() as BoardCommand.ControlValueChanged
        assertThat(cmd.oldValue).isWithin(0.001f).of(0.3f)
        assertThat(cmd.newValue).isWithin(0.001f).of(0.7f)
    }

    @Test
    fun `onFaderValueChange pushes undo command`() = runTest {
        val fader = ControlType.Fader(id = "f1", label = "Vol", ccNumber = 7, value = 0.5f)
        viewModel.onFaderValueChange(false, "blk1", "f1", fader, 0.9f)
        advanceTimeBy(600)
        assertThat(undoRedoManager.canUndo.value).isTrue()
    }

    @Test
    fun `addPedalBlock pushes BlockAdded command`() {
        val block = ControlBlock(id = "pedal-1", name = "Overdrive")
        every { boardRepository.getCurrentState() } returns com.gearboard.domain.model.BoardState()
        viewModel.addPedalBlock(block)
        assertThat(undoRedoManager.canUndo.value).isTrue()
        val cmd = undoRedoManager.undo()
        assertThat(cmd).isInstanceOf(BoardCommand.BlockAdded::class.java)
        assertThat((cmd as BoardCommand.BlockAdded).section).isEqualTo(SectionType.PEDALS)
    }

    @Test
    fun `removePedalBlock pushes BlockRemoved command`() {
        val block = ControlBlock(id = "pedal-1", name = "Overdrive")
        val state = com.gearboard.domain.model.BoardState(pedals = listOf(block))
        every { boardRepository.getCurrentState() } returns state
        viewModel.removePedalBlock("pedal-1")
        assertThat(undoRedoManager.canUndo.value).isTrue()
        val cmd = undoRedoManager.undo()
        assertThat(cmd).isInstanceOf(BoardCommand.BlockRemoved::class.java)
    }

    @Test
    fun `undo after onKnobValueChange restores old value in board`() = runTest {
        val state = com.gearboard.domain.model.BoardState(
            pedals = listOf(
                ControlBlock(
                    id = "blk1",
                    name = "Drive",
                    controls = listOf(ControlType.Knob(id = "k1", label = "Drive", ccNumber = 5, value = 0.3f))
                )
            )
        )
        every { boardRepository.getCurrentState() } returns state
        every { boardRepository.boardState } returns MutableStateFlow(state)

        val knob = ControlType.Knob(id = "k1", label = "Drive", ccNumber = 5, value = 0.3f)
        viewModel.onKnobValueChange(true, "blk1", "k1", knob, 0.7f)
        viewModel.undo()

        // Verify boardRepository was called to restore old value
        verify { boardRepository.updateControlInBlock(true, "blk1", "k1", any()) }
    }

    // --- Section expand/collapse ---

    @Test
    fun `togglePedalsExpanded flips state`() {
        assertThat(viewModel.pedalsExpanded.value).isTrue()
        viewModel.togglePedalsExpanded()
        assertThat(viewModel.pedalsExpanded.value).isFalse()
    }

    @Test
    fun `toggleAmpExpanded flips state`() {
        assertThat(viewModel.ampExpanded.value).isTrue()
        viewModel.toggleAmpExpanded()
        assertThat(viewModel.ampExpanded.value).isFalse()
    }

    // --- Dialog visibility ---

    @Test
    fun `showAddPedalBlockDialog sets state to true`() {
        viewModel.showAddPedalBlockDialog()
        assertThat(viewModel.showAddPedalBlockDialog.value).isTrue()
    }

    @Test
    fun `hideAddPedalBlockDialog sets state to false`() {
        viewModel.showAddPedalBlockDialog()
        viewModel.hideAddPedalBlockDialog()
        assertThat(viewModel.showAddPedalBlockDialog.value).isFalse()
    }
}
