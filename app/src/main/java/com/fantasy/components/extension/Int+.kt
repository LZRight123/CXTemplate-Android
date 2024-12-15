package com.fantasy.components.extension

import kotlin.time.Duration.Companion.seconds

fun Int.toTimeString(): String {
    val duration = this.seconds
    return "%02d:%02d".format(duration.inWholeMinutes, duration.inWholeSeconds % 60)
}