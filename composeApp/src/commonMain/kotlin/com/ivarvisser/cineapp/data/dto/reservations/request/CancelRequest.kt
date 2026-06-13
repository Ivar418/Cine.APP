package com.ivarvisser.cineapp.data.dto.reservations.request

import kotlinx.serialization.Serializable

@Serializable
data class CancelRequest(
    val reservationId: String
)
