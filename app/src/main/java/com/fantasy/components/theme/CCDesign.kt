package com.fantasy.components.theme

import androidx.compose.foundation.Indication
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.cctemplate.R
import com.fantasy.components.extension.boldBlack
import com.fantasy.components.extension.compose.CCPaddingValues
import com.fantasy.components.extension.size
import com.fantasy.components.widget.CCScaffold
import kotlin.random.Random

val Color.Companion.random
    get() =
        Color(
            Random.nextFloat().coerceIn(0f, 255f),
            Random.nextFloat().coerceIn(0f, 255f),
            Random.nextFloat().coerceIn(0f, 255f),
            1f
        )

@Stable
class CCMutableColors(
    val main: Color,
    // 常规前景色
    val f1: Color,
    val f2: Color,
    val f3: Color,
    // 主题前景色
    val f2_t: Color,
    // 常规背景色
    val b1: Color,
    val b2: Color,
    val b3: Color,
    // 主题背景色
    val b1_t: Color,
    val b2_t: Color,
    val b3_t: Color,
    // 主题彩色
    val pink_t: Color,
    val green_t: Color,
    val blue_t: Color,
    val red_t: Color,
    val error: Color,
    val black: Color,
    val white: Color,
) {
    //    var main: Color by mutableStateOf(main)
    //        private set
    //    var f1: Color by mutableStateOf(f1)
    //        private set
    //    var f2: Color by mutableStateOf(f2)
    //        private set
    //    var f3: Color by mutableStateOf(f3)
    //        private set
    //    var b1: Color by mutableStateOf(b1)
    //        private set
    //    var b2: Color by mutableStateOf(b2)
    //        private set
    //    var b3: Color by mutableStateOf(b3)
    //        private set
    //    var error: Color by mutableStateOf(error)
    //        private set
    //    var black: Color by mutableStateOf(black)
    //        private set
    //    var white: Color by mutableStateOf(white)
    //        private set
    //    var pink: Color by mutableStateOf(pink)
    //        private set

    val random: Color
        get() =
            Color(
                Random.nextFloat().coerceIn(0f, 255f),
                Random.nextFloat().coerceIn(0f, 255f),
                Random.nextFloat().coerceIn(0f, 255f),
                1f
            )
}

val LightColorPalette =
    CCMutableColors(
        main = Color(0xFF6735C0),
        // 常规前景色
        f1 = Color(0xFF0E0808),
        f2 = Color(0xFF676363),
        f3 = Color(0xFF97918B),
        // 主题前景色
        f2_t = Color(0xFF463130),
        // 常规背景色
        b1 = Color(0xFFFAF9F9),
        b2 = Color(0xFFF1F0F1),
        b3 = Color(0xFFECEAE1),
        // 主题背景色
        b1_t = Color(0xFFFAF7F5),
        b2_t = Color(0xFFEFEAE7),
        b3_t = Color(0xFFE8E1DD),
        // 主题彩色
        pink_t = Color(0xFFC73D66),
        green_t = Color(0xFFBAE800),
        blue_t = Color(0xFF323FD0),
        red_t = Color(0xFFF24016),
        error = Color(0xFFF24016),
        black = Color(0xFF000000),
        white = Color(0xFFFFFFFF),
    )

val DarkColorPalette = LightColorPalette

/*
使用 provides 后，在此作用域下 LocalFantasyMutableColors.current 会变成指定的值
FantasyTheme.colors 获取的是 LocalFantasyMutableColors.current 所以颜色会发生相应改变
CompositionLocalProvider(LocalFantasyColors provides DarkColorPalette) {
                Text(
                    text = "Dark",
                    color = MTheme.colors.mainColor
                )
 }
 */
val LocalCCMutableColors = compositionLocalOf { LightColorPalette }

val CCColor: CCMutableColors = LightColorPalette
//    @Composable @ReadOnlyComposable get() = LocalCCMutableColors.current

val CCMutableColors.sheetBackgroundColor: Color
    @Composable @ReadOnlyComposable get() = CCColor.f1.copy(0.1f)

object CCIndication {
    val ripple1: Indication = ripple(color = Color.White)
}

enum class CCFont {
    big1,
    big2,
    big3,
    big1b,
    big2b,
    big3b,
    f1,
    f2,
    f3,
    f4,
    f1b,
    f2b,
    f3b,
    f4b,
    ;

    companion object {
        //        fun custom(name: Int = R.font.maplemono_cn_extralight) =
        // custom(FontFamily(Font(resId = name)))
        fun custom(fontFamily: FontFamily) = TextStyle(fontFamily = fontFamily)
    }


    private val v1L = FontFamily(Font(resId = R.font.firasansextracondensed_light))
    private val v1B = FontFamily(Font(resId = R.font.firasansextracondensed_semibold))
    private val v2L = FontFamily(Font(resId = R.font.maplemono_cn_extralight))
    private val v2B = FontFamily(Font(resId = R.font.maplemono_cn_semibold))

    private val big1sp = 28
    private val big2sp = 26
    private val big3sp = 24
    private val f1sp = 17
    private val f2sp = 15
    private val f3sp = 13
    private val f4sp = 11
    val v1: TextStyle
        get() =
            when (this) {
                big1 -> TextStyle().size(big1sp)
                big2 -> TextStyle().size(big2sp)
                big3 -> TextStyle().size(big3sp)
                big1b -> TextStyle().size(big1sp)
                big2b -> TextStyle().size(big2sp)
                big3b -> TextStyle().size(big3sp)
                f1 -> TextStyle().size(f1sp)
                f2 -> TextStyle().size(f2sp)
                f3 -> TextStyle().size(f3sp)
                f4 -> TextStyle().size(f4sp)
                f1b -> TextStyle().size(f1sp)
                f2b -> TextStyle().size(f2sp)
                f3b -> TextStyle().size(f3sp)
                f4b -> TextStyle().size(f4sp)
            }
    val v2: TextStyle
        get() =
            when (this) {
                big1 -> custom(v2L).size(big1sp)
                big2 -> custom(v2L).size(big2sp)
                big3 -> custom(v2L).size(big3sp)
                big1b -> custom(v2B).size(big1sp)
                big2b -> custom(v2B).size(big2sp)
                big3b -> custom(v2B).size(big3sp)
                f1 -> custom(v2L).size(f1sp)
                f2 -> custom(v2L).size(f2sp)
                f3 -> custom(v2L).size(f3sp)
                f4 -> custom(v2L).size(f4sp)
                f1b -> custom(v2B).size(f1sp)
                f2b -> custom(v2B).size(f2sp)
                f3b -> custom(v2B).size(f3sp)
                f4b -> custom(v2B).size(f4sp)
            }
}

@Preview(heightDp = 1600)
@Composable
private fun Preview2() {
    CCScaffold() { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = CCPaddingValues(innerPadding, all = 18.dp)
        ) {
            item { Text(text = "v1 号字体", style = CCFont.big1.v1.boldBlack) }
            items(CCFont.entries) { Text(text = "Hello Word - ${it.name}", style = it.v1) }

            item {
                Text(
                    text = "v2 号字体",
                    style = CCFont.big1.v2.boldBlack,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
            items(CCFont.entries) { Text(text = "Hello Word - ${it.name}", style = it.v2) }

            item {
                Text(
                    text = "颜色",
                    style = CCFont.big1.v2.boldBlack,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
        }
    }
}
