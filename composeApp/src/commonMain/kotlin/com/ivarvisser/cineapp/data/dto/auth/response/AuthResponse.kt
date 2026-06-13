package com.ivarvisser.cineapp.data.dto.auth.response

import com.ivarvisser.cineapp.data.dto.users.response.UserResponse
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: UserResponse,
)
