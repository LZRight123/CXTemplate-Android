package com.fantasy.components.extension

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fantasy.components.theme.CCColor
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer



private fun createCustomTheme(duration: Int) = defaultShimmerTheme.copy(
    animationSpec = infiniteRepeatable(
        animation = tween(
            durationMillis = duration,
            delayMillis = 1_200,
            easing = LinearEasing,
        ),
        repeatMode = RepeatMode.Restart,
    ),
    rotation = 5f,
    shaderColors = listOf(
        Color.Unspecified.copy(alpha = 1.0f),
        Color.Unspecified.copy(alpha = 0.2f),
        Color.Unspecified.copy(alpha = 1.0f),
    ),
    shaderColorStops = null,
    shimmerWidth = 1000.dp,
)

/**
CompositionLocalProvider(
LocalShimmerTheme provides ccShimmerTheme,
) {

}
*/
val ccShimmerTheme = createCustomTheme(1000)

val lakeCardTheme = defaultShimmerTheme.copy(
    animationSpec = infiniteRepeatable(
        animation = tween(
            durationMillis = 6_500,
            delayMillis = 5_500,
            easing = LinearEasing,
        ),
    ),
    blendMode = BlendMode.DstIn,
    rotation = 0f,
    shaderColors = listOf(
        Color.Unspecified.copy(alpha = 0.0f),
        Color.Unspecified.copy(alpha = 1.0f),
        Color.Unspecified.copy(alpha = 1.0f),
        Color.Unspecified.copy(alpha = 0.0f),
    ),
    shaderColorStops = listOf(
        0.0f,
        0.1f,
        0.9f,
        1.0f,
    ),
    shimmerWidth = 1_000.dp,
)

val creditCardTheme = defaultShimmerTheme.copy(
    animationSpec = infiniteRepeatable(
        animation = tween(
            durationMillis = 600,
            delayMillis = 2_500,
            easing = LinearEasing,
        ),
    ),
    blendMode = BlendMode.Hardlight,
    rotation = 25f,
    shaderColors = listOf(
        Color.White.copy(alpha = 0.0f),
        Color.White.copy(alpha = 0.2f),
        Color.White.copy(alpha = 0.0f),
    ),
    shaderColorStops = null,
    shimmerWidth = 400.dp,
)
