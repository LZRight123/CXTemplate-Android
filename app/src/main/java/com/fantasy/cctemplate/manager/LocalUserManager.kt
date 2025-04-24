package com.fantasy.cctemplate.manager

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fantasy.cctemplate.manager.thirdSDKManager
import com.fantasy.cctemplate.model.CCToken
import com.fantasy.cctemplate.model.CCUser
import com.fantasy.components.tools.readObject
import com.fantasy.components.tools.cckv
import com.fantasy.components.tools.cclog
import com.fantasy.components.tools.getContext
import com.fantasy.components.tools.inPreview
import com.fantasy.components.tools.isDebugBuilder
import com.fantasy.components.tools.toJsonString
import com.fantasy.cctemplate.manager.configStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

private const val kAccessToken = "kAccessToken"
private const val kRefreshToken = "kRefreshToken"
private const val kTokenModel = "TokenModel.json"
private const val kCurrentUserInfo = "CurrentUserinfo.json"
private const val kPushAlias = "PushAlias"

// /data/user/0/com.fantasy.strangerbell.debug/cache
// /data/user/0/com.fantasy.strangerbell.debug/files
// 系统自动清理： 当设备的存储空间变得紧张时，系统会自动清理应用的缓存目录，以释放更多的存储空间。这是由系统决定的，你无法控制何时会发生。
private val fileParentFiles = getContext.filesDir
private val pushAlias = cckv.decodeString(kPushAlias, "")

val userManager = LocalUserManager.shared

class LocalUserManager private constructor() : ViewModel() {
    companion object {
        val shared = LocalUserManager()
    }

    var userInfo by mutableStateOf(if (isDebugBuilder || inPreview) CCUser.mock else CCUser())
        private set
    var tokenInfo = CCToken()
        private set
    val access_token get() = tokenInfo.access_token
    val logged get() = access_token.isNotEmpty()
    val isVipUser get() = false
    var fcmToken = ""

    init {

    }

    // 在路由到其他页面时，一定要获取用户数据, 提取一个方法是为了 preview
    fun syncData() {
        // 初始化一下
        viewModelScope.launch {
            // 激活 supabase
            tokenInfo = getTokenForCache()
//            supabaseManager.auth.awaitInitialization()
//            val session = supabaseManager.currentSession
//            cclog("│ ✅ supabase 初始化完成 syncData session $session", tag = "supabase")
//            if (session != null) {
//                refreshTokenModel(CCToken.fromSession(session))
//                fetchRemoteAndRefreshUser()
//            }
        }
        // 读取用户信息
        viewModelScope.launch {
            var user = cckv.decodeSerializable<CCUser>(kCurrentUserInfo)
            if (user == null) {
                val userInfoFile = File(fileParentFiles, kCurrentUserInfo)
                user = if (userInfoFile.isFile) {
                    userInfoFile.readObject<CCUser>()
                } else null
            }
            userInfo = user ?: CCUser()
        }

        if (logged) {
            viewModelScope.launch {
                delay(1000)
                // 配置 FCM
                if (fcmToken.isNotEmpty()) {
//                    val r = UserAPI.register_device(
//                        device_token = fcmToken,
//                    )
//                    cclog("register_device ${r.is200Ok} ${r.message}", tag = "FCM")
                }
                // 同步获取配置
                configStore.commonFetch()
            }
        }
    }

    fun getTokenForCache(): CCToken {
        // 先从 MMKV 里获取 token 字符串 如果有则直接使用
        // 如果没有则从 MMKV 读去 TokenModel 有则直接使用
        // 没有则从文件读取，有则直接用
        val _access_token = cckv.decodeString(kAccessToken)
        val _refresh_token = cckv.decodeString(kRefreshToken)
        var tokenModel = CCToken(access_token = _access_token, refresh_token = _refresh_token)

        if (_access_token.isEmpty() || _refresh_token.isEmpty()) {
            var token = cckv.decodeSerializable<CCToken>(kTokenModel)
            if (token == null) {
                val tokenFile = File(fileParentFiles, kTokenModel)
                token = if (tokenFile.isFile) {
                    tokenFile.readObject<CCToken>()
                } else null
            }
            if (token != null) {
                tokenModel = token
            }
        }
        return tokenModel
    }


    fun refreshTokenModel(model: CCToken) {
        tokenInfo = model
        /**
         * 三级缓存
         * 1. 内存缓存，
         * 2. access_token 存到 mmkv,
         * 3. 把 model 写一份到 mmkv
         * 4. 把 model 写一份到 文件
         */
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                cckv.encode(kAccessToken, tokenInfo.access_token)
                cckv.encode(kRefreshToken, tokenInfo.refresh_token)
            }
            launch {
                cckv.encode(kTokenModel, toJsonString(model))
            }
            launch {
                val text = toJsonString(model)
                File(fileParentFiles, kTokenModel).writeText(text)
            }
        }
    }

    private fun refreshUserInfo(model: CCUser) {
        userInfo = model
        // 设置极光别名
        if (model.user_id.isNotEmpty()) {// 为0表示mock logout
//            thirdSDKManager.bindPushTagAndAlias()
        }

        viewModelScope.launch(Dispatchers.IO) {
            launch {
                cckv.encode(kCurrentUserInfo, toJsonString(model))
            }
            launch {
                val text = toJsonString(model)
                File(fileParentFiles, kCurrentUserInfo).writeText(text)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            thirdSDKManager.unBindAlias(userInfo.user_id)
            refreshTokenModel(CCToken())
            refreshUserInfo(CCUser())
//            supabaseManager.signOut()
        }

//        routeToLogin()
    }

    suspend fun fetchRemoteAndRefreshUser(): CCUser? {
        viewModelScope.launch {
//            get_user_sub_status()
        }
//        val data = UserAPI.me().data ?: return null
//        if (data.is_new_user == true) {
//            if (!cckv.decodeBool("did_see_VipBuyView")) {
//                routeToVipBuy()
//                cckv.encode("did_see_VipBuyView", true)
//            }
//        }
//        refreshUserInfo(data)
        return null
    }

//    var sub by mutableStateOf(CCSubscription())
//        private set
//
//    suspend fun get_user_sub_status(): CCSubscription {
//        sub = SubscriptionAPI.status().data ?: CCSubscription()
//        return sub
//    }

    fun mockSuccess() {
        refreshTokenModel(CCToken(access_token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3Mjg0NzE2NjYsInN1YiI6IjU3In0.jgyN62buobCSYx0w_aFw3LRs76Yux7-VAAYnX8_BcUE"))
    }

    fun mockRefreshUser(user: CCUser) {
        userInfo = user
    }
}