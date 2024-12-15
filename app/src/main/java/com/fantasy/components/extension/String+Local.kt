package com.fantasy.components.extension

import com.fantasy.components.tools.getContext
import java.util.Locale

enum class LocalType {
    zh, zh_rTW, en
}

/**
 * 可以在 composable 外用, 通过 koin 提供的依赖注入函数解决了预览的问题
 */
fun Int.local() = getContext.getString(this)
fun Int.local(vararg formatArgs: Any) =
    getContext.getString(this, *formatArgs)


private val currentLocal get() = Locale.getDefault()//MainApplication.shared.resources.configuration.locales.get(0)

private val Locale.localType
    get() = if (language == "en") {
        LocalType.en
    } else if (language == "zh" && script == "Hant") {
        LocalType.zh_rTW
    } else {
        LocalType.zh
    }

val currentLocalType get() = currentLocal.localType

val xmDateFormatter
    get() = when (currentLocalType) {
        LocalType.en -> "MM/dd/yyyy"
        else -> "yyyy年M月d日"
    }
val xmDateFormatter2
    get() = when (currentLocalType) {
        LocalType.en -> "MM/dd/yyyy"
        else -> "yyyy年MM月dd日"
    }
val xmDateFormatter3
    get() = when (currentLocalType) {
        LocalType.en -> "MM/dd/yyyy EEEE"
        else -> "yyyy年MM月dd日 EEEE"
    }
val xmDateFormatter4
    get() = when (currentLocalType) {
        LocalType.en -> "MM/dd"
        else -> "MM月dd日"
    }

val xmDateFormatter5
    get() = when (currentLocalType) {
        LocalType.en -> "MM/dd HH:mm:ss"
        else -> "MM-dd HH:mm:ss"
    }

val xmDateFormatter6
    get() = when (currentLocalType) {
        LocalType.en -> "M/dd"
        else -> "M月dd日"
    }

val xmDateFormatter7
    get() = when (currentLocalType) {
        LocalType.en -> "yyyy/MM/dd日 HH:mm:ss"
        else -> "yyyy年MM月dd日 HH:mm:ss"
    }

val xmDateFormatter8 get() = when (currentLocalType) {
    LocalType.en ->  "YYYY-MM-dd"
    else -> "YYYY-MM-dd"
}

val xmDateFormatter9 get() = when (currentLocalType) {
    LocalType.en ->  "HH:mm"
    else -> "HH:mm"
}

val xmMothFormatter
    get() = when (currentLocalType) {
        LocalType.en -> "/M"
        else -> "M月"
    }


