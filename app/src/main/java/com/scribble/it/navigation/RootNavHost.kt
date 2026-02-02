package com.scribble.it.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.scribble.it.feature_canvas.presentation.canvaslist.navigation.CanvasListRoute
import com.scribble.it.feature_canvas.presentation.canvaslist.screen.CanvasListScreen
import com.scribble.it.feature_canvas.presentation.canvaslist.viewmodel.CanvasListViewModel
import com.scribble.it.feature_onboarding.presentation.navigation.OnboardingRoute
import com.scribble.it.feature_onboarding.presentation.screen.OnboardingScreen
import com.scribble.it.feature_onboarding.presentation.viewmodel.OnboardingViewModel

@Composable
fun RootNavHost(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = OnboardingRoute
    ) {
        composable<OnboardingRoute> {
            val viewModel: OnboardingViewModel = hiltViewModel()
            val uiState by viewModel.onBoardingUiState.collectAsStateWithLifecycle()

            OnboardingScreen(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState,
                eventsFlow = viewModel.eventsFlow,
                onAction = viewModel::viewAction,
                onNavigateToHome = {
                    navController.navigate(CanvasListRoute) {
                        popUpTo(OnboardingRoute) { inclusive = true }
                    }
                }
            )
        }

        composable<CanvasListRoute> {
            val viewModel: CanvasListViewModel = hiltViewModel()
            val uiState by viewModel.canvasListUiState.collectAsStateWithLifecycle()

            CanvasListScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                uiState = uiState,
                eventsFlow = viewModel.eventsFlow,
                onAction = viewModel::viewAction
            )
        }
    }
}