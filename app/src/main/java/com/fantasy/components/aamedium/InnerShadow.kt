package com.fantasy.components.aamedium

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fantasy.components.theme.CCColor
import com.fantasy.components.widget.PreviewScreen

/**
 * https://medium.com/@kappdev/inner-shadow-in-jetpack-compose-d80dcd56f6cf
 * Adds an inner shadow effect to the content.
 *
 * shape 👉 阴影的形状。
 * color 👉 阴影的颜色。
 * blurRadio 👉 阴影的模糊半径。
 * offsetY 👉 阴影沿 Y 轴的偏移量。
 * offsetX 👉 阴影沿 X 轴的偏移量。
 * spread 👉 将阴影扩大到其尺寸之外的量。
 *
 * @return A modified Modifier with the inner shadow effect applied.
 */
private fun Modifier.innerShadow(
    shape: Shape = RectangleShape,
    color: Color = Color.Black,
    radius: Dp = 4.dp,
    offsetY: Dp = 2.dp,
    offsetX: Dp = 2.dp,
    spread: Dp = 0.dp
) = this.drawWithContent {

    drawContent()

    drawIntoCanvas { canvas ->
        val shadowSize = Size(size.width + spread.toPx(), size.height + spread.toPx())
        val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

        val paint = Paint()
        paint.color = color


        canvas.saveLayer(size.toRect(), paint)
        canvas.drawOutline(shadowOutline, paint)

        paint.asFrameworkPaint().apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
            if (radius.toPx() > 0) {
                maskFilter = BlurMaskFilter(radius.toPx(), BlurMaskFilter.Blur.NORMAL)
            }
        }

        paint.color = Color.Black

        canvas.translate(offsetX.toPx(), offsetY.toPx())
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}

private fun Modifier.outerShadow(
    shape: Shape = RectangleShape,
    color: Color = Color.Black,
    radius: Dp = 4.dp,
    offsetY: Dp = 2.dp,
    offsetX: Dp = 2.dp,
    spread: Dp = 0.dp
) = this.drawWithContent {

    // 先绘制阴影
    drawIntoCanvas { canvas ->
        val shadowSize = Size(size.width + spread.toPx() * 2, size.height + spread.toPx() * 2)
        val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

        val paint = Paint()
        paint.color = color
        paint.asFrameworkPaint().apply {
            if (radius.toPx() > 0) {
                maskFilter = BlurMaskFilter(radius.toPx(), BlurMaskFilter.Blur.NORMAL)
            }
        }

        canvas.translate(offsetX.toPx() - spread.toPx(), offsetY.toPx() - spread.toPx())
        canvas.drawOutline(shadowOutline, paint)
        canvas.translate(-offsetX.toPx() + spread.toPx(), -offsetY.toPx() + spread.toPx())
    }

    // 然后绘制内容
    drawContent()
}

fun Modifier.innerShadow(
    shape: Shape = RectangleShape,
    color: Color = Color.Black,
    radius: Int = 4,
    offsetY: Int = 0,
    offsetX: Int = 0,
    spread: Int = 0
) = innerShadow(shape, color, radius.dp, offsetY.dp, offsetX.dp, spread.dp)

fun Modifier.outerShadow(
    shape: Shape = RectangleShape,
    color: Color = Color.Black,
    radius: Int = 4,
    offsetY: Int = 0,
    offsetX: Int = 0,
    spread: Int = 0
) = outerShadow(shape, color, radius.dp, offsetY.dp, offsetX.dp, spread.dp)

@Preview(showBackground = true)
@Composable
private fun _preview() {
    PreviewScreen {

        Box(modifier = Modifier
            .innerShadow(
                shape = RectangleShape,
                color = CCColor.random,
                radius = 10,
                offsetY = -5,
                offsetX = -5,
                spread = 10
            )
            .size(200.dp))


        Box(modifier = Modifier
            .outerShadow(
                shape = RectangleShape,
                radius = 0,
                offsetY = 10,
                offsetX = 10,
                spread = 1
            )
            .size(200.dp)
        )
    }
}