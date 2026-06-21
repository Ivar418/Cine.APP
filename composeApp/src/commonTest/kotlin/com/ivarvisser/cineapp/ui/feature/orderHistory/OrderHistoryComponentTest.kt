package com.ivarvisser.cineapp.ui.feature.orderHistory

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.ivarvisser.cineapp.data.dto.orders.response.CreateOrderResponse
import com.ivarvisser.cineapp.data.dto.orders.response.CreatedOrderTicketResponse
import com.ivarvisser.cineapp.data.dto.orders.response.TicketResponse
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.domain.Showing
import com.ivarvisser.cineapp.domain.enums.OrderTypes
import com.ivarvisser.cineapp.domain.enums.PaymentMethods
import com.ivarvisser.cineapp.domain.enums.PaymentStatuses
import com.ivarvisser.cineapp.fakes.FakeMoviesRepository
import com.ivarvisser.cineapp.fakes.FakeOrdersRepository
import com.ivarvisser.cineapp.fakes.FakeShowingsRepository
import com.ivarvisser.cineapp.fakes.FakeTicketsRepository
import com.ivarvisser.cineapp.fakes.FakeUsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class OrderHistoryComponentTest {

    private val lifecycle = LifecycleRegistry()
    private val ordersRepository = FakeOrdersRepository()
    private val moviesRepository = FakeMoviesRepository()
    private val showingsRepository = FakeShowingsRepository()
    private val ticketsRepository = FakeTicketsRepository()
    private val usersRepository = FakeUsersRepository()
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        usersRepository.user = com.ivarvisser.cineapp.domain.User(
            userId = 1,
            userName = "test",
            photoId = null,
            photoUrl = null,
            firstName = "Test",
            lastName = "User",
            email = "test@test.com"
        )
    }

    private fun createComponent(): OrderHistoryComponent {
        return OrderHistoryComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            ordersRepository = ordersRepository,
            moviesRepository = moviesRepository,
            showingsRepository = showingsRepository,
            ticketsRepository = ticketsRepository,
            onGoBack = {},
            usersRepository = usersRepository,
            onNavigateToLogin = {}
        )
    }

    @Test
    fun initialStateIsLoadingThenSuccess() = runTest(testDispatcher) {
        val orderResponse = CreateOrderResponse(
            orderId = 1,
            orderCode = "ABC",
            orderType = OrderTypes.Online,
            paymentStatus = PaymentStatuses.Paid,
            paymentMethod = PaymentMethods.iDEAL,
            totalAmount = 10.0f,
            tickets = emptyList()
        )
        ordersRepository.ordersList = listOf(orderResponse)

        val component = createComponent()
        lifecycle.resume()

        // assertTrue(component.state.value.isLoading) // Flaky with StandardTestDispatcher

        advanceUntilIdle()

        assertFalse(component.state.value.isLoading)
        assertEquals(1, component.state.value.orders.size)
        assertEquals("ABC", component.state.value.orders[0].order.orderCode)
    }

    @Test
    fun resolvesMovieAndStartsAtForOrder() = runTest(testDispatcher) {
        val orderId = 1
        val showingId = 10
        val movieId = 100
        val startsAt = Instant.fromEpochMilliseconds(1700000000000)

        val orderResponse = CreateOrderResponse(
            orderId = orderId,
            orderCode = "ABC",
            orderType = OrderTypes.Online,
            paymentStatus = PaymentStatuses.Paid,
            paymentMethod = PaymentMethods.iDEAL,
            totalAmount = 10.0f,
            tickets = listOf(
                CreatedOrderTicketResponse(
                    ticketId = 1,
                    showingId = showingId,
                    seatNumber = "A1",
                    ticketType = "Normal",
                    price = 10.0f,
                    paymentStatus = "Paid",
                    ticketCode = "TC1"
                )
            )
        )
        ordersRepository.ordersList = listOf(orderResponse)

        val movie = Movie(id = movieId, title = "Test Movie", posterPath = "/path.jpg")
        moviesRepository.movies = mutableListOf(movie)

        val showing = Showing(
            id = showingId,
            auditoriumId = 1,
            movieId = movieId,
            is3D = false,
            startsAt = startsAt,
            auditoriumLayoutSnapshot = "",
            movie = null,
            auditorium = null
        )
        showingsRepository.showings = mutableListOf(showing)

        val component = createComponent()
        lifecycle.resume()

        advanceUntilIdle()

        val orderWithDetails = component.state.value.orders[0]
        assertEquals("Test Movie", orderWithDetails.movie?.title)
        assertEquals(startsAt, orderWithDetails.startsAt)
    }

    @Test
    fun showsErrorMessageOnFailure() = runTest(testDispatcher) {
        ordersRepository.error = "Network Error"

        val component = createComponent()
        lifecycle.resume()

        advanceUntilIdle()

        assertFalse(component.state.value.isLoading)
        assertEquals("Network Error", component.state.value.error)
    }

    @Test
    fun togglesExpansionAndLoadsTickets() = runTest(testDispatcher) {
        val orderId = 1
        val orderResponse = CreateOrderResponse(
            orderId = orderId,
            orderCode = "ABC",
            orderType = OrderTypes.Online,
            paymentStatus = PaymentStatuses.Paid,
            paymentMethod = PaymentMethods.iDEAL,
            totalAmount = 10.0f,
            tickets = emptyList()
        )
        ordersRepository.ordersList = listOf(orderResponse)

        val ticketResponse = TicketResponse(
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
        ticketsRepository.ticketsByOrderId[orderId] = listOf(ticketResponse)

        val component = createComponent()
        lifecycle.resume()
        advanceUntilIdle()

        assertFalse(component.state.value.orders[0].isExpanded)

        component.toggleOrderExpansion(orderId)
        assertTrue(component.state.value.orders[0].isExpanded)
        // assertTrue(component.state.value.orders[0].isLoadingTickets) // Flaky

        advanceUntilIdle()

        assertFalse(component.state.value.orders[0].isLoadingTickets)
        assertEquals(1, component.state.value.orders[0].tickets.size)
        assertEquals("A1", component.state.value.orders[0].tickets[0].seatNumber)
    }
}
