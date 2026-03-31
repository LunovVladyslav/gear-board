package com.gearboard.ui.screens.presets

import com.gearboard.data.repository.BoardPresetRepository
import com.gearboard.data.repository.BoardRepository
import com.gearboard.data.repository.PresetRepository
import com.gearboard.data.repository.SettingsRepository
import com.gearboard.domain.model.BoardPreset
import com.gearboard.domain.model.BoardSection
import com.gearboard.domain.model.BoardState
import com.gearboard.domain.model.ControlBlock
import com.gearboard.domain.model.ControlType
import com.gearboard.domain.model.SectionType
import com.gearboard.midi.GearBoardMidiManager
import com.gearboard.util.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PresetViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var presetRepository: PresetRepository
    private lateinit var boardPresetRepository: BoardPresetRepository
    private lateinit var boardRepository: BoardRepository
    private lateinit var midiManager: GearBoardMidiManager
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var viewModel: PresetViewModel

    private fun makePreset(
        name: String = "Test",
        targetSoftware: String = "TestSW",
        controls: List<ControlType> = emptyList()
    ) = BoardPreset(
        id = "test-id",
        name = name,
        targetSoftware = targetSoftware,
        description = "desc",
        sections = listOf(
            BoardSection(SectionType.PEDALS, blocks = listOf(
                ControlBlock(id = "b1", name = "Block1", controls = controls)
            ))
        )
    )

    @Before
    fun setup() {
        presetRepository = mockk(relaxed = true)
        boardPresetRepository = mockk(relaxed = true)
        boardRepository = mockk(relaxed = true)
        midiManager = mockk(relaxed = true)
        settingsRepository = mockk(relaxed = true)

        every { presetRepository.getAllPresets() } returns emptyFlow()
        every { settingsRepository.isPremium } returns MutableStateFlow(false)
        every { boardRepository.boardState } returns MutableStateFlow(BoardState())
        every { boardRepository.getCurrentState() } returns BoardState()
        coEvery { boardPresetRepository.loadUserPresets() } returns emptyList()

        viewModel = PresetViewModel(
            presetRepository = presetRepository,
            boardPresetRepository = boardPresetRepository,
            boardRepository = boardRepository,
            midiManager = midiManager,
            settingsRepository = settingsRepository,
            gson = Gson()
        )
    }

    // --- applyBoardPreset goes through CCAssigner ---

    @Test
    fun `applyBoardPreset unassigned controls get CCs assigned before loadBoardState`() = runTest {
        val controls = List(3) { ControlType.Knob(label = "K$it", ccNumber = 0) }
        val preset = makePreset(controls = controls)

        viewModel.applyBoardPreset(preset)

        // boardRepository.loadBoardState must have been called with all CCs > 0
        coVerify {
            boardRepository.loadBoardState(withArg { state ->
                val allControls = state.pedals.flatMap { it.controls }
                assertThat(allControls.all { it.ccNumber > 0 }).isTrue()
            })
        }
    }

    @Test
    fun `applyBoardPreset no ccNumber 0 in reconstructed board state`() = runTest {
        val controls = List(5) { ControlType.Knob(label = "Knob$it", ccNumber = 0) }
        val preset = makePreset(controls = controls)

        viewModel.applyBoardPreset(preset)

        coVerify {
            boardRepository.loadBoardState(withArg { state ->
                val allCCs = state.pedals.flatMap { it.controls }.map { it.ccNumber }
                assertThat(allCCs.none { it == 0 }).isTrue()
            })
        }
    }

    @Test
    fun `applyBoardPreset calls loadBoardState which triggers undoRedoManager clear`() = runTest {
        val preset = makePreset()
        viewModel.applyBoardPreset(preset)

        // boardRepository.loadBoardState was called (which triggers the clear in BoardViewModel)
        coVerify { boardRepository.loadBoardState(any()) }
    }

    // --- importBoardPreset ---

    @Test
    fun `importBoardPreset with no conflicts calls applyBoardPreset directly`() = runTest {
        val preset = makePreset()
        val uri = mockk<android.net.Uri>()
        coEvery { boardPresetRepository.importPreset(uri) } returns
            BoardPresetRepository.PresetImportResult.Success(preset)
        coEvery { boardPresetRepository.validateImport(preset, any()) } returns
            BoardPresetRepository.ImportResult(preset, emptyList())

        viewModel.importBoardPreset(uri)

        coVerify { boardRepository.loadBoardState(any()) }
        assertThat(viewModel.showCcConflictDialog.value).isNull()
    }

    @Test
    fun `importBoardPreset with conflicts showCcConflictDialog emits non-null state`() = runTest {
        val preset = makePreset(controls = listOf(ControlType.Knob(label = "Drive", ccNumber = 10)))
        val uri = mockk<android.net.Uri>()
        val conflict = BoardPresetRepository.CcConflict(10, "Existing", "Drive")

        coEvery { boardPresetRepository.importPreset(uri) } returns
            BoardPresetRepository.PresetImportResult.Success(preset)
        coEvery { boardPresetRepository.validateImport(preset, any()) } returns
            BoardPresetRepository.ImportResult(preset, listOf(conflict))

        viewModel.importBoardPreset(uri)

        assertThat(viewModel.showCcConflictDialog.value).isNotNull()
        assertThat(viewModel.showCcConflictDialog.value!!.conflicts).hasSize(1)
        assertThat(viewModel.showCcConflictDialog.value!!.conflicts[0].ccNumber).isEqualTo(10)
    }

    @Test
    fun `importBoardPreset IoError shows toast with error text`() = runTest {
        val uri = mockk<android.net.Uri>()
        coEvery { boardPresetRepository.importPreset(uri) } returns
            BoardPresetRepository.PresetImportResult.IoError("Cannot open file")

        viewModel.importBoardPreset(uri)

        assertThat(viewModel.toastMessage.value).contains("Cannot open file")
    }

    @Test
    fun `importBoardPreset ParseError shows toast with error text`() = runTest {
        val uri = mockk<android.net.Uri>()
        coEvery { boardPresetRepository.importPreset(uri) } returns
            BoardPresetRepository.PresetImportResult.ParseError("Bad JSON")

        viewModel.importBoardPreset(uri)

        assertThat(viewModel.toastMessage.value).contains("Bad JSON")
    }

    // --- importBoardPresetForceReassign ---

    @Test
    fun `importBoardPresetForceReassign re-runs CCAssigner with current usedCCs`() = runTest {
        // Board has a control occupying CC 1
        val existingState = BoardState(
            pedals = listOf(
                ControlBlock(id = "e1", name = "Existing", controls = listOf(
                    ControlType.Knob(label = "ExKnob", ccNumber = 1)
                ))
            )
        )
        every { boardRepository.getCurrentState() } returns existingState

        // Incoming preset also wants CC 1
        val incomingControls = List(3) { ControlType.Knob(label = "K$it", ccNumber = if (it == 0) 1 else 0) }
        val preset = makePreset(controls = incomingControls)

        viewModel.importBoardPresetForceReassign(preset)

        coVerify {
            boardRepository.loadBoardState(withArg { state ->
                val allCCs = state.pedals.flatMap { it.controls }.map { it.ccNumber }
                // All CCs must be assigned
                assertThat(allCCs.none { it == 0 }).isTrue()
            })
        }
    }

    // --- saveCurrentAsBoardPreset ---

    @Test
    fun `saveCurrentAsBoardPreset snapshot contains all current block names`() = runTest {
        val state = BoardState(
            pedals = listOf(
                ControlBlock(id = "p1", name = "Overdrive", controls = emptyList()),
                ControlBlock(id = "p2", name = "Delay", controls = emptyList())
            )
        )
        every { boardRepository.getCurrentState() } returns state

        viewModel.saveCurrentAsBoardPreset("My Board")

        coVerify {
            boardPresetRepository.saveUserPreset(withArg { preset ->
                val blockNames = preset.sections.flatMap { it.blocks }.map { it.name }
                assertThat(blockNames).contains("Overdrive")
                assertThat(blockNames).contains("Delay")
            })
        }
    }

    @Test
    fun `saveCurrentAsBoardPreset toast shows saved name`() = runTest {
        every { boardRepository.getCurrentState() } returns BoardState()

        viewModel.saveCurrentAsBoardPreset("My Preset")

        assertThat(viewModel.toastMessage.value).contains("My Preset")
    }

    // --- dismissCcConflictDialog ---

    @Test
    fun `dismissCcConflictDialog clears showCcConflictDialog`() = runTest {
        val preset = makePreset()
        val uri = mockk<android.net.Uri>()
        val conflict = BoardPresetRepository.CcConflict(5, "Old", "New")

        coEvery { boardPresetRepository.importPreset(uri) } returns
            BoardPresetRepository.PresetImportResult.Success(preset)
        coEvery { boardPresetRepository.validateImport(preset, any()) } returns
            BoardPresetRepository.ImportResult(preset, listOf(conflict))

        viewModel.importBoardPreset(uri)
        assertThat(viewModel.showCcConflictDialog.value).isNotNull()

        viewModel.dismissCcConflictDialog()
        assertThat(viewModel.showCcConflictDialog.value).isNull()
    }

    // --- applyBoardPreset uses all four sections ---

    @Test
    fun `applyBoardPreset all four sections go through CCAssigner`() = runTest {
        val controls = List(2) { ControlType.Knob(label = "K$it", ccNumber = 0) }
        val preset = BoardPreset(
            id = "full",
            name = "Full",
            targetSoftware = "TestSW",
            description = "desc",
            sections = listOf(
                BoardSection(SectionType.PEDALS, blocks = listOf(
                    ControlBlock(id = "b1", name = "Pedal1", controls = controls)
                )),
                BoardSection(SectionType.AMP, blocks = listOf(
                    ControlBlock(id = "b2", name = "Amp1", controls = controls)
                )),
                BoardSection(SectionType.CAB, blocks = listOf(
                    ControlBlock(id = "b3", name = "Cab1", controls = controls)
                )),
                BoardSection(SectionType.EFFECTS, blocks = listOf(
                    ControlBlock(id = "b4", name = "FX1", controls = controls)
                ))
            )
        )

        viewModel.applyBoardPreset(preset)

        coVerify {
            boardRepository.loadBoardState(withArg { state ->
                val pedalCCs = state.pedals.flatMap { it.controls }.map { it.ccNumber }
                val ampCCs = state.ampBlocks.flatMap { it.controls }.map { it.ccNumber }
                val cabCCs = state.cabBlocks.flatMap { it.controls }.map { it.ccNumber }
                val effectCCs = state.effects.flatMap { it.controls }.map { it.ccNumber }

                assertThat(pedalCCs.none { it == 0 }).isTrue()
                assertThat(ampCCs.none { it == 0 }).isTrue()
                assertThat(cabCCs.none { it == 0 }).isTrue()
                assertThat(effectCCs.none { it == 0 }).isTrue()

                // Section ranges must not cross over
                assertThat(pedalCCs.all { it in 1..31 }).isTrue()
                assertThat(ampCCs.all { it in 32..63 }).isTrue()
                assertThat(cabCCs.all { it in 64..79 }).isTrue()
                assertThat(effectCCs.all { it in 80..110 }).isTrue()
            })
        }
    }
}
