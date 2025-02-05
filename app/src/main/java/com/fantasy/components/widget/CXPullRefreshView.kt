package com.fantasy.components.widget


import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fantasy.components.tools.navBarHeight
import com.fantasy.components.tools.safeAreaTop


/**
CXPullRefreshView(
refreshing = vm.refreshing,,
onRefresh = {
vm.refreshing = true
vm.refresh()
}
) {
LazyColumn() {
itemsIndexed(vm.items) { index, message ->
if (index >= vm.items.size - 1 && vm.canLoad) {
vm.loadNextItems()
}
}
item { CXRefreshFooter(vm = vm) }
}
}
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CXPullRefreshView(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    refreshingOffset: Dp = safeAreaTop + navBarHeight + 60.dp,
    isLoading: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val state = rememberPullToRefreshState()
    PullToRefreshBox(
        isRefreshing = refreshing,
        onRefresh =  onRefresh,
        state = state,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = refreshing,
                state = state,
                threshold = refreshingOffset
            )
        }
    ) {
        content()

        CXLoading(isShow = isLoading)
    }
}