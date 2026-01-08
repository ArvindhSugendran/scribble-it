package com.scribble.it.feature_onboarding.presentation.event

sealed class OnboardingPageEvent {
    data object NavigateToHome : OnboardingPageEvent()
}
