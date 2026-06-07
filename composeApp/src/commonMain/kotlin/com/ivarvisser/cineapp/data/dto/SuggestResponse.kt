package com.ivarvisser.cineapp.data.dto

import com.ivarvisser.cineapp.domain.Seat
import kotlinx.serialization.Serializable

@Serializable
data class SuggestResponse(
    val suggestionId: String,
    val seats: List<Seat>,
    val found: Boolean
)
