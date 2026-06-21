package com.ivarvisser.cineapp.fakes

import com.ivarvisser.cineapp.data.local.interfaces.UserStorage
import com.ivarvisser.cineapp.domain.User

class FakeUserStorage : UserStorage {
    private var user: User? = null
    private var accessToken: String? = null
    private var refreshToken: String? = null

    override fun saveUser(user: User) {
        this.user = user
    }

    override fun getUser(): User? = user

    override fun clear() {
        user = null
        accessToken = null
        refreshToken = null
    }

    override fun saveAccessToken(accessToken: String) {
        this.accessToken = accessToken
    }

    override fun getAccessToken(): String? = accessToken

    override fun saveRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }

    override fun getRefreshToken(): String? = refreshToken
}
