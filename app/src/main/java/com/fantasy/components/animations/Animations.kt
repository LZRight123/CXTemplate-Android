package com.fantasy.components.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.EaseOutElastic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.components.theme.CCColor
import com.fantasy.components.tools.screenHeight
import com.fantasy.components.tools.screenWith
import com.fantasy.components.tools.toPx
import com.fantasy.components.widget.PreviewScreen
import io.github.sagar_viradiya.rememberKoreography


fun Modifier.ccSlideInVertically(
    show: Boolean = true,
    duration: Int = 600,
    delay: Int = 0,
    reverse: Boolean = false, // 反向，默认从下方进入
    animationSpec: AnimationSpec<Float> = spring(),
    onDismissFinished: () -> Unit = {}
) = composed {
    val l = LocalInspectionMode.current
    val vector = if (reverse) -1 else 1
    val h = screenHeight.toPx() * vector
    val ty = remember {
        Animatable(if (l) 0f else h)
    }
    val koin = rememberKoreography {
        move(ty, -20f * vector, animationSpec = tween(duration, delay))
        move(ty, 0f, animationSpec = animationSpec)
    }
    val koout = rememberKoreography {
        move(ty, 0f, animationSpec = tween(0))
        move(ty, h, animationSpec = tween(duration))
    }
    LaunchedEffect(show) {
        if (show) {
            koin.dance(this)
        } else {
            koout.dance(this, onDismissFinished)
        }
    }

    graphicsLayer {
        translationY = ty.value
    }
}


@Composable
fun Modifier.ccSlideInHorizontally(
    show: Boolean = true,
    duration: Int = 600,
    delay: Int = 0,
    reverse: Boolean = false, // 反向，默认从右边进入
    initPosition: Int = screenWith,
    onDismissFinished: () -> Unit = {}
) = composed {
    val l = LocalInspectionMode.current
    val vector = if (reverse) -1 else 1
    val w = initPosition.toPx() * vector
    val tx = remember {
        Animatable(if (l) 0f else w)
    }
    val koin = rememberKoreography {
        move(tx, -20f * vector, animationSpec = tween(duration, delay))
        move(tx, 0f, animationSpec = spring(dampingRatio = 0.5f))
    }
    val koout = rememberKoreography {
        move(tx, 0f, animationSpec = tween(0))
        move(tx, w, animationSpec = tween(duration))
    }
    LaunchedEffect(show) {
        if (show) {
            koin.dance(this)
        } else {
            koout.dance(this, onDismissFinished)
        }
    }

    graphicsLayer {
        translationX = tx.value
    }
}

fun Modifier.ccAlphaIn(
    show: Boolean = true,
    duration: Int = 600,
    delay: Int = 0,
) = composed {
    val l = LocalInspectionMode.current
    val al = remember {
        Animatable(if (l) 1f else 0f)
    }
    val koin = rememberKoreography {
        move(al, 1f, animationSpec = tween(duration, delay))
    }
    val koout = rememberKoreography {
        move(al, 1f, animationSpec = tween(0))
        move(al, 0f, animationSpec = tween(duration))
    }
    LaunchedEffect(show) {
        if (show) {
            koin.dance(this)
        } else {
            koout.dance(this)
        }
    }

    graphicsLayer {
        alpha = al.value
    }
}

fun Modifier.ccScaleIn(
    show: Boolean = true,
    duration: Int = 600,
    delay: Int = 0,
) = composed {
    val l = LocalInspectionMode.current
    val sc = remember {
        Animatable(if (l) 1f else 0f)
    }
    val koin = rememberKoreography {
        move(sc, 1f, animationSpec = tween(duration, delay))
    }
    val koout = rememberKoreography {
        move(sc, 1f, animationSpec = tween(0))
        move(sc, 0f, animationSpec = tween(duration))
    }
    LaunchedEffect(show) {
        if (show) {
            koin.dance(this)
        } else {
            koout.dance(this)
        }
    }

    graphicsLayer {
        scaleX = sc.value
        scaleY = sc.value
    }
}

fun Modifier.ccJumpInVertically(
    show: Boolean = true,
    offset: Float = -20f,
    duration: Int = 300,
    delay: Int = 0,
    danceCount: Int = 1,
    onFinished: () -> Unit = {}
) = composed {
    val ty = remember {
        Animatable(0f)
    }
    val height = offset  * LocalDensity.current.density

    val ko = rememberKoreography {
        move(ty, height, animationSpec = tween(duration, easing = LinearEasing, delayMillis = delay))
        move(ty, -0f, animationSpec = tween(duration, easing = EaseOutElastic))
    }
    LaunchedEffect(show) {
        if (show) {
            ko.repeatDance(danceCount, this) {
                onFinished()
            }
        }
    }

    graphicsLayer {
        translationY = ty.value
    }

}



@Preview(showBackground = true)
@Composable
private fun _preview() {
    PreviewScreen {
        Box(
            modifier = Modifier
                .ccSlideInHorizontally()
                .ccSlideInVertically()
                .ccAlphaIn()
                .background(CCColor.random)
                .size(300.dp)
        )
    }
}