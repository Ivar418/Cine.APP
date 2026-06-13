package com.ivarvisser.cineapp.data.repository.implementations

import com.ivarvisser.cineapp.data.dto.auth.response.AuthResponse
import com.ivarvisser.cineapp.data.dto.users.response.UserFavoriteMoviesListResponse
import com.ivarvisser.cineapp.data.local.interfaces.UserStorage
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.UsersApi
import com.ivarvisser.cineapp.data.repository.interfaces.UsersRepository
import com.ivarvisser.cineapp.domain.User
import com.ivarvisser.cineapp.mapper.toUser
import com.ivarvisser.cineapp.utils.ResultOf
import net.codinux.log.Log

class UsersRepositoryImpl(
    private val usersApi: UsersApi,
    private val storage: UserStorage
) : UsersRepository {
    override suspend fun login(username: String, password: String): ResultOf<AuthResponse> {
        val user = usersApi.login(username, password)
        if (user is ResultOf.Success) {
            storage.saveUser(user.value.user.toUser())
            storage.saveAccessToken(user.value.accessToken)
            storage.saveRefreshToken(user.value.refreshToken)
        }
        return user
    }

    override suspend fun register(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): ResultOf<AuthResponse> {
        val user = usersApi.register(username, firstName, lastName, email, password)
        if (user is ResultOf.Success) {
            storage.saveUser(user.value.user.toUser())
            storage.saveAccessToken(user.value.accessToken)
            storage.saveRefreshToken(user.value.refreshToken)
        }
        return user
    }

    override suspend fun logout(userId: Int) {
        storage.getRefreshToken()?.let {
            usersApi.logout(it)
        }
        storage.clear()
    }

    override suspend fun isLoggedIn(): Boolean {
        storage.getRefreshToken()?.let {
            if (storage.getUser() != null) return true
            Log.debug(loggerName = "UsersRepositoryImpl") { "Debug: Refreshing token" }
            val user = usersApi.refreshToken(it)
            if (user is ResultOf.Success) {
                Log.debug(loggerName = "UsersRepositoryImpl") { "Debug: Token refreshed" }
                storage.saveUser(user.value.user.toUser())
                storage.saveAccessToken(user.value.accessToken)
                storage.saveRefreshToken(user.value.refreshToken)
                return true
            }
        }
        Log.debug(loggerName = "UsersRepositoryImpl") { "Debug: Not logged in" }
        return false
    }

    override suspend fun getUser(): User? {
        return storage.getUser()
    }

    override suspend fun getUserId(): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun getUsername(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun getEmail(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun setEmail(email: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getFirstName(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun setFirstName(firstName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getLastName(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun setLastName(lastName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getPhoto(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun setPhoto(photo: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteMovies(): ResultOf<UserFavoriteMoviesListResponse> {
        return usersApi.getFavoriteMovies()
    }

    override suspend fun addFavoriteMovie(movieId: Int): ResultOf<UserFavoriteMoviesListResponse> {
        return usersApi.addFavoriteMovie(movieId)
    }

    override suspend fun removeFavoriteMovie(movieId: Int): ResultOf<UserFavoriteMoviesListResponse> {
        return usersApi.removeFavoriteMovie(movieId)
    }

}
