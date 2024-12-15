package com.fantasy.components.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.components.extension.compose.consumeClicks
import com.fantasy.components.tools.screenWith
import com.fantasy.cxtemplate.R


/**
 * canClickOutside：是事可以点击外围-背后的区域
 * blog: https://juejin.cn/post/7001326105953042463
 */
@Composable
fun CXLoading(// Can click outside
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
            CXLottieView(
                modifier = Modifier.size((screenWith * 0.618).dp),
                resId = resId,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewScreen {
        CXLoading()

        CXLoading(resId =  R.raw.loading_gray)

    }
}
