package com.fantasy.components.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density


@Composable
fun CXTheme(
    content: @Composable () -> Unit
) {
    val colors = if (isSystemInDarkTheme()) DarkColorPalette  else  LightColorPalette
    CompositionLocalProvider(
        LocalCXMutableColors provides colors,
        LocalContentColor provides CXColor.f1,
        LocalDensity provides Density(LocalDensity.current.density,
            fontScale = 1f
        ),
    ) {
        val materialColors = MaterialTheme.colorScheme.copy(
            surface = CXColor.b1, // surface 背景
            onSurface = CXColor.f1, // surface 内容
            background = CXColor.b1, // Scaffold 背景
            onBackground = CXColor.f1, // Scaffold Button 内容
            primary = CXColor.f1, // Button 背景
            onPrimary = CXColor.f1,
            surfaceContainerHigh = CXColor.b1, //下拉刷新的指示条背景色是这个颜色
            onSurfaceVariant = CXColor.f1, //下拉刷新的指示条内容是这个颜色
            errorContainer = CXColor.error,
            error = CXColor.error,

        )

        MaterialTheme(
            colorScheme = materialColors,
            content = content
        )
    }
}