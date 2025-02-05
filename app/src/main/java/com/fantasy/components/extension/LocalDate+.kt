package com.fantasy.components.extension

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

fun LocalDateTime.toMilli(): Long {
    return atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun LocalDateTime.toNano() = toMilli() * 1000 + nano / 1000000

fun LocalDateTime.toStringFormat(pattern: String = "yyyy/MM/dd HH:mm:ss"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return format(formatter)
}

fun LocalDate.toStringFormat(pattern: String = "yyyy-MM-dd"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return format(formatter)
}

fun LocalDate.toMilli(): Long {
    return atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}


fun LocalTime.toStringFormat(pattern: String = "HH:mm"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return format(formatter)
}


fun LocalDateTime.calculateInitialDelayAtNow(): Long {
    val now = LocalDateTime.now()
    var delay = ChronoUnit.SECONDS.between(now, this)
    // 如果当前时间已经过了目标时间，计算到明天的延迟
    if (delay < 0) {
        val tomorrow = LocalDateTime.of(LocalDate.now().plusDays(1), toLocalTime())
        delay = ChronoUnit.SECONDS.between(now, tomorrow)
    }
    return delay
}

val LocalDateTime.isToday get() = toLocalDate() == LocalDate.now()

fun LocalTime.calculateInitialDelayAtNow(): Long = LocalDateTime.of(LocalDate.now(), this)
    .calculateInitialDelayAtNow()



private val ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"

// 字符串时间戳
//fun String.toLocalDateTime(): LocalDateTime? =
//    if (isNullOrEmpty()) null else toDoubleOrNull()?.toLong()?.toLocalDateTime()
fun String.toLocalDateTime(): LocalDateTime? {
    return try {
        LocalDateTime.parse(this)
    } catch (e: Exception) {
        null
//        LocalDateTime.now()
    }
}

fun String.toLocalDate(pattern: String = "yyyy-MM-dd"): LocalDate? {
    return try {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        LocalDate.parse(this, formatter)
    } catch (e: Exception) {
        null
    }
}

fun String.toLocalTime(pattern: String = "HH:mm"): LocalTime? {
    return try {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        LocalTime.parse(this, formatter)
    } catch (e: DateTimeParseException) {
        null
    }
}

fun Long.toLocalDateTime(): LocalDateTime? {
    if (this == 0L) {
        return null
    }
    val instant = Instant.ofEpochMilli(this)
    val zoneId = ZoneId.systemDefault()
    val d = LocalDateTime.ofInstant(instant, zoneId)
    return d
}

fun Long.toLocalDate(): LocalDate? {
    if (this == 0L) {
        return null
    }
    val instant = Instant.ofEpochMilli(this)
    val zoneId = ZoneId.systemDefault()
    return instant.atZone(zoneId).toLocalDate()
}

fun LocalDate.cxDateString(
    defaultValue: (LocalDate) -> String = {
        it.toStringFormat(cxDateFormatter)
    }
): String {
    val daysDifference = ChronoUnit.DAYS.between(this, LocalDate.now())
    return when (daysDifference) {
        0L -> "今天"
        1L -> "昨天"
        2L -> "前天"
        else -> defaultValue(this)
    }
}


val LocalDate.sevenDaysAgo: Boolean
    get() {
        val daysDifference = ChronoUnit.DAYS.between(this, LocalDate.now())
        return daysDifference > 7
    }