package com.fantasy.components.widget

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.components.theme.CXColor
import kotlinx.coroutines.delay

@Composable
fun CXThreeBounceAnimation(
    dotSize: Int = 14
) {
    val dots = listOf(
        remember { Animatable(0.2f) },
        remember { Animatable(0.2f) },
        remember { Animatable(0.2f) },
    )

    dots.forEachIndexed { index, animatable ->
        LaunchedEffect(animatable) {
            delay(index * 200L)
            animatable.animateTo(
                targetValue = 1f, animationSpec = infiniteRepeatable(
                    animation = tween(600, easing = FastOutLinearInEasing),
                    repeatMode = RepeatMode.Reverse,
                )
            )
        }
    }

    val dys = dots.map { it.value }

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        dys.forEachIndexed { index, dy ->
            Box(
                Modifier
                    .size(dotSize.dp)
                    .scale(dy)
                    .alpha(dy)
                    .background(color = CXColor.f3, shape = CircleShape)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewScreen {
        CXThreeBounceAnimation()
    }
}