package com.scribble.it.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(

    // Primary — Burnt Amber
    primary = Amber80,
    onPrimary = Amber20,
    primaryContainer = Amber30,
    onPrimaryContainer = Amber90,
    inversePrimary = Amber40,

    // Secondary — Dusty Violet
    secondary = Violet80,
    onSecondary = Violet20,
    secondaryContainer = Violet30,
    onSecondaryContainer = Violet90,

    // Tertiary — Moss Teal
    tertiary = Moss80,
    onTertiary = Moss20,
    tertiaryContainer = Moss30,
    onTertiaryContainer = Moss90,

    // Error (keep Material defaults or your existing reds)
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,

    // Background & Surfaces — Warm Graphite
    background = Graphite10,
    onBackground = Graphite90,

    surface = Graphite10,
    onSurface = Graphite90,

    surfaceVariant = Graphite20,
    onSurfaceVariant = Graphite80,

    outline = Graphite60,

    inverseSurface = Graphite90,
    inverseOnSurface = Graphite20
)

private val LightColorScheme = lightColorScheme(

    // Primary — Burnt Amber
    primary = Amber40,
    onPrimary = Color.White,
    primaryContainer = Amber90,
    onPrimaryContainer = Amber10,
    inversePrimary = Amber80,

    // Secondary — Dusty Violet
    secondary = Violet40,
    onSecondary = Color.White,
    secondaryContainer = Violet90,
    onSecondaryContainer = Violet10,

    // Tertiary — Moss Teal
    tertiary = Moss40,
    onTertiary = Color.White,
    tertiaryContainer = Moss90,
    onTertiaryContainer = Moss10,

    // Error
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,

    // Background & Surfaces — Warm Graphite
    background = Graphite99,
    onBackground = Graphite10,

    surface = Graphite99,
    onSurface = Graphite10,

    surfaceVariant = Graphite90,
    onSurfaceVariant = Graphite30,

    outline = Graphite50,

    inverseSurface = Graphite20,
    inverseOnSurface = Graphite95
)

val LightScribbleColors = ScribbleColors(
    canvasDrawIconBackground = Color(0xFFDACCB9),
    canvasDrawActionButtonRipple = Color(0xFF000000).copy(alpha = 0.28f)
)

val DarkScribbleColors = ScribbleColors(
    canvasDrawIconBackground = Color(0xFF3A2F1E),
    canvasDrawActionButtonRipple = Color(0xFFE6D3B1).copy(alpha = 0.18f)

)

val LocalScribbleColors = staticCompositionLocalOf {
    LightScribbleColors
}

object ScribbleTheme {
    val scribbleColors: ScribbleColors
        @Composable
        get() = LocalScribbleColors.current
}

@Composable
fun ScribbleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val scribbleColors = if (darkTheme) {
        DarkScribbleColors
    } else {
        LightScribbleColors
    }

    CompositionLocalProvider(
        LocalScribbleColors provides scribbleColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = shapes,
            content = content
        )
    }
}