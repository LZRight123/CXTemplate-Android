package com.fantasy.components.widget


import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.request.ImageRequest
import com.fantasy.components.extension.compose.ifNotNull
import com.fantasy.components.extension.mockImage
import com.fantasy.components.theme.CCColor
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.crossfade.CrossfadePlugin
import com.skydoves.landscapist.coil3.CoilImage
import com.skydoves.landscapist.coil3.CoilImageState
import com.skydoves.landscapist.components.ImageComponent
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.plugins.ImagePlugin
import com.valentinilk.shimmer.shimmer

/**
 * 图片处理学习文档
 * https://developer.android.com/jetpack/compose/graphics/images/customize?hl=zh-cn
 */
// https://github.com/skydoves/landscapist
@Composable
fun CCCoilImage(
    model: () -> Any?,
    modifier: Modifier = Modifier,
    imageLoadAnimation: Boolean = false,
    component: ImageComponent = rememberImageComponent {
        if (imageLoadAnimation) {
            +CrossfadePlugin(duration = 300)
        }
    },
    requestListener: (() -> ImageRequest.Listener)? = null,
    imageOptions: ImageOptions = ImageOptions(),
    onImageStateChanged: (CoilImageState) -> Unit = {},
    @DrawableRes previewPlaceholder: Int = 0,
    loading: @Composable (BoxScope.(imageState: CoilImageState.Loading) -> Unit)? = {
        coilShimmerLoading()
    },
    success: @Composable (BoxScope.(imageState: CoilImageState.Success, painter: Painter) -> Unit)? = null,
    failure: @Composable (BoxScope.(imageState: CoilImageState.Failure) -> Unit)? = {
        coilShimmerLoading()
    },
) {
    CoilImage(
        imageModel = model,
        component = component,
        requestListener = requestListener,
        imageOptions = imageOptions,
        onImageStateChanged = onImageStateChanged,
        modifier = modifier,
        // ColorPainter(CCColor.random)
        previewPlaceholder = if (previewPlaceholder == 0) ColorPainter(CCColor.random) else painterResource(
            id = previewPlaceholder
        ),
        loading = loading,
        success = success,
        failure = failure,
    )
}

@Composable
fun BoxScope.coilShimmerLoading() {
    Box(
        modifier = Modifier
            .background(CCColor.b1_t)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CCColor.f1.copy(0f),
                        CCColor.f1.copy(0.02f),
                        CCColor.f1.copy(0.04f),
                        CCColor.f1.copy(0.06f),
                        CCColor.f1.copy(0.08f),
                        CCColor.f1.copy(0.1f),
                        CCColor.f1.copy(0.12f),
                        CCColor.f1.copy(0.14f),
                    )
                )
            )
            .shimmer()
            .background(CCColor.b1_t)
            .matchParentSize()
    )
}


@Composable
fun BoxScope.coilCircularLoading() {
    Box(
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(18.dp),
            color = CCColor.f3,
            strokeWidth = 2.dp
        )
    }
}

data class PixelateTransformationPlugin(
    private val pixelSize: Int
) : ImagePlugin.PainterPlugin {
    @Composable
    override fun compose(imageBitmap: ImageBitmap, painter: Painter): Painter {
        val pixelatedBitmap = imageBitmap.asAndroidBitmap().let { input ->
            // 创建一个可变的 Bitmap
            val mutableBitmap = input.copy(Bitmap.Config.ARGB_8888, true)
            val width = mutableBitmap.width
            val height = mutableBitmap.height

            for (x in 0 until width step pixelSize) {
                for (y in 0 until height step pixelSize) {
                    val pixel = mutableBitmap.getPixel(
                        (x + pixelSize / 2).coerceAtMost(width - 1),
                        (y + pixelSize / 2).coerceAtMost(height - 1)
                    )
                    for (i in x until (x + pixelSize).coerceAtMost(width)) {
                        for (j in y until (y + pixelSize).coerceAtMost(height)) {
                            mutableBitmap.setPixel(i, j, pixel)
                        }
                    }
                }
            }
            mutableBitmap
        }

        return BitmapPainter(pixelatedBitmap.asImageBitmap())
    }

}


@Preview(showBackground = false)
@Composable
private fun Preview() {
    PreviewScreen(backgroundColor = CCColor.b2) {
        CCCoilImage(
            model = { mockImage },
            modifier = Modifier.size(200.dp)
        )
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            coilCircularLoading()
        }
    }

}
