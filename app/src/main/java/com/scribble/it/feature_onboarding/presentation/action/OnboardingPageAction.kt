package com.scribble.it.feature_onboarding.presentation.action

sealed class OnboardingPageAction {
    data class PageChanged(val page: Int) : OnboardingPageAction()
    data object NextClicked : OnboardingPageAction()
    data object GetStartedClicked : OnboardingPageAction()
    data object SkipClicked : OnboardingPageAction()
}