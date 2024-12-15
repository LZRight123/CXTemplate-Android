package com.fantasy.components.network.calladapter

import com.fantasy.components.network.NetworkResponse
import com.fantasy.components.tools.fromJson
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

typealias CustomResponseCallBack = (NetworkResponse<Any?>) -> Unit


class CustomCallAdapterFactory(private val handler: CustomResponseCallBack) : CallAdapter.Factory
    () {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        //检查returnType是否是Call<T>类型的
        if (getRawType(returnType) != Call::class.java) return null
        check(returnType is ParameterizedType) {
            "$returnType must be parameterized. Raw types are not supported"
        }
        //NetworkResponse<T> 透传下去 主要为了处理 callback 的 error
        val apiResultType = getParameterUpperBound(0, returnType)
//        return CustomCallAdapter(apiResultType, hanlder)

        return object : CallAdapter<Any, Call<Any>> {
            override fun responseType(): Type = apiResultType

            override fun adapt(call: Call<Any>): Call<Any> {
                return CatchingCall(call, handler)
            }

        }
    }
}

class CatchingCall(
    private val delegate: Call<Any>,
    private val handler: CustomResponseCallBack
) : Call<Any> {
    /**
     * 该方法会被Retrofit处理suspend方法的代码调用，并传进来一个callback,
     * 如果你回调了callback.onResponse，那么suspend方法就会成功返回
     * 如果你回调了callback.onFailure那么suspend方法就会抛异常
     * 所以我们这里的实现是回调callback.onResponse
     */
    override fun enqueue(callback: Callback<Any>) {
        delegate.enqueue(object : Callback<Any> {
            // 无论请求响应成功还是失败都回调 Response.success
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body is NetworkResponse<*>) {
                        handler(response.body() as NetworkResponse<Any?>)
                    }
                    callback.onResponse(this@CatchingCall, Response.success(body))
                } else {
                    val throwable = HttpException(response)
                    var responseNew = NetworkResponse<Any?>(throwable = throwable, code = response.code())

                    val eb = response.errorBody()?.string()
                    if (!eb.isNullOrEmpty()) {
                        val res = fromJson<NetworkResponse.ErrorBody>(eb)
                        if (!res?.message.isNullOrEmpty())
                            responseNew = responseNew.copy(message = res?.message ?: "")
                    }

                    handler(responseNew)
                    callback.onResponse(
                        this@CatchingCall,
                        Response.success(responseNew)
                    )
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                val response = NetworkResponse<Any?>(throwable = t)
                handler(response)
                callback.onResponse(
                    this@CatchingCall,
                    Response.success(response)
                )
            }
        })
    }

    override fun clone(): Call<Any> = delegate.clone()
    override fun execute(): Response<Any> = delegate.execute()
    override fun isExecuted(): Boolean = delegate.isExecuted
    override fun cancel() = delegate.cancel()
    override fun isCanceled(): Boolean = delegate.isCanceled
    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()
}
