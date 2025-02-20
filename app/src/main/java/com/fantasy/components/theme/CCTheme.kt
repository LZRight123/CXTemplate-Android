package com.fantasy.components.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density


@Composable
fun CCTheme(
    content: @Composable () -> Unit
) {
    val colors = if (isSystemInDarkTheme()) DarkColorPalette  else  LightColorPalette
    CompositionLocalProvider(
        LocalCCMutableColors provides colors,
        LocalContentColor provides CCColor.f1,
        LocalDensity provides Density(LocalDensity.current.density,
            fontScale = 1f
        ),
    ) {
        val materialColors = MaterialTheme.colorScheme.copy(
            surface = CCColor.b1, // surface 背景
            onSurface = CCColor.f1, // surface 内容
            background = CCColor.b1, // Scaffold 背景
            onBackground = CCColor.f1, // Scaffold Button 内容
            primary = CCColor.f1, // Button 背景
            onPrimary = CCColor.f1,
            surfaceContainerHigh = CCColor.b1, //下拉刷新的指示条背景色是这个颜色
            onSurfaceVariant = CCColor.f1, //下拉刷新的指示条内容是这个颜色
            errorContainer = CCColor.error,
            error = CCColor.error,

        )

        MaterialTheme(
            colorScheme = materialColors,
            content = content
        )
    }
}