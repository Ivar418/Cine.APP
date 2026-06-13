package com.ivarvisser.cineapp.data.dto.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String,
)
