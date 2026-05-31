package com.ivarvisser.cineapp.data.local.implementations

import com.ivarvisser.cineapp.data.local.interfaces.UserStorage
import com.ivarvisser.cineapp.domain.User
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json

class UserStorageImpl(
    private val settings: Settings
) : UserStorage {
    override fun saveUser(user: User) {
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
        settings.remove("user")
        settings.remove("accessToken")
        settings.remove("refreshToken")
    }

    override fun saveAccessToken(accessToken: String) {
        settings.putString("accessToken", accessToken)
    }

    override fun getAccessToken(): String? {
        return settings.getStringOrNull("accessToken")
    }

    override fun saveRefreshToken(refreshToken: String) {
        settings.putString("refreshToken", refreshToken)
    }

    override fun getRefreshToken(): String? {
        return settings.getStringOrNull("refreshToken")
    }
}