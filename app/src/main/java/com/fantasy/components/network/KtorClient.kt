package com.fantasy.components.network

import android.annotation.SuppressLint
import com.fantasy.components.tools.cclog
import com.fantasy.components.tools.isDebugBuilder
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.ProxyBuilder
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.http
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.api.SendingRequest
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.serialization.kotlinx.json.json
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder

val ccJson = Json { ccJsonBuild() }

fun JsonBuilder.ccJsonBuild(pretty: Boolean = false) {
    isLenient = true
    coerceInputValues = true
    ignoreUnknownKeys = true
    explicitNulls = false
    allowStructuredMapKeys = true
    encodeDefaults = true
    prettyPrint = pretty
}

@SuppressLint("TrustAllX509TrustManager")
val trustAllManager =
        object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        }

fun netClient(block: HttpClientConfig<*>.() -> Unit) =
        HttpClient(CIO) {
            expectSuccess = false // 这是关键设置，不要期望所有请求都成功
            if (isDebugBuilder) {
                engine {
                    proxy = ProxyBuilder.http("http://192.168.50.92:9999")
                    https { trustManager = trustAllManager }
                }
            }

            install(ContentNegotiation) { json(ccJson) }

            install(HttpRequestRetry) {
                maxRetries = 3
                retryOnExceptionOrServerErrors()
                exponentialDelay()
                delayMillis { retry -> retry * 3000L } // will retry in 3, 6, 9, etc. seconds
            }

            block() // 后面的覆盖前面的
        }

suspend inline fun <reified T> HttpClient.get(
        path: String,
        block: HttpRequestBuilder.() -> Unit = {}
): NetworkResponse<T> {
    return try {
        get {
                    url(path)
                    block()
                }
                .body<NetworkResponse<T>>()
    } catch (e: Exception) {
        cclog(e, "网络请求")
        NetworkResponse(code = -1, message = e.message ?: "未知错误", data = null, throwable = e)
    }
}

suspend inline fun <reified T> HttpClient.post(
        path: String,
        block: HttpRequestBuilder.() -> Unit
): NetworkResponse<T> {
    return try {
        post {
                    url(path)
                    block()
                }
                .body<NetworkResponse<T>>()
    } catch (e: Exception) {
        cclog(e, "网络请求")
        NetworkResponse(code = -1, message = e.message ?: "未知错误", data = null, throwable = e)
    }
}

/** 插件 */

/** 打印 */
val LoggingPlugin =
        createClientPlugin("LoggingHeadersPlugin") {
            var requestBodyContent: String? = null
            var rerulstring = ""
            on(SendingRequest) { request, content ->
                requestBodyContent =
                        when (content) {
                            is io.ktor.http.content.TextContent -> content.text
                            is io.ktor.http.content.ByteArrayContent ->
                                    content.bytes().decodeToString()
                            else -> content.toString()
                        }
                if (checkUrlLog(request.url.toString())) {
                    rerulstring = request.url.toString()
                    cclog("开始请求：${request.url}", tag = "网络请求")
                }
            }

            onResponse { response ->
                val request = response.request
                val onCallReceiveTime =
                        response.responseTime.timestamp - response.requestTime.timestamp
                var headerString = ""

                request.headers.forEach { field, value ->
                    headerString += "-H '$field: ${value.joinToString()}' \\\n"
                }
                val requestBody = requestBodyContent ?: ""
                val curlCmd =
                        "curl -X ${request.method} ${request.url} \\\n$headerString ${if (requestBody.length > 8000) "" else "-d '$requestBody'"}"
                var logString = curlCmd
                logString += "\n持续时间: $onCallReceiveTime ms"
                logString += "\n响应回参: ${response.bodyAsText()}"
                if (checkUrlLog(request.url.toString())) {
                    cclog(logString, tag = "网络请求")
                }
            }

            onClose { cclog("请求关闭：$rerulstring", tag = "网络请求") }
        }

private fun checkUrlLog(url: String): Boolean {
    return !url.contains("health/mobile")
}
