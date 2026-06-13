package com.ivarvisser.cineapp.data.repository.interfaces

import com.ivarvisser.cineapp.data.dto.orders.request.CreateOrderRequest
import com.ivarvisser.cineapp.data.dto.orders.response.CreateOrderResponse
import com.ivarvisser.cineapp.domain.Order
import com.ivarvisser.cineapp.utils.ResultOf
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {
    suspend fun createOrder(order: CreateOrderRequest): ResultOf<CreateOrderResponse>
    suspend fun getReservationPdfAsync(orderId: Int): ResultOf<ByteArray>
    suspend fun getOrderById(orderId: Int): ResultOf<CreateOrderResponse>
    suspend fun observeStatus(
        orderId: Int,
    ): Flow<Order?>

}
