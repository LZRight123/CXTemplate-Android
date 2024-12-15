package com.fantasy.components.extension.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fantasy.components.theme.CXColor


// tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
// tint 可以从 Theme 里面配置,见 CXTheme.kt
@Composable
fun Icon(
    @DrawableRes id: Int,
    size: Int,
    tint: Color = CXColor.f1
) {
    Icon(
        id = id,
        modifier = Modifier.size(size.dp),
        tint = tint
    )
}

@Composable
fun Icon(
    @DrawableRes id: Int,
    modifier: Modifier = Modifier.size(20.dp),
    tint: Color = CXColor.f1
) {
    Icon(
        painter = painterResource(id = id),
        contentDescription = null,
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun Icon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = CXColor.f1
) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier = modifier,
        tint = tint
    )
}