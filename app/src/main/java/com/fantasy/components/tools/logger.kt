package com.fantasy.components.tools

import android.util.Log

fun cclog(msg: Any?, tag: String = "") {
    if (isDebugBuilder) {
        if (msg is String) {
            Log.e("cclog-${tag}: ", msg)
        } else if (msg is Exception) {
            Log.e("cclog-${tag}", "错误调用堆栈:", msg)
        } else {
            print("cclog-${tag}: ")
            println(msg)
        }
    }
}
