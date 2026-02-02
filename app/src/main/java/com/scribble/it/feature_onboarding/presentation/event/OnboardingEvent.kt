package com.scribble.it.feature_onboarding.presentation.event

sealed class OnboardingEvent {
    data object NavigateToHome : OnboardingEvent()
}
