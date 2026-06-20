package com.ivarvisser.cineapp.ui.feature.orderHistory

import com.ivarvisser.cineapp.data.dto.orders.response.CreateOrderResponse
import com.ivarvisser.cineapp.data.dto.orders.response.TicketResponse
import com.ivarvisser.cineapp.domain.Movie
import kotlinx.datetime.Instant

data class OrderHistoryState(
    val orders: List<OrderWithDetails> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = true
)

data class OrderWithDetails(
    val order: CreateOrderResponse,
    val movie: Movie? = null,
    val startsAt: Instant? = null,
    val tickets: List<TicketResponse> = emptyList(),
    val isExpanded: Boolean = false,
    val isLoadingTickets: Boolean = false
)
