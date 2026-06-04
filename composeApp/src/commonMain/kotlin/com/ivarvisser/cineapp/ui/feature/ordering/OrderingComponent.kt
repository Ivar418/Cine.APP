package com.ivarvisser.cineapp.ui.feature.ordering

import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import com.ivarvisser.cineapp.data.repository.interfaces.ShowingsRepository
import com.ivarvisser.cineapp.domain.ENUM.SeatType
import com.ivarvisser.cineapp.domain.Seat
import com.ivarvisser.cineapp.domain.SeatFactory.buildSeatGrid
import com.ivarvisser.cineapp.utils.ResultOf
import kotlinx.coroutines.launch
import net.codinux.log.Log

class OrderingComponent(
    componentContext: ComponentContext,
    private val showingId: Int,
    private val movieId: Int,
    private val moviesRepository: MoviesRepository,
    private val showingsRepository: ShowingsRepository,
    private val onGoBack: () -> Unit
) : ComponentContext by componentContext {

    private val scope = coroutineScope()

    private val _state = MutableValue(OrderingUiState())
    val state: Value<OrderingUiState> = _state

    init {
        loadData()
    }

    private fun loadData() {
        scope.launch {
            _state.update { it.copy(isLoading = true) }

            val showingResult = showingsRepository.getShowingById(showingId)
            val movieResult = moviesRepository.getMovieById(movieId)
            val showingState = showingsRepository.getShowingStateById(showingId)

            if (showingResult is ResultOf.Success && movieResult is ResultOf.Success && showingState is ResultOf.Success) {
                val showing = showingResult.value
                val movie = movieResult.value
                val showingState = showingState.value

                _state.update {
                    it.copy(
                        isLoading = false,
                        showing = ShowingUi(
                            movieTitle = movie.title ?: "Unknown",
                            startsAt = showing.startsAt.toString(), // Format this if needed
                            auditoriumName = showing.auditorium?.name ?: "Unknown"
                        ),
                        summary = SummaryUi(
                            movieTitle = movie.title ?: "Unknown",
                            startsAt = showing.startsAt.toString()
                        ),
                        legend = listOf(
                            LegendItemUi("Available", Color.LightGray),
                            LegendItemUi("Occupied", Color.DarkGray),
                            LegendItemUi("Selected", Color(0xFFFFC107)),
                            LegendItemUi("Wheelchair", Color(0xFF64B5F6))
                        ),
                        seatSelection = it.seatSelection.copy(

                            auditorium = showing.auditorium,
                            allSeats = showingState.allSeats,
                            occupiedSeatKeys = showingState.occupiedKeys.toSet(),
                            grid = buildSeatGrid(showingState.allSeats)
                        )
                    )
                }
            } else {
                _state.update { it.copy(isLoading = false, errorMessage = "Failed to load data") }
            }
        }
    }

    fun loadOccupiedSeats(allSeats: List<Seat>) {
        scope.launch {
            _state.value.seatSelection.allSeats.forEach { seat ->


            }
        }

    }

    fun onAction(action: OrderingAction) {
        when (action) {
            is OrderingAction.OnBack -> {
                _state.update { it.copy(step = _state.value.step - 1) }
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
                _state.update { it.copy(seatSelection = it.seatSelection.copy(wheelchairCount = it.seatSelection.wheelchairCount + 1)) }
            }

            is OrderingAction.DecreaseWheelchairSeats -> {
                _state.update {
                    if (it.seatSelection.wheelchairCount > 0) {
                        it.copy(seatSelection = it.seatSelection.copy(wheelchairCount = it.seatSelection.wheelchairCount - 1))
                    } else it
                }
            }

            is OrderingAction.SearchSeats -> {
                _state.update { current ->
                    val totalNeeded =
                        current.seatSelection.normalCount + current.seatSelection.wheelchairCount
                    if (totalNeeded == 0) return@update current

                    // Very simple auto-selection logic: take the first N available seats
                    val newSuggested = mutableSetOf<String>()
                    var normalToFind = current.seatSelection.normalCount
                    var wcToFind = current.seatSelection.wheelchairCount

                    for (seat in current.seatSelection.allSeats) {
                        val key = "${seat.row}-${seat.col}"
                        if (key in current.seatSelection.occupiedSeatKeys) continue

                        if (seat.type == SeatType.Wheelchair && wcToFind > 0) {
                            newSuggested.add(key)
                            wcToFind--
                        } else if (seat.type == SeatType.Normal && normalToFind > 0) {
                            newSuggested.add(key)
                            normalToFind--
                        }

                        if (normalToFind == 0 && wcToFind == 0) break
                    }

                    current.copy(
                        seatSelection = current.seatSelection.copy(
                            suggestedSeatKeys = newSuggested
                        ),
                        pendingId = if (newSuggested.isNotEmpty()) "pending_id" else null
                    )
                }
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
                        step = 2,
                        seats = selectedSeats
                    )
                }
            }

            is OrderingAction.TicketTypeChanged -> {
                _state.update { current ->
                    current.copy(
                        seats = current.seats.map { seat ->
                            if (seat.id == action.seatId) {
                                seat.copy(
                                    ticketType = action.ticketType,
                                    price = when (action.ticketType) {
                                        "Student" -> "10.00"
                                        "Child" -> "8.50"
                                        "Senior" -> "9.00"
                                        else -> "12.50"
                                    }
                                )
                            } else seat
                        }
                    )
                }
            }

            is OrderingAction.GoToOverview -> {
                val totalPirce = _state.value.seats.sumOf { it.price?.toDoubleOrNull() ?: 0.0 }
                _state.update {
                    it.copy(
                        step = 3,
                        summary = it.summary.copy(
                            totalPrice = totalPirce.toString(),
                            movieTitle = _state.value.showing!!.movieTitle
                        )
                    )
                }

            }

            is OrderingAction.GoToPaymentMethods -> {
                _state.update {
                    it.copy(
                        step = 4,
                        paymentMethods = listOf(
                            "iDeal",
                            "Credit Card Online",
                            "Reserveren",
                            "Cadeaubon"
                        )
                    )
                }
            }

            is OrderingAction.PaymentMethodSelected -> {
                _state.update { it.copy(selectedPaymentMethod = action.paymentMethod, step = 5) }
            }

            is OrderingAction.BackToPaymentMethods -> {
                _state.update { it.copy(step = 4) }
            }

            is OrderingAction.ProcessOrder -> {
                // Implement order processing
            }

            is OrderingAction.CancelCheckout -> {
                onGoBack()
            }

            is OrderingAction.SeatClicked -> {
                Log.debug(loggerName = "OrderingComponent") { "Seat clicked: ${action.seatId}" }
                Log.debug(loggerName = "OrderingComponent") { "current seats: ${_state.value.seatSelection.suggestedSeatKeys}}" }

                _state.update { current ->
                    val key = action.seatId
                    current.copy(
                        seatSelection = current.seatSelection.copy(
                            suggestedSeatKeys = if (key in current.seatSelection.suggestedSeatKeys) {
                                current.seatSelection.suggestedSeatKeys - key
                            } else {
                                current.seatSelection.suggestedSeatKeys + key
                            }
                        )
                    )
                }
                Log.debug(loggerName = "OrderingComponent") { "Updated suggested seats: ${_state.value.seatSelection.suggestedSeatKeys}" }
            }

            else -> {}
        }
    }
}
