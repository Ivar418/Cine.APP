package com.ivarvisser.cineapp.data.repository.implementations

import com.ivarvisser.cineapp.data.dto.orders.request.CreateOrderRequest
import com.ivarvisser.cineapp.data.dto.orders.response.CreateOrderResponse
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.OrdersApi
import com.ivarvisser.cineapp.data.repository.interfaces.OrdersRepository
import com.ivarvisser.cineapp.domain.Order
import com.ivarvisser.cineapp.mapper.toOrder
import com.ivarvisser.cineapp.utils.ResultOf
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

class OrdersRepositoryImpl(
    private val ordersApi: OrdersApi
) : OrdersRepository {
    override suspend fun createOrder(order: CreateOrderRequest): ResultOf<CreateOrderResponse> {
        return ordersApi.createOrderAsync(order)
    }

    override suspend fun getReservationPdfAsync(orderId: Int): ResultOf<ByteArray> {
        return ordersApi.getReservationPdfAsync(orderId)
    }

    override suspend fun getOrderById(orderId: Int): ResultOf<CreateOrderResponse> {
        return ordersApi.getOrderById(orderId)
    }

    override suspend fun observeStatus(
        orderId: Int,
    ): Flow<Order?> = flow {
        val intervalMs: Long = 5_000
        var previous: Order? = null

        while (currentCoroutineContext().isActive) {
            try {
                val result = ordersApi.getOrderById(orderId)
                val current = when (result) {
                    is ResultOf.Failure -> {
                        null
                    }

                    is ResultOf.Success -> result.value.toOrder()
                }

                if (current != previous) {
                    emit(current)
                    previous = current
                }
            } catch (e: Exception) {
                // Handle errors as needed
            }

            delay(intervalMs)
        }
    }.distinctUntilChanged()
}
