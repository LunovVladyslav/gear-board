package com.gearboard.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * GearBoard typography system.
 * Uses system default sans-serif (which is typically Roboto on Android,
 * visually close to Inter used in the frontend mock).
 * All label/header text uses UPPERCASE + letter spacing per mock design.
 */
val GearBoardFontFamily = FontFamily.SansSerif

val GearBoardTypography = Typography(
    // 20sp — screen headers ("GEARBOARD", "MIDI MONITOR", etc.)
    headlineLarge = TextStyle(
        fontFamily = GearBoardFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        letterSpacing = 4.sp,
        color = GearBoardColors.Accent
    ),

    // 16sp — section titles ("PEDALS", "AMP", "CAB", "EFFECTS")
    headlineMedium = TextStyle(
        fontFamily = GearBoardFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        letterSpacing = 3.sp,
        color = GearBoardColors.TextPrimary
    ),

    // 14sp — body text, control values, preset names
    bodyLarge = TextStyle(
        fontFamily = GearBoardFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.5.sp,
        color = GearBoardColors.TextPrimary
    ),

    // 12sp — secondary text, descriptions
    bodyMedium = TextStyle(
        fontFamily = GearBoardFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.25.sp,
        color = GearBoardColors.TextSecondary
    ),

    // 10sp — small labels, knob labels, status indicators
    labelSmall = TextStyle(
        fontFamily = GearBoardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        letterSpacing = 1.5.sp,
        color = GearBoardColors.TextSecondary
    ),

    // 12sp — button labels, toggle labels (uppercase)
    labelMedium = TextStyle(
        fontFamily = GearBoardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 2.sp,
        color = GearBoardColors.TextSecondary
    ),

    // 14sp — larger labels, nav items
    labelLarge = TextStyle(
        fontFamily = GearBoardFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 1.sp,
        color = GearBoardColors.TextPrimary
    ),

    // 14sp — titles in cards, dialogs
    titleMedium = TextStyle(
        fontFamily = GearBoardFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        letterSpacing = 1.sp,
        color = GearBoardColors.TextPrimary
    ),

    // 16sp — section headers in settings, modal titles
    titleLarge = TextStyle(
        fontFamily = GearBoardFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        letterSpacing = 2.sp,
        color = GearBoardColors.Accent
    )
)
