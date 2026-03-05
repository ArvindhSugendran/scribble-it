package com.scribble.it.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
import com.scribble.it.feature_canvas.presentation.canvasdraw.navigation.CanvasDrawRoute
import com.scribble.it.feature_canvas.presentation.canvasdraw.screen.CanvasDrawScreen
import com.scribble.it.feature_canvas.presentation.canvasdraw.viewmodel.CanvasDrawViewModel
import com.scribble.it.feature_canvas.presentation.canvaslist.navigation.CanvasListRoute
import com.scribble.it.feature_canvas.presentation.canvaslist.screen.CanvasListScreen
import com.scribble.it.feature_canvas.presentation.canvaslist.viewmodel.CanvasListViewModel
import com.scribble.it.feature_canvas.presentation.canvasrecycle.navigation.CanvasRecycleRoute
import com.scribble.it.feature_canvas.presentation.canvasrecycle.screen.CanvasRecycleScreen
import com.scribble.it.feature_canvas.presentation.canvasrecycle.viewmodel.CanvasRecycleViewModel
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

        composable(
            route = CanvasListRoute::class,
            exitTransition = {
                if (targetState.destination.route == CanvasRecycleRoute::class.qualifiedName) {
                    ExitTransition.None
                } else {
                    slideOutHorizontally { -it }
                }
            },
            popEnterTransition = {
                if (initialState.destination.route == CanvasRecycleRoute::class.qualifiedName) {
                    EnterTransition.None
                } else {
                    slideInHorizontally { -it }
                }
            }
        ) {
            val viewModel: CanvasListViewModel = hiltViewModel()
            val uiState by viewModel.canvasListUiState.collectAsStateWithLifecycle()

            CanvasListScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                uiState = uiState,
                basePagingFlow = viewModel.basePagingFlow,
                searchPagingFlow = viewModel.searchPagingFlow,
                eventsFlow = viewModel.eventsFlow,
                onAction = viewModel::viewAction,
                onNavigateToCanvasDraw = { drawRoute ->
                    navController.navigate(route = drawRoute) {
                        popUpTo(CanvasListRoute)
                    }
                },
                onNavigateToCanvasRecycle = {
                    navController.navigate(CanvasRecycleRoute) {
                        popUpTo(CanvasListRoute)
                    }
                }
            )
        }

        composable(
            route = CanvasDrawRoute::class,
            enterTransition = { slideInHorizontally { it } },
            popExitTransition = { slideOutHorizontally { it } }
        ) {
            val viewModel: CanvasDrawViewModel = hiltViewModel()
            val uiState by viewModel.canvasDrawUiState.collectAsStateWithLifecycle()

            CanvasDrawScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                uiState = uiState,
                strokeColorPalette = viewModel.strokeColors,
                eventsFlow = viewModel.eventsFlow,
                onAction = viewModel::viewAction,
                onNavigateBackToCanvasList = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = CanvasRecycleRoute::class,
            enterTransition = { slideInVertically { it } },
            popExitTransition = { slideOutVertically { it } }
        ) {
            val viewModel: CanvasRecycleViewModel = hiltViewModel()
            val uiState by viewModel.canvasRecycleUiState.collectAsStateWithLifecycle()

            CanvasRecycleScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                uiState = uiState,
                eventsFlow = viewModel.eventsFlow,
                recyclePagingFlow = viewModel.recyclePagingFlow,
                onAction = viewModel::viewAction,
                onNavigateBackToCanvasList = {
                    navController.popBackStack()
                }
            )
        }
    }
}