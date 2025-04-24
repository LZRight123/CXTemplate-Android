package com.fantasy.cctemplate.model

import android.os.Parcelable
import androidx.annotation.Keep
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CCToken(
    val access_token: String = "",
    val refresh_token: String = "",
    val expires_at: Long = 0L,
    val expires_in: Double = 60.0,
    val token_type: String = "bearer"
)  {
    // 更新 Token 信息
    fun update(newToken: CCToken): CCToken {
        return this.copy(
            access_token = newToken.access_token,
            refresh_token =
            newToken.refresh_token.ifEmpty { this.refresh_token },
            expires_at = newToken.expires_at,
            expires_in = newToken.expires_in,
            token_type = newToken.token_type
        )
    }

    companion object {
        fun fromSession(session: UserSession) = CCToken(
            access_token = session.accessToken,
            refresh_token = session.refreshToken,
            expires_at = session.expiresAt.epochSeconds,
            expires_in = session.expiresIn.toDouble(),
            token_type = session.tokenType
        )
    }
}
