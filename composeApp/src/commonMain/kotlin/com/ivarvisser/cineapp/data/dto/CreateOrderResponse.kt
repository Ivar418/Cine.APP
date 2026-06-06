package com.ivarvisser.cineapp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderResponse(
    val orderId: Int,
    val orderCode: String,
    val orderType: String,
    val paymentStatus: String,
    val paymentMethod: String,
    val totalAmount: Float,
    val tickets: List<CreatedOrderTicketResponse>
)
