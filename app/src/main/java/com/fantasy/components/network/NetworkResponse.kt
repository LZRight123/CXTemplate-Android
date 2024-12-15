package com.fantasy.components.network

import androidx.annotation.Keep


//@JsonClass(generateAdapter = true)
@Keep
data class NetworkResponse<T>(
    val code: Int? = null,
//    val msg: String = "",
    val message: String = "",
    val data: T? = null,
    val page: Page? = null,
    val throwable: Throwable? = null,
) {
    data class ErrorBody(
        val message: String = "",
        val code: Int? = null,
    )

    val isSuccess get() = code == 200
    val isError: Boolean get() = throwable != null
    val isFailure get() = !isError && !isSuccess

    data class Page(
        val total: Int, //": 0,
        val pageIndex: Int, //": 0,
        val pageSize: Int, //": 0,
        val pageCount: Int, //": 0
    )

    val responseCode: NetworkResponseCode
        get() = when (code) {
            200 -> NetworkResponseCode.Ok
            400 -> NetworkResponseCode.ReLogin
            401 -> NetworkResponseCode.UserDisable
            408 -> NetworkResponseCode.Vip
            500 -> NetworkResponseCode.SystemError
            else -> NetworkResponseCode.UNKOWN
        }
}

enum class NetworkResponseCode(value: Int) {
    UNKOWN(-9999),

    Ok(200),
    ReLogin(400),
    UserDisable(401),
    Vip(408),
    SystemError(500),
}
