package com.fantasy.components.aamedium

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.components.extension.compose.addCardBack
import com.fantasy.components.extension.f2c
import com.fantasy.components.extension.randomString
import com.fantasy.components.theme.CXFont
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.fantasy.components.theme.CXColor
import com.fantasy.components.tools.navBarHeight
import com.fantasy.components.tools.safeAreaBottom
import com.fantasy.components.tools.safeAreaTop
import com.fantasy.components.tools.tabBarHeight

@Composable
fun Modifier.cxFadeEdge(
    color: Color = CXColor.b1,
    top: Dp = safeAreaTop + navBarHeight + 50.dp,
    bottom: Dp = safeAreaBottom + tabBarHeight + 80.dp
) = composed {
    topFadingEdge(
        color = color,
        width = top,
    )
        .bottomFadingEdge(
            color = color,
            width = bottom,
        )
}

fun Modifier.topFadingEdge(
    color: Color,
    isVisible: Boolean = true,
    width: Dp = 18.dp,
    spec: AnimationSpec<Dp>? = null
) = fadingEdge(FadeSide.TOP, color = color, width = width, isVisible = isVisible, spec = spec)

fun Modifier.bottomFadingEdge(
    color: Color,
    isVisible: Boolean = true,
    width: Dp = 18.dp,
    spec: AnimationSpec<Dp>? = null
) = fadingEdge(FadeSide.BOTTOM, color = color, width = width, isVisible = isVisible, spec = spec)

fun Modifier.rightFadingEdge(
    color: Color,
    isVisible: Boolean = true,
    width: Dp = 18.dp,
    spec: AnimationSpec<Dp>? = null
) = fadingEdge(FadeSide.RIGHT, color = color, width = width, isVisible = isVisible, spec = spec)

fun Modifier.leftFadingEdge(
    color: Color,
    isVisible: Boolean = true,
    width: Dp = 18.dp,
    spec: AnimationSpec<Dp>? = null
) = fadingEdge(FadeSide.LEFT, color = color, width = width, isVisible = isVisible, spec = spec)

fun Modifier.fadingEdge(
    vararg sides: FadeSide,
    color: Color,
    width: Dp = 18.dp,
    isVisible: Boolean = true,
    spec: AnimationSpec<Dp>? = null
) = composed {
    require(width > 0.dp) { "Invalid fade width: Width must be greater than 0" }

    val animatedWidth = spec?.let {
        animateDpAsState(
            targetValue = if (isVisible) width else 0.dp,
            animationSpec = spec,
            label = "Fade width"
        ).value
    }

    drawWithContent {
        this@drawWithContent.drawContent()

        sides.forEach { side ->
            val (start, end) = this.size.getFadeOffsets(side)

            val staticWidth = if (isVisible) width.toPx() else 0f
            val widthPx = animatedWidth?.toPx() ?: staticWidth

            val fraction = when (side) {
                FadeSide.LEFT, FadeSide.RIGHT -> widthPx / this.size.width
                FadeSide.BOTTOM, FadeSide.TOP -> widthPx / this.size.height
            }

            drawRect(
                brush = Brush.linearGradient(
                    0f to color,
                    fraction to Color.Transparent,
                    start = start,
                    end = end
                ),
                size = this.size
            )
        }
    }
}

enum class FadeSide {
    LEFT, RIGHT, BOTTOM, TOP
}

private fun Size.getFadeOffsets(side: FadeSide): Pair<Offset, Offset> {
    return when (side) {
        FadeSide.LEFT -> Offset.Zero to Offset(width, 0f)
        FadeSide.RIGHT -> Offset(width, 0f) to Offset.Zero
        FadeSide.BOTTOM -> Offset(0f, height) to Offset.Zero
        FadeSide.TOP -> Offset.Zero to Offset(0f, height)
    }
}

@Preview(showBackground = true)
@Composable
private fun _preview() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(18.dp),
        modifier = Modifier
            .cxFadeEdge()
            .bottomFadingEdge(
                color = CXColor.random,

                )
            .fillMaxSize(),

        ) {

        items(30) {
            Text(
                text = randomString(10), style = CXFont.f1b.v1.f2c,
                modifier = Modifier
                    .addCardBack()
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

    }
}