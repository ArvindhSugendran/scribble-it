package com.scribble.it.feature_onboarding.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class OnboardingPage(
    val id: Int,
    val title: String,
    val description: String,
    val imageVector: ImageVector,
    val backgroundColor: Color
)

