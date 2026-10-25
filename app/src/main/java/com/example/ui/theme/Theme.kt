package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FootballDarkColorScheme = darkColorScheme(
    primary = PitchGreen,
    onPrimary = PitchDarker,
    primaryContainer = PitchGreenDark,
    onPrimaryContainer = TextPrimary,
    secondary = TrophyGold,
    onSecondary = Color(0xFF261900),
    secondaryContainer = GoldContainer,
    onSecondaryContainer = TrophyGold,
    background = StadiumPitchDark,
    onBackground = TextPrimary,
    surface = CardPitchGreen,
    onSurface = TextPrimary,
    surfaceVariant = CardElevated,
    onSurfaceVariant = TextMuted,
    outline = CardBorder,
    outlineVariant = Color(0xFF1E3527),
    error = ErrorRed,
    onError = Color.White
)

private val FootballLightColorScheme = lightColorScheme(
    primary = PitchGreenDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC7F9DC),
    onPrimaryContainer = Color(0xFF003820),
    secondary = TrophyGoldDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFECC4),
    onSecondaryContainer = Color(0xFF4A3400),
    background = Color(0xFFF3F7F4),
    onBackground = Color(0xFF0D1812),
    surface = Color.White,
    onSurface = Color(0xFF0D1812),
    surfaceVariant = Color(0xFFE4EDE7),
    onSurfaceVariant = Color(0xFF384E42),
    outline = Color(0xFFB0C4B8),
    outlineVariant = Color(0xFFD0DED5),
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek stadium dark mode
    dynamicColor: Boolean = false, // Keep authentic football branding
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) FootballDarkColorScheme else FootballLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
