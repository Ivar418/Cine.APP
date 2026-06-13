package com.ivarvisser.cineapp.data.dto.users.response

import kotlinx.serialization.Serializable

@Serializable
data class UserFavoriteMoviesListResponse(
    val userId: Int,
    val favoriteMovies: List<UserFavoriteMovieResponse>
)