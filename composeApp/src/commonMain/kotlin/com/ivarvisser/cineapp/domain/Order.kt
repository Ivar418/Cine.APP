package com.ivarvisser.cineapp.domain

import com.ivarvisser.cineapp.data.dto.orders.response.CreatedOrderTicketResponse
import com.ivarvisser.cineapp.domain.enums.OrderTypes
import com.ivarvisser.cineapp.domain.enums.PaymentMethods
import com.ivarvisser.cineapp.domain.enums.PaymentStatuses

class Order(
    val orderId: Int,
    val orderCode: String,
    val orderType: OrderTypes,
    val paymentStatus: PaymentStatuses,
    val paymentMethod: PaymentMethods,
    val totalAmount: Float,
    val tickets: List<CreatedOrderTicketResponse>,
    val userId: Int?
) {
}