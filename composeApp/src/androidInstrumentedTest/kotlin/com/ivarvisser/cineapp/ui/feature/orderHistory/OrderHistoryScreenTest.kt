package com.ivarvisser.cineapp.ui.feature.orderHistory

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.ivarvisser.cineapp.data.dto.orders.response.CreateOrderResponse
import com.ivarvisser.cineapp.domain.User
import com.ivarvisser.cineapp.domain.enums.OrderTypes
import com.ivarvisser.cineapp.domain.enums.PaymentMethods
import com.ivarvisser.cineapp.domain.enums.PaymentStatuses
import com.ivarvisser.cineapp.fakes.FakeMoviesRepository
import com.ivarvisser.cineapp.fakes.FakeOrdersRepository
import com.ivarvisser.cineapp.fakes.FakeShowingsRepository
import com.ivarvisser.cineapp.fakes.FakeTicketsRepository
import com.ivarvisser.cineapp.fakes.FakeUsersRepository
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Test

class OrderHistoryScreenTest {

    private val lifecycle = LifecycleRegistry()
    private val ordersRepository = FakeOrdersRepository()
    private val moviesRepository = FakeMoviesRepository()
    private val showingsRepository = FakeShowingsRepository()
    private val ticketsRepository = FakeTicketsRepository()
    private val usersRepository = FakeUsersRepository()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testOrderHistoryDisplay() = runComposeUiTest(effectContext = UnconfinedTestDispatcher()) {
        usersRepository.user = User(
            userId = 1,
            userName = "test",
            photoId = null,
            photoUrl = null,
            firstName = "Test",
            lastName = "User",
            email = "test@test.com"
        )
        val orderResponse = CreateOrderResponse(
            orderId = 1,
            orderCode = "ABC",
            orderType = OrderTypes.Online,
            paymentStatus = PaymentStatuses.Paid,
            paymentMethod = PaymentMethods.iDEAL,
            totalAmount = 10.0f,
            tickets = emptyList(),
        )
        ordersRepository.ordersList = listOf(orderResponse)

        val component = OrderHistoryComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            ordersRepository = ordersRepository,
            moviesRepository = moviesRepository,
            showingsRepository = showingsRepository,
            ticketsRepository = ticketsRepository,
            usersRepository = usersRepository,
            onGoBack = {},
            onNavigateToLogin = {}
        )
        lifecycle.resume()

        setContent {
            OrderHistoryScreen(component = component)
        }

        // Check if the order code is displayed (since movie title is not resolved yet)
        onNodeWithText("Order: ABC", substring = true).assertIsDisplayed()

        // Click to expand
        onNodeWithText("Order: ABC", substring = true).performClick()

        // Check if "No tickets found" is displayed (since tickets are empty)
        onNodeWithText("No tickets found", substring = true).assertIsDisplayed()
    }
}
