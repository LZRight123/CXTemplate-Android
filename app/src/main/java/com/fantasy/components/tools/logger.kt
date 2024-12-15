package com.fantasy.components.tools

import android.util.Log

fun cxlog(msg: Any?) {
    if (isDebugBuilder) {
        if (msg is String) {
            Log.i("cxlog: ", msg)
        } else {
            print("cxlog: ")
            println(msg)
        }
    }
}
