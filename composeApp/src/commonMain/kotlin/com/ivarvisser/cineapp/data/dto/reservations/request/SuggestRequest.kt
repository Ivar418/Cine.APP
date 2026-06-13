package com.ivarvisser.cineapp.data.dto.reservations.request

import kotlinx.serialization.Serializable

@Serializable
data class SuggestRequest(
    val showingId: Int,
    val normalCount: Int,
    val wheelchairCount: Int
)
