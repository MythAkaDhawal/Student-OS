package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = StudentPrimaryDark,
    secondary = StudentSecondaryDark,
    tertiary = StudentTertiaryDark,
    background = StudentBackgroundDark,
    surface = StudentSurfaceDark,
    surfaceVariant = ElegantDarkSurfaceVariant,
    outline = ElegantDarkOutline,
    onPrimary = Color(0xFF003258), // Deep dark contrasting blue on primary highlights
    onSecondary = StudentBackgroundDark,
    onTertiary = Color(0xFF001D36),
    onBackground = Color(0xFFE2E2E6), // Light grayish white
    onSurface = Color(0xFFE2E2E6),
    onSurfaceVariant = Color(0xFFC4C7C5), // Soft/secondary light gray text
    error = ErrorColor
)

private val LightColorScheme = lightColorScheme(
    primary = StudentPrimaryLight,
    secondary = StudentSecondaryLight,
    tertiary = StudentTertiaryLight,
    background = StudentBackgroundLight,
    surface = StudentSurfaceLight,
    error = ErrorColor
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Keep dynamicColor false to enforce our high-polished intentional Slate Student OS theme!
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
