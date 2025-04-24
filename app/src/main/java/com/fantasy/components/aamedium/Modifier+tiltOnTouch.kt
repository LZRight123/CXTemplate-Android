package com.fantasy.components.aamedium

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.launch

/**
 * 鸣谢
 * https://x.com/Snokbert/status/1595078442231398401
 * https://gist.github.com/KlassenKonstantin/edebb0fdc192a2b0c2d4f55b85f06abe
 * 这位老哥写的更好，但是没分享代码 https://x.com/romankhrupa/status/1858475384733396999
 */
fun Modifier.tiltOnTouch(maxTiltDegrees: Float = 5f) =
    this.then(
        composed {
            val scope = rememberCoroutineScope()
            val offset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }

            pointerInput(Unit) {
                awaitEachGesture {
                    // 等待第一次按下事件
                    val down = awaitFirstDown(requireUnconsumed = false)
                    // 消费按下事件
                    down.consume()

                    val newOffset = down.position.normalize(size)
                    scope.launch { offset.animateTo(newOffset, spring) }

                    try {
                        // 处理拖动事件，直到手指抬起
                        do {
                            val event = awaitPointerEvent()
                            val currentChange = event.changes.lastOrNull()

                            if (currentChange?.changedToUp() == true) {
                                // 如果检测到抬起事件，跳出循环
                                scope.launch { offset.animateTo(Offset.Zero, releaseSpring) }
                                break
                            }
                            if (currentChange != null && !currentChange.isConsumed) {
                                // 消费移动事件
                                currentChange.consume()
                                val dragOffset = currentChange.position.normalize(size)
                                scope.launch { offset.animateTo(dragOffset, spring) }
                            }
                        } while (true)
                    } finally {
                        // 无论如何都确保恢复动画被触发
                        scope.launch { offset.animateTo(Offset.Zero, releaseSpring) }
                    }
                }
            }
                .graphicsLayer(
                    rotationY = offset.value.x * maxTiltDegrees,
                    rotationX = -offset.value.y * maxTiltDegrees,
                    cameraDistance = 20f
                )
        }
    )

private val spring = spring<Offset>(stiffness = Spring.StiffnessMediumLow)
private val releaseSpring = spring<Offset>()

private fun Offset.normalize(size: IntSize) =
    Offset(
        ((x - (size.width / 2)) / (size.width / 2)).coerceIn(-1f..1f),
        ((y - (size.height / 2)) / (size.height / 2)).coerceIn(-1f..1f)
    )