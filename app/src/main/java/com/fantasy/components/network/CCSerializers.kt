package com.fantasy.components.network

import com.fantasy.components.extension.now
import com.fantasy.components.tools.cclog
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * 参考
 * @Serializable(with = LocalDateTimeIso8601Serializer::class)
 */
object CCLocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("kotlinx.datetime.LocalDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDateTime {
        try {
            val dt = LocalDateTime.parse(decoder.decodeString())
            return dt.toInstant(TimeZone.UTC).toLocalDateTime(TimeZone.currentSystemDefault())
        } catch (e: Exception) {
            cclog("CCLocalDateTimeSerializer error $e")
            return LocalDateTime.now()
        }
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        // 将本地时间转换为 UTC 时区
        val utcInstant = value.toInstant(TimeZone.currentSystemDefault())
        val utcDateTime = utcInstant.toLocalDateTime(TimeZone.UTC)
        encoder.encodeString(utcDateTime.toString())
    }

}


data class TestCCLocalDateTimeSerializer(
    @Serializable(with = CCLocalDateTimeSerializer::class)
    val time: LocalDateTime
)
