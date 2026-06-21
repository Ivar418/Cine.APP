package com.ivarvisser.cineapp.data.repository.implementations

import com.ivarvisser.cineapp.data.dto.auth.response.AuthResponse
import com.ivarvisser.cineapp.data.dto.users.request.UpdateUserRequest
import com.ivarvisser.cineapp.data.dto.users.response.UserFavoriteMoviesListResponse
import com.ivarvisser.cineapp.data.local.interfaces.UserStorage
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.UsersApi
import com.ivarvisser.cineapp.data.repository.interfaces.UsersRepository
import com.ivarvisser.cineapp.domain.User
import com.ivarvisser.cineapp.mapper.toUser
import com.ivarvisser.cineapp.utils.ResultOf

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
        storage.getRefreshToken() ?: return false
        return usersApi.getProfile() !is ResultOf.Failure
    }

    override suspend fun getUser(): User? {
        return storage.getUser()
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

    override suspend fun updateProfile(request: UpdateUserRequest): ResultOf<User> {
        return when (val result = usersApi.updateProfile(request)) {
            is ResultOf.Success -> {
                val user = result.value.toUser()
                storage.saveUser(user)
                ResultOf.Success(user)
            }

            is ResultOf.Failure -> ResultOf.Failure(result.message, result.throwable)
        }
    }
}
