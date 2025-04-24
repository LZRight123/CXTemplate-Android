package com.fantasy.cctemplate.manager


import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fantasy.components.tools.cclog
import com.fantasy.components.tools.CCKV
import com.fantasy.components.tools.cckv
import com.fantasy.components.tools.getContext
import com.fantasy.components.tools.isDebugBuilder
import com.fantasy.cctemplate.manager.userManager
import kotlinx.coroutines.*

val kIsAgreePrivacy = "kIsAgreePrivacy"
val thirdSDKManager = ThirdSDKManager()

class ThirdSDKManager : ViewModel() {
    companion object {
        val shared = ThirdSDKManager()
    }

    @SuppressLint("StaticFieldLeak")
    val context = getContext

//     https://developers.weixin.qq.com/doc/oplatform/Mobile_App/WeChat_Login/Development_Guide.html

//    var wechatPayCallBack: ((BaseResp) -> Unit)? = null

    fun initSDK() {
        initSDKWithOutAuthorize()
        if (cckv.decodeBool(kIsAgreePrivacy)) {
            initSDKWithAuthorize()
        }
    }
    private fun initSDKWithOutAuthorize() {
        cclog("ThirdSDKManager initSDKWithOutAuthorize")
//        PushManager.getInstance().preInit(context)
//        UMConfigure.preInit(context, AppConfig.uMAppId, "Android")
        viewModelScope.launch(Dispatchers.IO) {
//            SpeechUtility.createUtility(context, SpeechConstant.APPID + "=d9c2bad7")
        }

    }
    fun initSDKWithAuthorize() {
        // 需要用户授权的
        viewModelScope.launch(Dispatchers.IO) {
            cclog("ThirdSDKManager initSDKWithAuthorize")

            initBugly()
            registerGeTui()
            registerUMeng()
            if (isDebugBuilder) {
                logGTPushIntent()
            }
        }
    }

    private fun initBugly() {
        // Debug 去以前的ND解决问题 https://bugly.qq.com/v2/crash-reporting/crashes/5f5183a674?pid=1
        // Prod  去这个链接解决问题 https://bugly.qq.com/v2/crash-reporting/crashes/99a14d3276?pid=1
        val userInfo = userManager.userInfo
//        val userStrategy = CrashReport.UserStrategy(context)
//        userStrategy.setCrashHandleCallback(object : CrashReport.CrashHandleCallback() {
//            override fun onCrashHandleStart(
//                crashType: Int,
//                errorType: String?,
//                errorMessage: String?,
//                errorStack: String?
//            ): MutableMap<String, String> {
//                ndLog(
//                    "崩溃啦 $crashType - errorType: ${errorType ?: ""} - errorMessage: ${errorMessage ?: "2"} - " +
//                            "errorStack:${errorStack ?: "3"}"
//                )
//                val map = mutableMapOf<String, String>()
//                map["useid"] = userInfo.id.toString()
//                map["username"] = userInfo.username
//                // 这个会出现在 extraMessage.txt 里
//                return map
//            }
//
//            override fun onCrashHandleStart2GetExtraDatas(
//                crashType: Int,
//                errorType: String?,
//                errorMessage: String?,
//                errorStack: String?
//            ): ByteArray {
//                return super.onCrashHandleStart2GetExtraDatas(crashType, errorType, errorMessage, errorStack)
//            }
//        })
//        CrashReport.initCrashReport(
//            context,
//            if (BuildConfig.DEBUG) "5f5183a674" else "99a14d3276",
//            BuildConfig.DEBUG,
//            userStrategy
//        )
//        CrashReport.setIsDevelopmentDevice(context, BuildConfig.DEBUG);
//
//        if (LocalUserManager.shared.isLogin) {
//            CrashReport.setUserId(userInfo.id.toString())
//            CrashReport.putUserData(context, "是否是Debug", "${BuildConfig.DEBUG}");
//            CrashReport.putUserData(context, "用户名", userInfo.username);
//            CrashReport.putUserData(context, "手机号",  userInfo.phone_number);
//            CrashReport.putUserData(context, "用户ID",userInfo.id.toString());
//        }
    }

    private fun registerGeTui() {
//        // https://docs.getui.com/getui/mobile/android/api/
//        val isOpenNotification = PushManager.getInstance().areNotificationsEnabled(context)
//        ndLog("个推推送 用户是否开启推送 $isOpenNotification")
//
//        viewModelScope.launch(Dispatchers.Main) {
//            // 为了保障推送功能可以正常使用，您务必确保用户同意《隐私政策》之后，调用初始化和注册 CID 方法。若不调用注册 CID 方法，推送功能无法正常使用。
//            PushManager.getInstance().initialize(context)
//            if (BuildConfig.DEBUG) {
//                PushManager.getInstance().setDebugLogger(context) {
//                    ndLog("个推推送 log ${it ?: "**"}")
//                }
//                try {
//                    PushManager.getInstance().checkManifest(context)
//                } catch (e: Exception) {
//                    ndLog("个推推送 报错 ${e.message}")
//                }
//            }
//            PushManager.getInstance().setBadgeNum(context, 0)
//        }
    }

    private fun registerUMeng() {
        viewModelScope.launch(Dispatchers.Main) {
//            UMConfigure.setLogEnabled(BuildConfig.DEBUG)
//
//            UMConfigure.init(context, AppConfig.uMAppId, "Android", UMConfigure.DEVICE_TYPE_PHONE, "")
//            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.MANUAL)
//            UMConfigure.submitPolicyGrantResult(context, true)
//
//            PlatformConfig.setWeixin(AppConfig.wechatAppID, AppConfig.wechatAppSecret)
//            PlatformConfig.setWXFileProvider("com.fantasy.getgirlsmoney.fileprovider");
//
//            TrackManager.onEvent(TrackEvent.start_up)
//
////            UMShareAPI
//            UMWeb("").apply {
//                setThumb(UMImage(context,""))
//            }
        }
    }

    fun bindPushAlias(alias: String) {
        if (!userManager.logged || alias.isBlank()) {
            return
        }

        viewModelScope.launch {
            cclog("个推推送 准备绑定别名: $alias")
//            PushManager.getInstance().bindAlias(context, alias)
        }
    }

    fun unBindAlias(alias: String) {
        cclog("个推推送 准备解绑别名 $alias")
//        PushManager.getInstance().unBindAlias(context, alias, true)
    }

    fun logGTPushIntent() {
        //在Android 开发工具中，参考如下代码生成 Intent
//        val intent = Intent(context, MainActivity::class.java)
//        //Scheme协议（gtpushscheme://com.getui.push/detail?）开发者可以自定义
//        intent.setData(Uri.parse("getgirlsmoney://app/Setting?"))
//        //如果设置了 package，则表示目标应用必须是 package 所对应的应用
//        intent.setPackage(BuildConfig.APPLICATION_ID)
//        //intent 中添加自定义键值对，value 为 String 型
////        intent.putExtra("payload", "payloadStr")
//        //gttask 不用赋值，添加 gttask 字段后，个推给客户端的 intent 里会自动拼接上 taskid 和 actionid ；app 端接收到参数以后，可通过下方 6.3 的 pushClick 方法解析及上报埋点。
//        intent.putExtra("gttask", "")
//        // 应用必须带上该Flag，如果不添加该选项有可能会显示重复的消息，强烈推荐使用Intent.FLAG_ACTIVITY_CLEAR_TOP
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val intentUri: String = intent.toUri(Intent.URI_INTENT_SCHEME)
//        ndLog("个推推送 intent ${intentUri}")
        // intent://app/Setting?#Intent;scheme=bellybook;launchFlags=0x4000000;package=com.fantasy.getgirlsmoney;component=com.fantasy.getgirlsmoney/.pages.MiddlewareActivity;S.gttask=;end
    }
}

/**
 * 功能扩展
 */
//fun ThirdSDKManager.shareWebpage(
//    model: ShareModel =  ShareModel(),
//) {
//    val umImage = if (model.img.isEmpty()) {
//        UMImage(context, R.mipmap.ic_launcher)
//    } else {
//        UMImage(context, model.img)
//    }
//    val web = UMWeb(model.url).apply {
//        setThumb(umImage)
//        title = model.title
//        model.desc?.let { desc ->
//            description = desc
//        }
//    }
//    val topActivity = ActivityUtils.getTopActivity()
//    ShareAction(topActivity).withMedia(web)
//        .setCallback(object : UMShareListener {
//            override fun onStart(p0: SHARE_MEDIA?) {
//                ndLog("UM分享 onStart")
//            }
//
//            override fun onResult(p0: SHARE_MEDIA?) {
//                ndLog("UM分享 onResult")
//            }
//
//            override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
//                ndLog("UM分享 onError")
//            }
//
//            override fun onCancel(p0: SHARE_MEDIA?) {
//                ndLog("UM分享 onCancel")
//            }
//
//        })
//        .setPlatform(SHARE_MEDIA.WEIXIN)
//        .share()
//}
//
//fun ThirdSDKManager.wechatAuth() {
//    val topActivity = ActivityUtils.getTopActivity()
//    UMShareAPI.get(context).getPlatformInfo(topActivity, SHARE_MEDIA.WEIXIN, object : UMAuthListener {
//        override fun onStart(p0: SHARE_MEDIA?) {
//            ndLog("UM授权 onStart")
//        }
//
//        override fun onComplete(p0: SHARE_MEDIA?, p1: Int, p2: MutableMap<String, String>?) {
//            ndLog("UM授权 onComplete")
//        }
//
//        override fun onError(p0: SHARE_MEDIA?, p1: Int, p2: Throwable?) {
//            ndLog("UM授权 onError")
//        }
//
//        override fun onCancel(p0: SHARE_MEDIA?, p1: Int) {
//            ndLog("UM授权 onCancel")
//        }
//
//    })
//}