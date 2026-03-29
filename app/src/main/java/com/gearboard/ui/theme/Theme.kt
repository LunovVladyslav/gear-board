package com.gearboard.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * GearBoard Material3 dark color scheme.
 * Maps our custom GearBoard colors to Material3 color slots.
 */
private val GearBoardColorScheme = darkColorScheme(
    primary = GearBoardColors.Accent,
    onPrimary = GearBoardColors.TextOnAccent,
    primaryContainer = GearBoardColors.AccentDim,
    onPrimaryContainer = GearBoardColors.TextPrimary,

    secondary = GearBoardColors.SurfaceElevated,
    onSecondary = GearBoardColors.TextPrimary,
    secondaryContainer = GearBoardColors.SurfaceVariant,
    onSecondaryContainer = GearBoardColors.TextPrimary,

    tertiary = GearBoardColors.ConnectedBle,
    onTertiary = GearBoardColors.TextPrimary,

    background = GearBoardColors.Background,
    onBackground = GearBoardColors.TextPrimary,

    surface = GearBoardColors.Surface,
    onSurface = GearBoardColors.TextPrimary,
    surfaceVariant = GearBoardColors.SurfaceVariant,
    onSurfaceVariant = GearBoardColors.TextSecondary,

    error = GearBoardColors.DangerText,
    onError = GearBoardColors.TextPrimary,
    errorContainer = GearBoardColors.Danger,
    onErrorContainer = GearBoardColors.DangerText,

    outline = GearBoardColors.BorderDefault,
    outlineVariant = GearBoardColors.BorderSubtle,

    inverseSurface = GearBoardColors.TextPrimary,
    inverseOnSurface = GearBoardColors.Background,
    inversePrimary = GearBoardColors.AccentDim,

    surfaceTint = GearBoardColors.Accent
)

/**
 * GearBoard app theme.
 * Always dark — this is a stage/studio music controller.
 */
@Composable
fun GearBoardTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = GearBoardColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // enableEdgeToEdge() in MainActivity already sets status/nav bar
            // to transparent and disables decor fitting. Setting solid colors
            // here would fight that and can cause an inset double-count gap.
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = GearBoardTypography,
        content = content
    )
}
