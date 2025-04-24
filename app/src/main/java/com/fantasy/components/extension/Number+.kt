package com.fantasy.components.extension

import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.time.Duration.Companion.seconds

fun Int.toTimeString(): String {
    val duration = this.seconds
    return "%02d:%02d".format(duration.inWholeMinutes, duration.inWholeSeconds % 60)
}

fun Double.toStringIfZero(block: () -> String = { "" }) = if (this == 0.0) block() else toString()
val Double.isZero get() = this == 0.0
val Float.isZero get() = this == 0f


val Double.string: String
    get() {
        // 判断是否为整数
        if (this % 1 == 0.0) {
            // 使用"%.0f"格式或直接转换为整数字符串
            return String.format("%.0f", this)
        } else {
            // 最多保留两位小数，去掉末尾的0
            val df = DecimalFormat("0.##")
            df.roundingMode = RoundingMode.HALF_UP
            return df.format(this)
        }
    }