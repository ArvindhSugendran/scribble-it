package com.scribble.it.feature_gate.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scribble.it.feature_canvas.domain.repository.CanvasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface StartupState {
    data object Loading : StartupState
    data object ShowCanvas : StartupState
    data object ShowOnboarding : StartupState
}

@HiltViewModel
class GateViewModel @Inject constructor(
    canvasRepository: CanvasRepository
) : ViewModel() {

    val startupState: StateFlow<StartupState> =
        canvasRepository
            .getOnBoardingStatus()
            .map { completed ->
                if (completed) {
                    StartupState.ShowCanvas
                } else {
                    StartupState.ShowOnboarding
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(0),
                StartupState.Loading
            )
}
