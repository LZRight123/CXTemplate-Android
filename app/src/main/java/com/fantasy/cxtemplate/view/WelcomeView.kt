package com.fantasy.cxtemplate.view

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fantasy.components.base.BaseViewModel
import com.fantasy.components.theme.CXColor
import com.fantasy.components.theme.CXFont
import com.fantasy.components.tools.cxlog
import com.fantasy.components.tools.screenWith
import com.fantasy.components.base.BaseScreen
import com.fantasy.components.widget.CXLottieView
import com.fantasy.components.widget.PreviewScreen
import com.fantasy.cxtemplate.R
import com.fantasy.cxtemplate.manager.userManager
import com.fantasy.cxtemplate.routeToMain
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

private val totalDuration = 300 // 总时长为2.5秒

class WelcomeViewModel : BaseViewModel() {
    init {
        viewModelScope.launch {
            userManager.syncData()
        }
    }

    override fun onCleared() {
        cxlog("WelcomeViewModel onCleared")
    }

    fun gotoMain() {
        viewModelScope.launch {
            delay(totalDuration.toLong())
            routeToMain()
        }
    }
}

class WelcomeView : BaseScreen() {
    @Composable
    override fun body() {
        val vm: WelcomeViewModel = viewModel()
        BackHandler {

        }

        LaunchedEffect(Unit) {
            vm.gotoMain()
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                CXLottieView(resId = R.raw.loading_gray, modifier = Modifier.fillMaxWidth(0.5f))

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Text(
                        text = "欢迎欢迎，热烈欢迎",
                        style = CXFont.f1.v1
                    )
                }
            }

        }
    }

    @Preview()
    @Composable
    private fun preview() {
        PreviewScreen() {

        }
        Content()
    }

}


