package com.ivarvisser.cineapp.data.remote.api.network.implementations

import com.ivarvisser.cineapp.data.dto.orders.response.TicketResponse
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.TicketsApi
import com.ivarvisser.cineapp.data.remote.util.NetworkConstants
import com.ivarvisser.cineapp.data.remote.util.safeApiCall
import com.ivarvisser.cineapp.utils.ResultOf
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import net.codinux.log.Log

class TicketsApiImpl(private val client: HttpClient) : TicketsApi {
    override suspend fun getTicketsByOrderIdAsync(orderId: Int): ResultOf<List<TicketResponse>> =
        safeApiCall {
            Log.debug(loggerName = "OrderApiImpl") { "Fetching tickets for order ID: $orderId" }
            // Endpoint: GET /api/orders/me/{orderId}
            val result = client.get("${NetworkConstants.Endpoints.TICKETS}/me/${orderId}")
                .body<List<TicketResponse>>()
            Log.debug(loggerName = "OrderApiImpl") { "Successfully fetched ${result.size} tickets for order $orderId" }
            result
        }
}