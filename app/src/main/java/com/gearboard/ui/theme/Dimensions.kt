package com.gearboard.ui.theme

import androidx.compose.ui.unit.dp

/**
 * GearBoard dimension constants for consistent sizing across the app.
 * Based on the spec requirements and frontend mock measurements.
 */
object GearBoardDimensions {
    // Knob
    val KnobMinSize = 64.dp          // MANDATORY minimum touch target
    val KnobDefaultSize = 64.dp      // Default knob diameter
    val KnobLargeSize = 80.dp        // Large knob (e.g., for amp controls)
    val KnobIndicatorWidth = 2.dp    // Amber indicator line width
    val KnobArcStrokeWidth = 2.dp    // Position arc stroke width

    // Knob rotation
    const val KNOB_MIN_ANGLE = -135f  // Start angle in degrees
    const val KNOB_MAX_ANGLE = 135f   // End angle in degrees
    const val KNOB_TOTAL_ANGLE = 270f // Total rotation range
    const val KNOB_SENSITIVITY = 200f // 1dp drag = 1/200 value change

    // Toggle
    val ToggleMinHeight = 56.dp      // Minimum toggle button height
    val ToggleStompSize = 64.dp      // Stomp button size
    val SwitchWidth = 48.dp          // Switch toggle width
    val SwitchHeight = 24.dp         // Switch toggle height
    val SwitchThumbSize = 16.dp      // Switch thumb diameter

    // Pedal card
    val PedalCardWidth = 160.dp      // From spec
    val PedalCardMinWidth = 160.dp
    val PedalCardMaxWidth = 400.dp

    // Effect/Cab card
    val EffectCardWidth = 240.dp
    val CabCardWidth = 320.dp

    // Section header
    val SectionDotSize = 12.dp       // On/off indicator dot
    val SectionHeaderHeight = 48.dp

    // General spacing
    val SpacingXS = 4.dp
    val SpacingS = 8.dp
    val SpacingM = 12.dp
    val SpacingL = 16.dp
    val SpacingXL = 24.dp
    val SpacingXXL = 32.dp

    // Border radius
    val RadiusS = 4.dp
    val RadiusM = 8.dp
    val RadiusL = 12.dp
    val RadiusXL = 16.dp

    // Border width
    val BorderThin = 1.dp
    val BorderMedium = 2.dp

    // Bottom navigation
    val BottomNavHeight = 64.dp
    val BottomNavIconSize = 24.dp

    // Top bar
    val TopBarHeight = 48.dp
    val ConnectionDotSize = 8.dp

    // Control size multiplier range (Settings)
    const val CONTROL_SIZE_MIN = 0.8f
    const val CONTROL_SIZE_MAX = 1.4f
    const val CONTROL_SIZE_DEFAULT = 1.0f

    // Fader
    val FaderMinHeight = 120.dp
    val FaderTrackWidth = 40.dp

    // Pad
    val PadMinSize = 80.dp

    // PresetNav
    val PresetNavArrowSize = 48.dp

    // Selector
    val SelectorMinHeight = 40.dp

    // Monitor
    const val MONITOR_MAX_EVENTS = 500
}
