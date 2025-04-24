package com.fantasy.components.animations

import android.graphics.drawable.Icon
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import com.fantasy.components.extension.compose.Icon
import com.fantasy.components.extension.compose.Image
import com.fantasy.components.widget.PreviewScreen
import io.github.sagar_viradiya.rememberKoreography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun PowSprayAnimation(
    spray: Boolean  = false,
    count: Int = 15,
    animateCompletion: () -> Unit = {},
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit
) {
    if (spray) {
        repeat(count) { index ->
            Box(
                contentAlignment = alignment,
                modifier = modifier
            ) {
                val tx = remember {
                    Animatable(0f)
                }
                val ty = remember {
                    Animatable(0f)
                }
                val al = remember {
                    Animatable(1f)
                }
                val sl = remember {
                    Animatable(1f)
                }
                val ko = rememberKoreography {
                    parallelMoves {
                        move(tx,  Random.nextFloat() * 360 - 180, animationSpec = tween(1000))
                        move(ty, Random.nextFloat() * 300 * -1, animationSpec = tween(1000))
                        move(al,  0f, animationSpec = tween(800, delayMillis = Random.nextInt(0, 500)))
                        move(sl, Random.nextFloat() * 0.7f + 0.5f, animationSpec = tween(1000))
                    }
                }
                LaunchedEffect(Unit) {
                    ko.dance(this) {
                        launch {
                            delay(100)
                            animateCompletion()
                        }
                    }
                }
                Box(
                    modifier = Modifier.graphicsLayer {
                        translationY = ty.value
                        translationX = tx.value
                        alpha = al.value
                        scaleX = sl.value
                        scaleY = sl.value
                    }
                ) {
                    content()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun _preview() {
    PreviewScreen {
        PowSprayAnimation {
            Icon(Icons.Default.Favorite, tint = Color.Red)
            Icon(Icons.Default.Star, tint = Color.Red)
        }
    }
}