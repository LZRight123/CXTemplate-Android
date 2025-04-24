package com.fantasy.components.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.components.extension.compose.addHazeContent
import com.fantasy.components.extension.compose.addHazeOver
import com.fantasy.components.extension.randomString
import com.fantasy.components.theme.CCColor
import dev.chrisbanes.haze.HazeProgressive
import dev.chrisbanes.haze.HazeState

@Composable
fun CCScaffold(
    modifier: Modifier = Modifier,
    containerColor: Color = CCColor.b2,
    contentColor: Color = CCColor.f1,
    isShowLoading: Boolean = false,
    canClickOutsideWithLoading: Boolean = false,
    haze:HazeState = remember { HazeState() },
    topHazeHeight: Int = 30,
    bottomHazeHeight: Int = 30,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable BoxScope.(PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = { topBar() },
        bottomBar = { bottomBar() },
        containerColor = containerColor,
        contentColor = contentColor,
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.addHazeContent(haze).fillMaxSize()) {
                content(innerPadding)
            }
            CCHaze(
                haze = haze,
                color = containerColor,
                topHazeHeight = topHazeHeight,
                bottomHazeHeight = bottomHazeHeight
            )

            CCLoading(
                isShow = isShowLoading,
                canClickOutside = canClickOutsideWithLoading
            )
        }
    }
}


@Composable
fun BoxScope.CCHaze(
    haze: HazeState,
    color: Color = CCColor.b2,
    topHazeHeight: Int = 30,
    bottomHazeHeight: Int = 30
) {
    if (topHazeHeight > 0) {
        Box(Modifier
            .addHazeOver(color = color, state = haze)
            .statusBarsPadding()
            .height(topHazeHeight.dp)
            .fillMaxWidth()
        )
    }

    if (bottomHazeHeight > 0) {
        Box(Modifier
            .addHazeOver(color = color,state = haze) {
                blurRadius = 15.dp
                progressive = HazeProgressive.verticalGradient(startIntensity = 0f, endIntensity = 1f)
            }
            .navigationBarsPadding()
            .height(bottomHazeHeight.dp)
            .fillMaxWidth()
            .align(Alignment.BottomStart)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    PreviewScreen { }
    val haze = remember { HazeState() }
    CCScaffold(
        topBar = {}
    ) {
        Box(Modifier.addHazeContent(state = haze).fillMaxSize()) {
            Text(text = randomString(300), modifier = Modifier.fillMaxSize())
            Text(text = randomString(300), modifier = Modifier.align(Alignment.BottomStart))
        }
        CCHaze(haze = haze)
    }
}