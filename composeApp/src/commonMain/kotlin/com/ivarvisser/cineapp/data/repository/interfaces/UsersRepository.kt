package com.ivarvisser.cineapp.data.repository.interfaces

import com.ivarvisser.cineapp.data.dto.auth.response.AuthResponse
import com.ivarvisser.cineapp.data.dto.users.response.UserFavoriteMoviesListResponse
import com.ivarvisser.cineapp.domain.User
import com.ivarvisser.cineapp.utils.ResultOf

interface UsersRepository {
    suspend fun login(username: String, password: String): ResultOf<AuthResponse>
    suspend fun register(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): ResultOf<AuthResponse>

    suspend fun logout(userId: Int)
    suspend fun isLoggedIn(): Boolean
    suspend fun getUser(): User?
    suspend fun getFavoriteMovies(): ResultOf<UserFavoriteMoviesListResponse>
    suspend fun addFavoriteMovie(movieId: Int): ResultOf<UserFavoriteMoviesListResponse>
    suspend fun removeFavoriteMovie(movieId: Int): ResultOf<UserFavoriteMoviesListResponse>
}