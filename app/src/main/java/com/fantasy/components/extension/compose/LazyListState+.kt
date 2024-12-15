package com.fantasy.components.extension.compose

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun LazyListState.indexOffset(index: Int = 0): Int {
    val density = LocalDensity.current

    val firstOffset by remember(this) {
        derivedStateOf {
            if (firstVisibleItemIndex == 0) {
                with(density) {
                    firstVisibleItemScrollOffset.toDp().value.toInt()
                }
            } else {
                100//预估一个值 比要变色的临界值大即可
            }
        }
    }
    return firstOffset
}

@Composable
fun LazyListState.scrollOffset(): Float {
    // 获取滚动偏移量
    val density = LocalDensity.current
    val scrollOffset by remember(this) {
        derivedStateOf {
            layoutInfo.viewportEndOffset + firstVisibleItemIndex * layoutInfo.viewportEndOffset - firstVisibleItemScrollOffset
        }
    }
    return with(density) { scrollOffset.toDp().value }
}

@Composable
fun LazyListState.topItemVisible(offset: Int = 24): Boolean {
    val density = LocalDensity.current
    val thresholdPx = with(density) { offset.dp.toPx() }
    return remember(this) {
        derivedStateOf {
            firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset < thresholdPx
        }
    }.value
}

/**
 * 利用 TopAppBarScrollBehavior 拿偏移量
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarScrollBehavior.offsetY(): Float {
    val density = LocalDensity.current
    val offset by remember {
        derivedStateOf {
            with(density) {
                state.contentOffset.toDp().value * -1
            }
        }
    }
    return offset
}