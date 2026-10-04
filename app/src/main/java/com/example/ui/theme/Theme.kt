package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val GamingColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = GamingDarkBackground,
    primaryContainer = GamingSurfaceElevated,
    onPrimaryContainer = NeonCyan,
    secondary = CyberViolet,
    onSecondary = TextPrimary,
    secondaryContainer = GamingSurfaceVariant,
    onSecondaryContainer = CyberViolet,
    tertiary = NeonRed,
    onTertiary = TextPrimary,
    background = GamingDarkBackground,
    onBackground = TextPrimary,
    surface = GamingSurface,
    onSurface = TextPrimary,
    surfaceVariant = GamingSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    outlineVariant = BorderNeon
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to gaming dark theme
    dynamicColor: Boolean = false, // Keep consistent cyber gaming styling
    content: @Composable () -> Unit
) {
    val colorScheme = GamingColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = GamingDarkBackground.toArgb()
                window.navigationBarColor = GamingDarkBackground.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
