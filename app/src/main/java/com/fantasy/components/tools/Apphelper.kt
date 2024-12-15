package com.fantasy.components.tools

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideOrientation
import com.fantasy.components.base.BaseScreen
import com.fantasy.components.widget.ImageViewerViewModel
import com.fantasy.components.widget.XMInfoBarMessage
import com.fantasy.components.widget.XMToastType
import dev.chrisbanes.haze.HazeState
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

enum class RouterAnimate {
    horizontal,
    vertical,
    fade,
    ;

    val orientation
        get() = when (this) {
            horizontal -> SlideOrientation.Horizontal
            vertical -> SlideOrientation.Vertical
            else -> SlideOrientation.Horizontal
        }
}

data class CXModalItem(
    val key: String = "",
    val screen: BaseScreen,
    val cancelable: Boolean = true,
    val id: String = UUID.randomUUID().toString()
) {
    var isCurrent by mutableStateOf(false)
}


object Apphelper {
    var navigator: Navigator? = null
    val navChildSize get() = navigator?.items?.size ?: 0
    val modals = mutableStateListOf<CXModalItem>()

    private val navBreadcrumbs
        get() = navigator?.items?.joinToString(" -> ") {
            it::class.java.simpleName
        }

    var animate by mutableStateOf(RouterAnimate.horizontal)
        private set
    var toast by mutableStateOf<XMInfoBarMessage?>(null)
        private set

    // 全局的 loading 实在没有办法才展示
    var loadingShow by mutableStateOf(false)

    /// 各种全局 vm 对象
    val image = ImageViewerViewModel()

    // 配对出现
    var attachShow by mutableStateOf(false)
    var attachContent: (@Composable () -> Unit)? = null
        private set


    val hazeState = HazeState()


    fun push(item: BaseScreen, animate: RouterAnimate = RouterAnimate.horizontal) {
        Apphelper.animate = animate
        navigator?.push(item)
        cxlog("导航流: ${navigator?.level} $navBreadcrumbs")
    }

    fun replace(item: BaseScreen, animate: RouterAnimate = RouterAnimate.horizontal) {
        Apphelper.animate = animate
        navigator?.replace(item)
        cxlog("导航流: ${navigator?.level} $navBreadcrumbs")
    }

    fun replaceAll(item: BaseScreen, animate: RouterAnimate = RouterAnimate.horizontal) {
        Apphelper.animate = animate
        navigator?.replaceAll(item)
        cxlog("导航流: ${navigator?.level} $navBreadcrumbs")
    }

    fun pop(animate: RouterAnimate = RouterAnimate.horizontal) {
        Apphelper.animate = animate
        navigator?.pop()
        cxlog("导航流: ${navigator?.level} $navBreadcrumbs")
    }

    fun popRoot(animate: RouterAnimate = RouterAnimate.horizontal) {
        Apphelper.animate = animate
        navigator?.popAll()
        cxlog("导航流: ${navigator?.level} $navBreadcrumbs")
    }

    fun show(target: BaseScreen, cancelable: Boolean = true) {
        val topScreen= navigator?.lastItem as? BaseScreen
        if (topScreen == null) {
            cxlog("检查 screen 的继承问题")
            return
        }
        modals += CXModalItem(
            key = topScreen.key,
            screen = target,
            cancelable = cancelable
        )

        val breadcrumbs = modals.map { it.screen }.map { it::class.java.simpleName }
        cxlog("导航流+sheet: $navBreadcrumbs -> $breadcrumbs")
    }

    /**
     * 主动掉用时 使用动画
     */
    fun dismiss(animated: Boolean = true) {
        MainScope().launch {
            val last = modals.lastOrNull()
            if (last == null) {
                cxlog("没有可关闭的")
                return@launch
            }
            if (animated) {
                last.isCurrent = false
                delay(150)
            }
            last.screen.clear()
            modals.remove(last)
            val breadcrumbs = modals.map { it.screen }.map { it::class.java.simpleName }
            cxlog("导航流+sheet: $navBreadcrumbs -> $breadcrumbs")
        }
    }


    fun toast(msg: String?, type: XMToastType = XMToastType.info) {
        if (!msg.isNullOrEmpty()) {
            MainScope().launch {
                if (toast != null) {
                    toastHidden()
                }
                delay(50)
                toast = XMInfoBarMessage(text = msg, type = type)
            }
        }
    }

    fun toastHidden() {
        toast = null
    }

    fun attachShow(
        content: @Composable () -> Unit,
    ) {
        attachContent = content
        attachShow = true
    }

    fun attachHidden() {
        attachContent = null
        attachShow = false
    }
}

