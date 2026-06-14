package com.ivarvisser.cineapp.ui.feature.OrderHistory

import com.ivarvisser.cineapp.data.dto.orders.response.CreateOrderResponse
import com.ivarvisser.cineapp.data.dto.orders.response.TicketResponse
import com.ivarvisser.cineapp.domain.Movie

data class OrderHistoryState(
    val orders: List<OrderWithDetails> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class OrderWithDetails(
    val order: CreateOrderResponse,
    val movie: Movie? = null,
    val tickets: List<TicketResponse> = emptyList(),
    val isExpanded: Boolean = false,
    val isLoadingTickets: Boolean = false
)
