package com.fantasy.cctemplate.model

import androidx.annotation.Keep
import com.fantasy.components.extension.now
import com.fantasy.components.extension.randomString
import com.fantasy.components.network.CCLocalDateTimeSerializer
import java.util.UUID
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable


@Keep
@Serializable
data class CCUser(
    val display_name: String = "", // 用于显示的用户名
    val username: String = "", //  用于显示在用户名下方的数字字母用户名
    val is_deleted: String = "", //  false,
    val avatar_url: String = "", //  "https://bellybook.com/avatar.png",
    val gender: Int = 0, // 0: 未知, 1: 男, 2: 女
    val is_new_user: Boolean = true, //  true,
    @Serializable(with = CCLocalDateTimeSerializer::class)
    val updated_at: LocalDateTime = LocalDateTime.now(), //  null,
    @Serializable(with = CCLocalDateTimeSerializer::class)
    val created_at: LocalDateTime = LocalDateTime.now(), //  "2025-03-03T06:05:23.305563",
    val profile_image_url: String = "", //  "https://bellybook.com/avatar.png",
    @Serializable(with = CCLocalDateTimeSerializer::class)
    val birth_date: LocalDateTime = LocalDateTime.now(), //  "2025-03-03T06:05:20.452402",
    val user_id: String = "", //  "66522857-c490-46c0-aa73-a65acf525575"
    val bio: String = "这位美食家什么都没有说",
) {
    companion object {
        val mock =
            CCUser(
                user_id = UUID.randomUUID().toString(),
                display_name = "胃之书",
                username = randomString(),
                avatar_url = "https://bellybook.com/avatar.png",
                birth_date = LocalDateTime.now(),
            )
    }
}

@Keep
@Serializable
data class CCUserUpdate(
    // """用户信息更新模型，用于接收用户更新个人资料的请求"""
    val display_name: String = "", // Optional[str] = None
    val username: String = "", //: Optional[str] = None
    val bio: String = "", //: Optional[str] = None
    val gender: Int = 0, //: Optional[int] = None
    @Serializable(with = CCLocalDateTimeSerializer::class)
    val birth_date: LocalDateTime? = null, //: Optional[datetime] = None
    val avatar_url: String? = null, //: Optional[str] = None
    val profile_image_url: String? = null, //: Optional[str] = None
    val is_new_user: String? = null, //: Optional[bool] = None
)
