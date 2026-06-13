package com.ivarvisser.cineapp.data.dto.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class LogoutRequest(
    val refreshToken: String,
)
