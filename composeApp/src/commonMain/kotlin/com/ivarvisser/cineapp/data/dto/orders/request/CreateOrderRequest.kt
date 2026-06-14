package com.ivarvisser.cineapp.data.dto.orders.request

import com.ivarvisser.cineapp.domain.enums.OrderTypes
import com.ivarvisser.cineapp.domain.enums.PaymentMethods
import kotlinx.serialization.Serializable

@Serializable
data class CreateOrderRequest(
    val orderType: OrderTypes,
    val paymentMethod: PaymentMethods,
    val tickets: List<TicketRequest>,
    val userId: Int? = null
)
