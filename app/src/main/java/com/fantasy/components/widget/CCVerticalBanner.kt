package com.fantasy.components.widget


import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.components.base.BaseScreen
import com.fantasy.components.widget.CCScaffold
import com.fantasy.components.theme.CCFont
import com.fantasy.components.theme.CCColor
import com.fantasy.components.widget.PreviewScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

private fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other) * other
}


@Composable
fun <T> CCVerticalBanner(
    items: List<T>,
    looping: Boolean = items.size > 1,
    duration: Long = 3000L,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit

) {
    if (items.isEmpty()) return

    val pageCount = items.size
    val startIndex = Int.MAX_VALUE / 2
    val pagerState = rememberPagerState { Int.MAX_VALUE }
    fun pageMapper(index: Int): Int {
        return (index - startIndex).floorMod(pageCount)
    }
    VerticalPager(
        state = pagerState,
        userScrollEnabled = false,
        modifier = modifier
    ) { index ->
        val page = pageMapper(index)
        content(items[page])
    }
    var underDragging by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit) {
        pagerState.interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> underDragging = true
                is PressInteraction.Release -> underDragging = false
                is PressInteraction.Cancel -> underDragging = false
                is DragInteraction.Start -> underDragging = true
                is DragInteraction.Stop -> underDragging = false
                is DragInteraction.Cancel -> underDragging = false
            }
        }
    }
    if (underDragging.not() && looping) {
        LaunchedEffect(key1 = underDragging) {
            try {
                while (true) {
                    delay(duration)
                    val current = pagerState.currentPage
                    val currentPos = pageMapper(current)
                    val nextPage = current + 1
                    if (underDragging.not()) {
                        val toPage =
                            nextPage.takeIf { nextPage < pagerState.pageCount }
                                ?: (currentPos + startIndex + 1)
                        if (toPage > current) {
                            pagerState.animateScrollToPage(toPage)
                        } else {
                            pagerState.scrollToPage(toPage)
                        }
                    }
                }
            } catch (e: Error) {

            }
        }
    }
}

@Preview
@Composable
private fun preview() {
    PreviewScreen { }
    CCVerticalBanner(
        items = listOf(1, 2, 3, 4),
        modifier = Modifier.height(200.dp).fillMaxWidth(),
    ) {
        Text(text = "$it", modifier = Modifier.background(CCColor.random).fillMaxSize().wrapContentSize())
    }
}