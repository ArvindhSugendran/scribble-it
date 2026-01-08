package com.scribble.it.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.scribble.it.feature_canvas.presentation.canvaslist.navigation.CanvasListRoute
import com.scribble.it.feature_onboarding.presentation.navigation.OnboardingRoute
import com.scribble.it.feature_onboarding.presentation.screens.OnboardingScreen
import com.scribble.it.feature_onboarding.presentation.viewmodel.OnboardingViewModel

@Composable
fun RootNavHost(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = OnboardingRoute,
    ) {
        composable<OnboardingRoute> {
            val viewModel: OnboardingViewModel = hiltViewModel()
            val uiState by viewModel.onBoardingUiState.collectAsStateWithLifecycle()

            OnboardingScreen(
                uiState = uiState,
                eventsFlow = viewModel.eventsFlow,
                onAction = viewModel::viewAction,
                onNavigateToHome = {
                    navController.navigate(CanvasListRoute) {
                        popUpTo(OnboardingRoute) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        composable<CanvasListRoute> {

        }
    }
}