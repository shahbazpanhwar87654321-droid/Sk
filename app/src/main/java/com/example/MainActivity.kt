package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.data.preferences.UserPreferencesRepository
import com.example.ui.navigation.AppNavHost
import com.example.ui.navigation.Screen
import com.example.ui.theme.GamingDarkBackground
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefs = UserPreferencesRepository(this)

        setContent {
            val isOnboardingCompleted by prefs.isOnboardingCompleted.collectAsState(initial = true)
            val isDarkMode by prefs.isDarkMode.collectAsState(initial = true)

            MyApplicationTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = GamingDarkBackground
                ) {
                    val startRoute = remember(isOnboardingCompleted) {
                        if (isOnboardingCompleted) Screen.Home.route else Screen.Onboarding.route
                    }
                    AppNavHost(startDestination = startRoute)
                }
            }
        }
    }
}
