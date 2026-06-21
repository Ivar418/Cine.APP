package com.ivarvisser.cineapp.data.local.implementations

import com.ivarvisser.cineapp.data.local.interfaces.TokenStorage
import com.ivarvisser.cineapp.data.local.interfaces.UserStorage
import com.ivarvisser.cineapp.domain.User
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import net.codinux.log.Log

class UserStorageImpl(
    private val settings: Settings,
    private val tokenStorage: TokenStorage
) : UserStorage {



    override fun saveUser(user: User) {
        Log.debug(loggerName = "UserStorageImpl") { "Saving user: ${user.userName}" }
        settings.putString(
            "user",
            Json.Default.encodeToString(user)
        )
    }

    override fun getUser(): User? {
        return settings.getStringOrNull("user")
            ?.let { Json.Default.decodeFromString<User>(it) }
    }

    override fun clear() {
        Log.debug(loggerName = "UserStorageImpl") { "Clearing user and tokens" }
        settings.remove("user")
        tokenStorage.clearTokens()
    }

    override fun saveAccessToken(accessToken: String) {
        Log.debug(loggerName = "UserStorageImpl") { "Saving access token (delegating to TokenStorage)" }
        tokenStorage.saveTokens(accessToken, tokenStorage.getRefreshToken() ?: "")
    }

    override fun getAccessToken(): String? {
        return tokenStorage.getAccessToken()
    }

    override fun saveRefreshToken(refreshToken: String) {
        Log.debug(loggerName = "UserStorageImpl") { "Saving refresh token (delegating to TokenStorage)" }
        tokenStorage.saveTokens(tokenStorage.getAccessToken() ?: "", refreshToken)
    }

    override fun getRefreshToken(): String? {
        return tokenStorage.getRefreshToken()
    }
}
