package com.fantasy.stomachbook.api.networking

import android.icu.util.TimeZone
import android.os.Build
import android.provider.Settings
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils
import com.fantasy.cctemplate.AppConfig
import com.fantasy.components.network.NetworkResponse
import com.fantasy.components.network.ccJson
import com.fantasy.components.network.netClient
import com.fantasy.components.tools.AppHelper
import com.fantasy.components.tools.cclog
import com.fantasy.components.tools.getContext
import com.fantasy.components.widget.CCToastType
import com.fantasy.cctemplate.manager.userManager

import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.headers
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


val networking = netClient {
//    install(LoggingPlugin)
    install(HttpTimeout) {
        requestTimeoutMillis = 30_000   // 请求总超时时间（例如 30 秒）
        connectTimeoutMillis = 30_000   // 连接服务器的超时时间
        socketTimeoutMillis = 30_000    // 等待服务器响应的时间
    }

    defaultRequest {
        url(AppConfig.baseUrl)
        headers {
            append("Client-Info", ccJson.encodeToString(standardHeader))
            append("traceId", randomLowercaseString())
            append("channel", "android")
            append("timezone", TimeZone.getDefault().rawOffset.toString())
            if (userManager.access_token.isNotEmpty()) {
                append(
                    HttpHeaders.Authorization,
                    "Bearer ${userManager.access_token}"
                )
            }
        }
        contentType(ContentType.Application.Json)
    }

    HttpResponseValidator {
        validateResponse {
            val response: NetworkResponse<JsonElement> = it.body()
            if (response.is200Ok) {
                return@validateResponse
            }
            cclog("url: ${it.request.url}  response: $response")
            when (response.code) {
//                400 -> {
//                    userManager.logout()
//                    AppHelper.toast(
//                        msg = response.message.ifEmpty { "登录过期。请重新登录" },
//                        type = CCToastType.error
//                    )
//                }

                401 -> {
//                    supabaseManager.refreshSession()
                }
                // 会员
                407 -> {
//                    routeToVipBuy()
                    AppHelper.toast(
                        msg = response.message.ifEmpty { "出错了，请联系开发者" },
                        type = CCToastType.error
                    )
                }

                500 -> AppHelper.toast(
                    msg = response.message.ifEmpty { "出错了，请联系开发者" },
                    type = CCToastType.error
                )

                502 -> AppHelper.toast(
                    msg = response.message.ifEmpty { "服务器重启中，请稍后" },
                )

                else -> {
                    if (response.isError) {
                        response.throwable?.printStackTrace()
                        AppHelper.toast("网络错误", type = CCToastType.error)
                    } else {
                        AppHelper.toast(
                            msg = response.message.ifEmpty { "未知错误" },
                            type = CCToastType.error
                        )
                    }
                }
            }
        }
    }
}


private fun randomLowercaseString(): String {
    val letters = "abcdefghijklmnopqrstuvwxyz"
    var randomString = ""
    repeat(16) {
        val randomIndex = letters.indices.random()
        val randomLetter = letters[randomIndex]
        randomString += randomLetter
    }
    return randomString
}


private val imei = Settings.Secure.getString(
    getContext.contentResolver,
    Settings.Secure.ANDROID_ID
)

private val standardHeader = CCRequestHeaders(
    device = DeviceUtils.getAndroidID(), //	设备信息，包括不仅限于设备编号、型号等
    device_token = imei
)

@Serializable
internal data class CCRequestHeaders(
    val version: String = AppUtils.getAppVersionName(), // 应用版本号
    val timestamp: Long = Clock.System.now().toEpochMilliseconds(), // 用户访问时间，毫秒级时间戳, // 用户访问时间，毫秒级时间戳
    val os: String = "Android ${Build.VERSION.RELEASE}", //	操作系统，windows、macOs、Ios、Android 等等 例如 Android 10
    val model: String = Build.MODEL, //	操作系统，windows、macOs、Ios、Android 等等 例如 Android 10
    val device: String, //	设备信息，包括不仅限于设备编号、型号等
    val device_token: String, //IMEI
)



