package com.fantasy.components.network.moshi

import com.fantasy.components.network.NetworkResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.rawType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkResponseAdapterFactory : JsonAdapter.Factory {
    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        if (type.rawType != NetworkResponse::class.java) return null

        // 获取 NetworkResponse 的泛型参数，比如 User
        val dataType: Type = (type as? ParameterizedType)
            ?.actualTypeArguments?.firstOrNull()
            ?: return null

        // 获取 泛型 的 JsonAdapter
//        val dataTypeAdapter = moshi.nextAdapter<Any>(
//            this, dataType, annotations
//        )
        val dataAdapter = moshi.adapter<Any>(dataType)

        return NetworkResponseAdapter(dataAdapter).nullSafe()
    }

    class NetworkResponseAdapter<T>(
        private val dataAdapter: JsonAdapter<T>
    ) : JsonAdapter<T>() {
        override fun fromJson(reader: JsonReader): T? {
            reader.beginObject()

            var code: Int? = null
            var msg: String? = null
            var message: String? = null
            var data: Any? = null
            var throwable: Throwable? = null
            var page: NetworkResponse.Page? = null

            while (reader.hasNext()) {
                when (reader.nextName()) {
                    "code" -> code = reader.nextInt()
                    "msg" -> msg = reader.nextString()
                    "message" -> message = reader.nextString()
                    "data" -> {
                        val json = reader.readJsonValue()
                        kotlin.runCatching {
                            data = dataAdapter.fromJsonValue(json)
                        }.getOrElse {
                            throwable = it
                            it.printStackTrace()
                        }
                    }

                    "page" -> {
                        val json = reader.readJsonValue()
                        page = com.fantasy.components.tools.fromJson(json)
                    }

                    else -> reader.skipValue()
                }
            }

            reader.endObject()

            return NetworkResponse(
                code = code,
//                msg = msg ?: "",
                message = message ?: "",
                data = data,
                throwable = throwable,
                page = page
            ) as T?
        }

        // 不需要序列化的逻辑
        override fun toJson(writer: JsonWriter, value: T?): Unit =
            TODO("Not yet implemented")
    }
}



