package com.fantasy.components.network

import androidx.annotation.Keep
import kotlinx.serialization.Serializable


//@JsonClass(generateAdapter = true)
@Keep
@Serializable
data class NetworkResponse<T>(
    val code: Int? = null,
//    val msg: String = "",
    val message: String = "",
    val data: T? = null,
    @kotlinx.serialization.Transient
    val throwable: Throwable? = null,
) {
    @Keep
    data class ErrorBody(
        val message: String = "",
        val code: Int? = null,
    )

    val is200Ok get() = code == 200
    val isError: Boolean get() = throwable != null
    val isFailure get() = !isError && !is200Ok

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
