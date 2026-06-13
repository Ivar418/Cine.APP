package com.ivarvisser.cineapp.data.local.implementations

import com.ivarvisser.cineapp.data.local.interfaces.TokenStorage
import com.russhwolf.settings.Settings
import net.codinux.log.Log


class TokenStorageImpl(private val settings: Settings) : TokenStorage {
    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
        private const val TAG = "TokenStorageImpl"
    }

    override fun getAccessToken(): String? = settings.getStringOrNull(ACCESS_TOKEN_KEY)

    override fun getRefreshToken(): String? = settings.getStringOrNull(REFRESH_TOKEN_KEY)

    override fun saveTokens(accessToken: String, refreshToken: String) {
        Log.debug(loggerName = TAG) { "Saving tokens (access token length: ${accessToken.length}, refresh token length: ${refreshToken.length})" }
        settings.putString(ACCESS_TOKEN_KEY, accessToken)
        settings.putString(REFRESH_TOKEN_KEY, refreshToken)
    }

    override fun clearTokens() {
        Log.debug(loggerName = TAG) { "Clearing tokens" }
        settings.remove(ACCESS_TOKEN_KEY)
        settings.remove(REFRESH_TOKEN_KEY)
    }
}
