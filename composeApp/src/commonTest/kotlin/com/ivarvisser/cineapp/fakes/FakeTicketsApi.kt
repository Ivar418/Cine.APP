package com.ivarvisser.cineapp.fakes

import com.ivarvisser.cineapp.data.dto.orders.response.TicketResponse
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.TicketsApi
import com.ivarvisser.cineapp.utils.ResultOf

class FakeTicketsApi : TicketsApi {
    var ticketsByOrderId = mutableMapOf<Int, List<TicketResponse>>()
    var error: String? = null

    override suspend fun getTicketsByOrderIdAsync(orderId: Int): ResultOf<List<TicketResponse>> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: ResultOf.Success(ticketsByOrderId[orderId] ?: emptyList())
    }
}
