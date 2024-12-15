package com.fantasy.components.widget

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import com.fantasy.components.extension.compose.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.components.animations.cxAlphaIn
import com.fantasy.components.extension.f2c
import com.fantasy.components.extension.randomString
import com.fantasy.components.theme.CXFont
import com.fantasy.components.tools.screenHeight

/**
 * 暂位图
 */
@Composable
fun CXEmptyView(
    @DrawableRes image: Int? = null,
    text: String = "暂时没有内容",
    subline: String = "",
    modifier: Modifier = Modifier
        .padding(top = (screenHeight * 0.15).dp, bottom = 24.dp)
        .fillMaxWidth(),
    forcesHidden: Boolean = false,
    bottomView: @Composable ColumnScope.() -> Unit = {},
) {
    if (forcesHidden.not()) {
        val d = LocalDensity.current.density
        val ty = remember {
            Animatable(10 * d)
        }
        LaunchedEffect(Unit) {
            ty.animateTo(0f, tween(600))
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = modifier
                .graphicsLayer {
                    translationY = ty.value
                }
                .cxAlphaIn(),
        ) {
            if (image != null) {
                Image(
                    id = image,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .height(200.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (text.isNotEmpty()) {
                    Text(text = text, style = CXFont.f1.v1)
                }
                if (subline.isNotEmpty()) {
                    Text(text = subline, style = CXFont.f3.v1.f2c)
                }
//                val networkState by useNetworkState()
//                if (networkState == NetworkState.Offline) {
//                    Text(text = "无网络连接，稍后重试", style = XMFont.f3.v1.f2c)
//                }
            }
            bottomView()
        }
    }
}

@Preview(
    locale = "zh-rTW",
    showBackground = true
)
@Composable
private fun Preview() {
    PreviewScreen {
        CXEmptyView(
            text = randomString(4),
            subline = randomString(10),
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            CXButton(
                onClick = {}
            ) {
                Text(text = "重试")
            }
        }
    }
}
