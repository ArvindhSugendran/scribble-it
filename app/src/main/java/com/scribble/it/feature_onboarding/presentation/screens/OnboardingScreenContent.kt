package com.scribble.it.feature_onboarding.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.scribble.it.feature_onboarding.presentation.action.OnboardingPageAction
import com.scribble.it.feature_onboarding.presentation.components.OnboardingBottomSection
import com.scribble.it.feature_onboarding.presentation.components.OnboardingPage
import com.scribble.it.feature_onboarding.presentation.state.OnboardingUiState

@Composable
fun OnboardingScreenContent(
    modifier: Modifier = Modifier,
    uiState: OnboardingUiState,
    onAction: (OnboardingPageAction) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { uiState.totalPages })

    // Get the current page's background color
    val currentBackgroundColor = uiState.pages[pagerState.currentPage].backgroundColor

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != uiState.currentPage) {
            onAction(OnboardingPageAction.PageChanged(pagerState.currentPage))
        }
    }

    LaunchedEffect(uiState.currentPage) {
        if (pagerState.currentPage != uiState.currentPage) {
            pagerState.animateScrollToPage(uiState.currentPage)
        }
    }

    // Use BoxWithConstraints to get screen dimensions
    BoxWithConstraints(
        modifier = modifier
            .background(currentBackgroundColor)
            .systemBarsPadding()
    ) {
        val screenHeight = maxHeight
        val screenWidth = maxWidth

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Pager takes most of the screen
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                OnboardingPage(
                    modifier = Modifier
                        .fillMaxSize(),
                    pageData = uiState.pages[page],
                    screenHeight = screenHeight,
                    screenWidth = screenWidth,
                )
            }

            OnboardingBottomSection(
                modifier = Modifier
                    .fillMaxWidth(),
                currentPage = uiState.currentPage,
                totalPages = uiState.totalPages,
                onNextClicked = { onAction(OnboardingPageAction.NextClicked) },
                onSkipClicked = { onAction(OnboardingPageAction.SkipClicked) },
                onGetStartedClicked = { onAction(OnboardingPageAction.GetStartedClicked) },
                screenWidth = screenWidth,
                screenHeight = screenHeight,
            )
        }
    }
}