package com.ivarvisser.cineapp.mapper

import com.ivarvisser.cineapp.data.dto.CreateOrderResponse
import com.ivarvisser.cineapp.domain.Order

fun CreateOrderResponse.toOrder(): Order = Order(
    orderId = orderId,
    orderCode = orderCode,
    orderType = orderType,
    paymentStatus = paymentStatus,
    paymentMethod = paymentMethod,
    totalAmount = totalAmount,
    tickets = tickets
)

