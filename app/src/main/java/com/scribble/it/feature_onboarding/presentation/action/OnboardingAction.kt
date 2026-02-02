package com.scribble.it.feature_onboarding.presentation.action

sealed class OnboardingAction {
    data class Changed(val page: Int) : OnboardingAction()
    data object NextClicked : OnboardingAction()
    data object GetStartedClicked : OnboardingAction()
    data object SkipClicked : OnboardingAction()
}