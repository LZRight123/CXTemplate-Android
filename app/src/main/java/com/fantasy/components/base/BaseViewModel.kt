package com.fantasy.components.base

import androidx.annotation.Keep
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fantasy.components.tools.cxlog
import kotlinx.coroutines.launch


enum class RequestState {
    /**
     * @param loading: 正在请求
     * @param empty: 用来显示占位图
     * @param ok: 正常状态
     */
    loading, empty, ok;

    val isLoading: Boolean get() = this == loading
    val isEmpty: Boolean get() = this == empty
    val isOk get() = this == ok
}

enum class LoadPageState() {
    /**
     * 空闲
     * 加载中...
     * 加载完成
     * 没有更多了
     */
    idle, loading, loaded, endedNoMore,
    ;

    val canLoad: Boolean
        get() = when (this) {
            loading, endedNoMore -> false
            idle, loaded -> true
        }
}

@Keep
data class PageRequest(
    var page: Int = 1,
    var page_size: Int = 20
) {
    fun reset() {
        page = 1
    }

    fun plus() {
        page += 1
    }

    val isStart: Boolean get() = page == 1

    val currentTotal: Int = page * page_size
}


/**
 * viewmodel 基类
 */
abstract class BaseViewModel : ViewModel() {
    var requestState by mutableStateOf(RequestState.ok)
    // 页面是否显示 loading 通过 requestState
    val isShowLoading get() = requestState.isLoading
    // 常用于下拉刷新
    var refreshing by mutableStateOf(false)

    init {
        cxlog("ViewModel 创建 $this")
    }

    override fun onCleared() {
        cxlog("ViewModel 释放 $this")
    }
}

/**
 * 分页 viewmodel 基类
 */
abstract class BasePaginationViewModel<T>(
    val page: PageRequest = PageRequest()
) : BaseViewModel() {
    val items = mutableStateListOf<T>()
    var loadState: LoadPageState by mutableStateOf(LoadPageState.idle)
    val canLoad: Boolean get() = loadState.canLoad

    val isFirstLoading: Boolean
        get() = loadState == LoadPageState.loading && page.isStart

    open fun refresh() {
        page.reset()
        requestDataForItems()
    }

    open fun loadNextItems() {
        page.plus()
        requestDataForItems()
    }

    private fun refreshLoadState() {
        loadState =
            if (page.currentTotal > items.size) LoadPageState.endedNoMore else LoadPageState.loaded
        refreshing = false
        requestState = if (items.isEmpty()) RequestState.empty else RequestState.ok
    }

    /**
     * 获取数据, 刷新请求状态
     */
    private fun requestDataForItems() {
        viewModelScope.launch() {
            loadState = LoadPageState.loading
            fetchItems()
            // 在这里其实可以统一刷新状态的
            refreshLoadState()
        }
    }

    abstract suspend fun fetchItems()
}

