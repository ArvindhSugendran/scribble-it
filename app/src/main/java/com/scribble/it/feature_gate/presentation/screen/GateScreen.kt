package com.scribble.it.feature_gate.presentation.screen

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.scribble.it.R
import com.scribble.it.feature_gate.presentation.viewmodel.GateViewModel
import com.scribble.it.feature_gate.presentation.viewmodel.StartupState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun GateScreen(
    modifier: Modifier = Modifier,
    onNavigateToCanvasScreen: () -> Unit,
    onNavigateToOnboardingScreen: () -> Unit,
    viewModel: GateViewModel = hiltViewModel<GateViewModel>(),
) {

    AnimatedSplashLogo(
        modifier = modifier,
        viewModel= viewModel,
        onAnimationFinished = { state ->
            when(state) {
                StartupState.Loading -> Unit
                StartupState.ShowCanvas -> onNavigateToCanvasScreen()
                StartupState.ShowOnboarding -> onNavigateToOnboardingScreen()
            }
        }
    )
}

@Composable
fun AnimatedSplashLogo(
    modifier: Modifier = Modifier,
    viewModel: GateViewModel,
    onAnimationFinished: (StartupState) -> Unit
) {

    val rotation = remember { androidx.compose.animation.core.Animatable(0f) }
    val scale = remember { androidx.compose.animation.core.Animatable(1f) }
    val alpha = remember { androidx.compose.animation.core.Animatable(1f) }

    val state by viewModel.startupState.collectAsStateWithLifecycle()

    val overshootEasing = Easing { fraction ->
        val tension = 2f
        val t = fraction - 1f
        t * t * ((tension + 1) * t + tension) + 1f
    }

    LaunchedEffect(Unit) {

        rotation.animateTo(
            targetValue = 360f,
            animationSpec = tween(
                durationMillis = 700,
                easing = overshootEasing
            )
        )

        scale.animateTo(
            targetValue = 1.1f,
            animationSpec = tween(200)
        )

        coroutineScope {
            launch {
                scale.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(200)
                )
            }

            launch {
                alpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(300)
                )
            }
        }
    }

    LaunchedEffect(state, alpha.value) {
        if (alpha.value == 0f && state != StartupState.Loading) {
            onAnimationFinished(state)
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(288.dp),
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RectangleShape)
                    .graphicsLayer {
                        this.scaleX = scale.value
                        this.scaleY = scale.value
                        this.rotationZ = rotation.value
                        this.alpha = alpha.value
                    }
            ) {

                Image(
                    painter = painterResource(R.drawable.scribble_splash),
                    contentDescription = null,
                    modifier = Modifier
                        .matchParentSize()
                )
            }
        }
    }

}