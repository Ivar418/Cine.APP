package com.ivarvisser.cineapp.data.repository.implementations

import com.ivarvisser.cineapp.data.dto.reservations.request.SuggestRequest
import com.ivarvisser.cineapp.data.dto.reservations.request.UpdateReservationSeatsRequest
import com.ivarvisser.cineapp.data.dto.reservations.response.SuggestResponse
import com.ivarvisser.cineapp.domain.Reservation
import com.ivarvisser.cineapp.fakes.FakeReservationsApi
import com.ivarvisser.cineapp.utils.ResultOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ReservationsRepositoryImplTest {

    private val api = FakeReservationsApi()
    private val repository = ReservationsRepositoryImpl(api)

    @Test
    fun suggestReturnsApiResult() = runTest {
        api.suggestResponse =
            SuggestResponse(suggestionId = "abc", seats = emptyList(), found = true)

        val result =
            repository.suggest(SuggestRequest(showingId = 1, normalCount = 2, wheelchairCount = 0))

        assertTrue(result is ResultOf.Success)
        assertEquals("abc", result.value.suggestionId)
        assertEquals(1, api.lastSuggestRequest?.showingId)
    }

    @Test
    fun confirmReturnsReservation() = runTest {
        api.confirmedReservation = Reservation(id = "r1", showingId = "1", status = "Confirmed")

        val result = repository.confirm("abc")

        assertTrue(result is ResultOf.Success)
        assertEquals("r1", result.value.id)
    }

    @Test
    fun cancelDelegatesToApi() = runTest {
        val result = repository.cancel("abc")

        assertTrue(result is ResultOf.Success)
        assertEquals("abc", api.lastCancelledId)
    }

    @Test
    fun updateSeatsDelegatesToApi() = runTest {
        val request = UpdateReservationSeatsRequest("abc", emptyList())

        val result = repository.updateSeats(request)

        assertTrue(result is ResultOf.Success)
        assertEquals(request, api.lastUpdateSeatsRequest)
    }
}
