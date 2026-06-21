package com.ivarvisser.cineapp.ui.feature.ordering

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.UriHandler
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.ivarvisser.cineapp.data.dto.orders.request.CreateOrderRequest
import com.ivarvisser.cineapp.data.dto.orders.request.TicketRequest
import com.ivarvisser.cineapp.data.dto.reservations.request.SuggestRequest
import com.ivarvisser.cineapp.data.dto.reservations.request.UpdateReservationSeatsRequest
import com.ivarvisser.cineapp.data.remote.util.NetworkConstants
import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import com.ivarvisser.cineapp.data.repository.interfaces.OrdersRepository
import com.ivarvisser.cineapp.data.repository.interfaces.ReservationsRepository
import com.ivarvisser.cineapp.data.repository.interfaces.ShowingsRepository
import com.ivarvisser.cineapp.data.repository.interfaces.UsersRepository
import com.ivarvisser.cineapp.domain.Order
import com.ivarvisser.cineapp.domain.SeatFactory.buildSeatGrid
import com.ivarvisser.cineapp.domain.enums.OrderTypes
import com.ivarvisser.cineapp.domain.enums.PaymentMethods
import com.ivarvisser.cineapp.domain.enums.PaymentStatuses
import com.ivarvisser.cineapp.domain.enums.SeatType
import com.ivarvisser.cineapp.getPlatform
import com.ivarvisser.cineapp.mapper.toOrder
import com.ivarvisser.cineapp.ui.component.openPaymentUrl
import com.ivarvisser.cineapp.utils.ResultOf
import io.ktor.http.URLBuilder
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.codinux.log.Log
import kotlin.time.Instant

class OrderingComponent(
    componentContext: ComponentContext,
    private val showingId: Int,
    private val movieId: Int,
    private val moviesRepository: MoviesRepository,
    private val showingsRepository: ShowingsRepository,
    private val ordersRepository: OrdersRepository,
    private val reservationsRepository: ReservationsRepository,
    private val usersRepository: UsersRepository,
    private val onGoBack: () -> Unit,
    private val onLogin: () -> Unit
) : ComponentContext by componentContext {

    private val scope = coroutineScope()

    private val _state = MutableValue(OrderingUiState())
    val state: Value<OrderingUiState> = _state

    private val _order = MutableStateFlow<Order?>(null)
    val order: StateFlow<Order?> = _order


    init {
        loadData()
        loadShowingPrices()
        suggestSeats()
        doOnDestroy { if (_state.value.pendingId != null && _state.value.order == null) cancelPendingReservation() }
    }

    private fun loadShowingPrices() {
        scope.launch {
            when (val result = showingsRepository.getShowingPrices(showingId)) {
                is ResultOf.Success -> {
                    val prices = result.value
                    Log.debug(loggerName = "OrderingComponent") { "Successfully loaded prices: $prices" }
                    _state.update {
                        it.copy(
                            prices = mapOf(
                                "Adult" to prices.prices.adult.price,
                                "Child" to prices.prices.child.price,
                                "Student" to prices.prices.student.price,
                                "Senior" to prices.prices.senior.price
                            )
                        )
                    }
                }

                is ResultOf.Failure -> {
                    Log.error(loggerName = "OrderingComponent") { "Failed to load prices: ${result.message}" }
                }
            }
        }
    }

    private fun loadData() {
        Log.debug(loggerName = "OrderingComponent") { "Loading data for showing $showingId and movie $movieId" }
        scope.launch {
            val isLoggedIn = usersRepository.isLoggedIn()
            _state.update { it.copy(isLoading = true, isLoggedIn = isLoggedIn) }

            val showingResult = showingsRepository.getShowingById(showingId)
            val movieResult = moviesRepository.getMovieById(movieId)
            val showingState = showingsRepository.getShowingStateById(showingId)

            if (showingResult is ResultOf.Success && movieResult is ResultOf.Success && showingState is ResultOf.Success) {
                val showing = showingResult.value
                val movie = movieResult.value
                val showingState = showingState.value

                _state.update {
                    it.copy(
                        isLoading = false, showing = ShowingUi(
                            movieTitle = movie.title,
                            startsAt = showing.startsAt.toString(), // Format this if needed
                            auditoriumName = showing.auditorium?.name ?: "Unknown"
                        ), summary = SummaryUi(
                            movieTitle = movie.title,
                            startsAt = showing.startsAt.toString()
                        ), legend = listOf(
                            LegendItemUi("Available", Color.LightGray),
                            LegendItemUi("Occupied", Color.DarkGray),
                            LegendItemUi("Selected", Color(0xFFFFC107)),
                            LegendItemUi("Wheelchair", Color(0xFF64B5F6))
                        ), seatSelection = it.seatSelection.copy(

                            auditorium = showing.auditorium,
                            allSeats = showingState.allSeats,
                            occupiedSeatKeys = showingState.occupiedKeys.toSet(),
                            grid = buildSeatGrid(showingState.allSeats)
                        )
                    )
                }
            } else {
                setError("Failed to load data")
            }
        }
    }

    private fun suggestSeats() {
        val normalCount = _state.value.seatSelection.normalCount
        val wheelchairCount = _state.value.seatSelection.wheelchairCount
        Log.debug(loggerName = "OrderingComponent") { "Suggesting seats: normal=$normalCount, wheelchair=$wheelchairCount" }
        if ((normalCount + wheelchairCount) == 0) return
        cancelPendingReservation()
        scope.launch {
            _state.update { it.copy(isBusy = true) }
            val result = reservationsRepository.suggest(
                SuggestRequest(showingId, normalCount, wheelchairCount)
            )

            when (result) {
                is ResultOf.Success -> {
                    val resp = result.value
                    Log.debug(loggerName = "OrderingComponent") { "Seat suggestion result: $resp" }
                    if (resp.found) {
                        _state.update {
                            it.copy(
                                pendingId = resp.suggestionId,
                                seatSelection = it.seatSelection.copy(suggestedSeatKeys = resp.seats.map { s -> "${s.row}-${s.col}" }
                                    .toSet()),
                                isBusy = false)
                        }
                    } else {
                        setError("Geen geschikte plekken beschikbaar.")
                    }
                }

                is ResultOf.Failure -> {
                    setError(result.message ?: "Fout bij het zoeken naar plekken")
                }
            }
        }
    }

    private fun cancelPendingReservation() {
        val pendingId = _state.value.pendingId ?: return
        Log.debug(loggerName = "OrderingComponent") { "Cancelling pending reservation: $pendingId" }
        _state.update {
            it.copy(
                pendingId = null,
                seatSelection = it.seatSelection.copy(suggestedSeatKeys = emptySet())
            )
        }
        scope.launch {
            withContext(NonCancellable) {
                when (val result = reservationsRepository.cancel(pendingId)) {
                    is ResultOf.Success -> {
                        Log.debug(loggerName = "OrderingComponent") { "Successfully cancelled reservation $pendingId" }
                    }

                    is ResultOf.Failure -> {
                        Log.error(loggerName = "OrderingComponent") { "Failed to cancel reservation: ${result.message}" }
                    }
                }
            }
        }
    }

    private fun updateReservationSeats(seatKeys: Set<String>) {
        val pendingId = _state.value.pendingId ?: return
        Log.debug(loggerName = "OrderingComponent") { "Updating reservation $pendingId with seats: $seatKeys" }
        scope.launch {
            // Filter the allSeats list to get the actual Seat objects for the keys
            val selectedSeats = _state.value.seatSelection.allSeats.filter {
                "${it.row}-${it.col}" in seatKeys
            }

            val result = reservationsRepository.updateSeats(
                UpdateReservationSeatsRequest(pendingId, selectedSeats)
            )

            if (result is ResultOf.Success) {
                _state.update { current ->
                    // Re-sync the SelectedSeatUi list used in the ticket selection step
                    val newSeats = selectedSeats.map { seat ->
                        // Try to preserve existing ticketType/price if this seat was already selected
                        current.seats.find { it.id == "${seat.row}-${seat.col}" } ?: SelectedSeatUi(
                            id = "${seat.row}-${seat.col}",
                            row = seat.row + 1,
                            seatNumber = seat.col + 1,
                            ticketType = null,
                            price = "0.00"
                        )
                    }
                    current.copy(
                        seats = newSeats,
                        seatSelection = current.seatSelection.copy(
                            suggestedSeatKeys = seatKeys
                        )
                    )
                }
                Log.debug(loggerName = "OrderingComponent") { "Successfully updated reservation seats for $pendingId" }
            }
            if (result is ResultOf.Failure) {
                Log.error(loggerName = "OrderingComponent") { "Failed to update seats: ${result.message}" }
            }
        }
    }


    fun buildPaymentUrl(
        order: Order
    ): String {
        // Simple URL encoding helper (Ktor's encodeURLParameter is not easily accessible here)
        // Use the normal website URL for redirections

        val returnUrl = URLBuilder(NetworkConstants.Endpoints.WasmVersions.PAYMENT_RESULT).apply {
            parameters.append("orderId", order.orderId.toString())
            parameters.append("showingId", showingId.toString())
            parameters.append("paymentMethod", order.paymentMethod.displayName)
        }.buildString()


        // Format amount: 9.5 -> "9,50" to match working version (comma separator, 2 decimals)
        val formattedAmount = order.totalAmount.toString().replace(".", ",")
            .let { if (!it.contains(",")) "$it,00" else if (it.substringAfter(",").length == 1) "${it}0" else it }

        Log.debug(loggerName = "OrderingComponent") { "Building payment URL with returnUrl: $returnUrl" }

        val reservationId = _state.value.confirmedReservation?.id ?: _state.value.pendingId
        if (reservationId == null) setError("Missing reservation ID")
        return URLBuilder(NetworkConstants.Endpoints.WasmVersions.PAYMENT_MOCK).apply {
            parameters.append("reference", order.orderCode)
            parameters.append("amount", formattedAmount)
            parameters.append("merchant", "CineNet-B.V.")
            parameters.append("description", "Bestelling ${order.orderCode}")
            parameters.append("returnUrl", returnUrl)
            parameters.append("ChosenPaymentType", order.paymentMethod.selector.toString())
            parameters.append("reservationId", reservationId!!)
        }.buildString()
    }

    fun setError(message: String) {
        _state.update {
            it.copy(
                errorMessage = message, isLoading = false, orderBusy = false
            )
        }
    }

    fun confirmOrder() {
        Log.debug(loggerName = "OrderingComponent") { "Confirming order" }
        scope.launch {
            _state.update { it.copy(orderBusy = true) }
            val pendingId = _state.value.pendingId
            if (pendingId.isNullOrEmpty()) {
                Log.debug(loggerName = "OrderingComponent") { "No pending ID to confirm" }
                _state.update { it.copy(orderBusy = false) }
                return@launch
            }

            val confirmedReservationResult = reservationsRepository.confirm(pendingId)
            if (confirmedReservationResult is ResultOf.Failure) {
                Log.error(loggerName = "OrderingComponent") { "Failed to confirm reservation $pendingId: ${confirmedReservationResult.message}" }
                setError(confirmedReservationResult.message ?: "Failed to confirm reservation")
                return@launch
            }
            if (confirmedReservationResult is ResultOf.Success) {
                val confirmedReservation = confirmedReservationResult.value
                Log.debug(loggerName = "OrderingComponent") { "Successfully confirmed reservation: $confirmedReservation" }
                _state.update {
                    it.copy(
                        confirmedReservation = confirmedReservation,
                        pendingId = confirmedReservation.id, // Ensure pendingId is updated to the confirmed ID
                        orderBusy = false
                    )
                }
            }

        }
    }


    fun handleReservation(order: Order) {
        scope.launch {
            when (val pdf = ordersRepository.getReservationPdfAsync(order.orderId)) {
                is ResultOf.Success -> {
                    getPlatform().openFile(pdf.value, "reservering-${order.orderCode}.pdf")
                }

                is ResultOf.Failure -> {
                    setError(pdf.message ?: "Error getting reservation PDF")
                }
            }
        }
    }

    fun createOrder(uriHandler: UriHandler) {
        Log.debug(loggerName = "OrderingComponent") { "Creating order with payment method: ${_state.value.selectedPaymentMethod}" }
        scope.launch {
            _state.update { it.copy(orderBusy = true) }

            val ticketRequest = _state.value.seats.map { seat ->
                TicketRequest(
                    showingId = showingId,
                    showDateInstant = Instant.parse(_state.value.showing?.startsAt ?: ""),
                    // Format seat number as "A1" (Row A, Seat 1) to match C# logic
                    seatNumber = "${('A' + seat.row - 1)}${seat.seatNumber}",
                    ticketType = seat.ticketType ?: "Adult",
                    price = seat.price?.toFloatOrNull() ?: 0.0F
                )
            }

            val orderType =
                if (_state.value.selectedPaymentMethod == PaymentMethods.Reservation) OrderTypes.Reservation else OrderTypes.Payment
            val request = CreateOrderRequest(
                orderType = orderType,
                paymentMethod = _state.value.selectedPaymentMethod,
                tickets = ticketRequest,
                userId = usersRepository.getUser()?.userId
            )
            Log.debug(loggerName = "OrderingComponent") { "Order request: $request" }
            val order = ordersRepository.createOrder(request)
            if (order is ResultOf.Failure) {
                Log.error(loggerName = "OrderingComponent") { "Failed to create order: ${order.message}" }
                setError(order.message ?: "Failed to create order")
                return@launch
            }
            if (order is ResultOf.Success) {
                Log.debug(loggerName = "OrderingComponent") { "Successfully created order: ${order.value.toOrder()}" }
                _state.update {
                    it.copy(
                        orderBusy = false, order = order.value.toOrder()
                    )
                }
                startOrderPolling(order.value.orderId)
                if (_state.value.selectedPaymentMethod == PaymentMethods.Reservation) {
                    handleReservation(order.value.toOrder())
                } else {
                    val url = buildPaymentUrl(order.value.toOrder())
                    Log.debug(loggerName = "OrderingComponent") { "Opening payment URL: $url" }
                    openPaymentUrl(uriHandler, url)
                    // Navigate back to home screen after opening the website

                }
            }
        }
    }

    private var pollingJob: Job? = null
    private fun startOrderPolling(orderId: Int) {
        pollingJob?.cancel()

        pollingJob = scope.launch {
            ordersRepository
                .observeStatus(orderId)
                .collect { updatedOrder ->
                    _order.value = updatedOrder

                    _state.update {
                        it.copy(order = updatedOrder)
                    }

                    if (updatedOrder?.paymentStatus != PaymentStatuses.Pending) {
                        pollingJob?.cancel()
                    }
                }
        }
    }


    fun onAction(action: OrderingAction) {
        Log.debug(loggerName = "OrderingComponent") { "Action triggered: $action" }
        when (action) {
            is OrderingAction.OnBack -> {
                if (_state.value.step <= 1) {
                    if (_state.value.order == null) {
                        cancelPendingReservation()
                    }
                    onGoBack()
                } else {
                    _state.update { it.copy(step = _state.value.step - 1) }

                }
            }

            is OrderingAction.IncreaseNormalSeats -> {
                _state.update { it.copy(seatSelection = it.seatSelection.copy(normalCount = it.seatSelection.normalCount + 1)) }
            }

            is OrderingAction.DecreaseNormalSeats -> {
                _state.update {
                    if (it.seatSelection.normalCount > 0) {
                        it.copy(seatSelection = it.seatSelection.copy(normalCount = it.seatSelection.normalCount - 1))
                    } else it
                }
            }

            is OrderingAction.IncreaseWheelchairSeats -> {
                _state.update {
                    it.copy(
                        seatSelection = it.seatSelection.copy(
                            wheelchairCount = it.seatSelection.wheelchairCount + 1
                        )
                    )
                }
                updateReservationSeats(_state.value.seatSelection.suggestedSeatKeys)
            }

            is OrderingAction.DecreaseWheelchairSeats -> {
                _state.update {
                    if (it.seatSelection.wheelchairCount > 0) {
                        it.copy(seatSelection = it.seatSelection.copy(wheelchairCount = it.seatSelection.wheelchairCount - 1))
                    } else it
                }
                updateReservationSeats(_state.value.seatSelection.suggestedSeatKeys)
            }

            is OrderingAction.SearchSeats -> {
                suggestSeats()
            }

            is OrderingAction.CancelPending -> {
                cancelPendingReservation()
            }

            is OrderingAction.ConfirmSeats -> {
                _state.update { current ->
                    val selectedSeats = current.seatSelection.allSeats.filter { seat ->
                        "${seat.row}-${seat.col}" in current.seatSelection.suggestedSeatKeys
                    }.map { seat ->
                        SelectedSeatUi(
                            id = "${seat.row}-${seat.col}",
                            row = seat.row + 1,
                            seatNumber = seat.col + 1,
                            ticketType = null,
                            price = "0.00"
                        )
                    }
                    current.copy(
                        step = 2, seats = selectedSeats
                    )
                }
            }

            is OrderingAction.TicketTypeChanged -> {
                _state.update { current ->
                    current.copy(
                        seats = current.seats.map { seat ->
                            if (seat.id == action.seatId) {
                                val price = current.prices[action.ticketType] ?: 12.50f
                                seat.copy(
                                    ticketType = action.ticketType, price = price.toString()
                                )
                            } else seat
                        })
                }
            }

            is OrderingAction.GoToOverview -> {
                val totalPrice = _state.value.seats.sumOf { it.price?.toDoubleOrNull() ?: 0.0 }
                _state.update {
                    it.copy(
                        step = 3, summary = it.summary.copy(
                            totalPrice = totalPrice.toString(),
                            movieTitle = _state.value.showing!!.movieTitle
                        )
                    )
                }
                confirmOrder()
            }

            is OrderingAction.GoToPaymentMethods -> {
                _state.update {
                    it.copy(
                        step = 4, paymentMethods = listOf(
                            PaymentMethods.iDEAL.displayName,
                            PaymentMethods.CreditCardOnline.displayName,
                            PaymentMethods.Reservation.displayName,
                            PaymentMethods.Giftcard.displayName
                        )
                    )
                }
            }

            is OrderingAction.PaymentMethodSelected -> {
                _state.update {
                    it.copy(
                        selectedPaymentMethod = action.paymentMethod, step = 5
                    )
                }
            }

            is OrderingAction.BackToPaymentMethods -> {
                _state.update { it.copy(step = 4) }
            }

            is OrderingAction.ProcessOrder -> {
                createOrder(action.uriHandler)
                _state.update { it.copy(step = 6) }
            }

            is OrderingAction.CancelCheckout -> {
                onGoBack()
            }

            is OrderingAction.SeatClicked -> {
                Log.debug(loggerName = "OrderingComponent") { "Seat clicked: ${action.seatId}" }
                val key = action.seatId

                _state.update { current ->
                    val newSuggested = if (key in current.seatSelection.suggestedSeatKeys) {
                        current.seatSelection.suggestedSeatKeys - key
                    } else {
                        current.seatSelection.suggestedSeatKeys + key
                    }

                    // Update counts based on the seat type being added/removed
                    val seat =
                        current.seatSelection.allSeats.find { "${it.row}-${it.col}" == key }
                    var newNormal = current.seatSelection.normalCount
                    var newWheelchair = current.seatSelection.wheelchairCount

                    if (seat != null) {
                        val isAdding = key in newSuggested
                        if (seat.type == SeatType.Wheelchair) {
                            newWheelchair =
                                if (isAdding) newWheelchair + 1 else (newWheelchair - 1).coerceAtLeast(
                                    0
                                )
                        } else {
                            newNormal =
                                if (isAdding) newNormal + 1 else (newNormal - 1).coerceAtLeast(0)
                        }
                    }

                    current.copy(
                        seatSelection = current.seatSelection.copy(
                            suggestedSeatKeys = newSuggested,
                            normalCount = newNormal,
                            wheelchairCount = newWheelchair
                        )
                    )
                }


                updateReservationSeats(_state.value.seatSelection.suggestedSeatKeys)
                Log.debug(loggerName = "OrderingComponent") { "Updated suggested seats: ${_state.value.seatSelection.suggestedSeatKeys}" }
            }

            is OrderingAction.Login -> {
                onLogin()
            }

            is OrderingAction.PerformLogin -> {
                scope.launch {
                    _state.update { it.copy(isBusy = true) }
                    val result = usersRepository.login(action.username, action.password)
                    when (result) {
                        is ResultOf.Success -> {
                            _state.update { it.copy(isLoggedIn = true, isBusy = false) }
                        }

                        is ResultOf.Failure -> {
                            setError(result.message ?: "Login mislukt")
                            _state.update { it.copy(isBusy = false) }
                        }
                    }
                }
            }


            else -> {}
        }
    }

}
