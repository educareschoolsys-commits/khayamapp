package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = Color(0xFF00224D),
    primaryContainer = EducareNavyDark,
    onPrimaryContainer = Color(0xFFD0E4FF),
    secondary = DarkGold,
    onSecondary = Color(0xFF3F2E00),
    secondaryContainer = Color(0xFF5A4300),
    onSecondaryContainer = Color(0xFFFFDF9E),
    tertiary = EducareSkyBlue,
    background = DarkBackground,
    onBackground = Color(0xFFE2E8F0),
    surface = DarkSurface,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = DarkSurfaceSubtle,
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569)
)

private val LightColorScheme = lightColorScheme(
    primary = EducareNavyPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0EDFF),
    onPrimaryContainer = EducareNavyDark,
    secondary = EducareGold,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFF3CD),
    onSecondaryContainer = EducareGoldDark,
    tertiary = EducareSkyBlue,
    background = EducareBackground,
    onBackground = Color(0xFF0F172A),
    surface = EducareSurface,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = EducareSurfaceSubtle,
    onSurfaceVariant = Color(0xFF334155),
    outline = EducareOutline
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve Educare branding across all Android versions
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
