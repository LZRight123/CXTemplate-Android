package com.fantasy.components.network.moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


val moshi = Moshi.Builder()
    .add(NetworkResponseAdapterFactory())
    .add(LenientJsonAdapterFactory())
    .add(FallbackAdapterFactory())
    .addOhterAdapter()
    .addLast(KotlinJsonAdapterFactory())
    .build()

//fun Moshi.Builder.addAllCustomAdapter() = add(StringAdapter())
//    .add(LongAdapter())
//    .add(IntAdapter())
//    .add(DoubleAdapter())
//    .add(FloatAdapter())
//    .add(BooleanAdapter())
//    .add(LocalDateTimeAdapter())
//    .add(LocalTimeAdapter())

fun Moshi.Builder.addOhterAdapter() = add(LocalDateTimeAdapter())
    .add(LocalTimeAdapter())
    .add(GenderAdapter())


/**
 * Token 目前只有这四个 STRING NUMBER BOOLEAN NULL
 * 解析 String, Long, Int, Double, Float, Boolean
 */
class StringAdapter(private val isNullable: Boolean = false) : JsonAdapter<String>() {
    override fun fromJson(reader: JsonReader): String? {
        return when (reader.peek()) {
            JsonReader.Token.BOOLEAN -> "${reader.nextBoolean()}"
            JsonReader.Token.NUMBER -> {
                // fix Int 类型转 String 时 123 变成了 "123.0"
                // 原因 数字类型统一用 double 格式转成了 sting
                val number = reader.nextDouble()
                if (number == number.toInt().toDouble()) {
                    number.toInt().toString()
                } else {
                    number.toString()
                }
            }

            JsonReader.Token.STRING -> reader.nextString()
            else -> {
                reader.nextNull<Unit>()
                if (isNullable) null else ""
            }
//                JsonReader.Token.BEGIN_ARRAY -> TODO()
//                JsonReader.Token.END_ARRAY -> TODO()
//                JsonReader.Token.BEGIN_OBJECT -> TODO()
//                JsonReader.Token.END_OBJECT -> TODO()
//                JsonReader.Token.NAME -> TODO()
//                JsonReader.Token.END_DOCUMENT -> TODO()
        }
    }

    override fun toJson(writer: JsonWriter, value: String?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value)
        }
    }
}

class LongAdapter(private val isNullable: Boolean = false) : JsonAdapter<Long>() {
    override fun fromJson(reader: JsonReader): Long? {
        return when (reader.peek()) {
            JsonReader.Token.BOOLEAN -> if (reader.nextBoolean()) 1 else 0
            JsonReader.Token.NUMBER -> {
                reader.nextDouble().toLong()
            }

            JsonReader.Token.STRING -> reader.nextString().toLongOrNull() ?: 0
            else -> {
                reader.nextNull<Unit>()
                if (isNullable) null else 0
            }
        }
    }

    override fun toJson(writer: JsonWriter, value: Long?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value)
        }
    }
}

class IntAdapter(private val isNullable: Boolean = false) : JsonAdapter<Int>() {
    override fun fromJson(reader: JsonReader): Int? {
        return when (reader.peek()) {
            JsonReader.Token.BOOLEAN -> if (reader.nextBoolean()) 1 else 0
            JsonReader.Token.NUMBER -> {
                reader.nextDouble().toInt()
            }

            JsonReader.Token.STRING -> reader.nextString().toIntOrNull() ?: 0
            else -> {
                reader.nextNull<Unit>()
                if (isNullable) null else 0
            }
        }
    }

    override fun toJson(writer: JsonWriter, value: Int?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value)
        }
    }
}

class DoubleAdapter(private val isNullable: Boolean = false) : JsonAdapter<Double>() {
    override fun fromJson(reader: JsonReader): Double? {
        return when (reader.peek()) {
            JsonReader.Token.BOOLEAN -> if (reader.nextBoolean()) 1.0 else 0.0
            JsonReader.Token.NUMBER -> {
                reader.nextDouble()
            }

            JsonReader.Token.STRING -> reader.nextString().toDoubleOrNull() ?: 0.0
            else -> {
                reader.nextNull<Unit>()
                if (isNullable) null else 0.0
            }
        }
    }

    override fun toJson(writer: JsonWriter, value: Double?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value)
        }
    }
}

class FloatAdapter(private val isNullable: Boolean = false) : JsonAdapter<Float>() {
    override fun fromJson(reader: JsonReader): Float? {
        return when (reader.peek()) {
            JsonReader.Token.BOOLEAN -> if (reader.nextBoolean()) 1f else 0f
            JsonReader.Token.NUMBER -> {
                reader.nextDouble().toFloat()
            }

            JsonReader.Token.STRING -> reader.nextString().toDoubleOrNull()?.toFloat() ?: 0f
            else -> {
                reader.nextNull<Unit>()
                if (isNullable) null else 0f
            }
        }
    }

    override fun toJson(writer: JsonWriter, value: Float?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value)
        }
    }
}

class BooleanAdapter(private val isNullable: Boolean = false) : JsonAdapter<Boolean>() {
    override fun fromJson(reader: JsonReader): Boolean? {
        return when (reader.peek()) {
            JsonReader.Token.BOOLEAN -> reader.nextBoolean()
            JsonReader.Token.NUMBER -> {
                reader.nextDouble() > 0
            }

            JsonReader.Token.STRING -> !reader.nextString().isNullOrEmpty()
            else -> {
                reader.nextNull<Unit>()
                if (isNullable) null else false
            }
        }
    }

    override fun toJson(writer: JsonWriter, value: Boolean?) {
        if (value == null) {
            writer.nullValue()
        } else {
            writer.value(value)
        }
    }
}

/**
 * LocalDateTime 解析器
 */
class LocalDateTimeAdapter {
    private val formatters = listOf(
        DateTimeFormatter.ISO_LOCAL_DATE_TIME,
        DateTimeFormatter.ISO_OFFSET_DATE_TIME,
        DateTimeFormatter.ISO_ZONED_DATE_TIME,
        DateTimeFormatter.RFC_1123_DATE_TIME,
        DateTimeFormatter.ofPattern("HH:mm"),
        // 添加其他可能的格式...
    )

    @ToJson
    fun toJson(value: LocalDateTime?): String? {
        return value?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @FromJson
    fun fromJson(value: String?): LocalDateTime? {
        if (value.isNullOrBlank()) return LocalDateTime.now()

        for (formatter in formatters) {
            try {
                return LocalDateTime.parse(value, formatter)
            } catch (e: DateTimeParseException) {
                // 继续尝试下一个格式
            }
        }
        // 如果所有格式都失败
        return null
    }
}

/**
 * LocalTimeAdapter 解析器
 */
class LocalTimeAdapter {
    private val formatters = listOf(
        DateTimeFormatter.ISO_LOCAL_TIME,
        DateTimeFormatter.ofPattern("HH:mm:ss"),
        DateTimeFormatter.ofPattern("HH:mm"),
        // 可以根据需要添加更多格式
    )

    @ToJson
    fun toJson(value: LocalTime?): String? {
        return value?.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    @FromJson
    fun fromJson(value: String?): LocalTime? {
        if (value.isNullOrBlank()) return null

        for (formatter in formatters) {
            try {
                return LocalTime.parse(value, formatter)
            } catch (e: DateTimeParseException) {
                // 继续尝试下一个格式
            }
        }

        // 如果所有格式都失败，记录错误并返回 null
        println("无法解析时间字符串: $value")
        return null
    }
}

class ListAdapter<T>(
    private val elementAdapter: JsonAdapter<T>,
    private val isNullable: Boolean = false
) : JsonAdapter<List<T>>() {
    @FromJson
    override fun fromJson(reader: JsonReader): List<T>? {
        if (reader.peek() == JsonReader.Token.NULL) {
            reader.nextNull<Unit>()
            return if (isNullable) null else emptyList()
        }
        val list = mutableListOf<T>()
        reader.beginArray()
        while (reader.hasNext()) {
            list.add(elementAdapter.fromJson(reader)!!)
        }
        reader.endArray()
        return list
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: List<T>?) {
        if (value == null) {
            writer.nullValue()
        }
        writer.beginArray()
        value?.forEach { element ->
            elementAdapter.toJson(writer, element)
        }
        writer.endArray()
    }
}
