package com.ivarvisser.cineapp.data.repository.implementations

import com.ivarvisser.cineapp.data.dto.orders.response.TicketResponse
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.TicketsApi
import com.ivarvisser.cineapp.data.repository.interfaces.TicketsRepository
import com.ivarvisser.cineapp.utils.ResultOf

class TicketsRepositoryImpl(
    private val ticketsApi: TicketsApi
) : TicketsRepository {
    override suspend fun getTicketsByOrderIdAsync(orderId: Int): ResultOf<List<TicketResponse>> {
        return ticketsApi.getTicketsByOrderIdAsync(orderId)
    }
}