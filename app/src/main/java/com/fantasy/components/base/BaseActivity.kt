package com.fantasy.components.base

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.fragment.app.FragmentActivity
import com.fantasy.components.theme.CXTheme

abstract class BaseActivity : FragmentActivity() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
        - SystemBars：一组系统级别的 UI 元素，包括 StatusBar、NavigationBar 和其他一些 UI 元素。这些元素提供了有关设备状态和导航的信息，并允许用户与设备进行交互。
        - StatusBar：StatusBar 是位于屏幕顶部的系统栏，显示了有关设备当前状态的信息，例如时间、电池电量和信号强度。在 Android 5.0（API 级别 21）及更高版本中，StatusBar 还可以显示通知和快捷设置。
        - NavigationBar：NavigationBar 是位于屏幕底部的系统栏，提供了有关设备导航的信息。它通常包含三个软键（Back、Home 和 Recents），用于控制应用程序和系统操作。在 Android 5.0（API 级别 21）及更高版本中，NavigationBar 还可以显示其他 UI 元素，例如键盘和 IME（输入法）选择器。
         */
        /**
         * https://developer.android.com/quick-guides/content/video/insets-in-compose?hl=en
         * https://www.youtube.com/watch?v=QRzepC9gHj4
         * 1. enableEdgeToEdge()
         * 2. xml android:windowSoftInputMode="adjustResize"
         * 3. 设置 themes.xml
         * <item name="android:statusBarColor">@android:color/transparent</item>
         * <item name="android:navigationBarColor">@android:color/transparent</item>
         * <!--        true 状态栏字体颜色为灰色， false 白色-->
         * <item name="android:windowLightStatusBar">true</item>
         * <item name="android:windowLightNavigationBar">true</item>
         * 通过以上三步设置完成全屏展示
         */
        enableEdgeToEdge()

        setContent {
            CXTheme {
                ComposeContent()
            }
        }
    }

    @Composable
    abstract fun ComposeContent()
}
