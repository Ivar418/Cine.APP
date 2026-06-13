package com.ivarvisser.cineapp.data.dto.orders.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderRequest(
    val orderType: String,
    val paymentMethod: String,
    val tickets: List<TicketRequest>,
    val userId: Int
)
