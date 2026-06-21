package com.ivarvisser.cineapp.ui.feature.ordering

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.ivarvisser.cineapp.domain.enums.PaymentMethods
import com.ivarvisser.cineapp.fakes.FakeMoviesRepository
import com.ivarvisser.cineapp.fakes.FakeOrdersRepository
import com.ivarvisser.cineapp.fakes.FakeReservationsRepository
import com.ivarvisser.cineapp.fakes.FakeShowingsRepository
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
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class OrderingComponentTest {

    private val lifecycle = LifecycleRegistry()
    private val moviesRepository = FakeMoviesRepository()
    private val showingsRepository = FakeShowingsRepository()
    private val ordersRepository = FakeOrdersRepository()
    private val reservationsRepository = FakeReservationsRepository()
    private val usersRepository = FakeUsersRepository()
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    private fun createComponent(
        onGoBack: () -> Unit = {},
        onLogin: () -> Unit = {}
    ): OrderingComponent {
        return OrderingComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            showingId = 1,
            movieId = 1,
            moviesRepository = moviesRepository,
            showingsRepository = showingsRepository,
            ordersRepository = ordersRepository,
            reservationsRepository = reservationsRepository,
            usersRepository = usersRepository,
            onGoBack = onGoBack,
            onLogin = onLogin
        )
    }

    @Test
    fun increaseAndDecreaseNormalSeatCounts() = runTest(testDispatcher) {
        val component = createComponent()
        lifecycle.create()
        advanceUntilIdle()

        val initialCount = component.state.value.seatSelection.normalCount

        component.onAction(OrderingAction.IncreaseNormalSeats)
        assertEquals(initialCount + 1, component.state.value.seatSelection.normalCount)

        component.onAction(OrderingAction.DecreaseNormalSeats)
        assertEquals(initialCount, component.state.value.seatSelection.normalCount)
    }

    @Test
    fun decreaseNormalSeatsNeverGoesBelowZero() = runTest(testDispatcher) {
        val component = createComponent()
        lifecycle.create()
        advanceUntilIdle()

        repeat(5) { component.onAction(OrderingAction.DecreaseNormalSeats) }

        assertEquals(0, component.state.value.seatSelection.normalCount)
    }

    @Test
    fun goToPaymentMethodsAdvancesStep() = runTest(testDispatcher) {
        val component = createComponent()
        lifecycle.create()
        advanceUntilIdle()

        component.onAction(OrderingAction.GoToPaymentMethods)

        assertEquals(4, component.state.value.step)
        assertTrue(component.state.value.paymentMethods.isNotEmpty())
    }

    @Test
    fun paymentMethodSelectedUpdatesStateAndStep() = runTest(testDispatcher) {
        val component = createComponent()
        lifecycle.create()
        advanceUntilIdle()

        component.onAction(OrderingAction.PaymentMethodSelected(PaymentMethods.iDEAL))

        assertEquals(PaymentMethods.iDEAL, component.state.value.selectedPaymentMethod)
        assertEquals(5, component.state.value.step)
    }

    @Test
    fun onBackAtFirstStepTriggersGoBackAndCancelsPending() = runTest(testDispatcher) {
        var backCalled = false
        val component = createComponent(onGoBack = { backCalled = true })
        lifecycle.create()
        advanceUntilIdle()

        component.onAction(OrderingAction.OnBack)
        advanceUntilIdle()

        assertTrue(backCalled)
    }

    @Test
    fun loginActionTriggersOnLoginCallback() = runTest(testDispatcher) {
        var loginCalled = false
        val component = createComponent(onLogin = { loginCalled = true })
        lifecycle.create()
        advanceUntilIdle()

        component.onAction(OrderingAction.Login)

        assertTrue(loginCalled)
    }
}
