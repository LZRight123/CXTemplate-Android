package com.fantasy.cxtemplate.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class TokenModel(
    val access_token: String = "",
    val token_type: String = "" // bearer
): Parcelable
