package com.fantasy.components.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fantasy.components.base.BasePaginationViewModel
import com.fantasy.components.base.LoadPageState
import com.fantasy.components.theme.CXColor


/**
 * 默认footer
 */
@Composable
fun CXRefreshFooter(vm: BasePaginationViewModel<*>) {
    when (vm.loadState) {
        LoadPageState.loading -> {
            if (!vm.isFirstLoading) {
                LoadingItem()
            }
        }
        LoadPageState.endedNoMore ->  {}
        else -> {}
    }
}

/**
 * 底部加载更多正在加载中...
 * */
@Composable
fun LoadingItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(35.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(16.dp),
            color = CXColor.f2.copy(0.6f),
            strokeWidth = 2.dp
        )

        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "加载中...",
            fontSize = 14.sp,
            color = CXColor.f2.copy(0.7f),
        )
    }
}

/**
 * 没有更多数据了
 * */
@Composable
fun NoMoreItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(35.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
//        Box(
//            modifier = Modifier
//                .width(60.dp)
//                .height(0.5.dp)
//                .background(NDColor.l1)
//        )
//        Text(
//            text = "没有更多了",
//            fontSize = 14.sp,
//            color = NDColor.f2,
//            modifier = Modifier
//                .padding(horizontal = 20.dp),
//        )
//        Box(
//            modifier = Modifier
//                .width(60.dp)
//                .height(0.5.dp)
//                .background(NDColor.l1)
//        )
    }
}
