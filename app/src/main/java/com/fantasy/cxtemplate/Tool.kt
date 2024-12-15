package com.fantasy.cxtemplate

import com.fantasy.components.tools.Apphelper
import com.fantasy.components.tools.RouterAnimate
import com.fantasy.cxtemplate.manager.userManager
import com.fantasy.cxtemplate.view.login.LoginMainView
import com.fantasy.cxtemplate.view.main.MainView

fun routeToMain() {
    if (userManager.isLogin) {
        Apphelper.replaceAll(MainView())
    } else {
        routeToLogin()
    }
}

fun routeToLogin() {
    // macos9 只有第一次启动时展示
//    if (!FantasyKV.shared.decodeBool("has_review_macos9", false)) {
//        Apphelper.replaceAll(LoginMACOS9View(), animate = RouterAnimate.vertical)
//    } else {
    Apphelper.replaceAll(LoginMainView(), animate = RouterAnimate.vertical)
//    }
}


