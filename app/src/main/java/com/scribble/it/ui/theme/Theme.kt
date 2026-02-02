package com.scribble.it.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
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

@Composable
fun ScribbleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes,
        content = content
    )
}