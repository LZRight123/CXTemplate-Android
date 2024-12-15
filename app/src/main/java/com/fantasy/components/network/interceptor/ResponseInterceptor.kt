package com.fantasy.components.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class ResponseInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        response.body?.let {
            val source = it.source()
            source.request(Long.MAX_VALUE)
            val buffer = source.buffer.clone()
//            val model = fromJson<Blank>(buffer.readUtf8())
//            val j1 = toJsonString(model)
//            val j2 = toJson(model) as? Map<String, Any>
//            print("")
        }
        return response
    }

}


