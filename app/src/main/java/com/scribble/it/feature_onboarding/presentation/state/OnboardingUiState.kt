package com.scribble.it.feature_onboarding.presentation.state

import com.scribble.it.feature_onboarding.domain.model.OnboardingPage

data class OnboardingUiState(
    val pages: List<OnboardingPage> = emptyList(),
    val currentPage: Int = 0,
    val totalPages: Int = 0
)
