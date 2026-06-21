package com.ivarvisser.cineapp.fakes

import com.ivarvisser.cineapp.data.dto.reservations.request.SuggestRequest
import com.ivarvisser.cineapp.data.dto.reservations.request.UpdateReservationSeatsRequest
import com.ivarvisser.cineapp.data.dto.reservations.response.SuggestResponse
import com.ivarvisser.cineapp.data.repository.interfaces.ReservationsRepository
import com.ivarvisser.cineapp.domain.Reservation
import com.ivarvisser.cineapp.utils.ResultOf

class FakeReservationsRepository : ReservationsRepository {
    var suggestResponse: SuggestResponse? = null
    var confirmedReservation: Reservation? = null
    var cancelledIds = mutableListOf<String>()
    var updatedSeatsRequests = mutableListOf<UpdateReservationSeatsRequest>()
    var error: String? = null

    override suspend fun suggest(request: SuggestRequest): ResultOf<SuggestResponse> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: suggestResponse?.let { ResultOf.Success(it) }
            ?: ResultOf.Success(
                SuggestResponse(
                    suggestionId = "fake-id",
                    seats = emptyList(),
                    found = false
                )
            )
    }

    override suspend fun confirm(suggestionId: String): ResultOf<Reservation> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: confirmedReservation?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("Not implemented in fake", null)
    }

    override suspend fun cancel(reservationId: String): ResultOf<Unit> {
        cancelledIds.add(reservationId)
        return error?.let { ResultOf.Failure(it, null) } ?: ResultOf.Success(Unit)
    }

    override suspend fun updateSeats(request: UpdateReservationSeatsRequest): ResultOf<Unit> {
        updatedSeatsRequests.add(request)
        return error?.let { ResultOf.Failure(it, null) } ?: ResultOf.Success(Unit)
    }
}
