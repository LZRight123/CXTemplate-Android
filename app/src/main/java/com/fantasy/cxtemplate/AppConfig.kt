package com.fantasy.cxtemplate

import com.fantasy.components.tools.isDebugBuilder


object AppConfig {
    enum class Env {
        dev,
        prod,
    }

    /**
     * 环境
     */
    val evn = if (isDebugBuilder) Env.dev else Env.prod
    val isDev = evn == Env.dev // 是否是开发环境
    val isProd: Boolean get() = evn == Env.prod

    val baseUrl: String
        get() = when (evn) {
            Env.dev -> "https://*****.com"
            Env.prod -> "https://*****.com"
        }


    const val ossBaseUrl = "*****"
    /**
     *  H5 domain
     */
    const val h5BaseUrl: String = "*****"
    /// 用户协议
    const val userAgreement: String = h5BaseUrl + "*****"
    /// 隐私政策
    const val userPrivacyPolicy: String = h5BaseUrl + "*****"
    /// 教程链接
    const val tutorialUrl: String = h5BaseUrl + "*****"
    // 声音贡献教程链接
    const val soundGuide: String = h5BaseUrl + "*****"


    val versionUpdateUrl: String
        get() = if (isDev) {
             "${ossBaseUrl}/*****.apk"
        } else "${ossBaseUrl}/*****.apk"

    val wechatAppID: String get() = "wx..."
    val wechatAppSecret: String get() = "*****"
    const val uMAppId = "*****"

    const val appVersion = "Version ${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})"
}

/**
 *
 */