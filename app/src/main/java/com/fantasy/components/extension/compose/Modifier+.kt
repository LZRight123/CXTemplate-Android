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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.blankj.utilcode.util.KeyboardUtils
import com.fantasy.components.theme.CCColor
import com.fantasy.components.tools.canBlur
import com.fantasy.components.tools.isDebugBuilder
import com.fantasy.components.tools.AppHelper
import dev.chrisbanes.haze.HazeEffectScope
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import kotlin.math.roundToInt

/**
 *  @description 自定义 click 去掉了水波纹 防止重复点击
 *  @author 梁泽
 */
@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.fantasyClick(
    indication: Indication? = null,
    time: Int = 1000,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
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
fun Modifier.consumeClicks() = fantasyClick(indication = null, onClick = NoOpLambda)

/**
 * Empty lambda.
 */
val NoOpLambda: () -> Unit = {}

inline fun <T : Any> Modifier.ifNotNull(value: T?, builder: (T) -> Modifier): Modifier =
    then(if (value != null) builder(value) else Modifier)

inline fun <T : Any> Modifier.ifNull(value: T?, builder: () -> Modifier): Modifier =
    then(if (value == null) builder() else Modifier)

inline fun Modifier.ifTrue(predicate: Boolean, builder: () -> Modifier) =
    then(if (predicate) builder() else Modifier)

inline fun Modifier.ifFalse(predicate: Boolean, builder: () -> Modifier) =
    then(if (!predicate) builder() else Modifier)
inline fun Modifier.ifDebug(builder: () -> Modifier): Modifier = ifTrue(isDebugBuilder, builder)

@Composable
inline fun Modifier.ifInPreview(builder: () -> Modifier): Modifier = ifTrue(LocalInspectionMode.current, builder)

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.debugClickable(
    onDoubleClick: () -> Unit = {},
    onClick: () -> Unit = {},
) =  ifTrue(isDebugBuilder) {
    Modifier.combinedClickable(
        onDoubleClick = onDoubleClick,
        onClick = onClick
    )
}

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





@SuppressLint("ModifierFactoryUnreferencedReceiver", "UnnecessaryComposedModifier")
@Composable
fun Modifier.addShadow(
    elevation: Dp = 4.dp,
    corner: Dp = 11.9.dp,
    color: Color = CCColor.f1.copy(0.5f),
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
    backgroundColor: Color = CCColor.b1,
    addBorder: Boolean = false,
) = composed {
    addShadow(elevation = elevation.dp, corner = cornerRadius)
        .then(
            if (addBorder)
                Modifier.border(0.3.dp, CCColor.f1.copy(0.3f), RoundedCornerShape(cornerRadius))
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
    backgroundColor: Color = CCColor.b1,
) = composed {
    addShadow(elevation = elevation, corner = cornerRadius, color = CCColor.f1.copy(0.7f))
        .background(backgroundColor, RoundedCornerShape(cornerRadius))
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
@Composable
fun Modifier.addTagBack(
    backgroundColor: Color = CCColor.b1,
    cornerRadius: Dp = 8.dp,
) = composed {
    clip(RoundedCornerShape(cornerRadius))
        .background(backgroundColor)
        .padding(horizontal = 10.dp, vertical = 5.dp)
}

@Composable
fun Modifier.addHazeContent(state: HazeState = AppHelper.hazeState) = Modifier.hazeSource(state)

// https://chrisbanes.github.io/haze/usage/
@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun Modifier.addHazeOver(
    color: Color = CCColor.b2,
    state: HazeState = AppHelper.hazeState,
    block: (HazeEffectScope.() -> Unit)? = null,
) = Modifier.hazeEffect(
    state = state,
    style = HazeMaterials.thin(containerColor = color)
) {
//    backgroundColor = color
    if (block == null) {
        blurRadius = 15.dp
        progressive = HazeProgressive.verticalGradient(startIntensity = 1f, endIntensity = 0f)
    } else {
        block()
    }
//    fallbackTint = HazeTint(color = color)
//    inputScale = HazeInputScale.Auto
//    progressive = HazeProgressive.verticalGradient(startIntensity = 1f, endIntensity = 0f)
}

fun Modifier.ccBlur(radius: Dp) = if (canBlur) Modifier.blur(radius = radius) else this
