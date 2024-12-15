package com.fantasy.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.components.extension.f1c
import com.fantasy.components.theme.CXColor
import com.fantasy.components.theme.CXFont
import kotlinx.coroutines.delay

@Composable
fun FpsCounter() {
    var frameCount by remember { mutableIntStateOf(0) }
    var fps by remember { mutableIntStateOf(0) }

    // Coroutine to update FPS every second
    LaunchedEffect(Unit) {
        while (true) {
            val startTime = System.nanoTime()
            val startFrameCount = frameCount
            delay(1000L)  // wait for 1 second
            val endFrameCount = frameCount
            val endTime = System.nanoTime()
            val deltaTime = (endTime - startTime) / 1_000_000_000.0
            fps = ((endFrameCount - startFrameCount) / deltaTime).toInt()
        }
    }

    // A state that changes every frame to trigger recomposition
    LaunchedEffect(Unit) {
        while (true) {
            frameCount++
            withFrameNanos { }
        }
    }

    // Display the FPS
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(CXColor.error, RoundedCornerShape(49.8f))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = "FPS: ",
            style = CXFont.f3b.v2.f1c,
        )
        Text(text = "$fps", style = CXFont.f1.v1.f1c)
    }

}

@Preview(showBackground = true)
@Composable
private fun preview() {
    PreviewScreen {
        FpsCounter()
    }
}