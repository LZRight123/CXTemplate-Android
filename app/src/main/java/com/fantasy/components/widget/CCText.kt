package com.fantasy.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.components.extension.color
import com.fantasy.components.extension.errorc
import com.fantasy.components.extension.f1c
import com.fantasy.components.extension.f3c
import com.fantasy.components.extension.randomString
import com.fantasy.components.theme.CCColor
import com.fantasy.components.theme.CCFont
import com.valentinilk.shimmer.shimmer

// 这个好 可以放进基础组件
@Composable
fun CCText(
    text: String?,
    length: Int = 5,
    style: TextStyle = CCFont.f1.v1.f1c,
    maxLines: Int = Int.MAX_VALUE,
    shimmerShape: Shape = RoundedCornerShape(4.dp),
    overflow: TextOverflow = TextOverflow.Ellipsis,
    modifier: Modifier = Modifier,
) {
    if (!text.isNullOrEmpty()) {
        Text(
            text = text, style = style, maxLines = maxLines, modifier = modifier,
            overflow = overflow
        )
    } else {
        Box(
            modifier = Modifier
                .clip(shimmerShape)
                .shimmer()
                .background(style.color.copy(0.3f))
        ) {
            Text(
                text = randomString(length),
                style = style,
                modifier = Modifier
                    .alpha(0f)
                    .then(modifier),
                maxLines = maxLines
            )
        }

    }
}

@Composable
fun CCTextTag(
    text: String?,
    textColor: Color = CCColor.b1,
    style: TextStyle = CCFont.f4b.v1.color(textColor),
    length: Int = 2,
    maxLines: Int = Int.MAX_VALUE,
    shimmerShape: Shape = RoundedCornerShape(4.dp),
    backgroundColor: Color = CCColor.green_t,
    modifier: Modifier = Modifier,
) {
    if (!text.isNullOrEmpty()) {
        CCText(
            text = text,
            style = style,
            maxLines = maxLines,
            modifier = modifier
                .clip(shimmerShape)
                .background(backgroundColor)
                .padding(4.dp, 2.dp)
        )
    } else {
        Box(
            modifier = Modifier
                .clip(shimmerShape)
                .shimmer()
                .background(backgroundColor.copy(0.8f))
                .padding(4.dp, 2.dp)
        ) {
            CCText(
                text = null,
                style = style.color(style.color.copy(0.8f)),
                length = length,
            )
        }

    }
}

@Preview
@Composable
private fun preview() {
    PreviewScreen(
        verticalArrangement = 10
    ) {
        CCText(text = randomString(15))
        CCText(text = null, style = CCFont.f3.v1.f3c)
        CCText(text = null)
        CCText(text = null, style = CCFont.big1b.v1.errorc)
        CCText(text = "CCTextTag")
        CCTextTag(text = "null")
        CCTextTag(text = null, backgroundColor = CCColor.error)
    }
}