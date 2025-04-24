package com.fantasy.cctemplate.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fantasy.cctemplate.R
import com.fantasy.cctemplate.manager.userManager
import com.fantasy.cctemplate.routeToMain
import com.fantasy.components.base.BaseScreen
import com.fantasy.components.base.BaseViewModel
import com.fantasy.components.theme.CCFont
import com.fantasy.components.tools.cclog
import com.fantasy.components.widget.CCLottieView
import com.fantasy.components.widget.PreviewScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val totalDuration = 300 // 总时长为2.5秒

class WelcomeViewModel : BaseViewModel() {
    init {
        viewModelScope.launch { userManager.syncData() }
    }

    override fun onCleared() {
        cclog("WelcomeViewModel onCleared")
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
        BackHandler {}

        LaunchedEffect(Unit) { vm.gotoMain() }

        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                CCLottieView(resId = R.raw.loading_gray, modifier = Modifier.fillMaxWidth(0.5f))

                Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                ) { Text(text = "欢迎欢迎，热烈欢迎", style = CCFont.f1.v1) }
            }
        }
    }

    @Preview()
    @Composable
    private fun preview() {
        PreviewScreen() {}
        Content()
    }
}
