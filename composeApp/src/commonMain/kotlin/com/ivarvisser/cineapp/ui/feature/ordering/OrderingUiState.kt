package com.ivarvisser.cineapp.ui.feature.ordering

import androidx.compose.ui.graphics.Color
import com.ivarvisser.cineapp.domain.Auditorium
import com.ivarvisser.cineapp.domain.Order
import com.ivarvisser.cineapp.domain.Reservation
import com.ivarvisser.cineapp.domain.Seat
import com.ivarvisser.cineapp.domain.SeatRow

data class OrderingUiState(
    val step: Int = 1,
    val isLoading: Boolean = false,
    val isBusy: Boolean = false,
    val orderBusy: Boolean = false,
    val errorMessage: String? = null,
    val showing: ShowingUi? = null,
    val summary: SummaryUi = SummaryUi(),
    val seatSelection: SeatSelectionUi = SeatSelectionUi(),
    val seats: List<SelectedSeatUi> = emptyList(),
    val paymentMethods: List<String> = emptyList(),
    val selectedPaymentMethod: String? = null,
    val legend: List<LegendItemUi> = emptyList(),
    val pendingId: String? = null,
    val showZones: Boolean = true,
    val confirmedReservation: Reservation? = null,
    val prices: Map<String, Float> = emptyMap(),
    val order: Order? = null,
    val isLoggedIn: Boolean = false
)

data class ShowingUi(
    val movieTitle: String,
    val startsAt: String,
    val auditoriumName: String,
)

data class SummaryUi(
    val movieTitle: String = "",
    val startsAt: String = "",
    val totalPrice: String = "0.00"
)

data class SeatSelectionUi(
    val normalCount: Int = 1,
    val wheelchairCount: Int = 0,

    val auditorium: Auditorium? = null,
    val allSeats: List<Seat> = emptyList(),
    val occupiedSeatKeys: Set<String> = emptySet(),
    val suggestedSeatKeys: Set<String> = emptySet(),
    val grid: List<SeatRow> = emptyList()
)

data class SelectedSeatUi(
    val id: String,
    val row: Int,
    val seatNumber: Int,
    val ticketType: String?,
    val price: String?
)

data class LegendItemUi(
    val label: String,
    val color: Color
)
