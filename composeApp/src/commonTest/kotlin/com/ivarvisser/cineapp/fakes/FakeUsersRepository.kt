package com.ivarvisser.cineapp.fakes

import com.ivarvisser.cineapp.data.dto.auth.response.AuthResponse
import com.ivarvisser.cineapp.data.dto.users.response.UserFavoriteMoviesListResponse
import com.ivarvisser.cineapp.data.repository.interfaces.UsersRepository
import com.ivarvisser.cineapp.domain.User
import com.ivarvisser.cineapp.utils.ResultOf

class FakeUsersRepository : UsersRepository {
    var user: User? = null
    var favoriteMovieIds = mutableListOf<Int>()
    var error: String? = null

    override suspend fun login(username: String, password: String): ResultOf<AuthResponse> {
        return ResultOf.Failure("Not implemented in fake", null)
    }

    override suspend fun register(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): ResultOf<AuthResponse> {
        return ResultOf.Failure("Not implemented in fake", null)
    }

    override suspend fun logout(userId: Int) {
        user = null
    }

    override suspend fun isLoggedIn(): Boolean {
        return user != null
    }

    override suspend fun getUser(): User? {
        return user
    }

    override suspend fun getFavoriteMovies(): ResultOf<UserFavoriteMoviesListResponse> {
        val userId = user?.userId ?: 0
        return error?.let { ResultOf.Failure(it, null) }
            ?: ResultOf.Success(
                UserFavoriteMoviesListResponse(
                    userId = userId,
                    favoriteMovies = favoriteMovieIds.map { movieId ->
                        com.ivarvisser.cineapp.data.dto.users.response.UserFavoriteMovieResponse(
                            id = 0,
                            userId = userId,
                            movieId = movieId
                        )
                    }
                )
            )
    }

    override suspend fun addFavoriteMovie(movieId: Int): ResultOf<UserFavoriteMoviesListResponse> {
        if (!favoriteMovieIds.contains(movieId)) {
            favoriteMovieIds.add(movieId)
        }
        return getFavoriteMovies()
    }

    override suspend fun removeFavoriteMovie(movieId: Int): ResultOf<UserFavoriteMoviesListResponse> {
        favoriteMovieIds.remove(movieId)
        return getFavoriteMovies()
    }

    override suspend fun updateProfile(request: com.ivarvisser.cineapp.data.dto.users.request.UpdateUserRequest): ResultOf<User> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: user?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("No user to update", null)
    }
}
