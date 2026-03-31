package com.gearboard.midi

import android.media.midi.MidiInputPort
import android.media.midi.MidiManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gearboard.domain.model.MidiDirection
import com.gearboard.domain.model.MidiEventType
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import app.cash.turbine.test
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class MidiManagerTest {

    private val mockInputPort = mockk<MidiInputPort>(relaxed = true)
    private val mockMidiManager = mockk<MidiManager>(relaxed = true)
    private val mockBle = mockk<BleMidiPeripheral>(relaxed = true)
    private lateinit var manager: GearBoardMidiManager

    @Before fun setup() {
        manager = GearBoardMidiManager(mockMidiManager, mockBle)
        manager.setInputPortForTest(mockInputPort)
    }

    // --- CC encoding ---

    @Test fun `sendControlChange encodes correct bytes for channel 1`() {
        manager.sendControlChange(7, 100, 1)

        val slot = slot<ByteArray>()
        verify { mockInputPort.send(capture(slot), 0, 3) }
        assertThat(slot.captured[0].toInt() and 0xFF).isEqualTo(0xB0)  // CC status, channel 0
        assertThat(slot.captured[1].toInt()).isEqualTo(7)
        assertThat(slot.captured[2].toInt()).isEqualTo(100)
    }

    @Test fun `sendControlChange encodes correct status byte for channel 5`() {
        manager.sendControlChange(10, 64, 5)

        val slot = slot<ByteArray>()
        verify { mockInputPort.send(capture(slot), 0, 3) }
        assertThat(slot.captured[0].toInt() and 0xFF).isEqualTo(0xB4)  // 0xB0 | 4
    }

    @Test fun `sendControlChange uses global midiChannel when channel overload not given`() {
        manager.midiChannel = 2  // channel index 2 → MIDI channel 3
        manager.sendControlChange(1, 64)

        val slot = slot<ByteArray>()
        verify { mockInputPort.send(capture(slot), 0, 3) }
        assertThat(slot.captured[0].toInt() and 0xFF).isEqualTo(0xB2)  // 0xB0 | 2
    }

    // --- PC encoding ---

    @Test fun `sendProgramChange encodes correct bytes`() {
        manager.sendProgramChange(5, 1)

        val slot = slot<ByteArray>()
        verify { mockInputPort.send(capture(slot), 0, 2) }
        assertThat(slot.captured[0].toInt() and 0xFF).isEqualTo(0xC0)
        assertThat(slot.captured[1].toInt()).isEqualTo(5)
    }

    // --- Value clamping ---

    @Test fun `sendControlChange clamps value above 127`() {
        manager.sendControlChange(1, 200, 1)

        val slot = slot<ByteArray>()
        verify { mockInputPort.send(capture(slot), 0, 3) }
        assertThat(slot.captured[2].toInt() and 0xFF).isAtMost(127)
    }

    @Test fun `sendControlChange clamps value below 0`() {
        manager.sendControlChange(1, -10, 1)

        val slot = slot<ByteArray>()
        verify { mockInputPort.send(capture(slot), 0, 3) }
        assertThat(slot.captured[2].toInt() and 0xFF).isAtLeast(0)
    }

    // --- Incoming MIDI ---

    @Test fun `incoming CC emits CONTROL_CHANGE event`() = runTest {
        manager.midiEvents.test {
            manager.simulateIncomingBytes(byteArrayOf(0xB0.toByte(), 7, 100))
            val event = awaitItem()
            assertThat(event.type).isEqualTo(MidiEventType.CONTROL_CHANGE)
            assertThat(event.data1).isEqualTo(7)
            assertThat(event.data2).isEqualTo(100)
            assertThat(event.direction).isEqualTo(MidiDirection.INCOMING)
        }
    }

    @Test fun `incoming PC emits PROGRAM_CHANGE event`() = runTest {
        manager.midiEvents.test {
            manager.simulateIncomingBytes(byteArrayOf(0xC0.toByte(), 3))
            val event = awaitItem()
            assertThat(event.type).isEqualTo(MidiEventType.PROGRAM_CHANGE)
            assertThat(event.data1).isEqualTo(3)
            assertThat(event.direction).isEqualTo(MidiDirection.INCOMING)
        }
    }

    @Test fun `incoming clock byte does not emit a MIDI event`() = runTest {
        manager.midiEvents.test {
            manager.simulateIncomingBytes(byteArrayOf(0xF8.toByte()))
            // No event should arrive — clock bytes are routed to MidiClockReceiver
            expectNoEvents()
        }
    }

    @Test fun `sendControlChange also emits outgoing event`() = runTest {
        manager.midiEvents.test {
            manager.sendControlChange(5, 64, 1)
            val event = awaitItem()
            assertThat(event.type).isEqualTo(MidiEventType.CONTROL_CHANGE)
            assertThat(event.direction).isEqualTo(MidiDirection.OUTGOING)
        }
    }
}
