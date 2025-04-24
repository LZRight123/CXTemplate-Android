package com.fantasy.components.aamedium

import android.os.VibrationEffect
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
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
import com.fantasy.components.theme.CCColor
import com.fantasy.components.theme.CCFont
import com.fantasy.components.tools.mada
import com.fantasy.components.tools.openUrl
import com.fantasy.components.widget.CCMarkdown
import com.fantasy.components.widget.PreviewScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.min

@Composable
fun CCTyperText(
    text: String,
    style: TextStyle = CCFont.f1.v2.f1c,
    modifier: Modifier = Modifier,
    charDuration: Int = 30, // 每个字符的间隔时间 如果想设置一个整体的时间 直接修改 spec
    madaStep: Long = 210, // mada 的最小间隔时间
    mada: Boolean = true,
    isMarkdown: Boolean = false,
    duration: Int = text.length * charDuration,
    cursor : String = "●",
    spec: AnimationSpec<Int> = tween(durationMillis = duration, easing = LinearEasing),
) {
    if (LocalInspectionMode.current) {
        Text(text = text, style = style, modifier = modifier)
        return
    }

    var textToAnimate by remember { mutableStateOf("") }
    val index = remember { Animatable(initialValue = 0, typeConverter = Int.VectorConverter) }

    LaunchedEffect(text) {
        textToAnimate = text
        index.animateTo(text.length, spec)
    }

    LaunchedEffect(Unit) {
        while (isActive && mada) {
//            cclog("cctyper text while 循环空转 ${index.value} ${text.length} ${textToAnimate.length}")
            if (index.value < textToAnimate.length) {
                mada(VibrationEffect.EFFECT_TICK)
            }
            delay((madaStep..madaStep + 50).random())
        }
    }


    val guanBiao = if (index.value >= textToAnimate.length - 1) "" else " $cursor"
    if (isMarkdown) {
        CCMarkdown(
            text = textToAnimate.substring(0, index.value) + guanBiao,
            style = style,
            linkStyle = style.color(CCColor.f1),
            modifier = modifier
        ) { openUrl(it) }
    } else {
        Text(
            text = textToAnimate.take(index.value) + guanBiao,
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
        CCTyperText(text = "Hello, World!")
        CCTyperText(text = randomString(100))
    }
}
