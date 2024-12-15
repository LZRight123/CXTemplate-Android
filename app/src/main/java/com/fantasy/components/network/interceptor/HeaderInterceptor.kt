package com.fantasy.components.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder
            .addHeader("ft-platform", "android")
            .addHeader("lz-name", "liangze")
            .addHeader("lz-pwd", "123456")
            .apply {
//                if (!MyUserManager.shared.tokenModel.accessToken.isEmpty()) {
//                    addHeader(
//                        "token",
//                        MyUserManager.shared.tokenModel.accessToken
//                    )
//                }
            }
        return chain.proceed(requestBuilder.build())
    }
}