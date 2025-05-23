package com.fantasy.components.widget


import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fantasy.components.theme.CCColor

@Composable
fun CCHLine(
    modifier: Modifier = Modifier,
    color: Color = CCColor.f2.copy(0.2f),
    thickness: Dp = 1.2.dp,
) {
    HorizontalDivider(
        modifier = modifier,
        color = color,
        thickness = thickness
    )
}

@Composable
fun CCVLine(
    modifier: Modifier = Modifier,
    color: Color = CCColor.f2.copy(0.2f),
    thickness: Dp = 1.2.dp,
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
        CCHLine()
        CCHLine()
    }
}

