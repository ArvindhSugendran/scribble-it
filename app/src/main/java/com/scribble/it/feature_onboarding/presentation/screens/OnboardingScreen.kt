package com.scribble.it.feature_onboarding.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.scribble.it.feature_onboarding.presentation.action.OnboardingPageAction
import com.scribble.it.feature_onboarding.presentation.event.OnboardingPageEvent
import com.scribble.it.feature_onboarding.presentation.state.OnboardingUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    uiState: OnboardingUiState,
    eventsFlow: Flow<OnboardingPageEvent>,
    onAction: (OnboardingPageAction) -> Unit,
    onNavigateToHome: () -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {

    LaunchedEffect(eventsFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            eventsFlow.collectLatest { event ->
                when (event) {
                    OnboardingPageEvent.NavigateToHome -> onNavigateToHome()
                }
            }
        }
    }

    OnboardingScreenContent(
        modifier = modifier,
        uiState = uiState,
        onAction = onAction,
    )
}