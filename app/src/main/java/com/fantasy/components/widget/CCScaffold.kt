package com.fantasy.components.widget

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.fantasy.components.theme.CCColor

@Composable
fun CCScaffold(
    modifier: Modifier = Modifier,
    title: String? = null,
    containerColor: Color = CCColor.b1,
    contentColor: Color = CCColor.f1,
    isShowLoading: Boolean = false,
    canClickOutsideWithLoading: Boolean = false,
    topBar: @Composable (Color) -> Unit = {
        CCNormalNavigationBar(
            title = title,
            backgroundColor = it
        )
    },
    bottomBar: @Composable (Color) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = { topBar(containerColor) },
        bottomBar = { bottomBar(containerColor) },
        containerColor = containerColor,
        contentColor = contentColor,
    ) { innerPadding ->
        content(innerPadding)
        CCLoading(
            isShow = isShowLoading,
            canClickOutside = canClickOutsideWithLoading
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CCScaffold(
        topBar = {
            CCNormalNavigationBar(
                title = "preview"
            )
        },
    ) { innerPadding ->
        Text(text = "1231313", modifier = Modifier.padding(innerPadding))
    }
}