package com.ivarvisser.cineapp.data.repository.implementations

import com.ivarvisser.cineapp.data.dto.orders.request.CreateOrderRequest
import com.ivarvisser.cineapp.data.dto.orders.response.CreateOrderResponse
import com.ivarvisser.cineapp.domain.enums.OrderTypes
import com.ivarvisser.cineapp.domain.enums.PaymentMethods
import com.ivarvisser.cineapp.domain.enums.PaymentStatuses
import com.ivarvisser.cineapp.fakes.FakeOrdersApi
import com.ivarvisser.cineapp.utils.ResultOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OrdersRepositoryImplTest {

    private val api = FakeOrdersApi()
    private val repository = OrdersRepositoryImpl(api)

    private fun order(id: Int) = CreateOrderResponse(
        orderId = id,
        orderCode = "CODE$id",
        orderType = OrderTypes.Online,
        paymentStatus = PaymentStatuses.Paid,
        paymentMethod = PaymentMethods.iDEAL,
        totalAmount = 10.0f,
        tickets = emptyList()
    )

    @Test
    fun createOrderReturnsApiResult() = runTest {
        api.createdOrder = order(1)
        val request = CreateOrderRequest(
            orderType = OrderTypes.Online,
            paymentMethod = PaymentMethods.iDEAL,
            tickets = emptyList()
        )

        val result = repository.createOrder(request)

        assertTrue(result is ResultOf.Success)
        assertEquals("CODE1", result.value.orderCode)
        assertEquals(request, api.lastCreateOrderRequest)
    }

    @Test
    fun getOrderByIdReturnsOrder() = runTest {
        api.ordersById[1] = order(1)

        val result = repository.getOrderById(1)

        assertTrue(result is ResultOf.Success)
        assertEquals(1, result.value.orderId)
    }

    @Test
    fun getMyOrdersReturnsList() = runTest {
        api.myOrders = mutableListOf(order(1), order(2))

        val result = repository.getMyOrders()

        assertTrue(result is ResultOf.Success)
        assertEquals(2, result.value.size)
    }

    @Test
    fun getOrderByIdPropagatesFailure() = runTest {
        api.error = "Not found"

        val result = repository.getOrderById(99)

        assertTrue(result is ResultOf.Failure)
        assertEquals("Not found", result.message)
    }
}
