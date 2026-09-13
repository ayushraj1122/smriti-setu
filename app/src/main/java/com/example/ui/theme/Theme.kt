package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
    darkColorScheme(
        primary = DarkPrimary,
        secondary = DarkSecondary,
        tertiary = DarkTertiary,
        background = SmritiHighContrastBg,
        surface = SmritiHighContrastCard,
        onPrimary = Color.Black,
        onSecondary = Color.Black,
        onBackground = Color.White,
        onSurface = Color.White
    )

private val LightColorScheme =
    lightColorScheme(
        primary = VibrantBluePrimary,
        secondary = VibrantOrientationBorder,
        tertiary = VibrantAttentionBorder,
        background = VibrantBg,
        surface = VibrantSurface,
        surfaceVariant = VibrantSurfaceVariant,
        primaryContainer = VibrantBlueContainer,
        onPrimaryContainer = VibrantOnBlueContainer,
        secondaryContainer = VibrantOrientationBg,
        onSecondaryContainer = VibrantOrientationText,
        tertiaryContainer = VibrantAttentionBg,
        onTertiaryContainer = VibrantAttentionText,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = VibrantTextPrimary,
        onSurface = VibrantTextPrimary,
        onSurfaceVariant = VibrantTextSecondary
    )

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

