package com.fantasy.stomachbook.api.networking

import com.fantasy.cctemplate.AppConfig
import com.fantasy.components.network.ccJson
import com.fantasy.components.network.netClient
import com.fantasy.components.tools.isDebugBuilder
import com.fantasy.cctemplate.manager.userManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.ProxyBuilder
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.http
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.sse.SSE
import io.ktor.client.plugins.timeout
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.readUTF8Line

sealed class SSEStatus {
    data object Open : SSEStatus()
    data class Event(val id: String?, val event: String?, val data: String?) : SSEStatus()
    data object Close : SSEStatus()
    data class Failure(val throwable: Throwable) : SSEStatus()

    val desc: String
        get() = when (this) {
            is Open -> "SSE 连接已打开"
            is Event -> "收到事件：id=$id, 类型=$event, 数据=$data"
            is Close -> "SSE 连接已关闭"
            is Failure -> "SSE 连接失败：${throwable.message}"
        }
}

object SSEController {
    val sseClient = netClient {
        install(SSE)

        defaultRequest {
            url(AppConfig.baseUrl)
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${userManager.access_token}")
            }
        }
    }

    suspend fun connect(
        url: String,
        body: Any?,
        block: (SSEStatus) -> Unit
    ) {
//        try {
        sseClient.preparePost(url) {
            timeout {
                requestTimeoutMillis = Long.MAX_VALUE
            }
            header("Accept", "text/event-stream")
            header("Cache-Control", "no-cache")
            header("Connection", "keep-alive")
            body?.let { setBody(it) }
        }.execute { response ->
            block(SSEStatus.Open)
            val channel = response.bodyAsChannel()
            var buffer = ""
            
            while (!channel.isClosedForRead) {
                val chunk = channel.readUTF8Line() ?: continue

                // 检测错误响应
                if (chunk.contains("\"detail\":\"Not Found\"")) {
                    block(SSEStatus.Failure(Exception("获取数据失败")))
                    break
                }

                buffer += chunk + "\n"

                // 处理完整的 SSE 事件
                if (chunk.isEmpty() && buffer.isNotEmpty()) {
                    val event = parseSSEEvent(buffer)
                    block(SSEStatus.Event(event.id, event.event, event.data))
                    buffer = ""
                }
            }
            block(SSEStatus.Close)
        }

//        } catch (e: Exception) {
//            block(SSEStatus.Failure(e))
//            cclog("sse error: $e")
//            e.printStackTrace()
//        }
    }

    // 定义 SSE 事件的数据类
    data class SSEEvent(
        val id: String? = null,
        val event: String? = null,
        val data: String = "",
        val retry: Long? = null
    )

    // 解析 SSE 事件的函数
    fun parseSSEEvent(eventText: String): SSEEvent {
        var id: String? = null
        var event: String? = null
        var retry: Long? = null
        val dataLines = mutableListOf<String>()

        eventText.lineSequence().forEach { line ->
            when {
                line.isEmpty() -> {}
                line.startsWith("id:") -> id = line.substringAfter("id:").trim()
                line.startsWith("event:") -> event = line.substringAfter("event:").trim()
                line.startsWith("data:") -> dataLines.add(line.substringAfter("data:"))
                line.startsWith("retry:") -> retry =
                    line.substringAfter("retry:").trim().toLongOrNull()
            }
        }

        // 将数据行合并为一个字符串
        val data = if (dataLines.isNotEmpty()) dataLines.joinToString("\n") else ""

        return SSEEvent(id, event, data, retry)
    }
}