package com.fantasy.components.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fantasy.components.theme.CCFont

@Composable
fun CCPlaceholderView(
    text: String = "Placeholder"
) {
    CCScaffold(
        topBar = {}
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                style = CCFont.big1b.v1
            )
        }
    }
}

@Preview
@Composable
fun CCPlaceholderViewPreview() {
    CCPlaceholderView()
}