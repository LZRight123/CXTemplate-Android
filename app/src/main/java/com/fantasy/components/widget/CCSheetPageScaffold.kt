package com.fantasy.components.widget


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.components.base.BaseScreen
import com.fantasy.components.widget.CCScaffold
import com.fantasy.components.theme.CCFont
import com.fantasy.components.theme.CCColor



@Composable
fun CCSheetPageScaffold(
    containerColor: Color = CCColor.b2_t,
    isShowLoading: Boolean = false,
    topHazeHeight: Int = 30,
    bottomHazeHeight: Int = 30,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable BoxScope.(PaddingValues) -> Unit
) {
    CCScaffold(
        containerColor = containerColor,
        topBar = {},
        bottomBar = bottomBar,
        isShowLoading = isShowLoading,
        topHazeHeight = topHazeHeight,
        bottomHazeHeight = bottomHazeHeight,
        modifier = Modifier
            .padding(top = 12.dp)
            .statusBarsPadding()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        content = content
    )
}

@Preview
@Composable
private fun preview() {
    PreviewScreen { }
    CCSheetPageScaffold {

    }
}