package com.fantasy.components.extension.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.KeyboardUtils
import com.fantasy.components.theme.CXColor
import com.fantasy.components.tools.canBlur
import com.fantasy.components.tools.isDebugBuilder
import com.fantasy.components.tools.Apphelper
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import kotlin.math.roundToInt

/**
 *  @description 自定义 click 去掉了水波纹 防止重复点击
 *  @author 梁泽
 */
@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.fantasyClick(
    indication: Indication? = ripple(),
    time: Int = 1000,
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    var lastClickTime = remember { 0L }
    clickable(
        interactionSource = remember {
            MutableInteractionSource()
        },
        indication = indication,
        enabled = enabled
    ) {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - time >= lastClickTime) {
            onClick()
            lastClickTime = currentTimeMillis
        }
    }
}

/**
 * Consumes touch events.
 */
fun Modifier.consumeClicks() = fantasyClick(indication = null,onClick = NoOpLambda)

/**
 * Empty lambda.
 */
val NoOpLambda: () -> Unit = {}

/**
 * 虚线边框
 */
fun Modifier.dashedBorder(
    width: Dp = 2.dp,
    radius: Dp,
    color: Color,
    lineLength: Dp = 5.dp,
    spaceLength: Dp = lineLength,
) = drawBehind {
    drawRoundRect(
        color = color,
        style = Stroke(
            width = width.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(lineLength.toPx(), spaceLength.toPx()),
            ),
        ),
        cornerRadius = CornerRadius(radius.toPx())
    )
}

fun Modifier.offsetPercent(offsetPercentX: Float = 0f, offsetPercentY: Float = 0f) =
    this.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.width, placeable.height) {
            val offsetX = (offsetPercentX * placeable.width).roundToInt()
            val offsetY = (offsetPercentY * placeable.height).roundToInt()
            placeable.placeRelative(offsetX, offsetY)
        }
    }

/**
 * 点击
 */
fun Modifier.clickBlankHiddenKeyboard() = composed {
    val view = LocalView.current
    fantasyClick {
        KeyboardUtils.hideSoftInput(view)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.debugClickable(
    onDoubleClick: () -> Unit = {},
    onClick: () -> Unit = {},
) = if (isDebugBuilder) Modifier.combinedClickable(
    onDoubleClick = onDoubleClick,
    onClick = onClick
) else Modifier



@SuppressLint("ModifierFactoryUnreferencedReceiver", "UnnecessaryComposedModifier")
@Composable
fun Modifier.addShadow(
    elevation: Dp = 4.dp,
    corner: Dp = 11.9.dp,
    color: Color = CXColor.f1.copy(0.5f),
    repeat: Int = 1,
) = composed {
    (1..repeat).fold(this) { m, _ ->
        m.shadow(
            elevation = elevation,
            shape = RoundedCornerShape(corner),
            ambientColor = color,
            spotColor = color,
        )
    }
}


@SuppressLint("ModifierFactoryUnreferencedReceiver")
@Composable
fun Modifier.addCardBack(
    elevation: Int = 4,
    cornerRadius: Dp = 18.dp,
    backgroundColor: Color = CXColor.b1,
    addBorder: Boolean = false,
) = composed {
    addShadow(elevation = elevation.dp, corner = cornerRadius)
        .then(
            if (addBorder)
                Modifier.border(0.3.dp, CXColor.f1.copy(0.3f), RoundedCornerShape(cornerRadius))
            else Modifier
        )
        .clip(RoundedCornerShape(cornerRadius))
        .background(backgroundColor)
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
@Composable
fun Modifier.addCardBackInB1(
    elevation: Dp = 4.dp,
    cornerRadius: Dp = 11.9.dp,
    backgroundColor: Color = CXColor.b1,
) = composed {
    addShadow(elevation = elevation, corner = cornerRadius, color = CXColor.f1.copy(0.7f))
        .background(backgroundColor, RoundedCornerShape(cornerRadius))
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
@Composable
fun Modifier.addTagBack(
    backgroundColor: Color = CXColor.b1,
    cornerRadius: Dp = 8.dp,
) = composed {
    clip(RoundedCornerShape(cornerRadius))
        .background(backgroundColor)
        .padding(horizontal = 10.dp, vertical = 5.dp)
}

@Composable
fun Modifier.addHazeContent() = Modifier.haze(Apphelper.hazeState)

// https://chrisbanes.github.io/haze/usage/
@Composable
fun Modifier.addHazeOver(
    color: Color,
) = Modifier.hazeChild(
    state = Apphelper.hazeState,
) {
    backgroundColor = color
    blurRadius = 10.dp
    fallbackTint = HazeTint(color = color)
//    inputScale = HazeInputScale.Auto
//    progressive = HazeProgressive.verticalGradient(startIntensity = 1f, endIntensity = 0f)
}

fun Modifier.cxBlur(radius: Dp) = if (canBlur) Modifier.blur(radius = radius) else this
