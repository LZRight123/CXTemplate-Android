package com.fantasy.components.network.moshi

import androidx.annotation.Keep
import com.fantasy.components.extension.local
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.io.Serializable

class GenderAdapter {
    @FromJson
    fun fromJson(value: Int): Gender {
        return when (value) {
            0 -> Gender.random
            1 -> Gender.male
            2 -> Gender.female
            else ->  Gender.random
        }
    }

    @ToJson
    fun toJson(gender: Gender): Int {
        return when (gender) {
            Gender.random -> 0
            Gender.male -> 1
            Gender.female -> 2
        }
    }
}

@Keep
enum class Gender(
    val text: String,
    val value: Int,
): Serializable {
    random(text = "R.string.k0051.local()", value = 0),
    male(text =" R.string.k0052.local()", value = 1),
    female(text = "R.string.k0053.local()", value = 2),
    ;
}
