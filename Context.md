# GearBoard — Full App Context for AI Agent

**Last updated:** 2026-03-31
**Project root:** `/Users/admin/Desktop/Design GearBoard MIDI Controller/`
**Package:** `com.gearboard`

---

## 1. WHAT THE APP IS

GearBoard is an Android MIDI controller app for guitarists and producers using amp simulation VST plugins on a connected Mac/PC. The phone acts as a physical MIDI control surface — replacing hardware foot controllers like MIDI Möbius or Behringer FCB1010 — and sends MIDI CC (Control Change) and PC (Program Change) messages to plugins such as:

- Neural DSP Archetype Petrucci X (primary target)
- Line 6 Helix Native
- IK Multimedia AmpliTube 5
- Native Instruments Guitar Rig 7
- Positive Grid BIAS FX 2

Connection is via **BLE-MIDI** (phone advertises as a BLE peripheral; Mac/PC connects to it) or **USB-MIDI** (USB cable).

---

## 2. TECH STACK

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose (Material 3) |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Database | Room (SQLite) |
| Async | Kotlin Coroutines + StateFlow |
| MIDI transport | Android MIDI API (`android.media.midi`) + BLE GATT |
| Preferences | DataStore |
| Serialization | Gson (sealed class adapter) |
| Min SDK | 31 (Android 12) — required for MIDI 2.0 API |
| Build | Gradle Kotlin DSL |

---

## 3. COMPLETE FILE STRUCTURE

```
app/src/main/java/com/gearboard/
│
├── GearBoardApplication.kt          @HiltAndroidApp entry point
├── MainActivity.kt                  Single activity; hosts NavGraph
│
├── di/
│   ├── DatabaseModule.kt            Provides Room DB, DAOs, Gson
│   └── MidiModule.kt                Provides GearBoardMidiManager, BleMidiPeripheral
│
├── domain/
│   ├── UndoRedoManager.kt           ★ @Singleton @Inject — wired to BoardViewModel
│   └── model/
│       ├── ControlType.kt           ★ Core sealed class: all MIDI control primitives
│       ├── Models.kt                AbSlot, BoardState, ControlBlock, AmpBlock, CabBlock,
│       │                            Preset, MidiMapping, MidiEvent, BlockAppearance, etc.
│       ├── BoardPreset.kt           BoardPreset + BoardSection (built-in preset format)
│       ├── BoardCommand.kt          ★ Sealed class for undo/redo commands
│       ├── AmpCabTemplates.kt       Blueprint objects: AmpTemplates, CabTemplates
│       │                            All ccNumber=0 — intentional placeholders
│       ├── CCAssigner.kt            ★ Assigns unique CCs at block instantiation time
│       └── LiveModels.kt            TimeSignature, LiveSession, BarEvent, LiveState,
│                                    SyncMode, ClockSource
│
├── data/
│   ├── local/
│   │   ├── GearBoardDatabase.kt     Room DB v5, 5 tables, explicit MIGRATION_4_5
│   │   ├── converter/
│   │   │   └── Converters.kt        Gson sealed-class adapter (ControlTypeAdapter)
│   │   ├── entity/
│   │   │   ├── Entities.kt          PresetEntity, MidiMappingEntity
│   │   │   ├── ControlItemEntity.kt stableId UUID + blockName column + @Index(stableId)
│   │   │   └── LiveEntities.kt      LiveSessionEntity, BarEventEntity
│   │   └── dao/
│   │       ├── PresetDao.kt
│   │       ├── MidiMappingDao.kt
│   │       ├── ControlItemDao.kt    upsertAll() + deleteOrphans() added
│   │       └── LiveModeDao.kt
│   │
│   ├── repository/
│   │   ├── BoardRepository.kt       ★ In-memory live board state (NOT Room-backed directly)
│   │   ├── ControlRepository.kt     Domain↔Entity mapping; toEntity() takes blockName param
│   │   ├── MidiMappingRepository.kt Room-backed CC mapping CRUD
│   │   ├── PresetRepository.kt      Room-backed user preset CRUD (Preset model)
│   │   ├── BoardPresetRepository.kt Built-in + user presets; validateImport() + CcConflict
│   │   ├── SettingsRepository.kt    DataStore: MIDI channel, control size, haptics, etc.
│   │   └── LiveModeRepository.kt    Room-backed live session + bar event CRUD
│   │
│   └── presets/                     ★ Built-in preset Kotlin objects (type-safe, read-only)
│       ├── NeuralDspPetrucciXPreset.kt
│       ├── NeuralDspGenericPreset.kt
│       ├── HelixNativePreset.kt
│       ├── AmpliTube5Preset.kt
│       ├── GuitarRig7Preset.kt
│       ├── BiasFx2Preset.kt
│       └── BlankPreset.kt
│
├── midi/
│   ├── GearBoardMidiManager.kt      ★ Central MIDI engine: drop-last CC throttle, BLE reconnect
│   ├── BleMidiScanner.kt            BLE device discovery + connection
│   ├── BleMidiPeripheral.kt         App advertises as BLE peripheral to host Mac/PC
│   ├── MidiCcAssigner.kt            Top-level fun autoAssignCC(section, usedCCs) — MidiMapVM only
│   └── MidiClockReceiver.kt         Sync engine: tick→bar/beat, BPM detection, bar event dispatch
│
├── live/
│   ├── StandalonePlaybackEngine.kt  Internal metronome + bar sequencer (no MIDI clock sync)
│   └── BarCalculator.kt             MIDI tick → bar/beat conversion utility
│
└── ui/
    ├── theme/
    │   ├── Color.kt                 GearBoardColors object (dark theme)
    │   ├── Type.kt
    │   ├── Theme.kt
    │   ├── Dimensions.kt
    │   └── LocalAccentColor.kt
    ├── navigation/
    │   └── GearBoardNavigation.kt   NavGraph: Board, Presets, MidiMap, Settings, Live, Monitor
    ├── components/
    │   ├── GearKnob.kt              Canvas rotary knob, 270° range, -135° to +135°
    │   ├── GearFader.kt             Vertical/horizontal linear slider
    │   ├── GearToggle.kt            Latching or momentary square button
    │   ├── GearTap.kt               Stateless pulse button (Tap Tempo)
    │   ├── GearSelector.kt          Dropdown overlay selector (DropdownMenu pattern)
    │   ├── GearPad.kt               MIDI note pad
    │   ├── GearPresetNav.kt         ◀ PRESET ▶ navigation row
    │   ├── AbToggle.kt              A/B bank badge switcher
    │   ├── ConnectionStatusBar.kt   Connection state pill in top bar
    │   ├── ConnectionState.kt       Sealed: Connected/Connecting/Disconnected/Error
    │   ├── DragReorder.kt           Drag-to-reorder list helper
    │   ├── PremiumSheet.kt          Premium feature gate sheet
    │   └── SectionHeader.kt         Collapsible section header composable
    └── screens/
        ├── board/
        │   ├── BoardScreen.kt       Main board; undo/redo buttons in top bar
        │   ├── BoardViewModel.kt    ★ Master state VM; MIDI send; UPSERT persist; undo/redo
        │   ├── ControlRenderer.kt   when(ControlType) → correct composable
        │   ├── PedalCard.kt         Block card with stomp toggle, rename, delete
        │   ├── EffectCard.kt        Effect block card
        │   ├── AmpCabSections.kt    Amp + Cab block UI
        │   ├── AddEditControlDialog.kt  Add/edit individual control
        │   ├── AddSheets.kt         Bottom sheets: add pedal/effect/amp/cab block
        │   ├── MappingDialog.kt     CC number assignment dialog
        │   ├── CustomizeBlockDialog.kt  Block color/layout customization
        │   └── OnboardingDialog.kt  First-launch intro → guided setup
        ├── midimap/
        │   ├── MidiMapScreen.kt     All controls with CC numbers; MIDI Learn button
        │   └── MidiMapViewModel.kt  MIDI Learn: auto-assign CC, wiggle, listen for override
        ├── live/
        │   ├── LiveModeScreen.kt    Bar timeline, preset scheduling, sync indicators
        │   └── LiveModeViewModel.kt Session CRUD, playback control, clock routing
        ├── settings/
        │   ├── SettingsScreen.kt
        │   ├── SettingsViewModel.kt
        │   └── ConnectionViewModel.kt
        ├── presets/
        │   ├── PresetScreen.kt      Built-in + user board preset sections; CC conflict dialog
        │   └── PresetViewModel.kt   Board preset + legacy Preset operations
        ├── setup/
        │   ├── GuidedSetupScreen.kt 3-step wizard: template → MIDI mapping → done
        │   └── GuidedSetupViewModel.kt
        └── monitor/
            ├── MonitorScreen.kt     Real-time MIDI event log
            └── MonitorViewModel.kt

app/src/test/java/com/gearboard/
    ├── util/MainDispatcherRule.kt
    ├── domain/UndoRedoManagerTest.kt
    ├── domain/model/CCAssignerTest.kt        ★ new
    ├── domain/model/ControlTypeTest.kt
    ├── data/repository/ControlRepositoryTest.kt  ★ new
    ├── midi/MidiCcAssignerTest.kt
    └── ui/screens/board/BoardViewModelTest.kt    ★ new

app/src/androidTest/java/com/gearboard/
    ├── data/local/MigrationTest.kt           ★ new (MIGRATION_4_5 verification)
    ├── data/local/PresetDaoTest.kt
    └── midi/MidiManagerTest.kt
```

---

## 4. CORE DOMAIN MODEL

### 4.1 ControlType (sealed class)
**File:** `domain/model/ControlType.kt`

The fundamental abstraction. Every control on the board is one subclass:

```kotlin
sealed class ControlType {
    abstract val id: String         // UUID — stable across saves (stableId in Room entity)
    abstract val label: String      // Display name, max 12 chars in UI
    abstract val midiChannel: Int   // 1-16
    abstract val size: ControlSize  // SMALL(0.75f), MEDIUM(1.0f), LARGE(1.25f)
    abstract val ccNumber: Int      // 0 = unassigned placeholder; -1 = sentinel (Pad)

    data class Knob(
        ccNumber: Int,
        defaultValue: Float = 0.5f,   // normalized 0f..1f
        value: Float = defaultValue,
        displayFormat: DisplayFormat = ZERO_TO_TEN,
        ccNumberA: Int = ccNumber,    // Bank A CC (default = ccNumber)
        ccNumberB: Int = ccNumber     // Bank B CC
    ) : ControlType()

    data class Toggle(
        ccNumber: Int,
        momentaryMode: Boolean = false, // false=latching on/off; true=pulse 127→0 in 50ms
        isOn: Boolean = false,
        isStompButton: Boolean = false, // true = block enable/disable stomp
        ccNumberA: Int = ccNumber,
        ccNumberB: Int = ccNumber
    ) : ControlType()

    data class Tap(ccNumber: Int) : ControlType()
    // Stateless — sends CC 127 on every press

    data class Selector(
        ccNumber: Int,
        positions: List<String>,        // e.g. ["Clean", "Crunch", "Lead"]
        selectedIndex: Int = 0,
        ccValues: List<Int>? = null     // null = auto-distribute; explicit = per-position
    ) : ControlType()

    data class Fader(
        ccNumber: Int,
        defaultValue: Float = 0.5f,
        value: Float = defaultValue,
        orientation: FaderOrientation,  // VERTICAL or HORIZONTAL
        displayFormat: DisplayFormat = ZERO_TO_TEN,
        ccNumberA: Int = ccNumber,
        ccNumberB: Int = ccNumber
    ) : ControlType()

    data class PresetNav(
        ccNumber: Int = 80,             // Used in CC_INC_DEC mode; -1 for PC mode
        navMode: PresetNavMode,         // CC_INC_DEC | CC_SPLIT | PC
        pcChannel: Int = 1
    ) : ControlType()
    // CC_INC_DEC: one CC, value 0=prev, 127=next (Neural DSP style)
    // PC: sends Program Change messages

    data class Pad(
        noteNumber: Int,                // 0-127
        ccNumber: Int = -1,            // Not used for pads; sentinel value
        midiChannel: Int = 10,         // Default to drums channel
        velocity: Int = 100
    ) : ControlType()
}

enum class PresetNavMode { CC_INC_DEC, CC_SPLIT, PC }
enum class FaderOrientation { VERTICAL, HORIZONTAL }
enum class ControlSize(val scaleFactor: Float) { SMALL(0.75f), MEDIUM(1.0f), LARGE(1.25f) }
enum class SectionType { PEDALS, AMP, CAB, EFFECTS }
```

### 4.2 DisplayFormat
```kotlin
enum class DisplayFormat(val display: String) {
    MIDI_RAW("MIDI Raw"),        // 0-127 raw
    ZERO_TO_TEN("0 – 10"),       // 0.0-10.0  ← default for amp params
    PERCENTAGE("Percent"),       // 0%-100%
    MILLISECONDS("Millisec"),    // 0ms-2000ms
    DECIBELS_OUTPUT("dB Out"),   // -∞ to 0 dB  ← output/level controls
    DECIBELS_BOOST("dB Boost"),  // 0 to +12 dB ← boost/gain add
    SEMITONES("Semitones")       // -12 to +12 st
}
```

**Important:** `DECIBELS` (old name) no longer exists — it was split into `DECIBELS_OUTPUT` and `DECIBELS_BOOST`. `parseDisplayFormat()` in ControlRepository silently falls back to `ZERO_TO_TEN` for unknown/legacy names.

### 4.3 Block Models
```kotlin
// For PEDALS and EFFECTS sections
data class ControlBlock(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: String = "Custom",
    val enabled: Boolean = false,    // Controlled by isStompButton Toggle
    val controls: List<ControlType> = emptyList(),
    val abSlot: AbSlot = AbSlot.A,
    val stateA: Map<String, Float>,  // controlId → normalized value, saved when switching banks
    val stateB: Map<String, Float>,
    val appearance: BlockAppearance = BlockAppearance(),
    val layoutMode: BlockLayout = BlockLayout.COMPACT
)

// For AMP section
data class AmpBlock(
    val id: String,
    val name: String,
    val controls: List<ControlType>,
    val appearance: BlockAppearance,
    val layoutMode: BlockLayout = BlockLayout.GRID,
    val currentSlot: AbSlot = AbSlot.A,
    val abStates: Map<AbSlot, Map<String, Float>> = emptyMap()
)

// For CAB section — same shape as AmpBlock
data class CabBlock(
    val id: String, val name: String,
    val controls: List<ControlType>,
    val appearance: BlockAppearance,
    val layoutMode: BlockLayout = BlockLayout.GRID,
    val currentSlot: AbSlot = AbSlot.A,
    val abStates: Map<AbSlot, Map<String, Float>> = emptyMap()
)

data class BoardState(
    val pedals: List<ControlBlock> = emptyList(),
    val ampBlocks: List<AmpBlock> = emptyList(),
    val cabBlocks: List<CabBlock> = emptyList(),
    val effects: List<ControlBlock> = emptyList()
)

enum class AbSlot { A, B }
enum class BlockLayout { GRID, ROW, COMPACT }
data class BlockAppearance(
    val accentColor: Long = 0xFFE8A020,  // Amber default
    val headerStyle: HeaderStyle = MINIMAL
)
```

### 4.4 BoardPreset (Built-in Presets)
```kotlin
data class BoardPreset(
    val id: String,
    val name: String,
    val targetSoftware: String,
    val description: String,
    val defaultMidiChannel: Int = 1,
    val setupInstructions: String = "",
    val sections: List<BoardSection>
)

data class BoardSection(
    val type: SectionType,     // PEDALS, AMP, CAB, EFFECTS
    val isExpanded: Boolean = true,
    val blocks: List<ControlBlock>
)
```

### 4.5 BoardCommand (Undo/Redo)
```kotlin
sealed class BoardCommand {
    data class ControlValueChanged(
        val blockId: String, val section: SectionType,
        val controlId: String, val oldValue: Float, val newValue: Float
    ) : BoardCommand()

    data class BlockAdded(val section: SectionType, val block: Any) : BoardCommand()
    data class BlockRemoved(val section: SectionType, val block: Any, val position: Int) : BoardCommand()
    data class BlockRenamed(val blockId: String, val section: SectionType, val oldName: String, val newName: String) : BoardCommand()
    data class AbSlotSaved(val blockId: String, val section: SectionType, val slot: AbSlot,
                           val oldStates: Map<String, Float>, val newStates: Map<String, Float>) : BoardCommand()
}
```

### 4.6 MidiMapping
```kotlin
data class MidiMapping(
    val id: Long = 0,
    val controlId: String,    // References ControlType.id (UUID)
    val controlName: String,
    val ccNumber: Int,        // The learned/assigned CC number
    val channel: Int = 0      // 0-15 (stored), 1-16 (sent)
)
```

---

## 5. CC NUMBER SYSTEM (CRITICAL)

### The CC0 Rule
- `ccNumber = 0` means **unassigned placeholder** — **NEVER send CC 0**
- `ccNumber = -1` means **Pad sentinel** — uses `noteNumber` instead
- Both `sendControlChange()` in `GearBoardMidiManager` and `sendControlMidi()` in `BoardViewModel` guard against `cc <= 0`

### CCAssigner (domain/model/CCAssigner.kt)
**The single source of truth for CC assignment.** Call this whenever creating blocks from templates:

```kotlin
object CCAssigner {
    // Section ranges — safe zones for each section type
    private val RANGES = mapOf(
        "pedals"  to 1..31,
        "amp"     to 32..63,
        "cab"     to 64..79,
        "effects" to 80..110
    )

    fun assign(controls: List<ControlType>, section: String, usedCCs: MutableSet<Int>): List<ControlType>
    fun assignAmp(block: AmpBlock, usedCCs: MutableSet<Int>): AmpBlock
    fun assignCab(block: CabBlock, usedCCs: MutableSet<Int>): CabBlock
    fun assignBlock(block: ControlBlock, section: String, usedCCs: MutableSet<Int>): ControlBlock
}
```

**Rules:**
- Controls with `ccNumber > 0` are left unchanged (already assigned)
- `PresetNav` is skipped (uses Program Change, not CC)
- Falls back to `range.first` if all CCs in range are exhausted
- `usedCCs` is updated in-place — always pass the same set across all sections in one setup

### CC Override via MIDI Learn (MidiMappingRepository)
The `midi_mappings` Room table stores CC overrides keyed by `controlId`. In `BoardViewModel`, `ccAssignments` is an in-memory map kept current by collecting `midiMappingRepository.getAllMappings()`. When sending MIDI:
```kotlin
val cc = ccAssignments.value[control.id] ?: control.ccNumber
if (cc <= 0) return  // guard: never send CC 0
```
The learned CC always takes priority over the template CC.

### CC Rate Limiting (GearBoardMidiManager)
Max ~30 messages/second per CC (33ms window). Uses **drop-last pending pattern** — the final value always arrives even after fast gestures:

```kotlin
private val ccLastSentMs = LongArray(128) { 0L }
private val pendingCC = arrayOfNulls<Pair<Int, Int>>(128) // (value, channel)
private val pendingScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

fun sendControlChange(ccNumber: Int, value: Int, channel: Int) {
    val cc = ccNumber.coerceIn(0, 127)
    if (cc == 0) return // guard: never send CC 0
    val now = System.currentTimeMillis()
    if (now - ccLastSentMs[cc] >= CC_THROTTLE_MS) {
        ccLastSentMs[cc] = now
        pendingCC[cc] = null
        sendMidiCC(cc, value, channel)
    } else {
        pendingCC[cc] = Pair(value, channel)  // overwrite any previous pending
        pendingScope.launch {
            delay(CC_THROTTLE_MS)
            pendingCC[cc]?.let { (pv, pch) ->
                pendingCC[cc] = null
                sendMidiCC(cc, pv, pch)  // guaranteed delivery of final value
            }
        }
    }
}
```

---

## 6. REPOSITORY LAYER

### BoardRepository (in-memory, NOT Room)
**File:** `data/repository/BoardRepository.kt`
Holds the live `BoardState` in a `MutableStateFlow`. All UI observes `boardState: StateFlow<BoardState>`.

Key operations:
- `addPedalBlock(block)`, `removePedalBlock(blockId)`, `updatePedalBlock(block)`, `renamePedalBlock()`
- `addAmpBlock()`, `removeAmpBlock()`, `updateAmpBlock()`, `switchAmpBlockAbSlot()`
- `addCabBlock()`, `removeCabBlock()`, `updateCabBlock()`, `switchCabBlockAbSlot()`
- `addEffectBlock()`, `removeEffectBlock()`, `updateEffectBlock()`
- `addControlToBlock(isPedals, blockId, control)` / `removeControlFromBlock()` / `updateControlInBlock()`
- `switchBlockAbSlot(isPedals, blockId, targetSlot)` — saves current control values into stateA/B, loads target
- `loadBoardState(state)` — replaces entire state (used on startup and preset apply)
- `getCurrentState(): BoardState` — synchronous snapshot

### ControlRepository (Room-backed, domain↔entity)
**File:** `data/repository/ControlRepository.kt`

Key points:
- `toEntity(control, sectionType, blockId, sortOrder, entityId, blockName)` — `stableId = control.id`; `blockName` is the block's display name, persisted for reload
- `toDomain(entity)` — reconstructs ControlType; uses `entity.stableId` as `id`
- `upsertAll(items)` / `deleteOrphans(activeIds)` — used by `persistCurrentState()` instead of delete-all+insert
- `parseDisplayFormat()` catches `IllegalArgumentException`, defaults to `ZERO_TO_TEN`

### BoardPresetRepository
**File:** `data/repository/BoardPresetRepository.kt`
- `builtInPresets: List<BoardPreset>` — all 7 preset objects, read-only
- `loadUserPresets()` — reads `.gearboard` JSON files from `filesDir/board_presets/`
- `saveUserPreset(preset)` / `deleteUserPreset(presetId)`
- `exportPreset(preset, uri)` / `importPreset(uri)` — via ContentResolver
- `validateImport(preset, currentBoardState): ImportResult` — checks CC conflicts before import
- `data class ImportResult(preset, conflicts: List<CcConflict>)`
- `data class CcConflict(ccNumber, existingLabel, incomingLabel)`

---

## 7. VIEWMODEL LAYER

### BoardViewModel
**File:** `ui/screens/board/BoardViewModel.kt`
Injected with: `BoardRepository`, `ControlRepository`, `MidiMappingRepository`, `GearBoardMidiManager`, `SettingsRepository`, `UndoRedoManager`.

**State exposed:**
- `boardState: StateFlow<BoardState>` — from BoardRepository
- `ccAssignments: MutableStateFlow<Map<String, Int>>` — live CC override map
- `controlSize: StateFlow<Float>` — global scale factor
- `globalMidiChannel: StateFlow<Int>` — 0-15
- `canUndo: StateFlow<Boolean>` — from UndoRedoManager
- `canRedo: StateFlow<Boolean>` — from UndoRedoManager
- Section expanded states, dialog visibility states

**init block:**
1. `loadPersistedState()` — reads Room `control_items`, reconstructs board using `entity.blockName` for real block names, shows onboarding if empty
2. Collects `midiMappingRepository.getAllMappings()` to keep `ccAssignments` fresh

**Adding blocks (all use CCAssigner + push undo command):**
```kotlin
fun addPedalBlock(block: ControlBlock) {
    val assigned = CCAssigner.assignBlock(migratePedalBlock(block), "pedals", collectUsedCCs())
    undoRedoManager.push(BoardCommand.BlockAdded(SectionType.PEDALS, assigned))
    boardRepository.addPedalBlock(assigned)
}
// same pattern for addEffectBlock, addAmpBlock, addCabBlock
```

**Removing blocks (push undo command before remove):**
```kotlin
fun removePedalBlock(blockId: String) {
    val block = boardRepository.getCurrentState().pedals.find { it.id == blockId }
    if (block != null) undoRedoManager.push(BoardCommand.BlockRemoved(SectionType.PEDALS, block, position))
    boardRepository.removePedalBlock(blockId)
}
```

**`sendControlMidi(control: ControlType)`:**
```kotlin
when (control) {
    is ControlType.Knob     -> { val cc = ccAssignments[id] ?: ccNumber; if (cc <= 0) return; send(cc, value*127) }
    is ControlType.Toggle   -> { val cc = ...; if (cc <= 0) return; if momentaryMode: send(cc,127), delay(50), send(cc,0)
                                  else: send(cc, if(isOn) 127 else 0) }
    is ControlType.Tap      -> { val cc = ...; if (cc <= 0) return; send(cc, 127) }
    is ControlType.Selector -> { val cc = ...; if (cc <= 0) return; send(cc, selectorCCValue(...)) }
    is ControlType.Fader    -> { val cc = ...; if (cc <= 0) return; send(cc, value*127) }
    is ControlType.PresetNav -> sendProgramChange(currentPreset, midiChannel)
    is ControlType.Pad       -> handled by sendPadOn/sendPadOff (Note On/Off)
}
// NOTE: ccNumberA/ccNumberB are intentionally NOT used for routing.
// A/B bank uses ONE CC per control with TWO saved values. BoardRepository
// restores control.value from stateA/stateB; sendControlMidi reads the correct value automatically.
```

**Knob/Fader value changes (push undo before update):**
```kotlin
fun onKnobValueChange(..., knob: ControlType.Knob, newValue: Float) {
    undoRedoManager.push(BoardCommand.ControlValueChanged(blockId, section, controlId, knob.value, newValue))
    val updated = knob.copy(value = newValue)
    updateControlInBlock(..., updated)
    sendControlMidi(updated)
}
```

**Undo/Redo:**
```kotlin
fun undo() {
    when (val cmd = undoRedoManager.undo()) {
        is BoardCommand.ControlValueChanged -> restore oldValue, re-send MIDI
        is BoardCommand.BlockAdded -> boardRepository.remove*(cmd.block.id)
        is BoardCommand.BlockRemoved -> boardRepository.add*(cmd.block)
        else -> {}
    }
    triggerAutoSave()
}
fun redo() { /* mirror of undo, applying newValue / re-adding / re-removing */ }
```

**Persistence (auto-save, UPSERT pattern):**
- `triggerAutoSave()` — debounces 300ms then calls `persistCurrentState()`
- `persistCurrentState()` — builds entity list with `blockName = block.name`, calls `upsertAll()` + `deleteOrphans(activeIds)` — no row churn for unchanged controls
- `loadPersistedState()` — on startup, reads entities grouped by section+blockId; uses `entities.first().blockName.ifEmpty { fallback }` for real names

### UndoRedoManager
**File:** `domain/UndoRedoManager.kt`
`@Singleton @Inject constructor()` — injected into BoardViewModel.
- `push(command)` — adds to undo stack, clears redo stack, caps at 50 entries
- `undo(): BoardCommand?` / `redo(): BoardCommand?`
- `canUndo: StateFlow<Boolean>` / `canRedo: StateFlow<Boolean>`
- `clear()` — called when a preset is loaded (old history is invalid)

### PresetViewModel
**File:** `ui/screens/presets/PresetViewModel.kt`
Injected with: `PresetRepository`, `BoardPresetRepository`, `BoardRepository`, `GearBoardMidiManager`, `SettingsRepository`, `Gson`.

**Board preset operations (new):**
- `builtInPresets: List<BoardPreset>` — direct from repository
- `userPresets: StateFlow<List<BoardPreset>>`
- `applyBoardPreset(preset)` — calls `reconstructBoardState(preset)` (runs CCAssigner on each block) → `boardRepository.loadBoardState()`
- `saveCurrentAsBoardPreset(name)` — snapshots current state → `boardPresetRepository.saveUserPreset()`
- `deleteBoardUserPreset(presetId)`
- `exportBoardPreset(preset, uri)` / `importBoardPreset(uri)` — import triggers `validateImport()` first
- `importBoardPresetForceReassign(preset)` — re-runs CCAssigner to resolve all conflicts, then applies
- `showApplyConfirmDialog: StateFlow<BoardPreset?>` — built-in preset confirmation
- `showCcConflictDialog: StateFlow<CcConflictDialogState?>` — import conflict resolution

**`reconstructBoardState(preset)`:** Runs all preset blocks through CCAssigner in section order to ensure no CC 0 values on the live board.

**Legacy preset operations (unchanged):** `loadPreset()`, `saveNewPreset()`, `overwritePreset()`, `deletePreset()`, `exportPresetJson()`, `importPresetFromJson()`

### MidiMapViewModel
**File:** `ui/screens/midimap/MidiMapViewModel.kt`
MIDI Learn flow — unchanged. Uses `MidiCcAssigner.autoAssignCC()` (not CCAssigner) for ad-hoc assignment.

### GuidedSetupViewModel
**File:** `ui/screens/setup/GuidedSetupViewModel.kt`
3-step onboarding wizard. Uses CCAssigner in section order across all blocks.

### Other ViewModels
- `SettingsViewModel` — global MIDI channel (0-15), control scale, haptics
- `ConnectionViewModel` — USB/BLE device list from `GearBoardMidiManager.availableDevices`
- `LiveModeViewModel` — live session CRUD, STANDALONE/SYNCED playback
- `MonitorViewModel` — collects `midiManager.midiEvents` into a display list

---

## 8. MIDI ENGINE

### GearBoardMidiManager
**File:** `midi/GearBoardMidiManager.kt`
Singleton. Injected everywhere MIDI needs to be sent or received.

**Connection:**
- `connectToDevice(deviceInfo, type)` — opens `MidiInputPort` (send to device) and `MidiOutputPort` (receive from device)
- `disconnect()` — closes ports, cancels `pendingScope` and `managerScope`
- `connectionState: StateFlow<ConnectionState>` — Connected/Connecting/Disconnected/Error
- `availableDevices: StateFlow<List<MidiDeviceInfo>>` — refreshed on hotplug

**Sending:**
```kotlin
fun sendControlChange(ccNumber: Int, value: Int)            // uses global channel
fun sendControlChange(ccNumber: Int, value: Int, channel: Int)  // drop-last throttle + CC 0 guard
fun sendProgramChange(program: Int)
fun sendProgramChange(program: Int, channel: Int)
fun sendNoteOn(note: Int, velocity: Int, channel: Int)
fun sendNoteOff(note: Int, channel: Int)
fun sendPreset(bank: Int, program: Int)                     // Bank Select CC0/CC32 + PC
```

**BLE auto-reconnect:**
`managerScope` watches `blePeripheral.state`. When state transitions to `Idle` while connected via BLE (host disconnected), automatically calls `blePeripheral.startAdvertising()`. Stops after 5 attempts and emits `ConnectionState.Error("BLE connection lost")`. Counter resets on successful reconnect.

**BLE dual-path:**
`sendMidiData()` sends to both:
1. `activeInputPort` (USB/BLE MIDI device in device mode)
2. `blePeripheral` (when Mac/PC is connected to the app as a peripheral)

### BleMidiPeripheral
**File:** `midi/BleMidiPeripheral.kt`
Advertises the Android device as a BLE MIDI peripheral. Handles GATT server, CCCD descriptor writes (notification enable = MIDI handshake), and incoming MIDI data from host. `startAdvertising()` is public and can be called again after disconnect for reconnect.

### MidiClockReceiver
**File:** `midi/MidiClockReceiver.kt`
Receives `onTick`, `onStart`, `onStop`, `onContinue`, `onSongPositionPointer`. Computes BPM (rolling 8-tick window). Dispatches `BarEvent` on bar advance.

---

## 9. PERSISTENCE DESIGN

### Database
**Room DB version = 5.** Explicit `MIGRATION_4_5` — no `fallbackToDestructiveMigration`.

```kotlin
// GearBoardDatabase.companion
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE control_items ADD COLUMN blockName TEXT NOT NULL DEFAULT ''")
        database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_control_items_stableId ON control_items(stableId)")
    }
}
```

### ControlItemEntity
```kotlin
@Entity(
    tableName = "control_items",
    indices = [Index(value = ["stableId"], unique = true)]
)
data class ControlItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val stableId: String,     // Domain UUID — survives UPSERT cycles
    val sectionType: String,  // SectionType.name
    val blockId: String,
    val blockName: String = "", // Human-readable block name — persisted for reload
    val controlType: String,  // "KNOB", "TOGGLE", etc.
    val label: String,
    val ccNumber: Int = 0,
    val midiChannel: Int = 1,
    val extraConfig: String = "", // JSON for type-specific fields
    val sortOrder: Int = 0
)
```

### UPSERT persist pattern
`persistCurrentState()` no longer uses `deleteAll() + insert`. Instead:
```kotlin
val entities = buildEntityList(state)  // includes blockName = block.name
val activeIds = entities.map { it.stableId }
controlRepository.upsertAll(entities)          // ON CONFLICT REPLACE by stableId index
if (activeIds.isNotEmpty()) controlRepository.deleteOrphans(activeIds)
else controlRepository.deleteAll()
```
Unchanged controls are updated in-place. Only truly removed controls are deleted.

### CC tracking (two-table)
Controls stored in `control_items` with assigned CC. MIDI Learn overrides in `midi_mappings`. At runtime, `BoardViewModel.ccAssignments` caches the override table:
```
ccAssignments[control.id] → overrides control.ccNumber (MIDI Learn takes priority)
```

### Block name reload
`loadPersistedState()` uses `entities.first().blockName.ifEmpty { fallback }`:
- Pedals/Effects: fallback = `blockId`
- Amp: fallback = `"Amplifier"`
- Cab: fallback = `"Cabinet"`

---

## 10. BUILT-IN PRESETS

Located in `data/presets/`. All are Kotlin `object` singletons. **Do not modify these files.**

| File | Target VST | Nav Mode |
|---|---|---|
| `NeuralDspPetrucciXPreset` | Neural DSP Archetype Petrucci X | CC_INC_DEC (CC80) |
| `NeuralDspGenericPreset` | Any Neural DSP Archetype | CC_INC_DEC (CC80) |
| `HelixNativePreset` | Line 6 Helix Native | PC |
| `AmpliTube5Preset` | IK Multimedia AmpliTube 5 | PC |
| `GuitarRig7Preset` | NI Guitar Rig 7 | PC |
| `BiasFx2Preset` | Positive Grid BIAS FX 2 | PC |
| `BlankPreset` | Any VST | CC_INC_DEC |

Built-in preset blocks have `ccNumber = 0` (blueprints). `PresetViewModel.reconstructBoardState()` runs them through CCAssigner before applying to the board.

---

## 11. A/B BANK SYSTEM

Every block has two independent value banks (A and B). Switching banks:
1. Current control values are captured and saved into `stateA` or `stateB`
2. Target bank's saved values are restored into `control.value`
3. `BoardViewModel` re-sends all controls in the block → VST syncs to the restored values

**Design decision (intentional):** A/B uses **ONE CC number per control, TWO saved values** — NOT two CC numbers. `ccNumberA`/`ccNumberB` exist on Knob/Fader/Toggle but are **not used for routing** in `sendControlMidi`. The correct value reaches the VST because `BoardRepository.switchBlockAbSlot()` already updated `control.value` before the re-send.

**`BoardRepository.switchBlockAbSlot(isPedals, blockId, targetSlot)`** — handles pedals/effects
**`BoardRepository.switchAmpBlockAbSlot(blockId, targetSlot)`** — handles amp blocks

---

## 12. TEMPLATES (AmpCabTemplates.kt)

Blueprints only — **never modify these**. All `ccNumber = 0`.

```
AmpTemplates:
  CLEAN_AMERICAN, BRITISH_CRUNCH, BRITISH_CLASS_A, HIGH_GAIN, MODERN_HIGH_GAIN, BASS_AMP

CabTemplates:
  OPEN_BACK_112, CLOSED_BACK_212, CLOSED_BACK_412, BASS_115, BASS_410
```

**How to use templates:**
```kotlin
val usedCCs = mutableSetOf<Int>()
val block = CCAssigner.assignAmp(AmpTemplates.HIGH_GAIN.copy(id = UUID.randomUUID().toString()), usedCCs)
boardRepository.addAmpBlock(block)
```

---

## 13. KNOWN ISSUES / GAPS

All major issues from the original sprint have been resolved. Remaining items:

| Issue | Status | Location |
|---|---|---|
| `MidiCcAssigner.kt` (midi/) and `CCAssigner.kt` (domain/model/) are parallel | Design intent | `MidiCcAssigner` is for MidiMapViewModel only; `CCAssigner` is for block creation |
| `connectedAndroidTest` requires a connected device | Expected | Run manually with device attached |

---

## 14. DATA FLOW: KNOB DRAG → MIDI SEND

```
User drags knob
    ↓
GearKnob composable → onValueChange(newFloat)
    ↓
BoardViewModel.onKnobValueChange(isPedals, blockId, controlId, knob, newFloat)
    ↓
undoRedoManager.push(ControlValueChanged(blockId, section, controlId, oldValue, newValue))
updated = knob.copy(value = newFloat)
updateControlInBlock(...)   → BoardRepository (in-memory update)
sendControlMidi(updated)
    ↓
cc = ccAssignments[knob.id] ?: knob.ccNumber
if (cc <= 0) return  // guard
midiValue = (newFloat * 127f).toInt()
GearBoardMidiManager.sendControlChange(cc, midiValue, channel)
    ↓
Drop-last throttle check (33ms per CC):
  - Fast path: send immediately via sendMidiCC()
  - Throttled: store as pendingCC[cc], schedule flush after 33ms
    ↓
activeInputPort.send(bytes)              → USB/BLE MIDI device
blePeripheral.sendMidiData(bytes)        → BLE host (Mac/PC)
    ↓
_midiEvents.tryEmit(MidiEvent(...))      → MonitorScreen
    ↓
triggerAutoSave() [debounced 300ms]
    ↓
persistCurrentState() → upsertAll() + deleteOrphans()
```

---

## 15. DATA FLOW: MIDI LEARN (MidiMapScreen)

```
User taps "Learn" on a control in MidiMapScreen
    ↓
MidiMapViewModel.startLearn(controlId, controlName, section, isToggle)
    ↓
Look up existing mapping → get current CC or 0
If CC == 0: autoAssignCC(section, usedCCs) → first free CC in section range
    ↓
midiMappingRepository.saveMapping(MidiMapping(controlId, learnCC))  ← persisted immediately
    ↓
midiManager.sendControlChange(learnCC, 127/64, channel)  ← wiggle triggers VST learn
    ↓
learnState = LearnState(isActive=true, ..., remainingSeconds=10)
    ↓
[Two parallel jobs]:
learnJob: countdown 10→0 → auto-cancel if no physical override
listenJob: collects midiEvents for INCOMING CC
    ↓
If incoming CC detected:
    assignMapping(controlId, controlName, detectedCC) → saveMapping()
    cancelLearn()
    ↓
BoardViewModel.ccAssignments auto-updates (observing getAllMappings() flow)
```

---

## 16. DATA FLOW: PRESET IMPORT (with CC conflict check)

```
User taps Import in PresetScreen
    ↓
PresetViewModel.importBoardPreset(uri)
    ↓
boardPresetRepository.importPreset(uri) → BoardPreset
    ↓
boardPresetRepository.validateImport(preset, currentBoardState)
    → collects all existing CC numbers from current board
    → finds overlaps with incoming preset CCs
    ↓
If no conflicts:
    applyBoardPreset(preset) → reconstructBoardState() → boardRepository.loadBoardState()
    boardPresetRepository.saveUserPreset(preset)
If conflicts:
    showCcConflictDialog = CcConflictDialogState(conflicts, preset)
    User chooses:
      "Import anyway" → importBoardPresetForceReassign(preset)
          → reassignPresetCCs() runs CCAssigner with currentUsedCCs
          → applyBoardPreset(reassigned) + saveUserPreset(reassigned)
      "Cancel" → dismiss
```

---

## 17. GUIDED SETUP FLOW

```
App first launch → loadPersistedState() finds empty DB → _showOnboarding = true
    ↓
OnboardingDialog → user chooses template or "Build from Scratch"
    ↓
GuidedSetupViewModel.selectTemplate(template)
    ↓
usedCCs.clear()
Create all blocks via CCAssigner (in section order)
boardRepository.add*Block() for each block
_mappingControls = list of key controls to walk through
    ↓
Step 1: Layout preview (shows what was created)
Step 2: Guided mapping (one control at a time)
    startMappingControl() → find control → send wiggle on its real CC
    User clicks "Got it" → markCurrentMapped() → advance
Step 3: Complete → settingsRepository.setGuidedSetupComplete(true)
    ↓
Navigate to Board
```

---

## 18. LIVE MODE FLOW

```
User opens Live tab → LiveModeViewModel initialized
    ↓
User creates a LiveSession (name, totalBars, BPM, time signature)
User schedules BarEvents (which preset loads at bar N, optional BPM/time sig changes)
    ↓
User taps Play:
  STANDALONE mode: StandalonePlaybackEngine starts internal timer
  SYNCED mode: LiveModeViewModel installs MidiClockReceiver on midiManager.clockReceiver
    ↓
On each MIDI Clock tick (0xF8):
  MidiClockReceiver.onTick() → increment tickCount
  When tickCount >= ticksPerBar: bar++, applyEventsAtBar(bar)
    applyEventsAtBar: sends midiManager.sendPreset(bank, program) if preset scheduled
  BPM computed from rolling 8-tick average
  emitState() → LiveModeViewModel → LiveModeScreen redraws bar timeline
    ↓
On MIDI Start (0xFA): reset to bar 1
On MIDI Stop (0xFC): isPlaying = false
On Song Position Pointer (0xF2): seek to bar
```

---

## 19. DEPENDENCY INJECTION (Hilt)

### DatabaseModule.kt
Provides:
- `GearBoardDatabase` (singleton, version 5, with MIGRATION_4_5)
- `PresetDao`, `MidiMappingDao`, `ControlItemDao`, `LiveModeDao`
- `Gson` with `ControlTypeAdapter` registered
- `ControlRepository`, `MidiMappingRepository`, `PresetRepository`, `LiveModeRepository`

### MidiModule.kt
Provides:
- `MidiManager` (system service)
- `BleMidiPeripheral` (singleton)
- `BleMidiScanner` (singleton)
- `GearBoardMidiManager` (singleton)

### Domain singletons (self-provided via @Inject)
- `UndoRedoManager` — `@Singleton @Inject constructor()`
- `BoardRepository` — `@Singleton @Inject constructor()`
- `BoardPresetRepository` — `@Singleton @Inject constructor()`
- `SettingsRepository` — `@Singleton @Inject constructor()`

---

## 20. THEME / UI CONSTANTS

Dark theme throughout. Key colors from `GearBoardColors`:
```kotlin
object GearBoardColors {
    val Background: Color         // Dark surface
    val Surface: Color            // Slightly lighter
    val SurfaceElevated: Color    // Card backgrounds
    val Accent: Color             // Orange/Amber = 0xFFE8A020 — primary interactive color
    val TextPrimary: Color
    val TextSecondary: Color
    val TextDisabled: Color
    val TextOnAccent: Color       // Text on orange buttons
    val DangerText: Color         // Red for delete actions
    val BorderDefault: Color
    val SurfaceVariant: Color
}
```

Control sizes are scaled by `controlSize` factor from Settings (default 1.0f).

---

## 21. NAMING CONVENTIONS

| Pattern | Meaning |
|---|---|
| `ccNumber = 0` | Unassigned placeholder — never send this |
| `ccNumber = -1` | Sentinel (Pad type — uses noteNumber instead) |
| `stableId` | UUID that survives Room UPSERT cycles |
| `isStompButton = true` | The block's master enable/disable Toggle |
| `blockId = "amp_main"` | Fixed ID for primary amp block (from guided setup) |
| `blockId = "cab_main"` | Fixed ID for primary cab block (from guided setup) |
| `section = "pedals/amp/cab/effects"` | String keys for CCAssigner and MidiCcAssigner |
| `SectionType.PEDALS/AMP/CAB/EFFECTS` | Enum used in Room entity `sectionType` column |
| `AbSlot.A / AbSlot.B` | A/B bank enum |
| `momentaryMode` | Renamed from `pulseMode` — do not use `pulseMode` anywhere |
| `DECIBELS_OUTPUT` / `DECIBELS_BOOST` | Split from old `DECIBELS` — do not use `DECIBELS` |
| `blockName` | New entity column (v5) storing human-readable block name for persistence |
