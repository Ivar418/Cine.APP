package com.ivarvisser.cineapp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreatedOrderTicketResponse(
    val ticketId: Int,
    val showingId: Int,
    val seatNumber: String,
    val ticketType: String,
    val price: Float,
    val paymentStatus: String,
    val ticketCode: String?
)
