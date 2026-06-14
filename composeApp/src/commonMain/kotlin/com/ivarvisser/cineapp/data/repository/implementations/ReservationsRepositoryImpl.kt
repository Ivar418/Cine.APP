package com.ivarvisser.cineapp.data.repository.implementations

import com.ivarvisser.cineapp.data.dto.reservations.request.SuggestRequest
import com.ivarvisser.cineapp.data.dto.reservations.request.UpdateReservationSeatsRequest
import com.ivarvisser.cineapp.data.dto.reservations.response.SuggestResponse
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.ReservationsApi
import com.ivarvisser.cineapp.data.repository.interfaces.ReservationsRepository
import com.ivarvisser.cineapp.domain.Reservation
import com.ivarvisser.cineapp.utils.ResultOf
import net.codinux.log.Log

class ReservationsRepositoryImpl(
    private val reservationsApi: ReservationsApi
) : ReservationsRepository {
    override suspend fun suggest(request: SuggestRequest): ResultOf<SuggestResponse> {
        Log.debug(loggerName = "ReservationsRepositoryImpl") { "Suggesting seats: $request" }
        return reservationsApi.suggest(request)
    }

    override suspend fun confirm(suggestionId: String): ResultOf<Reservation> {
        Log.debug(loggerName = "ReservationsRepositoryImpl") { "Confirming reservation: $suggestionId" }
        return reservationsApi.confirm(suggestionId)
    }

    override suspend fun cancel(reservationId: String): ResultOf<Unit> {
        Log.debug(loggerName = "ReservationsRepositoryImpl") { "Cancelling reservation: $reservationId" }
        return reservationsApi.cancel(reservationId)
    }

    override suspend fun updateSeats(request: UpdateReservationSeatsRequest): ResultOf<Unit> {
        Log.debug(loggerName = "ReservationsRepositoryImpl") { "Updating seats: $request" }
        return reservationsApi.updateSeats(request)
    }
}
