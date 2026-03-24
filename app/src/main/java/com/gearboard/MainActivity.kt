package com.gearboard

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.gearboard.ui.components.ConnectionState
import com.gearboard.ui.components.ConnectionStatusBar
import com.gearboard.ui.navigation.GearBoardBottomNav
import com.gearboard.ui.navigation.GearBoardNavHost
import com.gearboard.ui.screens.connect.ConnectViewModel
import com.gearboard.ui.theme.GearBoardTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { ConnectionStatusBar(state = connectionState) },
                    bottomBar = { GearBoardBottomNav(navController) }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        GearBoardNavHost(navController)
                    }
                }
            }
        }
    }
}
