package com.fantasy.components.widget


import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fantasy.components.theme.CXColor

@Composable
fun CXHLine(
    modifier: Modifier = Modifier,
    color: Color = CXColor.f3,
    thickness: Dp = 0.2.dp,
) {
    HorizontalDivider(
        modifier = modifier,
        color = color,
        thickness = thickness
    )
}

@Composable
fun CXVLine(
    modifier: Modifier = Modifier,
    color: Color = CXColor.f3,
    thickness: Dp = 0.2.dp,
) {
    VerticalDivider(
        modifier = modifier,
        color = color,
        thickness = thickness
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PreviewScreen(verticalArrangement = 24) {
        CXHLine()
        CXHLine()
    }
}

