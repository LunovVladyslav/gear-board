package com.gearboard.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cable
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Piano
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gearboard.ui.screens.board.BoardScreen
import com.gearboard.ui.screens.connect.ConnectScreen
import com.gearboard.ui.screens.live.LiveModeScreen
import com.gearboard.ui.screens.midimap.MidiMapScreen
import com.gearboard.ui.screens.monitor.MonitorScreen
import com.gearboard.ui.screens.presets.PresetScreen
import com.gearboard.ui.screens.settings.SettingsScreen
import com.gearboard.ui.screens.setup.GuidedSetupScreen
import com.gearboard.ui.theme.GearBoardColors

/**
 * Navigation routes.
 */
sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Board : Screen("board", "Board", Icons.Default.Dashboard)
    data object Connect : Screen("connect", "Connect", Icons.Default.Cable)
    data object Presets : Screen("presets", "Presets", Icons.Default.Folder)
    data object MidiMap : Screen("midi_map", "MIDI Map", Icons.Default.Piano)
    data object Monitor : Screen("monitor", "Monitor", Icons.Default.Sensors)
    data object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    data object Live : Screen("live", "Live", Icons.Default.MusicNote)
    data object GuidedSetup : Screen("guided_setup", "Setup", Icons.Default.Settings)
}

val bottomNavItems = listOf(
    Screen.Board,
    Screen.Presets,
    Screen.MidiMap,
    Screen.Live,
    Screen.Settings
)

/**
 * Bottom navigation bar with 5 items.
 */
@Composable
fun GearBoardBottomNav(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Hide bottom nav on GuidedSetup screen
    if (currentDestination?.route == Screen.GuidedSetup.route) return

    NavigationBar(
        containerColor = GearBoardColors.Surface,
        contentColor = GearBoardColors.TextPrimary
    ) {
        bottomNavItems.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            NavigationBarItem(
                icon = {
                    Icon(screen.icon, contentDescription = screen.label)
                },
                label = {
                    Text(
                        text = screen.label.uppercase(),
                        fontSize = 9.sp,
                        letterSpacing = 1.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Board.route) {
                            saveState = true
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = GearBoardColors.Accent,
                    selectedTextColor = GearBoardColors.Accent,
                    unselectedIconColor = GearBoardColors.TextDisabled,
                    unselectedTextColor = GearBoardColors.TextDisabled,
                    indicatorColor = GearBoardColors.SurfaceElevated
                )
            )
        }
    }
}

/**
 * Navigation host with all screens.
 * @param startDestination Initial route — "guided_setup" for first launch, "board" otherwise
 */
@Composable
fun GearBoardNavHost(
    navController: NavHostController,
    startDestination: String = Screen.Board.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Board.route) {
            BoardScreen(
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route) {
                        popUpTo(Screen.Board.route) {
                            saveState = true
                            inclusive = false
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(Screen.Connect.route) { ConnectScreen() }
        composable(Screen.Presets.route) { PresetScreen() }
        composable(Screen.MidiMap.route) { MidiMapScreen() }
        composable(Screen.Live.route) { LiveModeScreen() }
        composable(Screen.Monitor.route) { MonitorScreen() }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onOpenMonitor = {
                    navController.navigate(Screen.Monitor.route)
                }
            )
        }
        composable(Screen.GuidedSetup.route) {
            GuidedSetupScreen(
                onNavigateToBoard = {
                    navController.navigate(Screen.Board.route) {
                        popUpTo(Screen.GuidedSetup.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
