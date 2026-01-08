package com.scribble.it.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val shapes = Shapes(
    // Small components like chips, buttons
    small = RoundedCornerShape(8.dp),

    // Medium components like cards
    medium = RoundedCornerShape(16.dp),

    // Large surfaces like dialogs, bottom sheets
    large = RoundedCornerShape(24.dp)
)