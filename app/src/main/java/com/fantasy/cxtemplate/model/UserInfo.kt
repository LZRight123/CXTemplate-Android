package com.fantasy.cxtemplate.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.fantasy.components.extension.toLocalDateTime
import com.fantasy.components.extension.toStringFormat
import com.fantasy.components.extension.xmDateFormatter8
import com.fantasy.cxtemplate.AppConfig
import com.fantasy.cxtemplate.manager.userManager
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.util.UUID

@Keep
@Parcelize
data class UserInfo(
    val id: String = "",
    val short_id : String = "",
    val records_count : Int = 0,
    val username: String = "",
    val avatar: String = "",
    val bio: String = "",
    val gender: Int = 0,
    val brithday: String = "",
    val created_time: LocalDateTime? = null,
    val phone_number: String = "",
    val daily_token_limit: Int = 3000,
    val my_vip_name: String = "",
    val profile_picture: String = "",
    val did_get_free_vip: Boolean = false,
    val invite_code : String = "",
    val invite_count  : Int = 0,
    val invite_by : String = "",
    val is_subscribed : Boolean = false,
    val subscribe_number : Int = 0,
    val like_number : Int = 0,
    val is_new_user: Boolean = true,
    val is_vip : Boolean = false
): Parcelable {
    val pushAlias get() = "${AppConfig.evn.name}${id}"

    val isme get() = userManager.userInfo.id == id
    val addCommunityText get() = "${created_time?.toStringFormat(xmDateFormatter8) ?: ""} " + "加入社区"
    companion object {
        val mock = UserInfo(
            id = UUID.randomUUID().toString(),
            short_id = "12345",
            username = "快乐的小梁同学",
            avatar = "https://example.com/avatar.jpg",
            bio = "这是一个简短的个人简介",
            gender = 1,
            brithday = "2024-09-28T17:21:41.244777",
            created_time = "2024-09-28T17:21:41.244777".toLocalDateTime(),
            phone_number = "13800138000",
            daily_token_limit = 3000,
            my_vip_name = "普通会员",
            profile_picture = "https://example.com/profile.jpg",
            did_get_free_vip = false,
            is_new_user = false,
            invite_code = "DFEFES",
            invite_count = 3,
            records_count = 0,
        )
    }
}
