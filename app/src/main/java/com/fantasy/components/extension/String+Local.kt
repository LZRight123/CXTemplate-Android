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

val cxDateFormatter
    get() = when (currentLocalType) {
        LocalType.en -> "MM/dd/yyyy"
        else -> "yyyy年M月d日"
    }




