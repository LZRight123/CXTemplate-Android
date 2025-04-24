package com.fantasy.components.extension

import android.text.format.Time
import com.fantasy.components.tools.cclog
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.minus
import kotlinx.datetime.offsetAt
import kotlinx.datetime.offsetIn
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

/**
 * 获取当前的 LocalDateTime
 */
fun LocalDateTime.Companion.now(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime {
    val currentInstant = Clock.System.now()
    return currentInstant.toLocalDateTime(timeZone)
}

/**
 * 获取当前的 LocalDate
 */
fun LocalDate.Companion.now(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate {
    return LocalDateTime.now(timeZone).date
}

/**
 * 获取当前的 LocalTime
 */
fun LocalTime.Companion.now(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalTime {
    return  LocalDateTime.now(timeZone).time
}

/**
 * 格式化
 */
@OptIn(FormatStringsInDatetimeFormats::class)
fun LocalDateTime.toStringFormat(
    pattern: String = "dd/MM/yyyy HH:mm",
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): String {
    val instant = toInstant(timeZone)
    val formatter = DateTimeComponents.Format { byUnicodePattern(pattern) }
    return instant.format(formatter, offset = instant.offsetIn(timeZone))
}

// 转成和 iOS 一样的 ISO8601
fun LocalDateTime.toISO8601String(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val instant = toInstant(timeZone)
    return instant.format(
        format = DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET,
        offset = instant.offsetIn(timeZone)
    )
}

@OptIn(FormatStringsInDatetimeFormats::class)
fun LocalDate.toStringFormat(pattern: String = "yyyy-MM-dd"): String {
    val formatter = LocalDate.Format { byUnicodePattern(pattern) }
    return format(formatter)
}

//@OptIn(FormatStringsInDatetimeFormats::class)
//fun LocalTime.toStringFormat(pattern: String = "HH:mm"): String {
//    val formatter = LocalTime.Format { byUnicodePattern(pattern) }
//    return format(formatter)
//}

/**
 * 返回自 Unix 纪元以来的毫秒数
 */
val currentMillis
    get() = Clock.System.now().toEpochMilliseconds()
fun LocalDateTime.toMilli(timeZone: TimeZone = TimeZone.currentSystemDefault()): Long {
    val instant = toInstant(timeZone)
    return instant.toEpochMilliseconds()
}

fun LocalDate.toMilli(timeZone: TimeZone = TimeZone.currentSystemDefault()): Long {
    return atStartOfDayIn(timeZone).toEpochMilliseconds()
}

/**
 * 字符串、时间戳转
 */
// "yyyy-MM-dd"
@OptIn(FormatStringsInDatetimeFormats::class)
fun String.toLocalDate(pattern: String): LocalDate? {
    return try {
        val formatter = LocalDate.Format { byUnicodePattern(pattern) }
        LocalDate.parse(this, formatter)
    } catch (e: Exception) {
        null
    }
}

// "HH:mm"
@OptIn(FormatStringsInDatetimeFormats::class)
fun String.toLocalTime(pattern: String): LocalTime? {
    return try {
        val formatter = LocalTime.Format { byUnicodePattern(pattern) }
        LocalTime.parse(this, formatter)
    } catch (e: Exception) {
        null
    }
}

fun Long.toLocalDateTime(): LocalDateTime? {
    if (this == 0L) {
        return null
    }
    val instant = Instant.fromEpochMilliseconds(this)
    return instant.toLocalDateTime(TimeZone.currentSystemDefault())
}

fun Long.toLocalDate(): LocalDate? {
    if (this == 0L) {
        return null
    }
    val instant = Instant.fromEpochMilliseconds(this)
    return instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
}

val DayOfWeek.ccString: String
    get() =
        when (this) {
            DayOfWeek.MONDAY -> "星期一"
            DayOfWeek.TUESDAY -> "星期二"
            DayOfWeek.WEDNESDAY -> "星期三"
            DayOfWeek.THURSDAY -> "星期四"
            DayOfWeek.FRIDAY -> "星期五"
            DayOfWeek.SATURDAY -> "星期六"
            DayOfWeek.SUNDAY -> "星期日"
        }

val DayOfWeek.ccString2: String
    get() =
        when (this) {
            DayOfWeek.MONDAY -> "周一"
            DayOfWeek.TUESDAY -> "周二"
            DayOfWeek.WEDNESDAY -> "周三"
            DayOfWeek.THURSDAY -> "周四"
            DayOfWeek.FRIDAY -> "周五"
            DayOfWeek.SATURDAY -> "周六"
            DayOfWeek.SUNDAY -> "周日"
        }

val LocalDateTime.ccDMYString: String
    get() {
        // 获取日、月、年
        val day = this.dayOfMonth
        val month = this.month
        val year = this.year

        // 将月份转换为中文
        val chineseMonth =
            when (month) {
                Month.JANUARY -> "一月"
                Month.FEBRUARY -> "二月"
                Month.MARCH -> "三月"
                Month.APRIL -> "四月"
                Month.MAY -> "五月"
                Month.JUNE -> "六月"
                Month.JULY -> "七月"
                Month.AUGUST -> "八月"
                Month.SEPTEMBER -> "九月"
                Month.OCTOBER -> "十月"
                Month.NOVEMBER -> "十一月"
                Month.DECEMBER -> "十二月"
            }

        // 格式化日期，确保是两位数
        val formattedDay = if (day < 10) "0$day" else "$day"

        // 组合成所需格式
        return "$formattedDay/$chineseMonth/$year"
    }

val LocalDateTime.cc12HourFormat: String
    get() {
        val hour = this.hour
        val minute = this.minute

        // 确定是上午还是下午
        val amPm = if (hour < 12) "am" else "pm"

        // 计算12小时制的小时
        val hour12 =
            when (hour) {
                0 -> 12 // 午夜12点
                in 1..12 -> hour
                else -> hour - 12
            }

        // 格式化分钟，确保是两位数
        val minuteFormatted = if (minute < 10) "0$minute" else "$minute"

        return "$hour12:$minuteFormatted $amPm"
    }

val LocalDateTime.mealTimeOfDay: String
    get() =
        when (this.hour) {
            in 5..10 -> "早餐"
            in 11..13 -> "午餐"
            in 14..16 -> "下午茶"
            in 17..21 -> "晚餐"
            in 0..4 -> "夜宵"
            in 22..23 -> "夜宵"
            else -> "早餐"
        }

fun fetchCurrentWeek(
    base: LocalDate = LocalDate.now()
): List<LocalDate> {
    val dayOfWeek = base.dayOfWeek // MONDAY...SUNDAY

    // Swift 中 weekday 从 Sunday=1, Kotlin 是 Monday=1，因此需要调整
    val weekdayOffset = when (dayOfWeek) {
        DayOfWeek.SUNDAY -> 0
        else -> dayOfWeek.ordinal + 1 // ordinal: MONDAY = 0, TUESDAY = 1, ..., SUNDAY = 6
    }

    val startOfWeek = base.minus(weekdayOffset, DateTimeUnit.DAY)

    return (0..6).map { offset ->
        startOfWeek.plus(offset, DateTimeUnit.DAY)
    }
}


@OptIn(FormatStringsInDatetimeFormats::class)
private fun main() {
    val now = LocalDateTime.now()
    val now2 =
        LocalDateTime.parse(
            "2025-03-12 09:52:14",
            LocalDateTime.Format { byUnicodePattern("yyyy-MM-dd HH:mm:ss") }
        )
    val date1 = LocalDateTime.parse("2025-03-13T01:43:18.961236", LocalDateTime.Formats.ISO)
    //    val parsed = Instant.parse("2025-03-13T01:43:18.961236")
    //    val date2 = parsed.toLocalDateTime(TimeZone.currentSystemDefault())

    val date2 =
        date1.toInstant(TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())
    Clock.System.todayIn(TimeZone.currentSystemDefault())
    //    val l = java.time.LocalDateTime.parse("2025-03-13T01:43:18.961236",
    // DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    val date1String = date1.toStringFormat()
    cclog("$now  $now2 $date1 $date1String $date2 ")
}
