package com.ivarvisser.cineapp.data.dto


data class UserFavoriteMoviesListResponse(
    val userId: Int,
    val favoriteMovies: List<UserFavoriteMovieResponse>
)