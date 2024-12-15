package com.fantasy.cxtemplate.api

import com.fantasy.components.network.NetworkResponse
import com.fantasy.cxtemplate.model.UserInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface PublicAPI {
    @GET("https://api.github.com/users/AndroidDeveloperJourney")
    suspend fun getUser(): NetworkResponse<UserInfo>

    // http://v.juhe.cn/todayOnhistory/queryEvent.php?key=e1300567e653b2e517e66f609d006e42&date=${dateFormat}
    @GET("http://v.juhe.cn/todayOnhistory/queryEvent.php")
    suspend fun getTodayOnHistory(
        @Query("date") date: String,
        @Query("key") key: String = "e1300567e653b2e517e66f609d006e42",
    ): HistoryResponse

}

// 顶层响应
data class HistoryResponse(
    val reason: String,
    val result: List<HistoryEvent>,
    val error_code: Int
)

// 具体历史事件
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