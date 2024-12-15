package com.fantasy.components.aamedium

import android.os.VibrationEffect
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.fantasy.components.extension.color
import com.fantasy.components.extension.f1c
import com.fantasy.components.extension.randomString
import com.fantasy.components.theme.CXColor
import com.fantasy.components.theme.CXFont
import com.fantasy.components.tools.mada
import com.fantasy.components.tools.openUrl
import com.fantasy.components.widget.CXMarkdown
import com.fantasy.components.widget.PreviewScreen
import kotlinx.coroutines.delay

@Composable
fun CXTyperText(
    text: String,
    style: TextStyle = CXFont.f1.v1.f1c,
    modifier: Modifier = Modifier,
    charDuration: Int = 30, // 每个字符的间隔时间 如果想设置一个整体的时间 直接修改 spec
    madaStep: Long = 210, // mada 的最小间隔时间
    isMarkdown: Boolean = false,
    spec: AnimationSpec<Int> = tween(
        durationMillis = text.length * charDuration,
        easing = LinearEasing
    ),
) {
    if (LocalInspectionMode.current) {
        Text(text = text, style = style)
        return
    }

    var textToAnimate by remember { mutableStateOf("") }
    val index = remember { Animatable(initialValue = 0, typeConverter = Int.VectorConverter) }

    LaunchedEffect(text) {
        textToAnimate = text
        mada(VibrationEffect.EFFECT_TICK)
        index.animateTo(text.length, spec)
    }

    LaunchedEffect(text) {
        while (index.value < text.length) {
            delay((madaStep..madaStep + 50).random())
            mada(VibrationEffect.EFFECT_TICK)
        }
    }

    val guanBiao = if (index.value >= textToAnimate.length - 1) "" else " ●"
    if (isMarkdown) {
        CXMarkdown(
            content = textToAnimate.substring(0, index.value) + guanBiao,
            style = style,
            linkStyle = style.color(CXColor.f1),
            modifier = modifier
        ) {
            openUrl(it)
        }
    } else {
        Text(
            text = textToAnimate.substring(0, index.value) + guanBiao,
            style = style,
            modifier = modifier
        )
    }

}


@Preview(showBackground = true)
@Composable
private fun _preview() {
    PreviewScreen {
        Text(text = "???")
        CXTyperText(text = "Hello, World!")
        CXTyperText(text = randomString(100))
    }
}