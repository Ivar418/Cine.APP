package com.ivarvisser.cineapp.fakes

import com.ivarvisser.cineapp.data.dto.orders.request.CreateOrderRequest
import com.ivarvisser.cineapp.data.dto.orders.response.CreateOrderResponse
import com.ivarvisser.cineapp.data.repository.interfaces.OrdersRepository
import com.ivarvisser.cineapp.domain.Order
import com.ivarvisser.cineapp.utils.ResultOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeOrdersRepository : OrdersRepository {
    private val _orders = MutableStateFlow<List<CreateOrderResponse>>(emptyList())
    var ordersList: List<CreateOrderResponse>
        get() = _orders.value
        set(value) {
            _orders.value = value
        }

    var error: String? = null
    var getMyOrdersCallCount = 0

    override suspend fun createOrder(order: CreateOrderRequest): ResultOf<CreateOrderResponse> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: ResultOf.Failure("Not implemented in fake", null)
    }

    override suspend fun getReservationPdfAsync(orderId: Int): ResultOf<ByteArray> {
        return error?.let { ResultOf.Failure(it, null) } ?: ResultOf.Success(ByteArray(0))
    }

    override suspend fun getTicketsPdfAsync(orderId: Int): ResultOf<ByteArray> {
        return error?.let { ResultOf.Failure(it, null) } ?: ResultOf.Success(ByteArray(0))
    }

    override suspend fun getOrderById(orderId: Int): ResultOf<CreateOrderResponse> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: _orders.value.find { it.orderId == orderId }?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("Order not found", null)
    }

    override suspend fun getMyOrders(): ResultOf<List<CreateOrderResponse>> {
        getMyOrdersCallCount++
        return error?.let { ResultOf.Failure(it, null) } ?: ResultOf.Success(_orders.value)
    }

    override suspend fun observeStatus(orderId: Int): Flow<Order?> {
        // Simple implementation for now
        return MutableStateFlow<Order?>(null).asStateFlow()
    }

    override suspend fun downloadOrderPdfAsync(orderId: Int): ResultOf<ByteArray> {
        return error?.let { ResultOf.Failure(it, null) } ?: ResultOf.Success(ByteArray(0))
    }

    override fun observeMyOrders(delayMillis: Long?): Flow<List<CreateOrderResponse>> {
        return _orders.asStateFlow()
    }
}
