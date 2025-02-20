package com.fantasy.cxtemplate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import cafe.adriel.voyager.transitions.SlideTransition
import com.fantasy.components.base.BaseActivity
import com.fantasy.components.tools.isDebugBuilder
import com.fantasy.components.tools.Apphelper
import com.fantasy.components.tools.RouterAnimate
import com.fantasy.components.widget.CCFullscreenPopup
import com.fantasy.components.widget.CCLoading
import com.fantasy.components.widget.FpsCounter
import com.fantasy.components.widget.ImageViewer
import com.fantasy.components.widget.CCInfoBar
import com.fantasy.cxtemplate.view.WelcomeView

class MainActivity : BaseActivity() {

    @Composable
    override fun ComposeContent() {
        Box {
            Navigator(
                screen = WelcomeView(),
            ) { navigator ->
                LaunchedEffect(key1 = Unit) {
                    Apphelper.navigator = navigator
                }
                when (Apphelper.animate) {
                    RouterAnimate.fade -> FadeTransition(
                        navigator = navigator,
                    )

                    else -> SlideTransition(
                        navigator = navigator,
                        orientation = Apphelper.animate.orientation,
                    )
                }
            }

            HelperViews()
        }
    }


    @Composable
    fun BoxScope.HelperViews() {
        // 图片浏览
        AnimatedVisibility(
            visible = Apphelper.image.images != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ImageViewer(Apphelper.image)
        }

        if (isDebugBuilder) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(x = 60.dp)
            ) {
                FpsCounter()
            }
        }

        CCInfoBar(message = Apphelper.toast) {
            Apphelper.toastHidden()
        }

        // 普通弹框
//        CCNormalAlert(Apphelper.normalAlert)

        // 挂载视图
        // 权限弹框用过
        if (Apphelper.attachShow) {
            CCFullscreenPopup {
                Apphelper.attachContent?.let { it() }
            }
        }

        // 全局loading
        if (Apphelper.loadingShow) {
            CCFullscreenPopup {
                CCLoading()
            }
        }
    }
}
