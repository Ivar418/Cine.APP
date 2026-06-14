package com.ivarvisser.cineapp.data.repository.interfaces

import com.ivarvisser.cineapp.data.dto.orders.response.TicketResponse
import com.ivarvisser.cineapp.utils.ResultOf

interface TicketsRepository {
    suspend fun getTicketsByOrderIdAsync(orderId: Int): ResultOf<List<TicketResponse>>
}