package com.fantasy.components.extension.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Stable
fun CXPaddingValues(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp,
    all: Dp  = 0.dp,
    top: Dp = 0.dp,
    bottom: Dp = 0.dp
) = PaddingValues(
    start = horizontal + all,
    end = horizontal + all,
    top = innerPadding.calculateTopPadding() + all + vertical + top,
    bottom = innerPadding.calculateBottomPadding() + all + vertical + bottom,
)