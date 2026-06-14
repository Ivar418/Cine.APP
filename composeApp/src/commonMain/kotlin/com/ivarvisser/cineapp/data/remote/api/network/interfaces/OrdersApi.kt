package com.ivarvisser.cineapp.data.remote.api.network.interfaces

import com.ivarvisser.cineapp.data.dto.orders.request.CreateOrderRequest
import com.ivarvisser.cineapp.data.dto.orders.response.CreateOrderResponse
import com.ivarvisser.cineapp.utils.ResultOf

interface OrdersApi {
    suspend fun createOrderAsync(request: CreateOrderRequest): ResultOf<CreateOrderResponse>
    suspend fun getReservationPdfAsync(orderId: Int): ResultOf<ByteArray>
    suspend fun getTicketsPdfAsync(orderId: Int): ResultOf<ByteArray>
    suspend fun getOrderById(orderId: Int): ResultOf<CreateOrderResponse>
    suspend fun getMyOrdersAsync(): ResultOf<List<CreateOrderResponse>>
    suspend fun downloadOrderTicketPdfAsync(orderId: Int): ResultOf<ByteArray>
}
