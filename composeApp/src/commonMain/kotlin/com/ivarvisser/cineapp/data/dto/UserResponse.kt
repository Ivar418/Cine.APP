package com.ivarvisser.cineapp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val userId: Int,
    val userName: String,
    val photoId: Int?,
    val photoUrl: String?,
    val firstName: String,
    val lastName: String,
    val email: String,
)
