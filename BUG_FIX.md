# GearBoard Bug Fixes

## START: Assess current state first
Check each step's relevant files. Report status (DONE/PARTIAL/TODO) for all steps, then ask for confirmation before implementing. Start from first non-DONE step.

---

## STEP 1: Reduce padding
Files: `PedalCard.kt`, `EffectCard.kt`, `ControlRenderer.kt`
- Card padding: 4dp outer, 8dp inner
- A/B row padding: 4dp horizontal, 2dp vertical
- Control grid spacing: 4dp
- Keep knob touch target ≥ 64dp

## STEP 2: Fix grid layout
File: `ControlRenderer.kt`
- GRID: `GridCells.Adaptive(80.dp)`, spacing 4dp, maxHeight 400dp
- ROW: `LazyRow`, spacing 4dp
- COMPACT: `GridCells.Fixed(3)`, spacing 2dp
- Default for all new blocks: GRID (never single column)

## STEP 3: Show block name
Files: `PedalCard.kt`, `EffectCard.kt`
- Header row: `block.name.uppercase()` with `accentColor`, weight(1f), ellipsis
- Then: AbToggle → MoreVert icon (24dp)

## STEP 4: Accent color → all primitives
- New file `LocalAccentColor.kt`: `compositionLocalOf { GearBoardColors.Accent }`
- Wrap card content in `CompositionLocalProvider(LocalAccentColor provides block.appearance.accentColor)`
- In every primitive (GearKnob, GearToggle, GearTap, GearSelector, GearFader, GearPad): replace `GearBoardColors.Accent` with `LocalAccentColor.current`

## STEP 5: Fix status bar overlap
Files: `MainActivity.kt`, `ConnectionStatusBar.kt`, all screens

`MainActivity.kt`: add `enableEdgeToEdge()` before `setContent`

Root Scaffold:
```kotlin
Scaffold(
    topBar = { TopAppBar(windowInsets = TopAppBarDefaults.windowInsets, ...) },
    bottomBar = { NavigationBar(windowInsets = NavigationBarDefaults.windowInsets, ...) },
    contentWindowInsets = ScaffoldDefaults.contentWindowInsets
) { padding -> NavHost(modifier = Modifier.padding(padding)) }
```

Any screen with custom Column (no Scaffold): add `.statusBarsPadding()`

Verify: Board, Settings, MIDI Map, Presets, Guided Setup screens.

## STEP 6: MIDI channel chip in Board header
Files: `BoardScreen.kt`, `BoardViewModel.kt`

Add to TopAppBar title row:
```kotlin
Surface(onClick = { showChannelPicker = true },
        shape = RoundedCornerShape(4.dp),
        color = GearBoardColors.SurfaceVariant) {
    Text("CH $channel",
         modifier = Modifier.padding(horizontal=6.dp, vertical=2.dp),
         style = MaterialTheme.typography.labelSmall,
         color = GearBoardColors.TextSecondary)
}
```
On tap: DropdownMenu channels 1-16.
ViewModel: add `midiChannel: StateFlow<Int>` + `setMidiChannel(Int)`.
Keep channel selector in Settings (do not remove).

## STEP 7: Empty AMP/CAB templates
File: `AmpCabSections.kt`, `BoardViewModel.kt`

When section empty show: label + two buttons:
"Standard [Amp/Cab]" → apply template | "+ Add Control" → add manual

AmpTemplate.STANDARD: Gain, Bass, Mid, Treble, Presence, Master
CabTemplate.STANDARD: Model selector, Mic selector, Position fader

ViewModel: `fun applyAmpTemplate(AmpTemplate)` + `fun applyCabTemplate(CabTemplate)`

## STEP 8: Template thumbnails
Files: `AddPedalBlockSheet.kt`, `AddEffectSheet.kt`

Replace text list with `LazyVerticalGrid(GridCells.Fixed(2), spacing=8dp)`.
Each card 80dp height: icon 24dp (accentColor) + name labelMedium + "N controls" labelSmall secondary.

Icons: Overdrive→Bolt, Modulation→Waves, Delay→Repeat, Reverb→BlurOn,
Wah→SwapVert, EQ→Equalizer, Compressor→Compress, Gate→Block, Empty→Add

## STEP 9: Polish
- Toggle: `fillMaxWidth(0.8f)`, height 40dp
- `!` badge: 16dp size, offset(4dp,-4dp), text 9sp
- A/B buttons: 28×24dp each
- Section divider: 0.5dp, BorderDefault color, 16dp horizontal padding
- Replace green `+` tint with `GearBoardColors.Accent`
- Section dot: `isActive = controls.any { it.ccNumber != 0 }`

---

## Rules
After each step:
```bash
./gradlew assembleDebug
git add . && git commit -m "fix: step N — description"
```
Never combine steps. Fix build errors before continuing.