package com.ivarvisser.cineapp.data.dto

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class TicketRequest(
    val showingId: Int,
    val showDateInstant: Instant,
    val seatNumber: String,
    val ticketType: String,
    val price: Float
)