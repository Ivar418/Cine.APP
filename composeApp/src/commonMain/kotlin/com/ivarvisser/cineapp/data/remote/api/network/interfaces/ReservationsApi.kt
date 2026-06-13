package com.ivarvisser.cineapp.data.remote.api.network.interfaces

import com.ivarvisser.cineapp.data.dto.SuggestRequest
import com.ivarvisser.cineapp.data.dto.SuggestResponse
import com.ivarvisser.cineapp.data.dto.UpdateReservationSeatsRequest
import com.ivarvisser.cineapp.domain.Reservation
import com.ivarvisser.cineapp.utils.ResultOf

interface ReservationsApi {
    suspend fun suggest(request: SuggestRequest): ResultOf<SuggestResponse>
    suspend fun confirm(suggestionId: String): ResultOf<Reservation>
    suspend fun cancel(reservationId: String): ResultOf<Unit>
    suspend fun updateSeats(request: UpdateReservationSeatsRequest): ResultOf<Unit>
}
