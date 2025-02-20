package com.fantasy.components.widget


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fantasy.components.theme.CCColor
import com.google.accompanist.web.*

@Composable
fun CCWebKit(
    state: WebViewState,
    modifier: Modifier = Modifier.fillMaxSize(),
    captureBackPresses: Boolean = true,
    navigator: WebViewNavigator = rememberWebViewNavigator(),
    client: AccompanistWebViewClient = remember { defaultWebViewClient },
    chromeClient: AccompanistWebChromeClient = remember { AccompanistWebChromeClient() },
    l : Boolean = LocalInspectionMode.current,
    @SuppressLint("SetJavaScriptEnabled") onCreated: (WebView) -> Unit = {
        if (!l) {
            it.settings.apply {
                javaScriptEnabled = true
                userAgentString = "User-Agent:Android"
                domStorageEnabled = true
            }
        }
    },
) {
    Column(modifier = modifier) {
        val loadingState = state.loadingState
        if (loadingState is LoadingState.Loading) {
            LinearProgressIndicator(
                progress = { loadingState.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                color = CCColor.f1,
                trackColor = CCColor.b1
            )
        }

        WebView(
            state = state,
            modifier = Modifier
                .background(CCColor.b1)
                .fillMaxWidth()
                .weight(1f),
            captureBackPresses = captureBackPresses,
            navigator = navigator,
            onCreated = onCreated,
            client = client,
            chromeClient = chromeClient,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CCWebKit(rememberWebViewState(url = "https://liangmc.com"))
}


val defaultWebViewClient = object : AccompanistWebViewClient() {
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
        val url = request?.url.toString()
        if (url?.startsWith("http") == true || url?.startsWith("https://") == true) {
            return super.shouldOverrideUrlLoading(view, request)
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
//                super.onReceivedSslError(view, handler, error)
        handler?.proceed()
    }
}
