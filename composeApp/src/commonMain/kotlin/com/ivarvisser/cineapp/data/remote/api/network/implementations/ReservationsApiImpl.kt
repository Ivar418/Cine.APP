package com.ivarvisser.cineapp.data.remote.api.network.implementations

import com.ivarvisser.cineapp.data.dto.CancelRequest
import com.ivarvisser.cineapp.data.dto.ConfirmRequest
import com.ivarvisser.cineapp.data.dto.SuggestRequest
import com.ivarvisser.cineapp.data.dto.SuggestResponse
import com.ivarvisser.cineapp.data.dto.UpdateReservationSeatsRequest
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.ReservationsApi
import com.ivarvisser.cineapp.data.remote.util.safeApiCall
import com.ivarvisser.cineapp.domain.Reservation
import com.ivarvisser.cineapp.utils.ResultOf
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import net.codinux.log.Log

class ReservationsApiImpl(
    private val client: HttpClient
) : ReservationsApi {
    override suspend fun suggest(request: SuggestRequest): ResultOf<SuggestResponse> = safeApiCall {
        Log.debug(loggerName = "ReservationsApiImpl") { "Requesting seat suggestion: $request" }
        // Endpoint: POST /api/reservations/suggest
        val result = client.post("/api/reservations/suggest") {
            contentType(ContentType.Application.Json)
            setBody(request)


        }.body<SuggestResponse>()
        Log.debug(loggerName = "ReservationsApiImpl") { "Seat suggestion response: $result" }
        result
    }

    override suspend fun confirm(suggestionId: String): ResultOf<Reservation> = safeApiCall {
        Log.debug(loggerName = "ReservationsApiImpl") { "Confirming reservation: $suggestionId" }
        // Endpoint: POST /api/reservations/confirm (Request body: ConfirmRequest(suggestionId))
        val result = client.post("/api/reservations/confirm") {
            contentType(ContentType.Application.Json)
            setBody(ConfirmRequest(suggestionId))
        }.body<Reservation>()
        Log.debug(loggerName = "ReservationsApiImpl") { "Reservation confirmation response: $result" }
        result
    }

    override suspend fun cancel(reservationId: String): ResultOf<Unit> = safeApiCall {
        Log.debug(loggerName = "ReservationsApiImpl") { "Cancelling reservation: $reservationId" }
        // Endpoint: POST /api/reservations/cancel (Request body: CancelRequest(reservationId))
        client.post("/api/reservations/cancel") {
            contentType(ContentType.Application.Json)
            setBody(CancelRequest(reservationId))
        }.body<Unit>()
        Log.debug(loggerName = "ReservationsApiImpl") { "Reservation cancellation successful" }
    }

    override suspend fun updateSeats(request: UpdateReservationSeatsRequest): ResultOf<Unit> =
        safeApiCall {
            Log.debug(loggerName = "ReservationsApiImpl") { "Updating reservation seats: $request" }
            // Endpoint: POST /api/reservations/update-seats
            val result = client.post("/api/reservations/update-seats") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body<Unit>()
            Log.debug(loggerName = "ReservationsApiImpl") { "Reservation seats update successful" }
        }
}
