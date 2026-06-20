package com.ivarvisser.cineapp.ui.feature.orderHistory

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnResume
import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import com.ivarvisser.cineapp.data.repository.interfaces.OrdersRepository
import com.ivarvisser.cineapp.data.repository.interfaces.ShowingsRepository
import com.ivarvisser.cineapp.data.repository.interfaces.TicketsRepository
import com.ivarvisser.cineapp.data.repository.interfaces.UsersRepository
import com.ivarvisser.cineapp.domain.Movie
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
    private val usersRepository: UsersRepository,
    private val onGoBack: () -> Unit,
    private val onNavigateToLogin: () -> Unit
) : ComponentContext by componentContext {

    private val scope = coroutineScope()
    private val _state = MutableValue(OrderHistoryState())
    val state: Value<OrderHistoryState> = _state

    init {
        checkLoginStatus()
        doOnResume {
            checkLoginStatus()
        }
    }

    private fun checkLoginStatus() {
        scope.launch {
            val loggedIn = usersRepository.isLoggedIn()
            _state.update { it.copy(isLoggedIn = loggedIn) }
            if (loggedIn) {
                loadOrders()
            }
        }
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

    private fun resolveMovie(orderId: Int, allMovies: List<Movie>) {
        scope.launch {
            val orderItem =
                _state.value.orders.find { it.order.orderId == orderId } ?: return@launch

            // The API doesn't always fill the tickets in the order response, so we try to get them manually
            // if they are missing to find the showingId and eventually the movie.
            val tickets = if (orderItem.order.tickets.isEmpty()) {
                val ticketsResult = ticketsRepository.getTicketsByOrderIdAsync(orderId)
                if (ticketsResult is ResultOf.Success) {
                    // Update tickets in state for this order so they are already loaded when expanded
                    _state.update { current ->
                        current.copy(orders = current.orders.map {
                            if (it.order.orderId == orderId) it.copy(tickets = ticketsResult.value) else it
                        })
                    }
                    ticketsResult.value.map { it.showingId }
                } else {
                    emptyList()
                }
            } else {
                orderItem.order.tickets.map { it.showingId }
            }

            val showingId = tickets.firstOrNull() ?: return@launch

            val showingResult = showingsRepository.getShowingById(showingId)
            if (showingResult is ResultOf.Success) {
                val showing = showingResult.value
                val movieId = showing.movieId
                var movie = allMovies.find { it.id == movieId }

                if (movie == null) {
                    val movieResult = moviesRepository.getMovieById(movieId)
                    if (movieResult is ResultOf.Success) {
                        movie = movieResult.value
                    }
                }

                _state.update { current ->
                    current.copy(orders = current.orders.map {
                        if (it.order.orderId == orderId) it.copy(
                            movie = movie,
                            startsAt = showing.startsAt
                        ) else it
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

    fun onLogin() {
        onNavigateToLogin()
    }
}
