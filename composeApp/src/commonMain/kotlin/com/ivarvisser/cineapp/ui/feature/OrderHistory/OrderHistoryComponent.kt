package com.ivarvisser.cineapp.ui.feature.OrderHistory

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import com.ivarvisser.cineapp.data.repository.interfaces.OrdersRepository
import com.ivarvisser.cineapp.data.repository.interfaces.ShowingsRepository
import com.ivarvisser.cineapp.data.repository.interfaces.TicketsRepository
import com.ivarvisser.cineapp.getPlatform
import com.ivarvisser.cineapp.utils.ResultOf
import kotlinx.coroutines.launch
import net.codinux.log.Log

class OrderHistoryComponent(
    componentContext: ComponentContext,
    private val ordersRepository: OrdersRepository,
    private val moviesRepository: MoviesRepository,
    private val showingsRepository: ShowingsRepository,
    private val ticketsRepository: TicketsRepository,
    private val onGoBack: () -> Unit
) : ComponentContext by componentContext {

    private val scope = coroutineScope()
    private val _state = MutableValue(OrderHistoryState())
    val state: Value<OrderHistoryState> = _state

    init {
        loadOrders()
    }

    fun loadOrders() {
        scope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val ordersResult = ordersRepository.getMyOrders()
            val moviesResult = moviesRepository.getMovies()

            when (ordersResult) {
                is ResultOf.Success -> {
                    val allMovies = (moviesResult as? ResultOf.Success)?.value ?: emptyList()
                    val ordersWithDetails = ordersResult.value.map { order ->
                        OrderWithDetails(order = order)
                    }
                    _state.update { it.copy(orders = ordersWithDetails, isLoading = false) }

                    // Resolve movie for each order
                    ordersWithDetails.forEach { item ->
                        resolveMovie(item.order.orderId, allMovies)
                    }
                }

                is ResultOf.Failure -> {
                    _state.update { it.copy(error = ordersResult.message, isLoading = false) }
                    Log.error(loggerName = "OrderHistoryComponent") { "Failed to load orders: ${ordersResult.message}" }
                }
            }
        }
    }

    private fun resolveMovie(orderId: Int, allMovies: List<com.ivarvisser.cineapp.domain.Movie>) {
        scope.launch {
            val orderItem =
                _state.value.orders.find { it.order.orderId == orderId } ?: return@launch
            val showingId = orderItem.order.tickets.firstOrNull()?.showingId ?: return@launch

            val showingResult = showingsRepository.getShowingById(showingId)
            if (showingResult is ResultOf.Success) {
                val movieId = showingResult.value.movieId
                val movie = allMovies.find { it.id == movieId }
                _state.update { current ->
                    current.copy(orders = current.orders.map {
                        if (it.order.orderId == orderId) it.copy(movie = movie) else it
                    })
                }
            }
        }
    }

    fun toggleOrderExpansion(orderId: Int) {
        val orderDetail = _state.value.orders.find { it.order.orderId == orderId } ?: return
        if (!orderDetail.isExpanded && orderDetail.tickets.isEmpty()) {
            loadTicketsForOrder(orderId)
        }
        _state.update { current ->
            current.copy(
                orders = current.orders.map {
                    if (it.order.orderId == orderId) it.copy(isExpanded = !it.isExpanded) else it
                }
            )
        }
    }

    private fun loadTicketsForOrder(orderId: Int) {
        scope.launch {
            _state.update { current ->
                current.copy(orders = current.orders.map {
                    if (it.order.orderId == orderId) it.copy(isLoadingTickets = true) else it
                })
            }
            when (val result = ticketsRepository.getTicketsByOrderIdAsync(orderId)) {
                is ResultOf.Success -> {
                    _state.update { current ->
                        current.copy(orders = current.orders.map {
                            if (it.order.orderId == orderId) it.copy(
                                tickets = result.value,
                                isLoadingTickets = false
                            ) else it
                        })
                    }
                }

                is ResultOf.Failure -> {
                    _state.update { current ->
                        current.copy(orders = current.orders.map {
                            if (it.order.orderId == orderId) it.copy(isLoadingTickets = false) else it
                        })
                    }
                }
            }
        }
    }

    fun downloadAndOpenOrderPdf(orderId: Int) {
        scope.launch {
            // Find order code for filename
            val order = _state.value.orders.find { it.order.orderId == orderId }?.order
            val fileName = "order-${order?.orderCode ?: orderId}.pdf"

            when (val result = ordersRepository.downloadOrderPdfAsync(orderId)) {
                is ResultOf.Success -> {
                    getPlatform().openFile(result.value, fileName)
                }

                is ResultOf.Failure -> {
                    Log.error { "Failed to download PDF: ${result.message}" }
                }
            }
        }
    }

    fun onBack() {
        onGoBack()
    }
}
