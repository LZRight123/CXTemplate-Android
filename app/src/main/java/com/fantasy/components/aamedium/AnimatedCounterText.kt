package com.fantasy.components.aamedium

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.fantasy.components.extension.randomString
import com.fantasy.components.theme.CXFont
import com.fantasy.components.widget.PreviewScreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnimatedCounterText(
    text: String,
    style: TextStyle = CXFont.f1.v1,
    down: Boolean = true, // 默认动画从上往下
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier
    ) {
        text.forEach { d ->
            AnimatedContent(
                targetState = d,
                transitionSpec = {
                    if (down) {
                        slideInVertically { -it } togetherWith  slideOutVertically { it }
                    } else {
                        slideInVertically { it } togetherWith slideOutVertically { -it }
                    }
                }, label = ""
            ) {
                Text(text = it.toString(), style = style)
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun _preview() {
    PreviewScreen {
        AnimatedCounterText(text = randomString(5))
    }
}