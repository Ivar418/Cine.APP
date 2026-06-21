package com.ivarvisser.cineapp.data.dto.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(
    val refreshToken: String
)