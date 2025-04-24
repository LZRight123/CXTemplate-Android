package com.fantasy.cctemplate

import com.fantasy.components.tools.AppHelper
import com.fantasy.components.tools.RouterAnimate
import com.fantasy.cctemplate.view.login.LoginMainView
import com.fantasy.cctemplate.view.main.MainView
import com.fantasy.cctemplate.manager.userManager

fun routeToMain() {
    if (userManager.logged) {
        AppHelper.replaceAll(MainView())
    } else {
        routeToLogin()
    }
}

fun routeToLogin() {
    // macos9 只有第一次启动时展示
//    if (!FantasyKV.shared.decodeBool("has_review_macos9", false)) {
//        AppHelper.replaceAll(LoginMACOS9View(), animate = RouterAnimate.vertical)
//    } else {
    AppHelper.replaceAll(LoginMainView(), animate = RouterAnimate.vertical)
//    }
}


