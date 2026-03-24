package com.gearboard.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * GearBoard color palette — exact values from the frontend mock.
 * Dark theme only — this is a music hardware controller app.
 */
object GearBoardColors {
    // Backgrounds
    val Background = Color(0xFF0D0D0D)
    val Surface = Color(0xFF1A1A1A)
    val SurfaceVariant = Color(0xFF242424)
    val SurfaceElevated = Color(0xFF2A2A2A)
    val SurfaceHover = Color(0xFF3A3A3A)
    val SurfacePressed = Color(0xFF4A4A4A)

    // Accent (amber)
    val Accent = Color(0xFFE8A020)
    val AccentDim = Color(0xFFB87A10)
    val AccentHover = Color(0xFFC88A18)
    val AccentGlow = Color(0x4DE8A020)  // 30% alpha for shadow/glow effects

    // Text
    val TextPrimary = Color(0xFFF0F0F0)
    val TextSecondary = Color(0xFF888888)
    val TextDisabled = Color(0xFF444444)
    val TextOnAccent = Color(0xFF0D0D0D)  // Dark text on amber background

    // Borders
    val BorderDefault = Color(0xFF2A2A2A)
    val BorderSubtle = Color(0xFF3A3A3A)
    val BorderAccent = Color(0xFFE8A020)

    // Status
    val Danger = Color(0xFF8B2020)
    val DangerText = Color(0xFFFF6B6B)
    val DangerBackground = Color(0x338B2020)  // 20% alpha
    val DangerBorder = Color(0x808B2020)      // 50% alpha

    // Indicators
    val OnIndicator = Color(0xFFE8A020)   // LED "on" — amber
    val OffIndicator = Color(0xFF333333)  // LED "off" — dim
    val ConnectedUsb = Color(0xFF4CAF50)  // Green for USB connected
    val ConnectedBle = Color(0xFF4A90E2)  // Blue for BLE connected

    // Monitor colors
    val MidiIncoming = Color(0xFF4A90E2)  // Blue for incoming MIDI
    val MidiOutgoing = Color(0xFFE8A020)  // Amber for outgoing MIDI

    // Knob specific
    val KnobBodyDark = Color(0xFF1A1A1A)
    val KnobBodyLight = Color(0xFF2A2A2A)
    val KnobSurfaceDark = Color(0xFF2A2A2A)
    val KnobSurfaceLight = Color(0xFF3A3A3A)
    val KnobArc = Color(0xFF1A1A1A)       // Background arc
    val KnobArcActive = Color(0x4DE8A020) // Active arc (30% amber)
}
