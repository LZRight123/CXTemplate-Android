package com.fantasy.components.tools

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import coil3.util.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

class CCCoil private constructor() : ViewModel() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        val shared = CCCoil()

        fun start() {
            shared.viewModelScope.launch(Dispatchers.IO) {
                shared.commonInit()
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private val context = getContext

    val socketFactory: SSLSocketFactory
        get() {
            val r = SSLContext.getInstance("TLS")
            r.init(null, arrayOf(trustManager), SecureRandom())
            return r.socketFactory
        }
    private val trustManager = object : X509TrustManager {
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

    private fun commonInit() {
        SingletonImageLoader.setSafe { context ->
            ImageLoader.Builder(context)
                .crossfade(true)
                .logger(CustomCoilLogger())
                .components {
                    add(
                        OkHttpNetworkFetcherFactory(
                            callFactory = {
                                OkHttpClient.Builder()
                                    .connectTimeout(0, TimeUnit.MILLISECONDS)
                                    .readTimeout(0, TimeUnit.MILLISECONDS)
                                    .writeTimeout(0, TimeUnit.MILLISECONDS)
                                    .sslSocketFactory(
                                        SSLContext.getInstance("TLS")
                                            .apply {
                                                init(null, arrayOf(trustManager), SecureRandom())
                                            }
                                            .socketFactory,
                                        trustManager
                                    )

                                    .build()
                            }
                        )
                    )
                }
                .build()
        }

//        cclog("coil set imageLoader")
    }
}


class CustomCoilLogger(override var minLevel: Logger.Level = Logger.Level.Info) : Logger {
    override fun log(tag: String, level: Logger.Level, message: String?, throwable: Throwable?) {
//        ndLog("coil:${message} tag:${tag}, priority:$priority error:${throwable}")
    }
}
