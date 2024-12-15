package com.fantasy.components.widget

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.fantasy.components.base.BaseScreen
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState

class CXWebView : BaseScreen() {
    var url: String = ""
    var title: String = ""

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
        CXScaffold(
            topBar = {
                CXNormalNavigationBar(
                    title = title.ifEmpty { state.pageTitle },
//                    leftView = {
//                        XMIcon(R.drawable.system_xmark, size = 16) {
//                            Apphelper.pop(RouterAnimate.vertical)
//                        }
//                    }
                )
            },
        ) { innerPadding ->
            if (url.isEmpty()) {
                CXEmptyView()
            } else {
                CXWebKit(
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

                        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
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
                        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                            handler?.proceed()
                        }
                    }

                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun Preview() {
        url = "https://m.bilibili.com/"
        Content()
    }
}