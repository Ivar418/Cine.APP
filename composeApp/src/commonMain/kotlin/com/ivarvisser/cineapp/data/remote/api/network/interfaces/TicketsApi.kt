package com.ivarvisser.cineapp.data.remote.api.network.interfaces

import com.ivarvisser.cineapp.data.dto.orders.response.TicketResponse
import com.ivarvisser.cineapp.utils.ResultOf

interface TicketsApi {
    suspend fun getTicketsByOrderIdAsync(orderId: Int): ResultOf<List<TicketResponse>>
}