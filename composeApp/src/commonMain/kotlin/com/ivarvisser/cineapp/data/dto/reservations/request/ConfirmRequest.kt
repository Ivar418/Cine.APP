package com.ivarvisser.cineapp.data.dto.reservations.request

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmRequest(
    val suggestionId: String
)
