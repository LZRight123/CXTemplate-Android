package com.fantasy.components.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.lifecycle.LifecycleEffectOnce
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleOwner
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleStore
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import com.fantasy.components.tools.AppHelper
import com.fantasy.components.tools.CCModalItem
import com.fantasy.components.tools.SharedFlowBus
import com.fantasy.components.tools.cclog
import com.fantasy.components.widget.CCFullscreenPopup
import com.fantasy.components.widget.CCModalSheet
import java.util.UUID
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

// 创建 ViewModelStoreOwner
class BaseViewModelStoreOwner : ViewModelStoreOwner, ScreenLifecycleOwner {
    private val selfViewModelStore = ViewModelStore()

    override val viewModelStore: ViewModelStore
        get() = selfViewModelStore

    override fun onDispose(screen: Screen) {
        cclog("销毁 $screen", tag = "lifecycle")
        selfViewModelStore.clear() // 清理 ViewModelStore，调用 ViewModel 的 onCleared
    }
}

abstract class BaseScreen : Screen {
    private val realKey = javaClass.simpleName + "_${UUID.randomUUID()}"
    override val key: ScreenKey
        get() = realKey

    /** 如果不在导航栈里，screen 置为 null 时 viewmodel() 不会被释放，需要手动掉来释放 vm */
    fun clear() {
        ScreenLifecycleStore.remove(this)
    }

    @OptIn(ExperimentalVoyagerApi::class)
    @Composable
    final override fun Content() {
        var isDisposed by remember {
            mutableStateOf(false)
        }
        LifecycleEffectOnce {
            onDispose {
                isDisposed = true
            }
        }

        if (!isDisposed) {
            realContent()
        }
    }

    @Composable
    private fun realContent() {
        val customViewModelStoreOwner = remember { ScreenLifecycleStore.get(this) { BaseViewModelStoreOwner() } }

        CompositionLocalProvider(LocalViewModelStoreOwner provides customViewModelStoreOwner) {
            body()

            AppHelper.modals.filter { it.key == key }.forEach { modal ->
                LaunchedEffect(AppHelper.navigator?.lastItem) {
                    modal.isCurrent = AppHelper.navigator?.lastItem?.key == key
                }
                CCModalSheet(
                    data = if (modal.isCurrent) modal.screen else null,
                    onDataChange = { AppHelper.dismiss(animated = false) },
                    onSystemBack = { AppHelper.dismiss() },
                    cancelable = modal.cancelable
                ) {
                    screen -> screen.Content()
                }
            }
        }
    }

    @Composable
    abstract fun body()
}
