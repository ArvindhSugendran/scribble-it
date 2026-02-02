package com.scribble.it.feature_onboarding.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.scribble.it.feature_onboarding.presentation.action.OnboardingAction
import com.scribble.it.feature_onboarding.presentation.components.OnboardingBottom
import com.scribble.it.feature_onboarding.presentation.components.OnboardingPage
import com.scribble.it.feature_onboarding.presentation.event.OnboardingEvent
import com.scribble.it.feature_onboarding.presentation.state.OnboardingUiState
import com.scribble.it.feature_onboarding.ui.retrieveOnboardingBottomMetrics
import com.scribble.it.feature_onboarding.ui.retrieveOnboardingPageMetrics
import com.scribble.it.ui.adaptive.layoutConfig.getLayoutConfiguration
import com.scribble.it.ui.adaptive.scale.ScreenScale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    uiState: OnboardingUiState,
    eventsFlow: Flow<OnboardingEvent>,
    onAction: (OnboardingAction) -> Unit,
    onNavigateToHome: () -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {

    val configuration = LocalConfiguration.current

    val windowInfo = currentWindowAdaptiveInfo()
    val windowSizeClass = windowInfo.windowSizeClass

    val layoutConfig = remember(windowSizeClass, configuration) {
        getLayoutConfiguration(windowSizeClass, configuration)
    }

    val pagerState = rememberPagerState(pageCount = { uiState.totalPages })

    val currentBackgroundColor = uiState.pages[pagerState.currentPage].backgroundColor

    LaunchedEffect(eventsFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            eventsFlow.collectLatest { event ->
                when (event) {
                    OnboardingEvent.NavigateToHome -> onNavigateToHome()
                }
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != uiState.currentPage) {
            onAction(OnboardingAction.Changed(pagerState.currentPage))
        }
    }

    LaunchedEffect(uiState.currentPage) {
        if (pagerState.currentPage != uiState.currentPage) {
            pagerState.animateScrollToPage(uiState.currentPage)
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .background(currentBackgroundColor)
    ) {

        val scale = remember(maxWidth, maxHeight) {
            ScreenScale(
                width = maxWidth,
                height = maxHeight
            )
        }

        val onboardingPageMetrics = remember(layoutConfig, scale) {
            retrieveOnboardingPageMetrics(
                scale = scale,
                layout = layoutConfig
            )
        }

        val onboardingBottomMetrics = remember(layoutConfig, scale) {
            retrieveOnboardingBottomMetrics(
                scale = scale,
                layout = layoutConfig
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                OnboardingPage(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    pageData = uiState.pages[page],
                    metrics = onboardingPageMetrics
                )
            }

            OnboardingBottom(
                modifier = Modifier
                    .fillMaxWidth(),
                currentPage = uiState.currentPage,
                totalPages = uiState.totalPages,
                onNextClicked = { onAction(OnboardingAction.NextClicked) },
                onSkipClicked = { onAction(OnboardingAction.SkipClicked) },
                onGetStartedClicked = { onAction(OnboardingAction.GetStartedClicked) },
                metrics = onboardingBottomMetrics
            )
        }
    }
}