package com.fantasy.cxtemplate.api.networking

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.fantasy.components.network.NetworkResponse
import com.fantasy.components.network.calladapter.CustomCallAdapterFactory
import com.fantasy.components.network.interceptor.LogInterceptor
import com.fantasy.components.network.moshi.moshi
import com.fantasy.components.tools.isDebugBuilder
import com.fantasy.components.tools.Apphelper
import com.fantasy.components.widget.CCToastType
import com.fantasy.cxtemplate.AppConfig
import com.fantasy.cxtemplate.manager.userManager
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

object Networking {
    private val okHttpClient by lazy {
        generatorOKHttpClient(timeout = 15, openLog = true)
    }

    fun generatorOKHttpClient(
        timeout: Long = 0,
        needInterceptor: Boolean = true,
        openLog: Boolean = false
    ) = OkHttpClient.Builder()
        .connectTimeout(timeout, TimeUnit.SECONDS)
        .readTimeout(timeout, TimeUnit.SECONDS)
        .writeTimeout(timeout, TimeUnit.SECONDS)
        .apply {
            if (needInterceptor) {
//                addInterceptor(MoreBaseUrlInterceptor())
                addInterceptor(CustomHeaderInterceptor())
            }
            if (isDebugBuilder && openLog) {
                addNetworkInterceptor(LogInterceptor())
            }
        }
        .sslSocketFactory(socketFactory, trustManager) //配置下，可访问https
        .build()

    val socketFactory: SSLSocketFactory
        get() {
            val r = SSLContext.getInstance("TLS")
            r.init(null, arrayOf(trustManager), SecureRandom())
            return r.socketFactory
        }
    val trustManager = object : X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        override fun checkClientTrusted(
            chain: Array<X509Certificate>,
            authType: String
        ) {
        }

        @SuppressLint("TrustAllX509TrustManager")
        override fun checkServerTrusted(
            chain: Array<X509Certificate>,
            authType: String
        ) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }

    val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(AppConfig.baseUrl)
        .addConverterFactory(
            MoshiConverterFactory.create(moshi)
        )
        .addCallAdapterFactory(CustomCallAdapterFactory(this::handlerResponse))
        .build()

    val retrofitOpen: Retrofit = Retrofit.Builder()
        .client(generatorOKHttpClient(needInterceptor = false, openLog = true))
        .baseUrl(AppConfig.baseUrl)
        .build()

    private val imageRetrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://test.com")
        .build()

    inline fun <reified T> create(): T = retrofit.create(T::class.java)


    /**
     * 通用的全局配置
     */
    private fun handlerResponse(response: NetworkResponse<Any?>) {
        userManager.viewModelScope.launch {
            when (response.code) {
                400 -> {
                    userManager.logout()
                    Apphelper.toast(
                        msg = response.message.ifEmpty { "登录过期。请重新登录" },
                        type = CCToastType.error
                    )
                }

                401 -> {
                    userManager.logout()
                    Apphelper.toast(
                        msg = response.message.ifEmpty { "用户已被封禁" },
                    )
                }
                // 会员
                408 -> {
//                    Apphelper.push(MembershipView(), animate = RouterAnimate.vertical)
                    Apphelper.toast(response.message)
                }
                500 -> Apphelper.toast(
                    msg = response.message.ifEmpty { "服务出错。请联系开发者" },
                    type = CCToastType.error
                )
                502 -> Apphelper.toast(
                    msg = response.message.ifEmpty { "服务器重启中，请稍后" },
                )
                else -> {
                    if (response.isError) {
                        Apphelper.toast("网络错误", type = CCToastType.error)
                    } else if (response.isFailure) {
                        Apphelper.toast(
                            msg = response.message.ifEmpty { "未知错误" },
                            type = CCToastType.error
                        )
                    }
                }
            }
        }
    }
}
