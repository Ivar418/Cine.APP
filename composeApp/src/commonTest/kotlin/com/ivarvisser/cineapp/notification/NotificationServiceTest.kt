package com.ivarvisser.cineapp.notification

import com.ivarvisser.cineapp.fakes.FakeAppSettingsRepository
import com.ivarvisser.cineapp.fakes.FakeLocationService
import com.ivarvisser.cineapp.fakes.FakeMoviesRepository
import com.ivarvisser.cineapp.fakes.FakeOrdersRepository
import com.ivarvisser.cineapp.fakes.FakeShowingsRepository
import com.ivarvisser.cineapp.fakes.FakeTicketsRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class NotificationServiceTest {

    private val ordersRepository = FakeOrdersRepository()
    private val showingsRepository = FakeShowingsRepository()
    private val moviesRepository = FakeMoviesRepository()
    private val ticketsRepository = FakeTicketsRepository()
    private val appSettingsRepository = FakeAppSettingsRepository()
    private val locationService = FakeLocationService()

    private fun createService() = NotificationService(
        ordersRepository = ordersRepository,
        showingsRepository = showingsRepository,
        moviesRepository = moviesRepository,
        locationService = locationService,
        ticketsRepository = ticketsRepository,
        appSettingsRepository = appSettingsRepository
    )

    @Test
    fun doesNotFetchOrdersWhenShowTimeNotificationsDisabled() = runTest {
        appSettingsRepository.setShowTimeNotificationsEnabled(false)
        val service = createService()

        service.checkAndScheduleNotifications()

        assertEquals(0, ordersRepository.getMyOrdersCallCount)
    }

    @Test
    fun completesWithoutErrorWhenThereAreNoOrders() = runTest {
        val service = createService()

        service.checkAndScheduleNotifications()

        assertEquals(1, ordersRepository.getMyOrdersCallCount)
    }
}
