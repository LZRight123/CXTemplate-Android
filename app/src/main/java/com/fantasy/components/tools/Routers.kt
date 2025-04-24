package com.fantasy.components.tools

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.IntentUtils
import java.io.File

fun routeToShare(
    text: String? = "https://bellybook.cn",
    file: File? = null,
) {
    if (file != null) {
        val intent = IntentUtils.getShareTextImageIntent(text, file)
        ActivityUtils.getTopActivity().startActivity(intent)
    } else {
        if (text.isNullOrEmpty().not()) {
            val intent = IntentUtils.getShareTextIntent(text)
            ActivityUtils.getTopActivity().startActivity(intent)
        }
    }
}

/**
 * 系统跳转
 */
fun openAppDetail() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:" + getContext.packageName)
    ActivityUtils.getTopActivity()?.startActivity(intent)
}


fun openUrl(url: String) {
    ActivityUtils.getTopActivity()?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}

fun openAppRating() {
    var pname = AppUtils.getAppPackageName()
    if (pname.contains(".debug")) {
        pname = pname.replace(".debug", "")
    }
    cclog("pname is = ${pname}")
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${pname}"))
    ActivityUtils.getTopActivity()?.startActivity(intent)
}

fun isIgnoringBatteryOptimizations(context: Context = getContext): Boolean {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    val packageName = context.packageName
    return powerManager.isIgnoringBatteryOptimizations(packageName)
}
@SuppressLint("BatteryLife")
fun openBatteryOptimization() {
    val intent = Intent().apply {
        action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
        data = Uri.parse("package:" + AppUtils.getAppPackageName())
    }
    ActivityUtils.getTopActivity()?.startActivity(intent)
}

// groupKey: String = "819686580" 通过 scheme 需要群号
fun joinQQGroup() {
    openUrl("https://qm.qq.com/q/NU405x7WWm")
//    if (AppUtils.isAppInstalled("com.tencent.mobileqq")) {
//        val intent = Intent(Intent.ACTION_VIEW)
////      胃之书: https://qm.qq.com/q/8SSnsJ2Ody
////      陌生人:  "https://qm.qq.com/q/NU405x7WWm"
//        val url = "https://qm.qq.com/q/NU405x7WWm"
////        val url = "mqqapi://card/show_pslcard?src_type=internal&version=1&uin=$groupKey&card_type=group&source=qrcode"
//        intent.data = Uri.parse(url)
//        ActivityUtils.getTopActivity()?.startActivity(intent)
//    } else {
//        AppHelper.toast(R.string.k0112.local() )
//    }
}

fun openAppNotificationSettings(context: Context = getContext) {
    val intent = Intent().apply {
        action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }
    ActivityUtils.getTopActivity()?.startActivity(intent)
}