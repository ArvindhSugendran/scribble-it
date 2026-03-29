package com.scribble.it.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

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
    secondaryContainer = AmberSurface30,
    onSecondaryContainer = AmberSurface90,

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
    background = AmberGraphite10,
    onBackground = AmberGraphite90,

    surface = AmberGraphite10,
    onSurface = AmberGraphite90,

    surfaceVariant = AmberGraphite30,
    onSurfaceVariant = AmberGraphite80,

    outline = AmberGraphite60,

    inverseSurface = AmberGraphite90,
    inverseOnSurface = AmberGraphite20
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
    secondaryContainer = AmberSurface90,
    onSecondaryContainer = AmberSurface10,

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
    background = AmberGraphite99,
    onBackground = AmberGraphite10,

    surface = AmberGraphite99,
    onSurface = AmberGraphite10,

    surfaceVariant = AmberGraphite90,
    onSurfaceVariant = AmberGraphite30,

    outline = AmberGraphite50,

    inverseSurface = AmberGraphite20,
    inverseOnSurface = AmberGraphite95
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
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

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