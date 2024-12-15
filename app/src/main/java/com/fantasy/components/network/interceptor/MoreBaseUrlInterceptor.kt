package com.fantasy.components.network.interceptor

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response


const val MoreBaseURL = "MoreBaseURL"
const val MoreChangePathSegment = "MoreChangePathSegment"

/**
 * 用法
   @Headers(*[
        "MoreBaseURL: https://test.naduo.xx",
        "MoreChangePathSegment: 0,s1;1,s2",
   ])
 */
class MoreBaseUrlInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()
        val originBuild = originRequest.newBuilder()

        val originUrl = originRequest.url
        val newHttpURLBuilder = originUrl.newBuilder()

        originRequest.header(MoreBaseURL)?.toHttpUrlOrNull()?.let {
            newHttpURLBuilder.apply {
                scheme(it.scheme)
                host(it.host)
                port(it.port)
            }
        }

        originRequest.header(MoreChangePathSegment)?.let { response ->
            response.split(";").filter { it.isNotEmpty() }
                .forEach { item ->
                    val keyPath = item.split(",")
                    if (keyPath.size == 2) {
                        newHttpURLBuilder.apply {
                            setPathSegment(keyPath[0].toInt(), keyPath[1])
                        }
                    }
                }
        }

        originBuild.removeHeader(MoreBaseURL)
        originBuild.removeHeader(MoreChangePathSegment)

        val newHttpUrl = newHttpURLBuilder.build()
        val newRequest = originBuild.url(newHttpUrl).build()
        return chain.proceed(newRequest)
    }

}

// https://github.com/square/okhttp/blob/573bdaa43c34459a9261fa194c9dfb56c9b1ea2a/samples/guide/src/main/java/okhttp3/recipes/Progress.java#L82