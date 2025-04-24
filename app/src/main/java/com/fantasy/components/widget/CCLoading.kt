package com.fantasy.components.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.components.extension.compose.consumeClicks
import com.fantasy.components.theme.CCColor
import com.fantasy.cctemplate.R


/**
 * canClickOutside：是事可以点击外围-背后的区域
 * blog: https://juejin.cn/post/7001326105953042463
 */
@Composable
fun CCLoading(// Can click outside
    isShow: Boolean = true,
    resId: Int = R.raw.loading_gray,
    canClickOutside: Boolean = false
) {
    if (isShow) {
        Box(
            modifier = Modifier
                .then(
                    if (!canClickOutside) Modifier.consumeClicks() else Modifier
                )
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = CCColor.f3,
                strokeWidth = 3.dp,
                strokeCap = StrokeCap.Round,
            )

//            CCLottieView(
//                modifier = Modifier.size(80.dp),
//                resId = resId,
//            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewScreen {
        CCLoading()

        CCLoading(resId =  R.raw.loading_gray)

    }
}
