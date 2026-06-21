package com.ivarvisser.cineapp.data.dto.orders.response

import com.ivarvisser.cineapp.domain.enums.PaymentStatuses
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class TicketResponse(
    val id: Int,
    val showingId: Int,
    val movieTitle: String,
    val showDateTimeUtc: Instant,
    val seatNumber: String,
    val ticketType: String,
    val status: String,
    val paymentStatus: PaymentStatuses,
    val qrCodeGuid: String? = null,
    val qrIsActive: Boolean,
    val price: Float,
    val purchaseDateUtc: Instant
)
