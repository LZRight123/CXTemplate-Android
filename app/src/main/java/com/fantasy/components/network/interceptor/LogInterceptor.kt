package com.fantasy.components.network.interceptor

import com.fantasy.components.tools.cxlog
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer

//https://blog.csdn.net/mqdxiaoxiao/article/details/90728966
class LogInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request = request)

        val buffer = Buffer()
        request.body?.writeTo(buffer)
        val requestBody = buffer.readUtf8()
        // Content-Type: application/json
        val headerStringBuilder = StringBuilder()
        request.headers.forEach { (field, value) ->
            headerStringBuilder.append("-H '$field: $value' \\\n")
        }

        val curlCmd = "curl -X ${request.method} ${request.url} \\\n$headerStringBuilder ${if (requestBody.length > 800) "" else "-d '$requestBody'"}"

        var logString = curlCmd
        logString += "\n持续时间: ${response.receivedResponseAtMillis - response.sentRequestAtMillis}ms"
//        logString += "\nstatusCode: ${response.code}"
        response.body?.let {
            val source = it.source()
            source.request(Long.MAX_VALUE)
            val buffer = source.buffer.clone()
            logString += "\n响应回参: ${buffer.readUtf8()}"
        }
        cxlog("网络请求 $logString", )
        return response
    }

}