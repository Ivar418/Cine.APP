package com.ivarvisser.cineapp.domain

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: Int,
    val userName: String,
    val photoId: Int?,
    val photoUrl: String?,
    val firstName: String,
    val lastName: String,
    val email: String,
)