# GearBoard — Live Mode (with MIDI Clock Sync)

## Concept

Live Mode is a timeline of bars. The user assigns presets to specific bars.
GearBoard switches presets automatically at the right moment.

Two sync modes:
- **Standalone** — GearBoard generates its own clock from user-defined BPM + time signature
- **Synced** — GearBoard receives MIDI Clock from DAW and follows it as slave

Supports tempo and time signature changes mid-song.

After each BLOCK respond "BLOCK N COMPLETE" and wait for confirmation.

---

## MIDI Clock Primer

MIDI Clock messages (0xF8) are sent by the DAW 24 times per quarter note (PPQN = 24).
Additional messages:
- `0xFA` — Start
- `0xFB` — Continue
- `0xFC` — Stop
- `0xF2` — Song Position Pointer (SPP) — current position in beats

GearBoard in Synced mode:
1. Receives 0xFA → resets to bar 1, starts counting
2. Receives 0xF8 → increments tick counter
3. Every 24 ticks = 1 beat
4. Every (24 × numerator) ticks = 1 bar (for quarter-note-based signatures)
5. Every bar boundary → check if preset change scheduled → send MIDI if yes
6. Receives 0xFC → stops playback, holds position

**Time signature over MIDI Clock:**
MIDI Clock carries no time signature information — it only sends beats.
GearBoard must know the time signature from user input to calculate bar boundaries.
Example: 4/4 at 120 BPM → 24 ticks × 4 beats = 96 ticks per bar.
Example: 6/8 at 120 BPM → 24 ticks × 6 eighth-notes = 144 ticks per bar,
but each eighth note = half a quarter note, so adjust tick counting accordingly.

**BPM detection from MIDI Clock:**
Measure time between consecutive 0xF8 messages:
```kotlin
val bpm = 60000f / (tickIntervalMs * 24)
```
Average over last 8 ticks for stability.

---

## Data Model

### TimeSignature
```kotlin
data class TimeSignature(
    val numerator: Int,    // beats per bar: 4, 3, 6, 7, 5
    val denominator: Int   // beat unit: 4 = quarter, 8 = eighth
) {
    // Ticks per bar for MIDI Clock (PPQN = 24)
    fun ticksPerBar(): Int {
        val ticksPerQuarter = 24
        val ticksPerDenominator = ticksPerQuarter * (4f / denominator)
        return (ticksPerDenominator * numerator).toInt()
    }
}

// Examples:
// 4/4 → ticksPerBar = 96
// 6/8 → ticksPerBar = 72  (6 × 12 ticks per eighth)
// 7/8 → ticksPerBar = 84
// 3/4 → ticksPerBar = 72
```

### BarEvent
```kotlin
@Entity(tableName = "bar_events")
data class BarEvent(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val liveSessionId: String,
    val barNumber: Int,               // 1-based

    val presetId: String? = null,     // null = no change
    val bpm: Float? = null,           // standalone mode only
    val timeSignature: TimeSignature? = null  // null = keep previous
)
```

### LiveSession
```kotlin
@Entity(tableName = "live_sessions")
data class LiveSession(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val totalBars: Int,
    val initialBpm: Float,
    val initialTimeSignature: TimeSignature,
    val createdAt: Long = System.currentTimeMillis()
)
```

### LiveState (in-memory)
```kotlin
data class LiveState(
    val isPlaying: Boolean = false,
    val syncMode: SyncMode = SyncMode.STANDALONE,
    val currentBar: Int = 0,
    val currentBeat: Int = 0,
    val currentTick: Int = 0,         // 0..ticksPerBar-1
    val currentBpm: Float = 120f,
    val detectedBpm: Float? = null,   // from MIDI Clock, null if standalone
    val currentTimeSignature: TimeSignature = TimeSignature(4, 4),
    val currentPresetId: String? = null,
    val nextEventBar: Int? = null,
    val clockSource: ClockSource = ClockSource.NONE
)

enum class SyncMode { STANDALONE, SYNCED }
enum class ClockSource { NONE, BLE_MIDI, USB_MIDI }
```

---

## BLOCK 1: Data Layer + ViewModel

Implement Room entities and DAOs as above.

### LiveModeViewModel
```kotlin
class LiveModeViewModel : ViewModel() {

    val sessions: StateFlow<List<LiveSession>>
    val currentSession: StateFlow<LiveSession?>
    val barEvents: StateFlow<List<BarEvent>>
    val liveState: StateFlow<LiveState>
    val syncMode: StateFlow<SyncMode>

    fun createSession(name: String, bpm: Float, ts: TimeSignature, totalBars: Int)
    fun selectSession(session: LiveSession)
    fun setPresetAtBar(bar: Int, presetId: String?)
    fun setBpmAtBar(bar: Int, bpm: Float?)          // standalone only
    fun setTimeSignatureAtBar(bar: Int, ts: TimeSignature?)
    fun clearBar(bar: Int)

    // Standalone controls
    fun startStandalone()
    fun stopPlayback()
    fun resetPlayback()

    // Synced mode — called by MidiClockReceiver
    fun onMidiStart()
    fun onMidiContinue()
    fun onMidiStop()
    fun onMidiTick(tickTimestampMs: Long)
    fun onSongPositionPointer(beats: Int)

    fun setSyncMode(mode: SyncMode)
}
```

---

## BLOCK 2: Standalone Playback Engine

Used when no DAW is connected or user prefers manual control.

```kotlin
class StandalonePlaybackEngine(
    private val midiManager: MidiManager,
    private val presetRepository: PresetRepository
) {
    private var playbackJob: Job? = null

    fun start(
        session: LiveSession,
        events: List<BarEvent>,
        onStateUpdate: (LiveState) -> Unit
    ) {
        playbackJob = CoroutineScope(Dispatchers.Default).launch {

            var currentBpm = session.initialBpm
            var currentTs = session.initialTimeSignature
            var currentPresetId: String? = null
            val eventsByBar = events.groupBy { it.barNumber }

            for (bar in 1..session.totalBars) {
                if (!isActive) break

                eventsByBar[bar]?.firstOrNull()?.let { event ->
                    event.bpm?.let { currentBpm = it }
                    event.timeSignature?.let { currentTs = it }
                    event.presetId?.let { presetId ->
                        if (presetId != currentPresetId) {
                            currentPresetId = presetId
                            val preset = presetRepository.getPreset(presetId)
                            midiManager.sendPreset(preset)
                        }
                    }
                }

                // Emit beat-by-beat updates for UI dot indicator
                val beatMs = BarCalculator.beatDurationMs(currentBpm, currentTs.denominator)
                for (beat in 1..currentTs.numerator) {
                    if (!isActive) break
                    onStateUpdate(LiveState(
                        isPlaying = true,
                        syncMode = SyncMode.STANDALONE,
                        currentBar = bar,
                        currentBeat = beat,
                        currentBpm = currentBpm,
                        currentTimeSignature = currentTs,
                        currentPresetId = currentPresetId,
                        nextEventBar = findNextEventBar(eventsByBar, bar, session.totalBars)
                    ))
                    delay(beatMs)
                }
            }

            onStateUpdate(LiveState(isPlaying = false, currentBpm = session.initialBpm,
                currentTimeSignature = session.initialTimeSignature))
        }
    }

    fun stop() { playbackJob?.cancel() }
}

object BarCalculator {
    fun beatDurationMs(bpm: Float, denominator: Int): Long {
        val quarterNoteMs = 60000f / bpm
        return (quarterNoteMs * (4f / denominator)).toLong()
    }

    fun barDurationMs(bpm: Float, ts: TimeSignature): Long {
        return beatDurationMs(bpm, ts.denominator) * ts.numerator
    }
}
```

---

## BLOCK 3: MIDI Clock Receiver (Synced Mode)

### MidiClockReceiver

Hooks into the existing MidiManager MIDI input parsing.
Must be called on every incoming MIDI byte from BLE or USB.

```kotlin
class MidiClockReceiver(
    private val presetRepository: PresetRepository,
    private val midiManager: MidiManager,
    private val onStateUpdate: (LiveState) -> Unit
) {
    // Session data — set before starting
    var session: LiveSession? = null
    var events: List<BarEvent> = emptyList()

    // Runtime state
    private var tickCount = 0           // ticks within current bar
    private var currentBar = 1
    private var currentBeat = 0
    private var currentPresetId: String? = null
    private var currentTs = TimeSignature(4, 4)
    private var currentBpm = 120f

    // BPM detection
    private val tickTimestamps = ArrayDeque<Long>(8)

    // Call this from MidiManager when 0xFA received
    fun onStart() {
        tickCount = 0
        currentBar = 1
        currentBeat = 0
        currentPresetId = null
        currentTs = session?.initialTimeSignature ?: TimeSignature(4, 4)
        currentBpm = session?.initialBpm ?: 120f
        tickTimestamps.clear()
        applyEventsAtBar(1)
        emitState(isPlaying = true)
    }

    // Call this from MidiManager when 0xFB received
    fun onContinue() {
        emitState(isPlaying = true)
    }

    // Call this from MidiManager when 0xFC received
    fun onStop() {
        emitState(isPlaying = false)
    }

    // Call this from MidiManager on every 0xF8 tick
    fun onTick(timestampMs: Long) {
        // BPM detection — average over last 8 ticks
        tickTimestamps.addLast(timestampMs)
        if (tickTimestamps.size > 8) tickTimestamps.removeFirst()
        if (tickTimestamps.size >= 2) {
            val avgIntervalMs = (tickTimestamps.last() - tickTimestamps.first()) /
                                (tickTimestamps.size - 1).toFloat()
            currentBpm = 60000f / (avgIntervalMs * 24f)
        }

        tickCount++
        val ticksPerBar = currentTs.ticksPerBar()
        val ticksPerBeat = ticksPerBar / currentTs.numerator
        currentBeat = (tickCount / ticksPerBeat) + 1

        // Bar boundary reached
        if (tickCount >= ticksPerBar) {
            tickCount = 0
            currentBar++

            val sess = session ?: return
            if (currentBar > sess.totalBars) {
                onStop()
                return
            }

            applyEventsAtBar(currentBar)
        }

        emitState(isPlaying = true)
    }

    // Call this from MidiManager on 0xF2 (Song Position Pointer)
    fun onSongPositionPointer(beats: Int) {
        // beats = number of MIDI beats (sixth notes) from start
        // 1 MIDI beat = 6 MIDI clock ticks
        val totalTicks = beats * 6
        val ticksPerBar = currentTs.ticksPerBar()
        currentBar = (totalTicks / ticksPerBar) + 1
        tickCount = totalTicks % ticksPerBar
        applyEventsAtBar(currentBar)
        emitState(isPlaying = false)
    }

    private fun applyEventsAtBar(bar: Int) {
        val event = events.firstOrNull { it.barNumber == bar } ?: return
        event.timeSignature?.let { currentTs = it }
        event.presetId?.let { presetId ->
            if (presetId != currentPresetId) {
                currentPresetId = presetId
                CoroutineScope(Dispatchers.IO).launch {
                    val preset = presetRepository.getPreset(presetId)
                    midiManager.sendPreset(preset)
                }
            }
        }
    }

    private fun emitState(isPlaying: Boolean) {
        onStateUpdate(LiveState(
            isPlaying = isPlaying,
            syncMode = SyncMode.SYNCED,
            currentBar = currentBar,
            currentBeat = currentBeat,
            currentTick = tickCount,
            currentBpm = currentBpm,
            detectedBpm = currentBpm,
            currentTimeSignature = currentTs,
            currentPresetId = currentPresetId
        ))
    }
}
```

### Integration with MidiManager

In the existing MIDI input parsing (wherever raw bytes are processed):

```kotlin
// In MidiManager — add to incoming MIDI byte handler
fun onMidiByteReceived(byte: Byte, timestampMs: Long) {
    when (byte.toInt() and 0xFF) {
        0xF8 -> clockReceiver?.onTick(timestampMs)
        0xFA -> clockReceiver?.onStart()
        0xFB -> clockReceiver?.onContinue()
        0xFC -> clockReceiver?.onStop()
        0xF2 -> { /* handle SPP — needs 2 more bytes */ }
        else -> { /* existing MIDI message parsing */ }
    }
}
```

---

## BLOCK 4: Live Mode UI

### Navigation
Add Live tab to bottom navigation (5th item after Settings, or replace one).

### Session list screen

```
LIVE MODE                              [+ New]

┌──────────────────────────────────────────┐
│ My Song                            [▶]  │
│ 120 BPM · 4/4 · 32 bars                │
│ 3 preset changes                        │
└──────────────────────────────────────────┘
```

Swipe to delete. Tap to open timeline.

### Timeline screen

```
┌──────────────────────────────────────────────┐
│ My Song              [STANDALONE | SYNCED]   │
│                                              │
│  ← scroll horizontally →                    │
│ ┌────┬────┬────┬────┬────┬────┬────┬────┐  │
│ │ 1  │ 2  │ 3  │ 4  │ 5  │ 6  │ 7  │ 8  │  │
│ │    │    │    │    │    │    │    │    │  │
│ │CLN │    │HVY │    │    │    │LDR │    │  │
│ │120 │    │    │    │    │    │140 │    │  │
│ └────┴────┴────┴────┴────┴────┴────┴────┘  │
│                                              │
│  Bar 3 / 32  ●●●○  122 BPM  4/4            │
│  Preset: Heavy                              │
│                                              │
│  [Synced: waiting for DAW...]               │  ← synced mode status
│                                              │
│  [◀◀ Reset]      [▶ Start / ■ Stop]         │
└──────────────────────────────────────────────┘
```

**Sync mode toggle:** pill switch in header — STANDALONE / SYNCED.
In Synced mode: Start/Stop button is disabled (DAW controls playback).
Show status: "Waiting for DAW clock..." / "Synced · 122 BPM" / "DAW stopped".

**BarCell:**
```kotlin
@Composable
fun BarCell(
    barNumber: Int,
    event: BarEvent?,
    preset: Preset?,
    isActive: Boolean,
    onTap: () -> Unit
) {
    // Width: 56dp, Height: 80dp
    // Active: amber border + subtle amber background glow
    // Has event: amber top accent bar (2dp)
    // Empty: just bar number in muted color
}
```

**Beat indicator:**
Row of dots below bar info. Filled = elapsed beats in current bar.
```
4/4 at beat 3:  ● ● ● ○
6/8 at beat 4:  ● ● ● ● ○ ○
```

### Bar edit bottom sheet

Tap any bar → bottom sheet:
```
Bar 5

Preset        [Heavy          ▾]   ← picker from existing presets, "None" option
BPM           [               ]   ← number input, placeholder "120 (inherited)"
                                     disabled in Synced mode
Time sig      [4/4            ▾]   ← 4/4, 3/4, 6/8, 7/8, 5/4, 5/8, custom

[Clear bar]                [Done]
```

BPM field is disabled and shows "Controlled by DAW" when in Synced mode.

### New session dialog
```
New Live Session

Name          [My Song        ]
BPM           [120            ]   ← used for Standalone + UI preview in Synced
Time sig      [4/4 ▾          ]
Total bars    [32             ]

[Cancel]                  [Create]
```

---

## BLOCK 5: BLE MIDI Latency Warning

When Synced mode is active and clock source is BLE:
Show a one-time non-blocking banner:

```
┌─────────────────────────────────────────────────┐
│ ⚠  BLE MIDI has ~10–20ms jitter.               │
│    For tighter sync, use USB MIDI.    [Got it] │
└─────────────────────────────────────────────────┘
```

Show once per session. Do not show for USB MIDI connections.
Store "shown" flag in SharedPreferences key `live_ble_warning_shown`.

---

## What NOT to implement in v1

- Metronome audio click track
- MIDI Clock output (GearBoard as master clock)
- Sub-bar (beat-level) preset switching
- Import from DAW project files
- Loop mode
- Export/share sessions
- MIDI Timecode (MTC) — more complex than MIDI Clock

---

## Build & test after each block

**BLOCK 2:** Create 4/4 session at 120 BPM, assign presets at bars 1 and 3.
Start standalone. Verify preset switches after ~4 seconds (2 bars × 2000ms).

**BLOCK 3:** Connect DAW (Ableton/Reaper), enable MIDI Clock output to phone.
Switch to Synced mode. Press Play in DAW. Verify:
- BPM detected correctly in UI
- Preset switches at correct bar
- Stop in DAW → GearBoard stops
- BLE warning banner appears

**BLOCK 4:** Verify timeline scrolls correctly for 32+ bars,
active bar follows playback in real time,
bar edit sheet saves and clears correctly.