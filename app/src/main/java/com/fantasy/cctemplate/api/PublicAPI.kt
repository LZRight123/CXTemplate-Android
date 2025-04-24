package com.fantasy.cctemplate.api

import androidx.annotation.Keep
import com.fantasy.components.network.NetworkResponse
import com.fantasy.components.network.get
import com.fantasy.stomachbook.api.networking.networking
import io.ktor.http.parameters
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


object PublicAPI {
    suspend fun getUser() = networking.get<JsonElement>("https://api.github.com/users/AndroidDeveloperJourney")

    // http://v.juhe.cn/todayOnhistory/queryEvent.php?key=e1300567e653b2e517e66f609d006e42&date=${dateFormat}
    suspend fun getTodayOnHistory(date: String) = networking.get<HistoryResponse>("http://v.juhe.cn/todayOnhistory/queryEvent.php") {
        parameters {
            append("date", date)
            append("key", "e1300567e653b2e517e66f609d006e42")
        }
    }
}

// 顶层响应
@Keep
@Serializable
data class HistoryResponse(
    val reason: String,
    val result: List<HistoryEvent>,
    val error_code: Int
)

// 具体历史事件
@Keep
@Serializable
data class HistoryEvent(
    val day: String,
    val date: String,
    val title: String,
    val e_id: String
) {
    companion object {
        fun mock() = HistoryEvent(
            day = "12月12日",
            date = "2023-12-12",
            title = "孙中山诞辰",
            e_id = "539"
        )
    }
}