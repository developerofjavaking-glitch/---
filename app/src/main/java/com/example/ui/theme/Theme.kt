package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Pure & Elegant Islamic Light Color Scheme (Dark mode removed as requested)
private val LightColorScheme = lightColorScheme(
    primary = QuranGreenPrimary,
    onPrimary = QuranGreenOnPrimary,
    primaryContainer = QuranGreenContainer,
    onPrimaryContainer = QuranGreenOnContainer,
    secondary = QuranGoldSecondary,
    onSecondary = QuranGoldOnSecondary,
    secondaryContainer = QuranGoldContainer,
    onSecondaryContainer = QuranGoldOnContainer,
    tertiary = Color(0xFF065F46),
    onTertiary = Color.White,
    background = QuranBackgroundLight,
    surface = QuranSurfaceLight,
    surfaceVariant = QuranSurfaceVariantLight,
    onBackground = QuranTextPrimaryLight,
    onSurface = QuranTextPrimaryLight,
    onSurfaceVariant = QuranTextSecondaryLight,
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Always forced to light mode as requested by user
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Strictly Light Theme for best readability and serene Islamic aesthetics
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
