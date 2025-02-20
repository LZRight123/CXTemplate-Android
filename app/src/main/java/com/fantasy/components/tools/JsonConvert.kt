package com.fantasy.components.tools

import android.content.Context
import androidx.annotation.Keep
import com.fantasy.components.extension.currentLocalType
import com.fantasy.components.network.moshi.addOhterAdapter
import com.fantasy.components.network.moshi.moshi
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException


/**
 * fromJson2List
 */
inline fun <reified T> fromJson2List(json: Any?): List<T>? {

    val listOfCardsType = Types.newParameterizedType(
        MutableList::class.java,
        T::class.java
    )
    val jsonAdapter = moshi.adapter<List<T>>(listOfCardsType)
    return jsonAdapter.fromJsonValue(json)
}

/**
 * fromJson 解析的类不能被混淆 需要被  @Keep 注解修饰
 */
inline fun <reified T> fromJson(json: Any?): T? {
    val jsonAdapter = moshi.adapter(T::class.java)
    return when (json) {
        is String -> if (json.isBlank()) null else jsonAdapter.fromJson(json)
        else -> jsonAdapter.fromJsonValue(json)
    }
}

inline fun <reified T> toJsonString(model: T?): String {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .addOhterAdapter()
        .build()
    return moshi.adapter(T::class.java).toJson(model)

}

inline fun <reified T> toJson(model: T?): Any? {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .addOhterAdapter()
        .build()
    return moshi.adapter(T::class.java).toJsonValue(model)
}

@Keep
data class JsonConvertTestModel(
    val string1: String? = "",
    val string2: String? = "",
    val string3: String? = "",


//    val double: Double = 0.0,
//    val float: Float = 0f,
//    val long: Long = 0,
//    val int: Int = 0,
//    val boolean: Boolean,
//    val message: String = "",
    val list: List<String> = listOf("1", "@")
)

fun jsonConvertTest() {

    val jsonString = """
        {
            "string1": null,
            "string2": 1.2,
            "string3": 2,
            "list": null
        }
    """.trimIndent()
    val obj = fromJson<JsonConvertTestModel>(jsonString)
    cclog(obj)
//    val model = JsonConvertTestModel(
//        string = "",
//        double = null,
//        float = 0f,
//        long = null,
//        int = null,
//        boolean = null,
//        list = listOf("1", "2", "3")
//    )
//    val js = toJsonString(model)
//    ndLog(js)
}


fun loadJSONFromAsset(
    folderName: String = "zh",
    fileName: String,
    context: Context = getContext,
): String? {
    return try {
        val inputStream = context.assets.open("$folderName/$fileName")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        String(buffer, Charsets.UTF_8)
    } catch (ex: IOException) {
        ex.printStackTrace()
        null
    }
}

//fun loadJSONFromRaw(
//    @RawRes file: Int,
//    context: Context
//): String? {
//    return try {
//        val inputStream = context.resources.openRawResource(file)
//        val size = inputStream.available()
//        val buffer = ByteArray(size)
//        inputStream.read(buffer)
//        inputStream.close()
//        String(buffer, Charsets.UTF_8)
//    } catch (ex: IOException) {
//        ex.printStackTrace()
//        null
//    }
//}

inline fun <reified T> fromAsset(fileName: String): T? {
    return fromJson<T>(loadJSONFromAsset(
        fileName = fileName,
        folderName = currentLocalType.name
    ))
}