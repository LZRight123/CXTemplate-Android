package com.fantasy.components.widget

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import com.fantasy.components.extension.compose.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.components.animations.ccAlphaIn
import com.fantasy.components.extension.f1c
import com.fantasy.components.extension.f2c
import com.fantasy.components.extension.randomString
import com.fantasy.components.theme.CCFont
import com.fantasy.components.tools.screenHeight
import com.fantasy.components.tools.screenWidth
import com.fantasy.cctemplate.R
import me.nikhilchaudhari.usenetworkstate.NetworkState
import me.nikhilchaudhari.usenetworkstate.useNetworkState

/**
 * 暂位图
 */
@Composable
fun CCEmptyView(
    @DrawableRes image: Int? = null,
    title: String = "暂无数据",
    subline: String = "",
    modifier: Modifier = Modifier,
    forcesHidden: Boolean = false,
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
                .graphicsLayer {
                    translationY = ty.value
                }
                .ccAlphaIn()
                .fillMaxWidth(),
        ) {
            if (image != null) {
                Image(
                    id = image,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(240.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.offset(y = (-24).dp)
            ) {
                Text(
                    text = title,
                    style = CCFont.f2b.v2.f1c,
                )
                if (subline.isNotEmpty()) {
                    val networkState by useNetworkState()
                    if (networkState == NetworkState.Offline && LocalInspectionMode.current.not()) {
                        Text(text = "无网络连接，稍后重试", style = CCFont.f3.v1.f2c)
                    } else {
                        Text(
                            text = subline,
                            style = CCFont.f3.v1.f2c,
                        )
                    }
                }

            }

        }
    }
}

@Preview(
    locale = "zh-rTW",
    showBackground = true,
    heightDp = 2800
)
@Composable
private fun Preview() {
    PreviewScreen {
//        CCEmptyView(
//            image = R.drawable.empty_collection,
//            title = "暂无展柜",
//            subline = "从创建一个展柜开始，开始你的收藏家旅程。"
//        )
//        CCEmptyView(
//            image = R.drawable.empty_collections,
//            title = "暂无藏品",
//            subline = "别让你的展柜空荡荡的！"
//        )
//        CCEmptyView(
//            image = R.drawable.empty_card,
//            title = "暂无卡牌收藏",
//            subline = "记录餐食获得点数，即可开始集换式卡牌之旅。"
//        )
//        CCEmptyView(
//            image = R.drawable.empty_food,
//            title = "暂无餐饮记录",
//            subline = "记录餐食获得点数，即可开始集换式卡牌之旅。"
//        )
//        CCEmptyView(
//            image = R.drawable.empty_unlockfood,
//            title = "暂无解锁记录",
//            subline = "带上胃之书，品味人间美味，解锁人生食单。"
//        )
//        CCEmptyView(
//            image = R.drawable.daily_report_empty,
//            title = "暂无报告",
//            subline = "这一天还没有记录哦。"
//        )
//        CCEmptyView(
//            image = R.drawable.daily_report_empty,
//            title = "暂无记录",
//            subline = "用胃之书记录一日三餐。"
//        )
    }
}
