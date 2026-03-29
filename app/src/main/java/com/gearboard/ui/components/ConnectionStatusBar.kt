package com.gearboard.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gearboard.ui.theme.GearBoardColors
import com.gearboard.ui.theme.GearBoardDimensions

/**
 * Connection state sealed class used by ConnectionStatusBar.
 */
sealed class ConnectionState {
    data object Disconnected : ConnectionState()
    data object Scanning : ConnectionState()
    data class Connecting(val deviceName: String) : ConnectionState()
    data class Connected(val deviceName: String, val type: ConnectionType) : ConnectionState()
    data class Error(val message: String) : ConnectionState()
}

enum class ConnectionType { USB, BLUETOOTH }

/**
 * ConnectionStatusBar — persistent top bar indicator for MIDI connection status.
 *
 * Always visible. Shows:
 * - Disconnected: grey dot
 * - Scanning/Connecting: pulsing amber dot
 * - Connected USB: green dot + device name
 * - Connected BLE: blue dot + device name
 * - Error: red dot + error message
 */
@Composable
fun ConnectionStatusBar(
    state: ConnectionState,
    modifier: Modifier = Modifier
) {
    val dotColor by animateColorAsState(
        targetValue = when (state) {
            is ConnectionState.Disconnected -> GearBoardColors.OffIndicator
            is ConnectionState.Scanning -> GearBoardColors.Accent
            is ConnectionState.Connecting -> GearBoardColors.Accent
            is ConnectionState.Connected -> when (state.type) {
                ConnectionType.USB -> GearBoardColors.ConnectedUsb
                ConnectionType.BLUETOOTH -> GearBoardColors.ConnectedBle
            }
            is ConnectionState.Error -> GearBoardColors.DangerText
        },
        animationSpec = tween(300),
        label = "dotColor"
    )

    // Pulse animation for Scanning/Connecting states
    val isPulsing = state is ConnectionState.Scanning || state is ConnectionState.Connecting
    val pulseAlpha = if (isPulsing) {
        val transition = rememberInfiniteTransition(label = "pulse")
        val alpha by transition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(800),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulseAlpha"
        )
        alpha
    } else {
        1f
    }

    val statusText = when (state) {
        is ConnectionState.Disconnected -> "Disconnected"
        is ConnectionState.Scanning -> "Scanning..."
        is ConnectionState.Connecting -> "Connecting to ${state.deviceName}..."
        is ConnectionState.Connected -> state.deviceName
        is ConnectionState.Error -> state.message
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(GearBoardColors.Surface)
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(GearBoardDimensions.TopBarHeight)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // GEARBOARD title
        Text(
            text = "GEARBOARD",
            color = GearBoardColors.Accent,
            fontSize = 14.sp,
            letterSpacing = 3.sp,
            modifier = Modifier.weight(1f)
        )

        // Connection status
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Status dot
            Box(
                modifier = Modifier
                    .size(GearBoardDimensions.ConnectionDotSize)
                    .alpha(pulseAlpha)
                    .clip(CircleShape)
                    .background(dotColor)
            )

            // Status text
            Text(
                text = statusText,
                color = GearBoardColors.TextSecondary,
                fontSize = 11.sp,
                maxLines = 1
            )
        }
    }
}

/**
 * GearBoardTopBar — shared top bar used by Presets, MidiMap, and Settings screens.
 * Matches the Board screen's top bar: 48dp height, Surface background, "GEARBOARD" left.
 * Does NOT add statusBarsPadding — the parent Scaffold/layout is responsible for
 * positioning this below the system status bar.
 *
 * @param actions optional right-side content (icons, buttons, etc.)
 */
@Composable
fun GearBoardTopBar(
    actions: @Composable RowScope.() -> Unit = {}
) {
    Surface(
        color = GearBoardColors.Surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(GearBoardDimensions.TopBarHeight)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "GEARBOARD",
                color = GearBoardColors.Accent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
                modifier = Modifier.weight(1f)
            )
            actions()
        }
    }
}

@Composable
fun ConnectionDot(
    state: ConnectionState,
    modifier: Modifier = Modifier
) {
    val dotColor by animateColorAsState(
        targetValue = when (state) {
            is ConnectionState.Disconnected -> GearBoardColors.OffIndicator
            is ConnectionState.Scanning -> GearBoardColors.Accent
            is ConnectionState.Connecting -> GearBoardColors.Accent
            is ConnectionState.Connected -> when (state.type) {
                ConnectionType.USB -> GearBoardColors.ConnectedUsb
                ConnectionType.BLUETOOTH -> GearBoardColors.ConnectedBle
            }
            is ConnectionState.Error -> GearBoardColors.DangerText
        },
        animationSpec = tween(300),
        label = "dotColor"
    )

    val isPulsing = state is ConnectionState.Scanning || state is ConnectionState.Connecting
    val pulseAlpha = if (isPulsing) {
        val transition = rememberInfiniteTransition(label = "pulse")
        val alpha by transition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(800),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulseAlpha"
        )
        alpha
    } else {
        1f
    }

    Box(
        modifier = modifier
            .size(GearBoardDimensions.ConnectionDotSize)
            .alpha(pulseAlpha)
            .clip(CircleShape)
            .background(dotColor)
    )
}
