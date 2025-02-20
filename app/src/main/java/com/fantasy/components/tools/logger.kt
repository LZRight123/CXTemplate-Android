package com.fantasy.components.tools

import android.util.Log

fun cclog(msg: Any?) {
    if (isDebugBuilder) {
        if (msg is String) {
            Log.i("cclog: ", msg)
        } else {
            print("cclog: ")
            println(msg)
        }
    }
}
