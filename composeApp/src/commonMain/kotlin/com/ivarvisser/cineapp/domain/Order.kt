package com.ivarvisser.cineapp.domain

import com.ivarvisser.cineapp.data.dto.orders.response.CreatedOrderTicketResponse

class Order(
    val orderId: Int,
    val orderCode: String,
    val orderType: String,
    val paymentStatus: String,
    val paymentMethod: String,
    val totalAmount: Float,
    val tickets: List<CreatedOrderTicketResponse>
) {
}