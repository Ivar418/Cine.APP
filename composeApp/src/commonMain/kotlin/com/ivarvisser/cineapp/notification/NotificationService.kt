package com.ivarvisser.cineapp.notification

import com.ivarvisser.cineapp.data.dto.orders.response.CreateOrderResponse
import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import com.ivarvisser.cineapp.data.repository.interfaces.OrdersRepository
import com.ivarvisser.cineapp.data.repository.interfaces.ShowingsRepository
import com.ivarvisser.cineapp.utils.ResultOf
import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.local.localNotifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.codinux.log.Log
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes


class NotificationService(
    private val ordersRepository: OrdersRepository,
    private val showingsRepository: ShowingsRepository,
    private val moviesRepository: MoviesRepository
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun startMonitoring() {
        // Subscribe to orders
        scope.launch {
            ordersRepository.observeMyOrders(1_800_000).collect { orders ->
                Log.debug(loggerName = "NotificationService") { "Received ${orders.size} orders" }
                processOrders(orders)
            }
        }

        // Monitor GPS
        scope.launch {
            // TODO: Logic to observe GPS coordinates and trigger local notifications
        }
    }

    suspend fun checkAndScheduleNotifications() {
        val orders = ordersRepository.getMyOrders()
        if (orders is ResultOf.Success) {
            processOrders(orders.value)
        }
    }

    private suspend fun processOrders(orders: List<CreateOrderResponse>) {
        Log.debug(loggerName = "NotificationService") { "Processing ${orders.size} orders" }
        val moviesResult = moviesRepository.getMovies()
        val allMovies = (moviesResult as? ResultOf.Success)?.value ?: emptyList()

        for (order in orders) {
            val firstTicket = order.tickets.firstOrNull() ?: continue
            val showingResult = showingsRepository.getShowingById(firstTicket.showingId)

            if (showingResult is ResultOf.Success) {
                val showing = showingResult.value
                val movie = allMovies.find { it.id == showing.movieId }

                // 1. Schedule notification for show start
                sendLocalNotification(
                    titleContent = "Your show for ${movie?.title ?: "your movie"} is starting!",
                    bodyContent = "Don't forget to check in for order: ${order.orderCode}",
                    scheduledAtEpochMs = showing.startsAt.toEpochMilliseconds() - 1_800_000L
                )
            }
        }
    }

    fun startTestNotification() {
        scope.launch {
            while (isActive) {
                sendLocalNotification(
                    titleContent = "Test 15 Notification",
                    bodyContent = "Scheduled at ${
                        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                    }",
                    scheduledAtEpochMs = Clock.System.now().plus(15.minutes).toEpochMilliseconds()
                )
                sendLocalNotification(
                    titleContent = "Test 1M Notification",
                    bodyContent = "Scheduled at ${
                        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                    }",
                    scheduledAtEpochMs = Clock.System.now().plus(1.minutes).toEpochMilliseconds()
                )
                delay(15_000.milliseconds)
            }
        }
    }

    fun sendLocalNotification(
        titleContent: String,
        bodyContent: String,
        scheduledAtEpochMs: Long? = null
    ) {
        KMPNotifier.localNotifier.notify {
            this.title = titleContent
            this.body = bodyContent
            if (scheduledAtEpochMs != null && scheduledAtEpochMs > Clock.System.now()
                    .toEpochMilliseconds()
            ) {
                this.scheduledAt = scheduledAtEpochMs
            }
        }


    }
}

