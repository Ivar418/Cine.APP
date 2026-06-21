package com.ivarvisser.cineapp.fakes

import com.ivarvisser.cineapp.data.dto.reservations.request.SuggestRequest
import com.ivarvisser.cineapp.data.dto.reservations.request.UpdateReservationSeatsRequest
import com.ivarvisser.cineapp.data.dto.reservations.response.SuggestResponse
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.ReservationsApi
import com.ivarvisser.cineapp.domain.Reservation
import com.ivarvisser.cineapp.utils.ResultOf

class FakeReservationsApi : ReservationsApi {
    var suggestResponse: SuggestResponse? = null
    var confirmedReservation: Reservation? = null
    var error: String? = null
    var lastSuggestRequest: SuggestRequest? = null
    var lastCancelledId: String? = null
    var lastUpdateSeatsRequest: UpdateReservationSeatsRequest? = null

    override suspend fun suggest(request: SuggestRequest): ResultOf<SuggestResponse> {
        lastSuggestRequest = request
        return error?.let { ResultOf.Failure(it, null) }
            ?: suggestResponse?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("No suggestion set", null)
    }

    override suspend fun confirm(suggestionId: String): ResultOf<Reservation> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: confirmedReservation?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("No reservation set", null)
    }

    override suspend fun cancel(reservationId: String): ResultOf<Unit> {
        lastCancelledId = reservationId
        return error?.let { ResultOf.Failure(it, null) } ?: ResultOf.Success(Unit)
    }

    override suspend fun updateSeats(request: UpdateReservationSeatsRequest): ResultOf<Unit> {
        lastUpdateSeatsRequest = request
        return error?.let { ResultOf.Failure(it, null) } ?: ResultOf.Success(Unit)
    }
}
