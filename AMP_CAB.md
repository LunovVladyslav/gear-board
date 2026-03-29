# GearBoard — Amp & Cabinet Block System

## Goal
Refactor Amplifier and Cabinet sections to work identically to Pedals and
Effects: addable blocks with templates, A/B, customization, and rename.
Currently they only allow adding raw primitives with no templates or blocks.

---

## START: Assess current state
Check `AmpCabSections.kt`, `BoardViewModel.kt`, `BoardRepository.kt`,
`Models.kt`. Report what exists vs what needs changing. Confirm before starting.

---

## STEP 1: Domain model — AmpBlock and CabBlock

In `Models.kt`, add block models mirroring `PedalBlock`/`EffectBlock`:

```kotlin
data class AmpBlock(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val controls: List<ControlType> = emptyList(),
    val appearance: BlockAppearance = BlockAppearance(),
    val layoutMode: BlockLayout = BlockLayout.GRID,
    val currentSlot: AbSlot = AbSlot.A,
    val abStates: Map<AbSlot, Map<String, Float>> = emptyMap()
)

data class CabBlock(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val controls: List<ControlType> = emptyList(),
    val appearance: BlockAppearance = BlockAppearance(),
    val layoutMode: BlockLayout = BlockLayout.GRID,
    val currentSlot: AbSlot = AbSlot.A,
    val abStates: Map<AbSlot, Map<String, Float>> = emptyMap()
)
```

Update `BoardState` to use `List<AmpBlock>` and `List<CabBlock>`
instead of `AmpSettings` and `CabinetSettings`.

---

## STEP 2: Amp templates

```kotlin
object AmpTemplates {

    val CLEAN_AMERICAN = AmpBlock(
        name = "Clean American",
        // Fender-style: loud clean headroom, bright
        controls = listOf(
            Knob("Volume", 0, displayFormat = ZERO_TO_TEN),
            Knob("Bass", 0, displayFormat = ZERO_TO_TEN),
            Knob("Mid", 0, displayFormat = ZERO_TO_TEN),
            Knob("Treble", 0, displayFormat = ZERO_TO_TEN),
            Knob("Reverb", 0, displayFormat = PERCENTAGE),
            Toggle("Bright", 0)
        )
    )

    val BRITISH_CRUNCH = AmpBlock(
        name = "British Crunch",
        // Marshall-style: shared EQ, two channels blended
        controls = listOf(
            Knob("Gain", 0, displayFormat = ZERO_TO_TEN),
            Knob("Bass", 0, displayFormat = ZERO_TO_TEN),
            Knob("Mid", 0, displayFormat = ZERO_TO_TEN),
            Knob("Treble", 0, displayFormat = ZERO_TO_TEN),
            Knob("Presence", 0, displayFormat = ZERO_TO_TEN),
            Knob("Master", 0, displayFormat = ZERO_TO_TEN),
            Toggle("Channel", 0)   // Ch1/Ch2
        )
    )

    val BRITISH_CLASS_A = AmpBlock(
        name = "British Class A",
        // Vox-style: no mid control, tone cut, Top Boost
        controls = listOf(
            Knob("Volume", 0, displayFormat = ZERO_TO_TEN),
            Knob("Bass", 0, displayFormat = ZERO_TO_TEN),
            Knob("Treble", 0, displayFormat = ZERO_TO_TEN),
            Knob("Cut", 0, displayFormat = ZERO_TO_TEN),  // tone cut (inverse)
            Toggle("Top Boost", 0)
        )
    )

    val HIGH_GAIN = AmpBlock(
        name = "High Gain",
        // Mesa-style: multi-channel, tight response
        controls = listOf(
            Knob("Gain", 0, displayFormat = ZERO_TO_TEN),
            Knob("Bass", 0, displayFormat = ZERO_TO_TEN),
            Knob("Mid", 0, displayFormat = ZERO_TO_TEN),
            Knob("Treble", 0, displayFormat = ZERO_TO_TEN),
            Knob("Presence", 0, displayFormat = ZERO_TO_TEN),
            Knob("Master", 0, displayFormat = ZERO_TO_TEN),
            Selector("Channel", 0, listOf("Clean", "Crunch", "Lead")),
            Toggle("Bright", 0)
        )
    )

    val MODERN_HIGH_GAIN = AmpBlock(
        name = "Modern High Gain",
        // Bogner/Diezel-style: boutique, tight + loose switch
        controls = listOf(
            Knob("Gain", 0, displayFormat = ZERO_TO_TEN),
            Knob("Bass", 0, displayFormat = ZERO_TO_TEN),
            Knob("Mid", 0, displayFormat = ZERO_TO_TEN),
            Knob("Treble", 0, displayFormat = ZERO_TO_TEN),
            Knob("Presence", 0, displayFormat = ZERO_TO_TEN),
            Knob("Master", 0, displayFormat = ZERO_TO_TEN),
            Knob("Depth", 0, displayFormat = ZERO_TO_TEN),
            Selector("Channel", 0, listOf("A", "B")),
            Toggle("Tight", 0),
            Toggle("Bright", 0)
        )
    )

    val BASS_AMP = AmpBlock(
        name = "Bass Amp",
        controls = listOf(
            Knob("Gain", 0, displayFormat = ZERO_TO_TEN),
            Knob("Bass", 0, displayFormat = ZERO_TO_TEN),
            Knob("Low Mid", 0, displayFormat = ZERO_TO_TEN),
            Knob("High Mid", 0, displayFormat = ZERO_TO_TEN),
            Knob("Treble", 0, displayFormat = ZERO_TO_TEN),
            Knob("Master", 0, displayFormat = ZERO_TO_TEN),
            Knob("Blend", 0, displayFormat = PERCENTAGE),  // dry/wet
            Toggle("Bright", 0)
        )
    )
}
```

---

## STEP 3: Cabinet templates

```kotlin
object CabTemplates {

    val OPEN_BACK_112 = CabBlock(
        name = "1×12 Open",
        // warm, airy — typical combo
        controls = listOf(
            Selector("Mic Type", 0, listOf("SM57", "MD421", "R121")),
            Fader("Mic Position", 0, orientation = HORIZONTAL, displayFormat = PERCENTAGE),
            Knob("Distance", 0, displayFormat = ZERO_TO_TEN),
            Knob("Level", 0, displayFormat = DECIBELS)
        )
    )

    val CLOSED_BACK_212 = CabBlock(
        name = "2×12 Closed",
        controls = listOf(
            Selector("Mic Type", 0, listOf("SM57", "MD421", "R121", "U87")),
            Fader("Mic Position", 0, orientation = HORIZONTAL, displayFormat = PERCENTAGE),
            Knob("Distance", 0, displayFormat = ZERO_TO_TEN),
            Knob("Level", 0, displayFormat = DECIBELS),
            Knob("Room", 0, displayFormat = PERCENTAGE)
        )
    )

    val CLOSED_BACK_412 = CabBlock(
        name = "4×12 Closed",
        // typical for British and high-gain styles
        controls = listOf(
            Selector("Mic Type", 0, listOf("SM57", "MD421", "R121", "U87")),
            Fader("Mic Position", 0, orientation = HORIZONTAL, displayFormat = PERCENTAGE),
            Knob("Distance", 0, displayFormat = ZERO_TO_TEN),
            Knob("Level", 0, displayFormat = DECIBELS),
            Knob("Room", 0, displayFormat = PERCENTAGE),
            Toggle("Phase", 0)
        )
    )

    val BASS_115 = CabBlock(
        name = "Bass 1×15",
        controls = listOf(
            Selector("Mic Type", 0, listOf("MD421", "D112", "SM7B")),
            Fader("Mic Position", 0, orientation = HORIZONTAL, displayFormat = PERCENTAGE),
            Knob("Distance", 0, displayFormat = ZERO_TO_TEN),
            Knob("Level", 0, displayFormat = DECIBELS)
        )
    )

    val BASS_410 = CabBlock(
        name = "Bass 4×10",
        controls = listOf(
            Selector("Mic Type", 0, listOf("MD421", "D112", "SM7B")),
            Fader("Mic Position", 0, orientation = HORIZONTAL, displayFormat = PERCENTAGE),
            Knob("Distance", 0, displayFormat = ZERO_TO_TEN),
            Knob("Level", 0, displayFormat = DECIBELS),
            Toggle("Tweeter", 0)
        )
    )
}
```

---

## STEP 4: Update BoardViewModel

Add amp/cab block management mirroring pedal/effect methods:

```kotlin
// Amp
val ampBlocks: StateFlow<List<AmpBlock>>
fun addAmpBlock(block: AmpBlock)
fun removeAmpBlock(blockId: String)
fun updateAmpBlock(blockId: String, updated: AmpBlock)
fun renameAmpBlock(blockId: String, name: String)
fun switchAmpAbSlot(blockId: String, slot: AbSlot)
fun saveAmpAbState(blockId: String, slot: AbSlot)
fun addAmpControl(blockId: String, control: ControlType)
fun updateAmpControl(blockId: String, control: ControlType)
fun removeAmpControl(blockId: String, controlId: String)
fun updateAmpBlockAppearance(blockId: String, appearance: BlockAppearance)

// Cab — identical pattern
val cabBlocks: StateFlow<List<CabBlock>>
// ... same methods for cab
```

---

## STEP 5: Update BoardRepository

Persist `List<AmpBlock>` and `List<CabBlock>` using JSON serialization
into `BoardStateEntity`, same approach as pedals/effects.

---

## STEP 6: Rewrite AmpCabSections.kt

Replace the current fixed primitive list with block-based UI,
identical in structure to `PedalCard`/`EffectCard`.

### AmpSection
```kotlin
@Composable
fun AmpSection(
    ampBlocks: List<AmpBlock>,
    onAddBlock: () -> Unit,
    onRemoveBlock: (String) -> Unit,
    onAbSwitch: (String, AbSlot) -> Unit,
    onControlValueChange: (String, String, Float) -> Unit,
    onAddControl: (String) -> Unit,
    onEditControl: (String, ControlType) -> Unit,
    onCustomize: (String) -> Unit,
    onRename: (String) -> Unit,
    // ...
) {
    // LazyRow of AmpBlockCard + ADD AMPLIFIER BLOCK button
}
```

### AmpBlockCard
Identical structure to `PedalCard`:
- Header: block name (accent color) + A/B toggle + MoreVert menu
- Body: `RenderControlList(controls, layoutMode)`
- ContextMenu: Rename, Add Control, Customize, Delete

### Empty state (no blocks)
```kotlin
// Show template picker immediately — no empty void
Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text("Choose an amp type to get started",
         color = GearBoardColors.TextSecondary)
    Spacer(8.dp)
    // Mini grid of template cards (reuse TemplateCard composable)
}
```

---

## STEP 7: AddAmpBlockSheet and AddCabBlockSheet

New bottom sheets (mirror `AddPedalBlockSheet`):

```kotlin
// Thumbnail grid — reuse TemplateCard composable
// Amp icons mapping:
"CLEAN AMERICAN"    → Icons.Default.LightMode     (bright, clean)
"BRITISH CRUNCH"    → Icons.Default.Bolt           (crunch)
"BRITISH CLASS A"   → Icons.Default.Star           (classic)
"HIGH GAIN"         → Icons.Default.Whatshot       (fire)
"MODERN HIGH GAIN"  → Icons.Default.GraphicEq      (modern)
"BASS AMP"          → Icons.Default.Piano          (bass clef feel)
"EMPTY"             → Icons.Default.Add

// Cab icons:
"1×12 OPEN"         → Icons.Default.RadioButtonUnchecked
"2×12 CLOSED"       → Icons.Default.Adjust
"4×12 CLOSED"       → Icons.Default.Apps
"BASS 1×15"         → Icons.Default.Circle
"BASS 4×10"         → Icons.Default.GridView
"EMPTY"             → Icons.Default.Add
```

---

## STEP 8: Update Guided Setup templates

In `GuidedSetupViewModel.kt`, update Guitar/Bass/MultiFX templates
to use `AmpBlock` and `CabBlock` lists instead of `AmpSettings`:

```kotlin
// Guitar Amp Sim template
ampBlocks = listOf(AmpTemplates.HIGH_GAIN)
cabBlocks = listOf(CabTemplates.CLOSED_BACK_412)

// Bass Amp Sim template
ampBlocks = listOf(AmpTemplates.BASS_AMP)
cabBlocks = listOf(CabTemplates.BASS_410)

// Multi-FX template
ampBlocks = listOf(AmpTemplates.MODERN_HIGH_GAIN)
cabBlocks = listOf(CabTemplates.CLOSED_BACK_212)
```

---

## Rules
After each step:
```bash
./gradlew assembleDebug
git add . && git commit -m "feat: step N — description"
```
Fix errors before continuing. Never combine steps.