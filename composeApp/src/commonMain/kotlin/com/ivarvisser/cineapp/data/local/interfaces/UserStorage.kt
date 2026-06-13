package com.ivarvisser.cineapp.data.local.interfaces

import com.ivarvisser.cineapp.domain.User

interface UserStorage {
    fun saveUser(user: User)
    fun getUser(): User?
    fun clear()
    fun saveAccessToken(accessToken: String)
    fun getAccessToken(): String?
    fun saveRefreshToken(refreshToken: String)
    fun getRefreshToken(): String?
}