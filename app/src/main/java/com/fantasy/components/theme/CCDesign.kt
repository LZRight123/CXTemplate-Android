package com.fantasy.components.theme

import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fantasy.components.extension.boldBlack
import com.fantasy.components.extension.compose.CCPaddingValues
import com.fantasy.components.widget.CCScaffold

@Stable
class CCMutableColors(
    main: Color,
    f1: Color,
    f2: Color,
    f3: Color,
    b1: Color,
    b2: Color,
    b3: Color,
    error: Color,
    black: Color,
    white: Color,
) {
    var main: Color by mutableStateOf(main)
        private set
    var f1: Color by mutableStateOf(f1)
        private set
    var f2: Color by mutableStateOf(f2)
        private set
    var f3: Color by mutableStateOf(f3)
        private set
    var b1: Color by mutableStateOf(b1)
        private set
    var b2: Color by mutableStateOf(b2)
        private set
    var b3: Color by mutableStateOf(b3)
        private set
    var error: Color by mutableStateOf(error)
        private set
    var black: Color by mutableStateOf(black)
        private set
    var white: Color by mutableStateOf(white)
        private set

    val random: Color
        get() = Color(
            kotlin.random.Random.nextFloat().coerceIn(0f, 255f),
            kotlin.random.Random.nextFloat().coerceIn(0f, 255f),
            kotlin.random.Random.nextFloat().coerceIn(0f, 255f),
            1f
        )

}

enum class Theme(
    val light: Color,
    val dark: Color
) {
    main(light = Color(0xFF6735C0), dark = Color(0xFFFFFFFF)),
    f1(light = Color(0xFF212121), dark = Color(0xFFFCFCFC)),
    f2(light = Color(0xFF7C7C7C), dark = Color(0xFFB3B3B3)),
    f3(light = Color(0xFFCECECE), dark = Color(0xFF8B8B8B)),
    b1(light = Color(0xFFFFFFFF), dark = Color(0xFF202020)),
    b2(light = Color(0xFFF8F8F8), dark = Color(0xFF0D0D0D)),
    b3(light = Color(0xFFD7D7D7), dark = Color(0xFF000000)),
    error(light = Color(0xFFFF4040), dark = Color(0xFFFFFFFF)),
    black(light = Color(0xFF000000), dark = Color(0xFFFFFFFF)),
    white(light = Color(0xFFFFFFFF), dark = Color(0xFFFFFFFF)),
    ;
}

val LightColorPalette = CCMutableColors(
    main = Theme.main.light,
    f1 = Theme.f1.light,
    f2 = Theme.f2.light,
    f3 = Theme.f3.light,
    b1 = Theme.b1.light,
    b2 = Theme.b2.light,
    b3 = Theme.b3.light,
    error = Theme.error.light,
    black = Theme.black.light,
    white = Theme.white.light,
)

val DarkColorPalette = CCMutableColors(
    main = Theme.main.dark,
    f1 = Theme.f1.dark,
    f2 = Theme.f2.dark,
    f3 = Theme.f3.dark,
    b1 = Theme.b1.dark,
    b2 = Theme.b2.dark,
    b3 = Theme.b3.dark,
    error = Theme.error.dark,
    black = Theme.black.dark,
    white = Theme.white.dark,
)

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

val CCColor: CCMutableColors
    @Composable
    @ReadOnlyComposable
    get() = LocalCCMutableColors.current

val CCMutableColors.sheetBackgroundColor: Color
    @Composable
    @ReadOnlyComposable
    get() = CCColor.f1.copy(0.45f)

object CCIndication {
    val ripple1: Indication = ripple(color = Color.White)
}


enum class CCFont {
    big1, big1b,
    big2, big2b,
    big3, big3b,
    f1, f1b,
    f2, f2b,
    f3, f3b,
    ;

    private val big1sp = 32.sp
    private val big2sp = 24.sp
    private val big3sp = 20.sp
    private val f1sp = 17.sp
    private val f2sp = 15.sp
    private val f3sp = 13.sp
    val v1: TextStyle
        get() = when (this) {
            big1 -> TextStyle(fontSize = big1sp)
            big2 -> TextStyle(fontSize = big2sp)
            big3 -> TextStyle(fontSize = big3sp)
            big1b -> TextStyle(fontSize = big1sp, fontWeight = FontWeight.SemiBold)
            big2b -> TextStyle(fontSize = big2sp, fontWeight = FontWeight.SemiBold)
            big3b -> TextStyle(fontSize = big3sp, fontWeight = FontWeight.SemiBold)
            f1 -> TextStyle(fontSize = f1sp)
            f2 -> TextStyle(fontSize = f2sp)
            f3 -> TextStyle(fontSize = f3sp)
            f1b -> TextStyle(fontSize = f1sp, fontWeight = FontWeight.SemiBold)
            f2b -> TextStyle(fontSize = f2sp, fontWeight = FontWeight.SemiBold)
            f3b -> TextStyle(fontSize = f3sp, fontWeight = FontWeight.SemiBold)
        }
    val v2: TextStyle
        get() = when (this) {
            big1 -> TextStyle(fontSize = big1sp)
            big2 -> TextStyle(fontSize = big2sp)
            big3 -> TextStyle(fontSize = big3sp)
            big1b -> TextStyle(fontSize = big1sp, fontWeight = FontWeight.SemiBold)
            big2b -> TextStyle(fontSize = big2sp, fontWeight = FontWeight.SemiBold)
            big3b -> TextStyle(fontSize = big3sp, fontWeight = FontWeight.SemiBold)
            f1 -> TextStyle(fontSize = f1sp)
            f2 -> TextStyle(fontSize = f2sp)
            f3 -> TextStyle(fontSize = f3sp)
            f1b -> TextStyle(fontSize = f1sp, fontWeight = FontWeight.SemiBold)
            f2b -> TextStyle(fontSize = f2sp, fontWeight = FontWeight.SemiBold)
            f3b -> TextStyle(fontSize = f3sp, fontWeight = FontWeight.SemiBold)
        }
}


@Preview(heightDp = 1600)
@Composable
private fun Preview2() {
    CCScaffold(
        title = "设计系统-基础",
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = CCPaddingValues(innerPadding, all = 18.dp)
        ) {
            item {
                Text(text = "v1 号字体", style = CCFont.big1.v1.boldBlack)
            }
            items(CCFont.entries) {
                Text(text = "Hello Word - ${it.name}", style = it.v1)
            }

            item {
                Text(
                    text = "v2 号字体",
                    style = CCFont.big1.v2.boldBlack,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
            items(CCFont.entries) {
                Text(text = "Hello Word - ${it.name}", style = it.v2)
            }

            item {
                Text(
                    text = "颜色",
                    style = CCFont.big1.v2.boldBlack,
                    modifier = Modifier.padding(top = 20.dp)
                )
            }
            items(Theme.entries) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .background(it.light)
                            .height(20.dp)
                            .fillMaxWidth()
                    )
                    Text(text = it.name, style = CCFont.f1.v1)
                }
            }
        }
    }
}