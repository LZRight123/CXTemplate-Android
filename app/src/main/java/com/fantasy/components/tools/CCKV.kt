package com.fantasy.components.tools

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import com.fantasy.components.network.ccJson
import com.tencent.mmkv.MMKV
import kotlinx.serialization.Serializable

val cckv = CCKV()

class CCKV : ViewModel() {
    companion object {
        fun start() {
            val rootDir = MMKV.initialize(getContext)
//            cclog("FantasyKV root: $rootDir")
        }
    }

    val kv by lazy { MMKV.mmkvWithID("fantasy") }

    fun <T> encode(key: String, value: T): Boolean = when (value) {
        is String -> kv.encode(key, value)
        is Boolean -> kv.encode(key, value)
        is Int -> kv.encode(key, value)
        is Parcelable -> kv.encode(key, value)
        else -> false
    }

    fun decodeString(key: String, default: String = ""): String =
        if (inPreview) "" else
            kv.decodeString(key) ?: default

    fun decodeBool(key: String, default: Boolean = false): Boolean =
        if (inPreview) false else
            kv.decodeBool(key) ?: default

    fun decodeInt(key: String, default: Int = 0): Int =
        if (inPreview) 0 else
            kv.decodeInt(key) ?: default

    inline fun <reified T> decodeSerializable(key: String, default: T? = null): T? =
        if (inPreview) null else
            kv.decodeString(key)?.let { ccJson.decodeFromString(it) } ?: default

    fun removeValueForKey(vararg keys: String) = keys.forEach { kv.removeValueForKey(it) }

    fun containsKey(key: String) = kv.containsKey(key)
}