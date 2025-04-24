package com.fantasy.components.tools

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.RomUtils
import com.fantasy.components.extension.compose.addCardBack
import com.fantasy.components.extension.compose.fantasyClick
import com.fantasy.components.extension.randomString
import com.fantasy.components.theme.CCFont
import com.fantasy.components.widget.CCToastType
import com.fantasy.components.widget.PreviewScreen

object PermissionManager {
    private const val alertTip = "开启权限后再试"
    private const val cameraTitle = "${appName}需要申请相机权限"
    private const val cameraContent =
        "${appName}需要申请相机权限以便您拍照进行识别，收藏等服务，拒绝或取消授权不影响使用其他服务"
    private const val albumTitle = "${appName}需要申请相册权限"
    private const val albumContent =
        "${appName}需要申请相册权限以便您从相册选取用户头像、主页封面，选择图片进行识别，收藏等服务，拒绝或取消授权不影响使用其他服务"

    private val versionCode = AppUtils.getAppVersionCode()
    val localCaptureKey get() = "localCaptureKey$versionCode"
    private val localPhotoAlbumKey get() = "localPhotoAlbumKey$versionCode"
    val localNotificationKey get() = "localNotionftionKey$versionCode"


    // ✅ 检测相机权限（不请求）
    val isCameraAuthorized
        get() = ContextCompat.checkSelfPermission(
            getContext,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    // ✅ 只判断是否拥有通知权限（不请求）
    val IsNotificationPermissionGranted
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                getContext,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // Android 12 以下默认允许
        }

    fun checkCapture(
        completion: (Boolean, List<String>) -> Unit,
    ) {
        PermissionUtils.permission(
            Manifest.permission.CAMERA,
        ).explain { activity, denied, shouldRequest ->
//            if (cckv.decodeBool(localCaptureKey, false).not()) {
//                AppHelper.attachShow {
//                    PermissionTipPopup(
//                        title = cameraTitle,
//                        content = cameraContent
//                    )
//                }
//            }
            shouldRequest.start(true)
        }
            .callback { isAllGranted, granted, deniedForever, denied ->
                cckv.encode(localCaptureKey, true)
//                AppHelper.attachHidden()
                cclog("isAllGranted ${isAllGranted}, denied is ${denied} ")
                completion(isAllGranted, denied)
                // 小米市场不能往这跳
                if (isAllGranted.not()) {
//                    AppHelper.toast(
//                        alertTip,
//                        CCToastType.error
//                    )
                }
            }
            .request()
    }

    fun checkNotification(completion: (Boolean, List<String>) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionUtils.permission(
                Manifest.permission.POST_NOTIFICATIONS,
            ).callback { isAllGranted, granted, deniedForever, denied ->
                cckv.encode(localNotificationKey, true)
                cclog("通知权限回掉 isAllGranted ${isAllGranted}, denied is ${denied}", tag = "FCM")
                completion(isAllGranted, denied)
            }
                .request()
        } else {
            completion(true, emptyList())
        }
    }


    fun checkPhotoAlbum(
        completion: (Boolean) -> Unit,
    ) {
        val utils = if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)) {
            PermissionUtils.permission(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            PermissionUtils
                .permission(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                )
        }
        utils.explain { _, _, shouldRequest ->
            if (cckv.decodeBool(localPhotoAlbumKey, false).not()) {
                AppHelper.attachShow {
                    PermissionTipPopup(
                        title = albumTitle,
                        content = albumContent
                    )
                }
            }
            shouldRequest.start(true)
        }
            .callback { isAllGranted, granted, deniedForever, denied ->
                AppHelper.attachHidden()
                if (RomUtils.isXiaomi().not()) {
                    cckv.encode(localPhotoAlbumKey, true)
                }
                // 小米市场不能往这跳
                if (isAllGranted.not()) {
                    AppHelper.toast(
                        alertTip,
                        CCToastType.error
                    )
//                    PermissionUtils.launchAppDetailsSettings()
                }

                cclog("isAllGranted ${isAllGranted} ${granted}, ${deniedForever} denied is ${denied} ")
                completion(isAllGranted)
            }
            .request()
    }

    fun checkClipboard(
        completion: (Boolean) -> Unit,
    ) {
        PermissionUtils.permission(
            Manifest.permission.CAMERA,
        ).explain { activity, denied, shouldRequest ->
            if (cckv.decodeBool(localCaptureKey, false).not()) {
                AppHelper.attachShow {
                    PermissionTipPopup(
                        title = "需要访问剪切板权限",
                        content = "复制东西"
                    )
                }
            }
            shouldRequest.start(true)
        }
            .callback { isAllGranted, granted, deniedForever, denied ->
                if (RomUtils.isXiaomi().not()) {
                    cckv.encode(localCaptureKey, true)
                }
                AppHelper.attachHidden()
                cclog("isAllGranted ${isAllGranted}, denied is ${denied} ")
                completion(isAllGranted)
                // 小米市场不能往这跳
                if (isAllGranted.not()) {
                    AppHelper.toast(
                        alertTip,
                        CCToastType.error
                    )
//                    PermissionUtils.launchAppDetailsSettings()
                }
            }
            .request()
    }


}


/**
 * 弹框浮层
 */
@Composable
fun PermissionTipPopup(title: String, content: String) {

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 12.dp)
            .fantasyClick(null) {
                AppHelper.attachHidden()
            }
            .addCardBack()
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(text = title, style = CCFont.f1b.v2)
        Text(text = content, style = CCFont.f2.v2)
    }
}

@Preview
@Composable
private fun preivew() {
    PreviewScreen {

    }
    PermissionTipPopup(
        title = "确定",
        content = randomString(100)
    )
}
