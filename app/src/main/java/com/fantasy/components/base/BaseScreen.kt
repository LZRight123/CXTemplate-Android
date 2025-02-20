package com.fantasy.components.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleOwner
import cafe.adriel.voyager.core.lifecycle.ScreenLifecycleStore
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import com.fantasy.components.tools.cclog
import com.fantasy.components.tools.Apphelper
import com.fantasy.components.widget.CCModalSheet
import java.util.UUID


// 创建 ViewModelStoreOwner
class BaseViewModelStoreOwner : ViewModelStoreOwner, ScreenLifecycleOwner {
    private val selfViewModelStore = ViewModelStore()

    override val viewModelStore: ViewModelStore
        get() = selfViewModelStore

    override fun onDispose(screen: Screen) {
        cclog("Screen要销毁啦: ${screen::class.java.simpleName}")
        selfViewModelStore.clear()  // 清理 ViewModelStore，调用 ViewModel 的 onCleared
    }
}

abstract class BaseScreen : Screen {
    private val realKey = javaClass.simpleName + "_${UUID.randomUUID()}"
    override val key: ScreenKey
        get() = realKey

    /**
     * 如果不在导航栈里，screen 置为 null 时 viewmodel() 不会被释放，需要手动掉来释放 vm
     */
    fun clear() {
        ScreenLifecycleStore.remove(this)
    }

    @Composable
    final override fun Content() {
        val customViewModelStoreOwner = remember(this) {
            ScreenLifecycleStore.get(this) {
                BaseViewModelStoreOwner()
            }
        }

        CompositionLocalProvider(
            LocalViewModelStoreOwner provides customViewModelStoreOwner
        ) {
            body()

            Apphelper.modals.filter { it.key == key }.forEach { modal ->
                LaunchedEffect(Apphelper.navigator?.lastItem) {
                    modal.isCurrent = Apphelper.navigator?.lastItem?.key == key
                }
                CCModalSheet(
                    data = if (modal.isCurrent) modal.screen else null ,
                    onDataChange = {
                        Apphelper.dismiss(animated = false)
                    },
                    onSystemBack = {
                        Apphelper.dismiss()
                    },
                    cancelable = modal.cancelable
                ) {
                    it.Content()
                }
            }
        }
    }

    @Composable
    abstract fun body()
}
