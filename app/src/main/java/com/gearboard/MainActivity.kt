package com.gearboard

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gearboard.data.repository.SettingsRepository
import com.gearboard.ui.components.ConnectionState
import com.gearboard.ui.components.ConnectionStatusBar
import com.gearboard.ui.navigation.GearBoardBottomNav
import com.gearboard.ui.navigation.GearBoardNavHost
import com.gearboard.ui.navigation.Screen
import com.gearboard.ui.screens.connect.ConnectViewModel
import com.gearboard.ui.theme.GearBoardTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check guided setup completion synchronously (fast DataStore read)
        val setupComplete = runBlocking { settingsRepository.guidedSetupComplete.first() }
        val startRoute = if (setupComplete) Screen.Board.route else Screen.GuidedSetup.route

        setContent {
            GearBoardTheme {
                val navController = rememberNavController()
                val connectViewModel: ConnectViewModel = hiltViewModel()
                val connectionState by connectViewModel.connectionState.collectAsStateWithLifecycle()

                // Keep screen on when connected
                DisposableEffect(connectionState) {
                    if (connectionState is ConnectionState.Connected) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    } else {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    }
                    onDispose {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    }
                }

                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                val showGlobalTopBar = currentRoute != Screen.Board.route && currentRoute != Screen.GuidedSetup.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { if (showGlobalTopBar) ConnectionStatusBar(state = connectionState) },
                    bottomBar = { GearBoardBottomNav(navController) }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                    ) {
                        GearBoardNavHost(
                            navController = navController,
                            startDestination = startRoute
                        )
                    }
                }
            }
        }
    }
}
