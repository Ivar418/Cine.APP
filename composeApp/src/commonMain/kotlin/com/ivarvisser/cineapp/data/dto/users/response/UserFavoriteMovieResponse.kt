package com.ivarvisser.cineapp.data.dto.users.response

import kotlinx.serialization.Serializable

@Serializable
data class UserFavoriteMovieResponse(
    val id: Int,
    val userId: Int,
    val movieId: Int
)