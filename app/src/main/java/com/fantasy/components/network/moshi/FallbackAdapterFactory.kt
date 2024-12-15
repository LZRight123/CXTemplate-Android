package com.fantasy.components.network.moshi

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.rawType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


/**
 * 空值降级处理
 * https://code.luasoftware.com/tutorials/android/android-moshi-adapter-convert-null-to-empty-string/
 * https://blog.csdn.net/c6E5UlI1N/article/details/120643972
 */
class FallbackAdapterFactory : JsonAdapter.Factory {
    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        // 用 type == List::class.java 也OK
//        val rawType = Types.getRawType(type)
        if (type.rawType == List::class.java) {
            val elementType = (type as? ParameterizedType)?.actualTypeArguments?.firstOrNull()
            if (elementType != null) {
                val elementAdapter = moshi.adapter<Any>(elementType)
                return ListAdapter(
                    elementAdapter,
//                    isNullable = type == List::class.javaObjectType 先返回 emptyList() 技术债
                )
            }
        }

        return when (type) {
            String::class.java, String::class.javaObjectType -> StringAdapter(isNullable = type == String::class.javaObjectType)
            Long::class.java, Long::class.javaObjectType -> LongAdapter(isNullable = type == Long::class.javaObjectType)
            Int::class.java, Int::class.javaObjectType -> IntAdapter(isNullable = type == Int::class.javaObjectType)
            Double::class.java, Double::class.javaObjectType -> DoubleAdapter(isNullable = type == Double::class.javaObjectType)
            Float::class.java, Float::class.javaObjectType -> FloatAdapter(isNullable = type == Float::class.javaObjectType)
            Boolean::class.java, Boolean::class.javaObjectType -> BooleanAdapter(isNullable = type == Boolean::class.javaObjectType)
            else -> null
        }
    }
}