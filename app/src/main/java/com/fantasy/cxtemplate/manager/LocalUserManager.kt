package com.fantasy.cxtemplate.manager

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fantasy.components.tools.localDelay
import com.fantasy.components.tools.readObject
import com.fantasy.components.tools.writeObject
import com.fantasy.cxtemplate.model.UserInfo
import com.fantasy.components.tools.CXKV
import com.fantasy.components.tools.getContext
import com.fantasy.components.tools.isDebugBuilder
import com.fantasy.cxtemplate.model.TokenModel
import com.fantasy.cxtemplate.routeToLogin
import com.fantasy.getgirlsmoney.manager.ConfigStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

private const val kAccessToken = "kAccessToken"
private const val kTokenModel = "TokenModel.json"
private const val kCurrentUserInfo = "CurrentUserinfo.json"
private const val kPushAlias = "PushAlias"

// /data/user/0/com.fantasy.strangerbell.debug/cache
// /data/user/0/com.fantasy.strangerbell.debug/files
// 系统自动清理： 当设备的存储空间变得紧张时，系统会自动清理应用的缓存目录，以释放更多的存储空间。这是由系统决定的，你无法控制何时会发生。
private val fileParentFiles = getContext.filesDir
private val pushAlias = CXKV.shared.decodeString(kPushAlias, "")

val userManager = LocalUserManager.shared
class LocalUserManager private constructor() : ViewModel() {
    companion object {
        val shared = LocalUserManager()
    }

    // debug时看推送信息  deviceToken cid
    var deviceToken by mutableStateOf("")
    var cid by mutableStateOf("")

    var userInfo by mutableStateOf(if (isDebugBuilder) UserInfo.mock else UserInfo())
        private set
    private var _access_token = ""
    val access_token
        get() = if (isDebugBuilder) {
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3Mjk3NjE2MjksInN1YiI6ImFmYTQzZjg4LWExMWUtNDc4My05MDVmLTEwM2I2MGFjOGU0YiJ9.czkKia2oOKhcUzWxPIZ2aq_5keRZGAtIXlGYsiG5EE4"
//            _access_token.ifEmpty { getToken() }
        } else _access_token.ifEmpty { getToken() }
    val isLogin get() = access_token.isNotEmpty()

    init {

    }
    // 在路由到其他页面时，一定要获取用户数据, 提取一个方法是为了 preview
    fun syncData() {
        // 从 MMKV 里获取 如果有则直接使用 如果没有则从文件读取
        getToken()

        viewModelScope.launch {
            var user = CXKV.shared.decodeParcelable<UserInfo>(kCurrentUserInfo)
            if (user == null) {
                val userInfoFile = File(fileParentFiles, kCurrentUserInfo)
                user = if (userInfoFile.isFile) {
                    userInfoFile.readObject<UserInfo>()
                } else null
            }
            userInfo = user ?: UserInfo()

            commonInit()
        }
    }

    private fun getToken(): String {
        // 先从 MMKV 里获取 token 字符串 如果有则直接使用
        // 如果没有则从 MMKV 读去 TokenModel 有则直接使用
        // 没有则从文件读取，有则直接用
        _access_token = CXKV.shared.decodeString(kAccessToken)

        if (_access_token.isEmpty()) {
            var token = CXKV.shared.decodeParcelable<TokenModel>(kTokenModel)
            if (token == null) {
                val tokenFile = File(fileParentFiles, kTokenModel)
                token = if (tokenFile.isFile) {
                    tokenFile.readObject<TokenModel>()
                } else null
            }
            _access_token = token?.access_token ?: ""
        }
        return _access_token
    }

    private suspend fun commonInit() {
        if (isLogin) {
            // 异步获取信息
            viewModelScope.launch {
                fetchRemoteAndRefreshUser()
            }
            // 同步获取步骤
            localDelay(1000 * 0)

            ConfigStore.shared.commonFetch()
        }
    }

    fun loginSuccess(model: TokenModel) {
        if (model.access_token.isEmpty()) {
            return
        }
        /**
         * 三级缓存
         * 1. 内存缓存，
         * 2. access_token 存到 mmkv,
         * 3. 把 model 写一份到 mmkv
         * 4. 把 model 写一份到 文件
         */
        _access_token = model.access_token
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                CXKV.shared.encode(kAccessToken, _access_token)
            }
            launch {
                CXKV.shared.encode(kTokenModel, model)
            }
            launch {
                File(fileParentFiles, kTokenModel).writeObject(model)
            }
        }
    }

    private fun refreshUserInfo(model: UserInfo) {
        userInfo = model
        // 设置极光别名
        if (model.id.isNotEmpty()) {// 为0表示mock logout
            if (pushAlias == model.pushAlias) {
                ThirdSDKManager.shared.bindPushAlias(model.pushAlias)
            } else {
                CXKV.shared.encode(kPushAlias, model.pushAlias)
                ThirdSDKManager.shared.unBindAlias(model.pushAlias)
                ThirdSDKManager.shared.bindPushAlias(model.pushAlias)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            launch {
                CXKV.shared.encode(kCurrentUserInfo, model)
            }
            launch {
                File(fileParentFiles, kCurrentUserInfo).writeObject(model)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val alias = userInfo.pushAlias
            ThirdSDKManager.shared.unBindAlias(alias)
            loginSuccess(TokenModel())
            refreshUserInfo(UserInfo())
        }

//        MainViewModel.shared.reset()
        routeToLogin()
    }

    suspend fun fetchRemoteAndRefreshUser(): UserInfo? {
//        val data = Networking.create<UserAPI>().me().data
//        data?.let {
//            refreshUserInfo(it)
//        }
//        return data
        return null
    }

//    suspend fun updateUserInfo(mod: UserAPI.UserUpdate, uiimage: UIImage?): Boolean {
//        var params = mod
//        if (uiimage != null) {
//            val res = AliyunOSSManager.shared.uploadBatch(
//                items = listOf(uiimage.compress()),
//                type = XMImageType.avatar,
//            ).firstOrNull()
//            if (res.isNullOrEmpty()) {
//                return false
//            }
//            params = mod.copy(avatar = res)
//        }
//        val response = Networking.create<UserAPI>().update(params)
////        fetchRemoteAndRefreshUser()
//        response.data?.let {
//            refreshUserInfo(it)
//        }
//        return response.isSuccess
//    }

    fun mockSuccess() {
        loginSuccess(TokenModel(access_token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3Mjg0NzE2NjYsInN1YiI6IjU3In0.jgyN62buobCSYx0w_aFw3LRs76Yux7-VAAYnX8_BcUE"))
        refreshUserInfo(UserInfo.mock)
    }

    fun mockRefreshUser(user: UserInfo) {
        userInfo = user
    }
}