package com.fantasy.components.network

import com.fantasy.components.network.calladapter.CustomCallAdapterFactory
import com.fantasy.components.network.interceptor.HeaderInterceptor
import com.fantasy.components.network.moshi.NetworkResponseAdapterFactory
import com.fantasy.components.network.moshi.FallbackAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

private object NetworkSample {
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(HeaderInterceptor())
//            .addInterceptor(CustomHeaderInterceptor())
//            .addInterceptor(ResponseInterceptor()) //有了CustomCallAdapterFactory就不需要这个
            .apply {
//                if (isDebugger) {
//                    addInterceptor(LogInterceptor())
//                }
            }
            .sslSocketFactory(socketFactory, trustManager) //配置下，可访问https
            .build()
    }

    private val socketFactory: SSLSocketFactory
        get() {
            val r = SSLContext.getInstance("TLS")
            r.init(null, arrayOf(trustManager), SecureRandom())
            return r.socketFactory
        }
    private val trustManager = object : X509TrustManager {
        override fun checkClientTrusted(
            chain: Array<X509Certificate>,
            authType: String
        ) {
        }

        override fun checkServerTrusted(
            chain: Array<X509Certificate>,
            authType: String
        ) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }

    private val moshi = Moshi.Builder()
        .add(NetworkResponseAdapterFactory())
        .add(KotlinJsonAdapterFactory())
        .add(FallbackAdapterFactory())
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
//        .baseUrl(ProjectConfig.baseUrl)
        .addConverterFactory(
            MoshiConverterFactory.create(moshi)
        )
        .addCallAdapterFactory(CustomCallAdapterFactory(this::handlerResponse))
        .build()

    inline fun <reified T> create(): T = retrofit.create(T::class.java)

    private fun handlerResponse(response: NetworkResponse<Any?>) {
//        if (response.responseCode == NetworkResponseCode.accessTokenExpired) {
//            routeToLogin()
//        }
//        if (response.isSuccess) {
//            print("")
//        }
//        if (response.isError) {
//            print("")
//        }
    }
}

//fun <T> Call<T>.request(completion: (T?, Throwable?) -> Unit) {
//    this.enqueue(object : Callback<T> {
//        override fun onResponse(call: Call<T>, response: Response<T>) {
//            completion(response.body(), null)
//        }
//
//        override fun onFailure(call: Call<T>, t: Throwable) {
//            completion(null, t)
//        }
//    })
//}