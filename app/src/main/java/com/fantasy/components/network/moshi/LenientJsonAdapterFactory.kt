package com.fantasy.components.network.moshi

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import java.lang.reflect.Type

/// 宽松的Json 模式 lenient
class LenientJsonAdapterFactory : JsonAdapter.Factory {
    override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
        // 获取原始的 JsonAdapter
        val adapter = moshi.nextAdapter<Any>(this, type, annotations)

        // 包装原始的 JsonAdapter，设置 lenient 模式
        return object : JsonAdapter<Any>() {
            override fun fromJson(reader: JsonReader): Any? {
                reader.isLenient = true
                return adapter.fromJson(reader)
            }

            override fun toJson(writer: JsonWriter, value: Any?) {
                adapter.toJson(writer, value)
            }
        }
    }
}