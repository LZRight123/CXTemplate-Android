package com.fantasy.cxtemplate.api.networking

import android.annotation.SuppressLint
import android.icu.util.TimeZone
import android.os.Build
import android.provider.Settings
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils
import com.fantasy.components.extension.toMilli
import com.fantasy.components.tools.toJsonString
import com.fantasy.components.tools.getContext
import com.fantasy.cxtemplate.manager.userManager
import okhttp3.Interceptor
import okhttp3.Response
import java.time.LocalDateTime

class CustomHeaderInterceptor : Interceptor {
    @SuppressLint("HardwareIds")
    override fun intercept(chain: Interceptor.Chain): Response {
        val imei = Settings.Secure.getString(
            getContext.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        val standardHeader = NDRequestHeaders(
            device = DeviceUtils.getAndroidID(), //	设备信息，包括不仅限于设备编号、型号等
            device_token = imei
        )
        val requestBuilder = chain.request().newBuilder()
            .apply {
                addHeader("Client-Info", toJsonString(standardHeader))
                addHeader("traceId", randomLowercaseString())
                addHeader("channel", "android")
                addHeader("timezone", TimeZone.getDefault().rawOffset.toString())
                if (userManager.access_token.isNotEmpty()) {
                    addHeader(
                        "Authorization",
                        "Bearer ${userManager.access_token}"
                    )
                }
            }
        return chain.proceed(requestBuilder.build())
    }

    fun randomLowercaseString(): String {
        val letters = "abcdefghijklmnopqrstuvwxyz"
        var randomString = ""
        repeat(16) {
            val randomIndex = letters.indices.random()
            val randomLetter = letters[randomIndex]
            randomString += randomLetter
        }
        return randomString
    }
}

internal data class NDRequestHeaders(
    val version: String = AppUtils.getAppVersionName(), // 应用版本号
    val timestamp: Long = LocalDateTime.now().toMilli(), // 用户访问时间，毫秒级时间戳, // 用户访问时间，毫秒级时间戳
    val os: String = "Android ${Build.VERSION.RELEASE}", //	操作系统，windows、macOs、Ios、Android 等等 例如 Android 10
    val model: String = Build.MODEL, //	操作系统，windows、macOs、Ios、Android 等等 例如 Android 10
    val device: String, //	设备信息，包括不仅限于设备编号、型号等
    val device_token: String, //IMEI
)