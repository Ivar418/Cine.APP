package com.ivarvisser.cineapp.fakes

import com.ivarvisser.cineapp.data.dto.orders.request.CreateOrderRequest
import com.ivarvisser.cineapp.data.dto.orders.response.CreateOrderResponse
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.OrdersApi
import com.ivarvisser.cineapp.utils.ResultOf

class FakeOrdersApi : OrdersApi {
    var ordersById = mutableMapOf<Int, CreateOrderResponse>()
    var myOrders = mutableListOf<CreateOrderResponse>()
    var createdOrder: CreateOrderResponse? = null
    var lastCreateOrderRequest: CreateOrderRequest? = null
    var error: String? = null

    override suspend fun createOrderAsync(request: CreateOrderRequest): ResultOf<CreateOrderResponse> {
        lastCreateOrderRequest = request
        return error?.let { ResultOf.Failure(it, null) }
            ?: createdOrder?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("No order set", null)
    }

    override suspend fun getReservationPdfAsync(orderId: Int): ResultOf<ByteArray> {
        return error?.let { ResultOf.Failure(it, null) } ?: ResultOf.Success(ByteArray(0))
    }

    override suspend fun getTicketsPdfAsync(orderId: Int): ResultOf<ByteArray> {
        return error?.let { ResultOf.Failure(it, null) } ?: ResultOf.Success(ByteArray(0))
    }

    override suspend fun getOrderById(orderId: Int): ResultOf<CreateOrderResponse> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: ordersById[orderId]?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("Order not found", null)
    }

    override suspend fun getMyOrdersAsync(): ResultOf<List<CreateOrderResponse>> {
        return error?.let { ResultOf.Failure(it, null) } ?: ResultOf.Success(myOrders)
    }

    override suspend fun downloadOrderTicketPdfAsync(orderId: Int): ResultOf<ByteArray> {
        return error?.let { ResultOf.Failure(it, null) } ?: ResultOf.Success(ByteArray(0))
    }
}
