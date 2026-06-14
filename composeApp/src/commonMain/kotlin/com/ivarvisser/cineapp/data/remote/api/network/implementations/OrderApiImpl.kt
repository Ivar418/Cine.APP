package com.ivarvisser.cineapp.data.remote.api.network.implementations

import com.ivarvisser.cineapp.data.dto.orders.request.CreateOrderRequest
import com.ivarvisser.cineapp.data.dto.orders.response.CreateOrderResponse
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.OrdersApi
import com.ivarvisser.cineapp.data.remote.util.NetworkConstants
import com.ivarvisser.cineapp.data.remote.util.safeApiCall
import com.ivarvisser.cineapp.utils.ResultOf
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import net.codinux.log.Log

class OrderApiImpl(
    private val client: HttpClient
) : OrdersApi {

    override suspend fun createOrderAsync(request: CreateOrderRequest) = safeApiCall {
        val result = client.post(NetworkConstants.Endpoints.ORDERS)
        {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
            .body<CreateOrderResponse>()
        Log.debug(loggerName = "OrderApiImpl") { "Debug: Created Order: $result." }
        result
    }

    override suspend fun getReservationPdfAsync(orderId: Int): ResultOf<ByteArray> = safeApiCall {
        Log.debug(loggerName = "OrderApiImpl") { "Fetching reservation PDF for order: $orderId" }
        // Endpoint: GET /api/orders/{orderId}/reservation-pdf
        val result = client.get(
            "${NetworkConstants.Endpoints.ORDERS}/${orderId}/reservation-pdf"
        ).body<ByteArray>()
        Log.debug(loggerName = "OrderApiImpl") { "Successfully fetched reservation PDF for order: $orderId (${result.size} bytes)" }
        result
    }

    override suspend fun getTicketsPdfAsync(orderId: Int): ResultOf<ByteArray> = safeApiCall {
        Log.debug(loggerName = "OrderApiImpl") { "Fetching tickets PDF for order: $orderId" }
        // Endpoint: GET /api/orders/{orderId}/tickets-pdf
        val result = client.get(
            "${NetworkConstants.Endpoints.ORDERS}/${orderId}/tickets-pdf"
        ).body<ByteArray>()
        Log.debug(loggerName = "OrderApiImpl") { "Successfully fetched tickets PDF for order: $orderId (${result.size} bytes)" }
        result
    }

    override suspend fun getOrderById(orderId: Int): ResultOf<CreateOrderResponse> = safeApiCall {
        Log.debug(loggerName = "OrderApiImpl") { "Fetching order details for order ID: $orderId" }
        // Endpoint: GET /api/orders/{orderId}
        val result = client.get("${NetworkConstants.Endpoints.ORDERS}/${orderId}")
            .body<CreateOrderResponse>()
        Log.debug { "Successfully fetched order details for order ID: $orderId: $result" }
        result
    }

    override suspend fun getMyOrdersAsync(): ResultOf<List<CreateOrderResponse>> = safeApiCall {
        Log.debug(loggerName = "OrderApiImpl") { "Fetching my orders" }
        // Endpoint: GET /api/orders/me
        val result = client.get("${NetworkConstants.Endpoints.ORDERS}/me")
            .body<List<CreateOrderResponse>>()
        Log.debug(loggerName = "OrderApiImpl") { "Successfully fetched ${result.size} orders" }
        result
    }


    override suspend fun downloadOrderTicketPdfAsync(orderId: Int): ResultOf<ByteArray> =
        safeApiCall {
            Log.debug(loggerName = "OrderApiImpl") { "Downloading PDF for order ID: $orderId" }
            // Endpoint: GET /api/orders/me/{orderId}/download
            val result = client.get("${NetworkConstants.Endpoints.ORDERS}/me/${orderId}/download")
                .body<ByteArray>()
            result
        }
}
