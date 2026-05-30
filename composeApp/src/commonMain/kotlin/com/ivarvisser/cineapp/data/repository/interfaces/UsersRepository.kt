package com.ivarvisser.cineapp.data.repository.interfaces

interface UsersRepository {
    suspend fun login(username: String, password: String): Boolean
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
    suspend fun getUserId(): Int?
    suspend fun getUsername(): String?
    suspend fun getEmail(): String?
    suspend fun setEmail(email: String)
    suspend fun getFirstName(): String?
    suspend fun setFirstName(firstName: String)
    suspend fun getLastName(): String?
    suspend fun setLastName(lastName: String)
    suspend fun getPhotoId(): Int?
    suspend fun setPhoto(photo: Photo)
    suspend fun getFavoriteMovies(): List<Int>
    suspend fun addFavoriteMovie(movieId: Int)
    suspend fun removeFavoriteMovie(movieId: Int)
}