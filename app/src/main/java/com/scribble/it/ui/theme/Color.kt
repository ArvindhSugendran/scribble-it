package com.scribble.it.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// =======================
// Primary Palette (Amber)
// =======================
val Amber10 = Color(0xFF2A1600)
val Amber20 = Color(0xFF4A2B00)
val Amber30 = Color(0xFF6B4000)
val Amber40 = Color(0xFF8C5600)
val Amber80 = Color(0xFFFFC266)
val Amber90 = Color(0xFFFFE2B3)

// =======================
// Secondary Palette (Violet)
// =======================

val Violet10 = Color(0xFF1C1626)
val Violet20 = Color(0xFF2F2440)
val Violet30 = Color(0xFF43305A)
val Violet40 = Color(0xFF594075)
val Violet80 = Color(0xFFD6C7FF)
val Violet90 = Color(0xFFEDE6FF)

// =======================
// Secondary Palette (Amber Version)
// =======================

val AmberSurface10 = Color(0xFF1F1408)
val AmberSurface20 = Color(0xFF2C1F10)
val AmberSurface30 = Color(0xFF3A2F1F)
val AmberSurface40 = Color(0xFF4A3A28)
val AmberSurface80 = Color(0xFFFFD9A3)
val AmberSurface90 = Color(0xFFFFF3E0)

// =======================
// Tertiary Palette (Moss)
// =======================
val Moss10 = Color(0xFF0E2624)
val Moss20 = Color(0xFF1A403C)
val Moss30 = Color(0xFF265A54)
val Moss40 = Color(0xFF34746C)
val Moss80 = Color(0xFF9FE3D9)
val Moss90 = Color(0xFFCFF3EE)

// =======================
// Error Palette (Reds)
// =======================
val Red10 = Color(0xFF410001)
val Red20 = Color(0xFF680003)
val Red30 = Color(0xFF930006)
val Red40 = Color(0xFFBA1B1B)
val Red80 = Color(0xFFFFB4A9)
val Red90 = Color(0xFFFFDAD4)

// =======================
// Neutral Palette (Greys)
// =======================
val Grey10 = Color(0xFF191C1D)
val Grey20 = Color(0xFF2D3132)
val Grey90 = Color(0xFFE0E3E3)
val Grey95 = Color(0xFFEFF1F1)
val Grey99 = Color(0xFFFBFDFD)

// =======================
// Variant Palette (surfaces/containers)
// =======================
val AmberGraphite10 = Color(0xFF181410)
val AmberGraphite20 = Color(0xFF24201B)
val AmberGraphite30 = Color(0xFF312B24)
val AmberGraphite50 = Color(0xFF5F554B)
val AmberGraphite60 = Color(0xFF786D63)
val AmberGraphite80 = Color(0xFFD3C8BC)
val AmberGraphite90 = Color(0xFFEAE2D9)
val AmberGraphite95 = Color(0xFFF4EEE7)
val AmberGraphite99 = Color(0xFFFDFBF8)

// Custom Colors
@Immutable
data class ScribbleColors(
    val canvasDrawIconBackground: Color,
    val canvasDrawActionButtonRipple: Color
)