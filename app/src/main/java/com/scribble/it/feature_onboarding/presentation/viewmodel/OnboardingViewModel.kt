package com.scribble.it.feature_onboarding.presentation.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Share
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scribble.it.feature_onboarding.domain.model.OnboardingPage
import com.scribble.it.feature_onboarding.presentation.action.OnboardingAction
import com.scribble.it.feature_onboarding.presentation.event.OnboardingEvent
import com.scribble.it.feature_onboarding.presentation.state.OnboardingUiState
import com.scribble.it.ui.theme.Amber30
import com.scribble.it.ui.theme.Moss30
import com.scribble.it.ui.theme.Violet30
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
) : ViewModel() {

    private val onboardingPages = listOf(
        OnboardingPage(
            id = 1,
            title = "Welcome to Scribble",
            description = "Unleash your creativity with the most intuitive drawing app",
            imageVector = Icons.Outlined.Brush,
            backgroundColor = Violet30
        ),
        OnboardingPage(
            id = 2,
            title = "Draw Freely",
            description = "Smooth brushes and natural feel for your artistic expression",
            imageVector = Icons.Outlined.Palette,
            backgroundColor = Moss30
        ),
        OnboardingPage(
            id = 3,
            title = "Save & Share",
            description = "Export your masterpieces in high quality and share with the world",
            imageVector = Icons.Outlined.Share,
            backgroundColor = Amber30
        )
    )

    private val _onBoardingUiState: MutableStateFlow<OnboardingUiState> = MutableStateFlow(
        OnboardingUiState(
            pages = onboardingPages,
            currentPage = 0,
            totalPages = onboardingPages.size
        )
    )
    val onBoardingUiState: StateFlow<OnboardingUiState> = _onBoardingUiState.asStateFlow()

    private val eventChannel: Channel<OnboardingEvent> = Channel(Channel.BUFFERED)
    val eventsFlow: Flow<OnboardingEvent> = eventChannel.receiveAsFlow()

    fun viewAction(action: OnboardingAction) {
        when (action) {
            is OnboardingAction.Changed -> onPageChanged(action.page)
            is OnboardingAction.NextClicked -> onNextClicked()
            is OnboardingAction.GetStartedClicked -> onGetStartedClicked()
            is OnboardingAction.SkipClicked -> onSkipClicked()
        }
    }

    private fun onPageChanged(page: Int) {
        _onBoardingUiState.update { state->
            state.copy(currentPage = page)
        }
    }

    private fun onNextClicked() {
        _onBoardingUiState.update { state->
            state.copy(currentPage = state.currentPage + 1)
        }
    }

    private fun onGetStartedClicked() {
        viewModelScope.launch {
            eventChannel.send(OnboardingEvent.NavigateToHome)
        }
    }

    private fun onSkipClicked() {
        viewModelScope.launch {
            eventChannel.send(OnboardingEvent.NavigateToHome)
        }
    }
}