package com.ivarvisser.cineapp.data.repository.implementations

import com.ivarvisser.cineapp.data.dto.orders.response.TicketResponse
import com.ivarvisser.cineapp.domain.enums.PaymentStatuses
import com.ivarvisser.cineapp.fakes.FakeTicketsApi
import com.ivarvisser.cineapp.utils.ResultOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Instant

class TicketsRepositoryImplTest {

    private val api = FakeTicketsApi()
    private val repository = TicketsRepositoryImpl(api)

    @Test
    fun getTicketsByOrderIdReturnsTickets() = runTest {
        val ticket = TicketResponse(
            id = 1,
            showingId = 10,
            movieTitle = "Test Movie",
            showDateTimeUtc = Instant.fromEpochMilliseconds(1700000000000),
            seatNumber = "A1",
            ticketType = "Normal",
            status = "Valid",
            paymentStatus = PaymentStatuses.Paid,
            qrIsActive = true,
            price = 10.0f,
            purchaseDateUtc = Instant.fromEpochMilliseconds(1700000000000)
        )
        api.ticketsByOrderId[1] = listOf(ticket)

        val result = repository.getTicketsByOrderIdAsync(1)

        assertTrue(result is ResultOf.Success)
        assertEquals(1, result.value.size)
        assertEquals("A1", result.value[0].seatNumber)
    }

    @Test
    fun getTicketsByOrderIdReturnsEmptyForUnknownOrder() = runTest {
        val result = repository.getTicketsByOrderIdAsync(404)

        assertTrue(result is ResultOf.Success)
        assertEquals(0, result.value.size)
    }
}
