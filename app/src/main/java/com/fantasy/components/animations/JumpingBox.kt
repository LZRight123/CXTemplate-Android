package com.fantasy.components.animations


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import io.github.sagar_viradiya.rememberKoreography

@Composable
fun JumpingBox(
    isJumping: Boolean = true,
    targetValue: Float = -20f,
    content: @Composable () -> Unit
) {
    if (isJumping.not()) {
        content()
    } else {
//    val infiniteTransition = rememberInfiniteTransition(label = "jumping")
//    val dy by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(500, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "jumping"
//    )
//
//    val travelDistance = with(LocalDensity.current) { 10.dp.toPx() }

        val translationYAnimation = remember {
            Animatable(0f)
        }
        val koreography = rememberKoreography {
            move(
                translationYAnimation,
                targetValue = targetValue,
                animationSpec = infiniteRepeatable(
                    animation = tween(700, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
        LaunchedEffect(Unit) {
            koreography.repeatDance(1, this)//.danceForever(this)
        }

        Box(modifier = Modifier.graphicsLayer {
            translationY = translationYAnimation.value
        }) {
            content()
        }
    }
}

@Preview
@Composable
fun JumpingBoxPreview() {
    JumpingBox {

    }
}

