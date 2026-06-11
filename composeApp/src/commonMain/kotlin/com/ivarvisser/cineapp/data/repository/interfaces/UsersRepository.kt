package com.ivarvisser.cineapp.data.repository.interfaces

import com.ivarvisser.cineapp.data.dto.AuthResponse
import com.ivarvisser.cineapp.data.dto.UserFavoriteMoviesListResponse
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
    suspend fun getUserId(): Int?
    suspend fun getUsername(): String?
    suspend fun getEmail(): String?
    suspend fun setEmail(email: String)
    suspend fun getFirstName(): String?
    suspend fun setFirstName(firstName: String)
    suspend fun getLastName(): String?
    suspend fun setLastName(lastName: String)
    suspend fun getPhoto(): String?
    suspend fun setPhoto(photo: String)
    suspend fun getFavoriteMovies(): ResultOf<UserFavoriteMoviesListResponse>
    suspend fun addFavoriteMovie(movieId: Int): ResultOf<UserFavoriteMoviesListResponse>
    suspend fun removeFavoriteMovie(movieId: Int): ResultOf<UserFavoriteMoviesListResponse>
}