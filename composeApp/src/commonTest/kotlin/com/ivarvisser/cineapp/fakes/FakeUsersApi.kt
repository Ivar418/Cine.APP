package com.ivarvisser.cineapp.fakes

import com.ivarvisser.cineapp.data.dto.auth.response.AuthResponse
import com.ivarvisser.cineapp.data.dto.users.request.UpdateUserRequest
import com.ivarvisser.cineapp.data.dto.users.response.UserFavoriteMoviesListResponse
import com.ivarvisser.cineapp.data.dto.users.response.UserResponse
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.UsersApi
import com.ivarvisser.cineapp.utils.ResultOf

class FakeUsersApi : UsersApi {
    var authResponse: AuthResponse? = null
    var profileResponse: UserResponse? = null
    var favoriteMoviesResponse: UserFavoriteMoviesListResponse? = null
    var updatedProfileResponse: UserResponse? = null
    var error: String? = null
    var lastLogoutToken: String? = null

    override suspend fun login(username: String, password: String): ResultOf<AuthResponse> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: authResponse?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("No auth response set", null)
    }

    override suspend fun register(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): ResultOf<AuthResponse> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: authResponse?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("No auth response set", null)
    }

    override suspend fun logout(refreshToken: String): ResultOf<Unit> {
        lastLogoutToken = refreshToken
        return error?.let { ResultOf.Failure(it, null) } ?: ResultOf.Success(Unit)
    }

    override suspend fun getFavoriteMovies(): ResultOf<UserFavoriteMoviesListResponse> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: favoriteMoviesResponse?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("No favorites set", null)
    }

    override suspend fun addFavoriteMovie(movieId: Int): ResultOf<UserFavoriteMoviesListResponse> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: favoriteMoviesResponse?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("No favorites set", null)
    }

    override suspend fun removeFavoriteMovie(movieId: Int): ResultOf<UserFavoriteMoviesListResponse> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: favoriteMoviesResponse?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("No favorites set", null)
    }

    override suspend fun getProfile(): ResultOf<UserResponse> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: profileResponse?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("No profile set", null)
    }

    override suspend fun updateProfile(request: UpdateUserRequest): ResultOf<UserResponse> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: updatedProfileResponse?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("No updated profile set", null)
    }
}
