package com.ivarvisser.cineapp.fakes

import com.ivarvisser.cineapp.data.dto.orders.response.TicketResponse
import com.ivarvisser.cineapp.data.repository.interfaces.TicketsRepository
import com.ivarvisser.cineapp.utils.ResultOf

class FakeTicketsRepository : TicketsRepository {
    var ticketsByOrderId = mutableMapOf<Int, List<TicketResponse>>()
    var error: String? = null

    override suspend fun getTicketsByOrderIdAsync(orderId: Int): ResultOf<List<TicketResponse>> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: ticketsByOrderId[orderId]?.let { ResultOf.Success(it) }
            ?: ResultOf.Success(emptyList())
    }
}
