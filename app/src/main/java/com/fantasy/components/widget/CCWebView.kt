package com.fantasy.components.widget

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.fantasy.components.aamedium.outerShadow
import com.fantasy.components.base.BaseScreen
import com.fantasy.components.extension.compose.Icon
import com.fantasy.components.theme.CCColor
import com.fantasy.components.tools.AppHelper
import com.fantasy.components.tools.RouterAnimate
//import com.fantasy.cctemplate.manager.ShareModel
//import com.fantasy.cctemplate.manager.shareWebpage
import com.fantasy.cctemplate.manager.thirdSDKManager
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.WebViewState
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState

class CCWebView(
    val url: String = "",
    val title: String = "",
) : BaseScreen() {

    @Composable
    override fun body() {
        val view = LocalView.current
        LaunchedEffect(key1 = Unit) {
            val window = (view.context as ComponentActivity).window
            // 重置为默认颜色
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }

        val state = rememberWebViewState(url = url)
        val navigator = rememberWebViewNavigator()
        CCScaffold(
            topBar = { browserHeader(state) },
            bottomBar = { browserFooter(navigator = navigator, state = state) },
            topHazeHeight = 0,
            bottomHazeHeight = 0,
        ) { innerPadding ->
            if (url.isEmpty()) {
                CCEmptyView(modifier = Modifier.padding(top = 200.dp))
            } else {
                CCWebKit(
                    state = state,
                    navigator = navigator,
                    captureBackPresses = false,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    client = object : AccompanistWebViewClient() {
                        override fun onPageFinished(view: WebView, url: String?) {
                            super.onPageFinished(view, url)
                            view?.loadUrl(
                                """javascript:console.log(document.cookie);
                                 """.trimIndent()
                            )
                        }

                        override fun onPageStarted(
                            view: WebView,
                            url: String?,
                            favicon: Bitmap?
                        ) {
                            super.onPageStarted(view, url, favicon)
                            view.loadUrl(
                                """javascript:console.log('WebView 掉 js at onPageStarted')
                                """.trimIndent()
                            )
                            view.loadUrl(
                                """javascript:console.log(url)
                                """.trimIndent()
                            )
                        }

                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            // 在这里处理重定向逻辑
                            val u = request?.url.toString()
                            if (u.startsWith("http") || u.startsWith("https://")) {
                                return super.shouldOverrideUrlLoading(view, request)
                            } else {
//                                return false
                            }
                            // 返回 true 表示自己处理重定向，不加载新的页面
                            // 返回 false 表示继续加载重定向后的页面
                            return super.shouldOverrideUrlLoading(view, request)
                        }

                        override fun shouldInterceptRequest(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): WebResourceResponse? {
                            return super.shouldInterceptRequest(view, request)
                        }

                        @SuppressLint("WebViewClientOnReceivedSslError")
                        override fun onReceivedSslError(
                            view: WebView?,
                            handler: SslErrorHandler?,
                            error: SslError?
                        ) {
                            handler?.proceed()
                        }
                    }

                )
            }
        }
    }

    @Composable
    fun browserHeader(state: WebViewState) {
        CCNormalNavigationBar(
            modifier = Modifier.background(CCColor.b1),
            title = title.ifEmpty { state.pageTitle },
            leftView = {
                CCButton(
                    onClick = {
                        AppHelper.pop(RouterAnimate.vertical)
                    },
                    backgroundColor = CCColor.b2,
                    modifier = Modifier
                        .outerShadow(
                            shape = CircleShape,
                            color = CCColor.b1,
                        )
                        .clip(CircleShape)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = CCColor.f1,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        )
    }

    @Composable
    fun browserFooter(navigator: WebViewNavigator, state: WebViewState) {
        Column {
            CCHLine()
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .background(CCColor.b1)
                    .padding(24.dp, 16.dp)
                    .navigationBarsPadding()
                    .fillMaxWidth()
            ) {
                CCButton(
                    onClick = {
                        navigator.navigateBack()
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowLeft,
                        tint = if (navigator.canGoBack) CCColor.f1 else CCColor.f3,
                        modifier = Modifier.size(20.dp)
                    )
                }

                CCButton(
                    onClick = {
                        navigator.navigateForward()
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        tint = if (navigator.canGoForward) CCColor.f1 else CCColor.f3,
                        modifier = Modifier.size(20.dp)
                    )
                }

                CCButton(
                    onClick = {
                        navigator.reload()
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        tint = CCColor.f1,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(Modifier.weight(1f))
                CCButton(
                    onClick = {
//                        val share = if (!state.lastLoadedUrl.isNullOrEmpty()) {
//                            ShareModel(
//                                url = state.lastLoadedUrl ?: "",
//                                title = state.pageTitle ?: title,
//                            )
//                        } else ShareModel()
//                        thirdSDKManager.shareWebpage(share)
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Share,
                        tint = CCColor.f1,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CCWebView(url = "https://m.bilibili.com/").body()
}