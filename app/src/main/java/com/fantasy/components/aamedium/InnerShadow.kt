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
import com.fantasy.components.theme.CXColor
import com.fantasy.components.widget.PreviewScreen

/**
 * Adds an inner shadow effect to the content.
 *
 * @param shape The shape of the shadow.
 * @param color The color of the shadow.
 * @param blur The blur radius of the shadow.
 * @param offsetY The shadow offset along the Y-axis.
 * @param offsetX The shadow offset along the X-axis.
 * @param spread The amount to expand the shadow beyond its size.
 *
 * @return A modified Modifier with the inner shadow effect applied.
 */
fun Modifier.innerShadow(
    shape: Shape = RectangleShape,
    color: Color = Color.Black,
    blur: Dp = 4.dp,
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
            if (blur.toPx() > 0) {
                maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
            }
        }

        paint.color = Color.Black

        canvas.translate(offsetX.toPx(), offsetY.toPx())
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}

@Composable
fun Modifier.outerShadow(
    shape: Shape,
    color: Color = CXColor.f1,
    blur: Dp = 4.dp,
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
            if (blur.toPx() > 0) {
                maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
            }
        }

        canvas.translate(offsetX.toPx() - spread.toPx(), offsetY.toPx() - spread.toPx())
        canvas.drawOutline(shadowOutline, paint)
        canvas.translate(-offsetX.toPx() + spread.toPx(), -offsetY.toPx() + spread.toPx())
    }

    // 然后绘制内容
    drawContent()
}

@Preview(showBackground = true)
@Composable
private fun _preview() {
    PreviewScreen {

        Box(modifier = Modifier
            .innerShadow(
                shape = RectangleShape,
                color = CXColor.random,
                blur = 10.dp,
                offsetY = -5.dp,
                offsetX = -5.dp,
                spread = 10.dp
            )
            .size(200.dp))


        Box(modifier = Modifier
            .outerShadow(
                shape = RectangleShape,
                blur = 0.dp,
                offsetY = 10.dp,
                offsetX = 10.dp,
                spread = 1.dp
            )
            .size(200.dp)
        )
    }
}