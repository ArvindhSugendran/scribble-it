package com.scribble.it.feature_onboarding.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

data class OnboardingPageMetrics(
    val illustrationSize: Dp,
    val horizontalPadding: Dp,
    val verticalSpacing: Dp,
    val titleFontSize: TextUnit,
    val descriptionFontSize: TextUnit
)
