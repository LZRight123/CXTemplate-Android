package com.fantasy.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.components.theme.CCColor
import com.fantasy.components.theme.sheetBackgroundColor

@Composable
fun CCCapsuleIndicator(
    color: Color = CCColor.sheetBackgroundColor,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(modifier = Modifier.clip(CircleShape).background(color).size(32.dp, 4.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewScreen {
        Text(text = "haha")

        CCCapsuleIndicator()
    }

}