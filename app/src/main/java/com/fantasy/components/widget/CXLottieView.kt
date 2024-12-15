package com.fantasy.components.widget


import androidx.annotation.RawRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun CXLottieView(
    @RawRes resId: Int,
    iterations: Int = LottieConstants.IterateForever,
    isPlaying: Boolean = true,
    contentScale: ContentScale = ContentScale.FillWidth,
    alignment: Alignment = Alignment.Center,
    speed: Float = 1f,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(resId)
    )

    LottieAnimation(
        composition = composition,
        iterations = iterations,
        isPlaying = isPlaying,
        modifier = modifier,
        contentScale = contentScale,
        speed = speed,
        alignment = alignment,
    )
}

