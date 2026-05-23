package com.aj.tempshot.ui.theme

import androidx.compose.foundation.isSystemInDarkMode
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Tertiary,
    background = Background,
    surface = Surface,
    error = Error,
    primaryContainer = PrimaryContainer,
    secondaryContainer = SecondaryContainer,
    tertiaryContainer = TertiaryContainer,
    errorContainer = ErrorContainer,
    outline = Outline
)

private val DarkColors = darkColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Tertiary,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    error = Error,
    primaryContainer = PrimaryContainer,
    secondaryContainer = SecondaryContainer,
    tertiaryContainer = TertiaryContainer,
    errorContainer = ErrorContainer,
    outline = Outline
)

@Composable
fun TempShotTheme(
    useDarkTheme: Boolean = isSystemInDarkMode(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
