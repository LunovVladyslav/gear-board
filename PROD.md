# GearBoard — Production Quality Sprint

You are working on **GearBoard**, an Android MIDI controller app (Kotlin + Jetpack Compose).
Project root: `/Users/admin/Desktop/Design GearBoard MIDI Controller/`
Package: `com.gearboard`
Current DB version: 5

All major blockers from the previous sprint are resolved. This sprint focuses on:
test coverage, error handling, UI polish, and production hardening.

---

## MANDATORY READING — before touching any file

**Architecture invariants you must never break:**
- `CCAssigner` (domain/model/) is the ONLY place CC numbers are assigned at block creation.
  `MidiCcAssigner` (midi/) is used ONLY in `MidiMapViewModel` for ad-hoc MIDI Learn assignment.
- `ccNumber = 0` → unassigned placeholder → NEVER send. Guard: `if (cc <= 0) return`
- `ccNumber = -1` → Pad sentinel → uses `noteNumber` instead
- `stableId` → UUID on `ControlItemEntity` that survives UPSERT cycles. Never rename or remove.
- A/B bank uses ONE CC per control, TWO saved values. `ccNumberA/ccNumberB` exist on the
  model but are NOT used for CC routing in `sendControlMidi` — this is intentional by design.
- `AmpCabTemplates.kt` and `data/presets/*.kt` — DO NOT MODIFY these files.
- `ControlType` sealed class hierarchy — DO NOT remove or rename subclasses.
- `momentaryMode` (not `pulseMode`), `DECIBELS_OUTPUT`/`DECIBELS_BOOST` (not `DECIBELS`)
- Persist pattern: `upsertAll() + deleteOrphans()` — do not revert to `deleteAll() + insert`

**What was fixed in the previous sprint (do not re-fix, do not regress):**
- Block names persist via `blockName` column in `ControlItemEntity` (DB v5)
- `MIGRATION_4_5` replaces `fallbackToDestructiveMigration` — Room DB v5 has explicit migration
- `sendControlMidi` has `if (cc <= 0) return` guard on all branches
- `sendControlChange` uses drop-last pending pattern (not simple drop)
- `UndoRedoManager` is wired to `BoardViewModel` — undo/redo buttons in `BoardScreen`
- `PresetScreen` shows built-in and user presets; CC conflict dialog on import
- `BoardPresetRepository` has `validateImport()` + `CcConflict` data class
- `BleMidiPeripheral.startAdvertising()` is public; auto-reconnect in `GearBoardMidiManager`

---

## TASK 1 — Complete the test suite

All test files go in `app/src/test/java/com/gearboard/`.
Unit test runner: JUnit4 + MockK + Truth.
Use `MainDispatcherRule` from `com.gearboard.util.MainDispatcherRule` for ViewModel tests.

### 1a — CCAssignerTest
**File:** `domain/model/CCAssignerTest.kt`
```kotlin
// Required coverage — implement all of these:

// Section range correctness
"assignBlock pedals → all CCs in 1..31"
"assignBlock amp → all CCs in 32..63"
"assignBlock cab → all CCs in 64..79"
"assignBlock effects → all CCs in 80..110"
"assignAmp → all CCs in 32..63"
"assignCab → all CCs in 64..79"

// Uniqueness
"assignBlock produces no duplicate CC numbers within a block"
"multiple blocks sharing usedCCs set produce no duplicates across blocks"
"usedCCs is updated in-place after assignBlock"

// Pre-assigned controls
"control with ccNumber > 0 is left unchanged"
"mixed block: pre-assigned kept, unassigned filled from range"
"ccNumber = 0 is replaced, ccNumber > 0 is preserved"

// Special controls
"PresetNav is skipped — ccNumber unchanged after assignBlock"
"Pad ccNumber = -1 is skipped — sentinel preserved"
"Toggle with isStompButton = true is assigned a CC normally"

// Cross-section isolation
"pedals and amp do not interfere when usedCCs is shared"
"all four sections assigned sequentially with shared usedCCs — no conflicts"

// Exhaustion fallback
"when all CCs in range are used, falls back to rangeStart"
```

### 1b — ControlRepositoryTest
**File:** `data/repository/ControlRepositoryTest.kt`

This tests the `toEntity() → toDomain()` round-trip for every ControlType subclass.
Use an in-memory Room database (no mocking needed for DAO — use real Room with `allowMainThreadQueries()`).
```kotlin
// Required coverage:

// Knob round-trip
"Knob: ccNumber, defaultValue, value, displayFormat, ccNumberA, ccNumberB all survive round-trip"
"Knob: stableId from domain is preserved as entity stableId and restored on toDomain"
"Knob: blockName passed to toEntity is readable from entity.blockName"

// Toggle round-trip
"Toggle: momentaryMode, isStompButton, isOn, ccNumberA, ccNumberB survive round-trip"
"Toggle: momentaryMode = true survives"
"Toggle: isStompButton = true survives"

// Selector round-trip
"Selector: positions list survives round-trip"
"Selector: ccValues = null survives (auto-distribute flag preserved)"
"Selector: explicit ccValues list survives round-trip"
"Selector: selectedIndex survives round-trip"

// Fader round-trip
"Fader: orientation VERTICAL survives"
"Fader: orientation HORIZONTAL survives"
"Fader: displayFormat PERCENTAGE survives"
"Fader: ccNumberA, ccNumberB survive round-trip"

// PresetNav round-trip
"PresetNav: navMode CC_INC_DEC survives"
"PresetNav: navMode PC survives"
"PresetNav: pcChannel survives"

// Pad round-trip
"Pad: noteNumber survives"
"Pad: velocity survives"
"Pad: midiChannel 10 survives"
"Pad: ccNumber = -1 sentinel survives"

// DisplayFormat legacy safety
"parseDisplayFormat returns ZERO_TO_TEN for unknown string (legacy DECIBELS name)"
"parseDisplayFormat returns ZERO_TO_TEN for empty string"
"parseDisplayFormat returns ZERO_TO_TEN for null/blank"

// DisplayFormat all current values
"parseDisplayFormat correctly maps all current DisplayFormat enum values"
```

### 1c — BoardViewModelTest
**File:** `ui/screens/board/BoardViewModelTest.kt`

Mock all dependencies with MockK (`relaxed = true` for repositories, strict for midiManager sends).
Use `MainDispatcherRule`.
```kotlin
// MIDI send correctness
"Knob send: value * 127 reaches midiManager as integer"
"Knob send: cc from ccAssignments overrides control.ccNumber"
"Knob send: ccNumber = 0 → nothing sent (guard)"
"Toggle ON → midiManager receives 127"
"Toggle OFF → midiManager receives 0"
"Toggle momentaryMode → midiManager receives 127 then 0"
"Tap → midiManager always receives 127"
"Selector → midiManager receives selectorCCValue(index, count, ccValues)"
"Fader → midiManager receives value * 127"
"Pad → midiManager receives NoteOn, not CC"

// A/B bank
"switchBlockAbSlot calls boardRepository.switchBlockAbSlot with correct args"
"after switchBlockAbSlot, sendControlMidi is called for each control in block"
"B slot controls send with their restored B values"

// Undo/Redo
"onKnobValueChange pushes ControlValueChanged before updating"
"undo ControlValueChanged: restores oldValue, re-sends MIDI, triggers autosave"
"redo ControlValueChanged: re-applies newValue, re-sends MIDI"
"addPedalBlock pushes BlockAdded command"
"undo BlockAdded removes the block from boardRepository"
"removePedalBlock pushes BlockRemoved with snapshot"
"undo BlockRemoved re-adds the block"

// CC assignment
"addPedalBlock: result has no ccNumber = 0 after CCAssigner"
"addAmpBlock: all CCs in 32..63 range"
"collectUsedCCs: collects CCs from all sections without duplicates"

// Persist
"persistCurrentState calls upsertAll with correct blockName for each block"
"persistCurrentState calls deleteOrphans with active stableIds"
"loadPersistedState uses entity.blockName for block name (not blockId)"
"loadPersistedState uses fallback name when blockName is empty"
```

### 1d — PresetViewModelTest
**File:** `ui/screens/presets/PresetViewModelTest.kt`
```kotlin
"applyBoardPreset: all blocks go through CCAssigner before loadBoardState"
"applyBoardPreset: no ccNumber = 0 in reconstructed board state"
"applyBoardPreset: calls undoRedoManager.clear()"
"importBoardPreset with no conflicts: applyBoardPreset called directly"
"importBoardPreset with conflicts: showCcConflictDialog emits non-null state"
"importBoardPresetForceReassign: re-runs CCAssigner with current usedCCs"
"saveCurrentAsBoardPreset: snapshot contains all current block names"
```

### 1e — MigrationTest (androidTest)
**File:** `app/src/androidTest/java/com/gearboard/data/local/MigrationTest.kt`
```kotlin
// Uses MigrationTestHelper + SupportSQLiteOpenHelper
"MIGRATION_4_5: runs without exception"
"MIGRATION_4_5: blockName column exists in control_items after migration"
"MIGRATION_4_5: existing rows have blockName = '' (default)"
"MIGRATION_4_5: stableId unique index exists after migration"
"full schema v5: all expected tables and columns present"
"full schema v5: Room validateMigration passes"
```

---

## TASK 2 — Error handling hardening

### 2a — GearBoardMidiManager: port send errors
**File:** `midi/GearBoardMidiManager.kt`

Currently `activeInputPort.send(bytes, ...)` can throw `IOException` (port closed, device disconnected mid-send). Wrap the low-level send in `sendMidiCC()`:
```kotlin
private fun sendMidiCC(cc: Int, value: Int, channel: Int) {
    try {
        val bytes = buildCCBytes(cc, value, channel)
        activeInputPort?.send(bytes, 0, bytes.size, System.nanoTime())
        blePeripheral.sendMidiData(bytes)
        _midiEvents.tryEmit(MidiEvent(type = "CC", channel = channel + 1, data1 = cc, data2 = value))
    } catch (e: IOException) {
        // Port closed mid-send — treat as disconnect
        Log.w("GearBoard", "MIDI send failed on CC $cc: ${e.message}")
        _connectionState.update { ConnectionState.Error("Send failed: ${e.message}") }
    } catch (e: IllegalStateException) {
        // Port already closed
        Log.w("GearBoard", "MIDI port closed during send: ${e.message}")
    }
}
```

Also wrap `sendNoteOn`, `sendNoteOff`, `sendProgramChange`, `sendPreset` with the same try/catch pattern.

### 2b — BoardPresetRepository: import error handling
**File:** `data/repository/BoardPresetRepository.kt`

`importPreset(uri)` calls `ContentResolver.openInputStream()` which can throw. Currently uncaught. Wrap:
```kotlin
sealed class PresetImportResult {
    data class Success(val preset: BoardPreset) : PresetImportResult()
    data class ParseError(val message: String) : PresetImportResult()
    data class IoError(val message: String) : PresetImportResult()
}

fun importPreset(uri: Uri): PresetImportResult {
    return try {
        val json = contentResolver.openInputStream(uri)?.bufferedReader()?.readText()
            ?: return PresetImportResult.IoError("Cannot open file")
        val preset = gson.fromJson(json, BoardPreset::class.java)
            ?: return PresetImportResult.ParseError("Invalid preset format")
        PresetImportResult.Success(preset)
    } catch (e: IOException) {
        PresetImportResult.IoError(e.message ?: "IO error")
    } catch (e: JsonSyntaxException) {
        PresetImportResult.ParseError(e.message ?: "JSON parse error")
    }
}
```

Update `PresetViewModel.importBoardPreset()` to handle all three result types and show appropriate `toastMessage`.

### 2c — ControlRepository: extraConfig parse safety
**File:** `data/repository/ControlRepository.kt`

`toDomain(entity)` calls `gson.fromJson(entity.extraConfig, ...)`. If `extraConfig` is malformed JSON (e.g., from a previous version or truncated write), this throws `JsonSyntaxException`. Add a try/catch around each `fromJson` call that returns a safe default:
```kotlin
private inline fun <reified T> safeFromJson(json: String, default: T): T {
    return if (json.isBlank()) default
    else try {
        gson.fromJson(json, T::class.java) ?: default
    } catch (e: JsonSyntaxException) {
        Log.w("GearBoard", "Failed to parse extraConfig, using default: ${e.message}")
        default
    }
}
```

### 2d — pendingScope cancellation in GearBoardMidiManager
**File:** `midi/GearBoardMidiManager.kt`

Verify that `pendingScope` is cancelled in:
1. `disconnect()` — cancel + recreate the scope
2. The Hilt `@PreDestroy` or `closeableRegistry` cleanup if using `@Singleton`

Also cancel any in-flight pending jobs for all CCs when `disconnect()` is called:
```kotlin
fun disconnect() {
    pendingScope.coroutineContext.cancelChildren() // cancel all pending flush jobs
    pendingCC.fill(null) // clear all pending values
    activeInputPort?.close()
    // ...rest of existing disconnect logic
}
```

---

## TASK 3 — UI completeness

### 3a — BoardScreen: undo/redo button state
**File:** `ui/screens/board/BoardScreen.kt`

The undo/redo buttons exist but verify they are correctly wired:
1. Both buttons must use `enabled = canUndo` / `enabled = canRedo` from ViewModel state.
2. Disabled state must use `GearBoardColors.TextDisabled` (not just alpha).
3. Add content descriptions: `contentDescription = "Undo last action"` / `"Redo last action"`.
4. Undo button must show the action being undone in a tooltip or snackbar:
```kotlin
   // When undo is tapped, show a Snackbar:
   // "Undone: Gain changed" for ControlValueChanged
   // "Undone: Block removed" for BlockRemoved
   // "Undone: Block added" for BlockAdded
```

### 3b — PresetScreen: loading and empty states
**File:** `ui/screens/presets/PresetScreen.kt`

Add proper states:
1. **Loading state** — while `userPresets` StateFlow is loading from disk, show a `CircularProgressIndicator`.
2. **Empty user presets** — when `userPresets` is empty, show a placeholder:
```
   [save icon]
   No saved boards yet
   Tap the save button to save your current board as a preset
```
3. **Apply confirmation dialog** — triggered by `showApplyConfirmDialog`. Must show preset name and targetSoftware in the message: `"Apply '${preset.name}' for ${preset.targetSoftware}? Your current board will be replaced."`
4. **CC conflict dialog** — triggered by `showCcConflictDialog`. Show a scrollable list of conflicts (max 5 visible, scroll for more). Each row: `"CC ${conflict.ccNumber}: '${conflict.existingLabel}' ↔ '${conflict.incomingLabel}'"`.

### 3c — ConnectionStatusBar: BLE reconnect state
**File:** `ui/components/ConnectionStatusBar.kt`

Add a new visual state for the BLE reconnect attempt:
```kotlin
// When ConnectionState is Disconnected and BLE reconnect is in progress,
// show a pulsing amber indicator instead of the static disconnected grey:
// "Reconnecting... (attempt N/5)"
// Use the existing Accent color (0xFFE8A020) for this state.
```

The reconnect attempt count must be exposed from `GearBoardMidiManager` as a `StateFlow<Int>` named `bleReconnectAttempts`. `ConnectionViewModel` collects it and passes to `ConnectionStatusBar`.

### 3d — MidiMapScreen: visual feedback for CC 0 controls
**File:** `ui/screens/midimap/MidiMapScreen.kt`

Controls with `ccNumber = 0` (unassigned) should be visually distinct from assigned ones:
- Show a warning amber badge `"No CC"` instead of the CC number
- Row background should use a subtle amber tint (use `GearBoardColors.Accent` at 10% alpha)
- Tapping the row should immediately trigger MIDI Learn (same as tapping the Learn button)

---

## TASK 4 — Performance and correctness

### 4a — collectUsedCCs efficiency
**File:** `ui/screens/board/BoardViewModel.kt`

Current implementation iterates all sections and all controls every time a block is added.
This is O(n) on every add — acceptable, but make it readable and verify correctness:
```kotlin
private fun collectUsedCCs(): MutableSet<Int> {
    val state = boardRepository.getCurrentState()
    val used = mutableSetOf<Int>()

    fun ControlType.collectCC() {
        if (ccNumber > 0) used.add(ccNumber)
    }

    state.pedals.flatMap { it.controls }.forEach { it.collectCC() }
    state.effects.flatMap { it.controls }.forEach { it.collectCC() }
    state.ampBlocks.flatMap { it.controls }.forEach { it.collectCC() }
    state.cabBlocks.flatMap { it.controls }.forEach { it.collectCC() }

    // Also include MIDI Learn overrides so assigned CCs are not reused
    ccAssignments.value.values.forEach { used.add(it) }

    return used
}
```

Verify the existing implementation matches this. If it does not include `ccAssignments.value.values`, add that — otherwise MIDI Learn assignments can conflict with new block CCs.

### 4b — A/B bank re-send after switch
**File:** `ui/screens/board/BoardViewModel.kt`

Verify that after every `switchBlockAbSlot` / `switchAmpBlockAbSlot` / `switchCabBlockAbSlot`, the ViewModel re-sends all controls in the switched block. If the re-send is missing or only partial (e.g., skips Toggles), fix it:
```kotlin
fun switchBlockAbSlot(isPedals: Boolean, blockId: String, targetSlot: AbSlot) {
    boardRepository.switchBlockAbSlot(isPedals, blockId, targetSlot)
    // Re-send all controls so VST syncs to the new bank values
    val state = boardRepository.getCurrentState()
    val block = if (isPedals)
        state.pedals.find { it.id == blockId }
    else
        state.effects.find { it.id == blockId }
    block?.controls?.forEach { sendControlMidi(it) }
    triggerAutoSave()
}
// Same pattern for switchAmpBlockAbSlot and switchCabBlockAbSlot
```

### 4c — Debounce knob undo pushes
**File:** `ui/screens/board/BoardViewModel.kt`

Currently every tiny knob drag movement pushes a `ControlValueChanged` command, flooding the undo stack (50 rapid pushes = only 50 history slots). Fix by debouncing undo pushes for continuous controls (Knob, Fader):
```kotlin
// Per-control debounce: only push undo when the user stops dragging for 500ms
private val undoPendingJobs = ConcurrentHashMap<String, Job>()
private val undoDebounceMs = 500L

fun onKnobValueChange(isPedals: Boolean, blockId: String, controlId: String,
                      knob: ControlType.Knob, newValue: Float) {
    // Always update value immediately (smooth UI)
    val updated = knob.copy(value = newValue)
    updateControlInBlock(isPedals, blockId, controlId, updated)
    sendControlMidi(updated)

    // Debounce undo push: cancel previous pending push for this control
    undoPendingJobs[controlId]?.cancel()
    val capturedOldValue = knob.value // capture before the closure
    undoPendingJobs[controlId] = viewModelScope.launch {
        delay(undoDebounceMs)
        if (abs(newValue - capturedOldValue) > 0.005f) { // threshold: ignore noise
            undoRedoManager.push(BoardCommand.ControlValueChanged(
                blockId, if (isPedals) SectionType.PEDALS else SectionType.EFFECTS,
                controlId, capturedOldValue, newValue
            ))
        }
        undoPendingJobs.remove(controlId)
    }
    triggerAutoSave()
}
// Apply same debounce pattern to onFaderValueChange
```

### 4d — Prevent undo/redo during MIDI Learn
**File:** `ui/screens/board/BoardScreen.kt`

Undo/redo buttons must be disabled while MIDI Learn is active in `MidiMapViewModel`. Collect `MidiMapViewModel.learnState.isActive` in `BoardScreen` and pass it as an additional disabled condition:
```kotlin
val learnActive by midiMapViewModel.learnState.collectAsState()
// Undo button: enabled = canUndo && !learnActive.isActive
// Redo button: enabled = canRedo && !learnActive.isActive
```

---

## TASK 5 — Documentation and code hygiene

### 5a — Add KDoc to public API surface
Add KDoc comments to all `public` / `internal` functions in these files:
- `domain/model/CCAssigner.kt` — document each function, the CC range table, and the `usedCCs` mutation contract
- `midi/GearBoardMidiManager.kt` — document the drop-last throttle, CC 0 guard, BLE reconnect, dual-path send
- `data/repository/BoardPresetRepository.kt` — document `validateImport`, `ImportResult`, `CcConflict`
- `domain/UndoRedoManager.kt` — document the 50-entry cap, clear-on-preset behavior

Format:
```kotlin
/**
 * Assigns unique CC numbers to all unassigned controls in [block].
 *
 * Controls with [ControlType.ccNumber] > 0 are left unchanged.
 * [ControlType.PresetNav] controls are skipped (they use Program Change).
 * CC numbers are drawn from the section's safe range (see [RANGES]).
 * Falls back to [IntRange.first] if the range is exhausted.
 *
 * @param block The block whose controls need CC assignment.
 * @param section One of "pedals", "amp", "cab", "effects".
 * @param usedCCs Mutable set of already-used CC numbers across all sections.
 *                Updated in-place — pass the same set for all blocks in one setup.
 * @return A new [ControlBlock] with all previously-0 CC numbers filled in.
 */
fun assignBlock(block: ControlBlock, section: String, usedCCs: MutableSet<Int>): ControlBlock
```

### 5b — Remove dead code
Search for and remove:
- Any remaining references to `pulseMode` (renamed to `momentaryMode`)
- Any remaining references to `DisplayFormat.DECIBELS` (split into `DECIBELS_OUTPUT` / `DECIBELS_BOOST`)
- Any remaining references to `fallbackToDestructiveMigration` (replaced with explicit migration)
- Any `TODO` comments that were resolved in the previous sprint

Use `./gradlew compileDebugKotlin` to verify no compilation errors after removal.

### 5c — Verify no CC 0 can leak to MIDI send
Do a project-wide search for all calls to `midiManager.sendControlChange(` and `sendControlMidi(`. Verify that every call site either:
1. Has a `if (cc <= 0) return` guard before it, OR
2. The CC number provably cannot be 0 at that point (e.g., it was just returned by `CCAssigner`)

Document any found without a guard and add the guard.

---

## TASK 6 — Gradle and build hygiene

### 6a — Verify test dependencies
Ensure `app/build.gradle.kts` contains all required test dependencies:
```kotlin
dependencies {
    // Unit tests
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("com.google.truth:truth:1.4.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    // Android tests
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.room:room-testing:2.6.1")
    androidTestImplementation("com.google.truth:truth:1.4.2")
}
```

### 6b — Run the full test suite
After completing all tasks, run:
```bash
# Unit tests
./gradlew test --info

# Check for compilation errors
./gradlew compileDebugKotlin compileDebugUnitTestKotlin

# Lint (fix all errors, warnings are acceptable)
./gradlew lintDebug
```

Fix any failures before considering a task complete.

---

## VERIFICATION CHECKLIST

After completing all tasks, verify each item passes:

**Tests**
- [ ] `./gradlew test` — 0 failures, 0 errors
- [ ] `CCAssignerTest` — all 15+ test cases pass
- [ ] `ControlRepositoryTest` — all ControlType round-trips pass, legacy fallback works
- [ ] `BoardViewModelTest` — all MIDI send, undo/redo, A/B bank, persist tests pass
- [ ] `PresetViewModelTest` — applyBoardPreset goes through CCAssigner, no CC 0 in result
- [ ] `MigrationTest` (androidTest) — MIGRATION_4_5 verified with MigrationTestHelper

**Error handling**
- [ ] `sendMidiCC` wrapped in try/catch — IOException cannot crash the app
- [ ] `importPreset` returns `PresetImportResult` — malformed JSON shows error toast, not crash
- [ ] `toDomain` uses `safeFromJson` — corrupted `extraConfig` returns safe default

**UI**
- [ ] Undo/redo buttons disabled correctly (no CC assignments, no MIDI Learn active)
- [ ] Undo snackbar shows action description
- [ ] PresetScreen has loading state, empty state, apply confirmation, CC conflict list
- [ ] ConnectionStatusBar shows reconnect attempt count
- [ ] MidiMapScreen: CC 0 controls show amber "No CC" badge

**Correctness**
- [ ] `collectUsedCCs` includes `ccAssignments.value.values` — MIDI Learn CCs not reused
- [ ] A/B bank switch re-sends ALL controls in the block (Knob, Toggle, Fader, Selector)
- [ ] Knob undo is debounced — single drag = one undo history entry
- [ ] No `sendControlChange` call can pass cc = 0 (project-wide audit complete)
- [ ] `./gradlew compileDebugKotlin` — 0 errors
- [ ] No references to `pulseMode`, `DECIBELS` (old), `fallbackToDestructiveMigration`

---

## FILES THAT MUST NOT BE MODIFIED

- `domain/model/AmpCabTemplates.kt`
- `domain/model/ControlType.kt` sealed hierarchy (adding new members is OK)
- `data/presets/NeuralDspPetrucciXPreset.kt`
- `data/presets/NeuralDspGenericPreset.kt`
- `data/presets/HelixNativePreset.kt`
- `data/presets/AmpliTube5Preset.kt`
- `data/presets/GuitarRig7Preset.kt`
- `data/presets/BiasFx2Preset.kt`
- `data/presets/BlankPreset.kt`