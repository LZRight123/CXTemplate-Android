package com.fantasy.components.aamedium

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.components.theme.CXColor
import com.fantasy.components.widget.PreviewScreen
import io.github.sagar_viradiya.rememberKoreography

enum class ButtonState { Pressed, Idle }

fun Modifier.bounceClick(
    enabled: Boolean = true,
    pressedScale: Float = 0.98f,
) = if (enabled.not()) this else composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(
        if (buttonState == ButtonState.Pressed) pressedScale else 1f,
        label = "scale"
    )

    graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}

fun Modifier.shakeClickEffect(
    enabled: Boolean = true,
    ofssetX: Float = 30f
) = if (enabled.not()) this else composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val tx = remember {
        Animatable(0f)
    }
    val ko = rememberKoreography {
        move(tx, -ofssetX, animationSpec = tween(durationMillis = 40))
        move(tx, ofssetX, animationSpec = tween(durationMillis = 40))
        move(tx, -ofssetX, animationSpec = tween(durationMillis = 30))
        move(tx, ofssetX, animationSpec = tween(durationMillis = 30))
        move(tx, 0f, animationSpec = tween(durationMillis = 40))
    }
    val scope = rememberCoroutineScope()

    this
        .graphicsLayer {
            translationX = tx.value
        }
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ko.dance(scope)
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}


@Preview(showBackground = true)
@Composable
private fun _preview() {
    PreviewScreen {
        Box(
            modifier = Modifier
                .bounceClick(pressedScale = 0.5f)
                .background(CXColor.random)
                .size(100.dp)
        )

        Box(
            modifier = Modifier
                .shakeClickEffect(ofssetX = 60f)
                .background(CXColor.random)
                .size(100.dp)
        )
    }
}