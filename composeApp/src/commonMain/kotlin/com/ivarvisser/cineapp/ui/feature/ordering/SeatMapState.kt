package com.ivarvisser.cineapp.ui.feature.ordering

import com.ivarvisser.cineapp.domain.Auditorium
import com.ivarvisser.cineapp.domain.Seat

data class SeatMapState(
    val auditorium: Auditorium,
    val seats: List<Seat>,
    val occupied: Set<String> = emptySet(),
    val suggested: Set<String> = emptySet()
) {
    fun seatKey(seat: Seat) = "${seat.row}-${seat.col}"
}