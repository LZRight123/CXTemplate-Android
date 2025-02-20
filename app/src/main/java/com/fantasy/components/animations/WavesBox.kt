package com.fantasy.components.animations


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import com.fantasy.components.theme.CCColor
import io.github.sagar_viradiya.rememberKoreography

@Composable
fun WavesBox(
    waveColor: Color = CCColor.f1.copy(0.6f),
    shape: Shape = CircleShape,
    content: @Composable () -> Unit
) {
    val waves = remember {
        listOf(
            Animatable(0f),
            Animatable(0f),
            Animatable(0f),
            Animatable(0f),
        )
    }

    val koreography = rememberKoreography {
        parallelMoves {
            waves.forEachIndexed { index, animatable ->
                move(
                    animatable,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            2000,
                            delayMillis = index * 600,
                            easing = FastOutLinearInEasing
                        ),
                        repeatMode = RepeatMode.Restart,
                    )
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        koreography.dance(this)
    }
//        val waves = listOf(
//        remember { Animatable(0f) },
//        remember { Animatable(0f) },
//        remember { Animatable(0f) },
//        remember { Animatable(0f) },
//    )
//    val animationSpec = infiniteRepeatable<Float>(
//        animation = tween(2000, easing = FastOutLinearInEasing),
//        repeatMode = RepeatMode.Restart,
//    )
//
//    waves.forEachIndexed { index, animatable ->
//        LaunchedEffect(animatable) {
//            delay(index * 1000L)
//            animatable.animateTo(
//                targetValue = 1f, animationSpec = animationSpec
//            )
//        }
//    }
//    val dys = waves.map { it.value }

    Box(modifier = Modifier) {
        waves.forEach { animation ->
            Box(
                Modifier
                    .graphicsLayer {
                        scaleX = animation.value * 0.45f + 1
                        scaleY = animation.value * 0.45f + 1
                        alpha = 1-animation.value
                    }
                    .background(color = waveColor, shape = shape)
                    .matchParentSize(),
            )
        }
        content()
    }
}

@Preview
@Composable
fun WavesBoxPreview() {
    WavesBox {

    }
}

