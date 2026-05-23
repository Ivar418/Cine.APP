package com.ivarvisser.cineapp.ui.feature.ordering

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.ivarvisser.cineapp.domain.Auditorium
import com.ivarvisser.cineapp.domain.Seat
import com.ivarvisser.cineapp.domain.SeatFactory

class SeatMapComponent(
    componentContext: ComponentContext,
    auditorium: Auditorium,
    allSeats: List<Seat>,
    occupiedKeys: Set<String>,
    suggestedKeys: Set<String>,
    showZones: Boolean = true,
    onSeatClick: (Seat) -> Unit
) : ComponentContext by componentContext {


    private val scope = coroutineScope()

    private val _state = MutableValue(
        SeatMapState(
            auditorium = auditorium,
            seats = SeatFactory.createSeats(auditorium)
        )
    )
    val state: Value<SeatMapState> = _state


    fun onSeatClick(seat: Seat) {
        val key = "${seat.row}-${seat.col}"
        _state.update { current ->
            current.copy(
                suggested = if (key in _state.value.suggested) {
                    _state.value.suggested - key
                } else {
                    _state.value.suggested + key
                }
            )
        }
    }
}