package com.ivarvisser.cineapp.data.dto.showings.response

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class ShowingsWithPricesResponse(
    val showingId: String,
    val movieTitle: String,
    val startsAt: Instant,
    val prices: ShowingPricesResponse
)
