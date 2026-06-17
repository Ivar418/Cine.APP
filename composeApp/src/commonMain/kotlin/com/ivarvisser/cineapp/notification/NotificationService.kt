package com.ivarvisser.cineapp.notification

import com.ivarvisser.cineapp.data.dto.orders.response.CreateOrderResponse
import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import com.ivarvisser.cineapp.data.repository.interfaces.OrdersRepository
import com.ivarvisser.cineapp.data.repository.interfaces.ShowingsRepository
import com.ivarvisser.cineapp.utils.ResultOf
import com.mmk.kmpnotifier.local.LocalNotifications
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.codinux.log.Log
import kotlin.time.Clock
import kotlin.time.Instant

class NotificationService(
    private val ordersRepository: OrdersRepository,
    private val showingsRepository: ShowingsRepository,
    private val moviesRepository: MoviesRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun startMonitoring() {
        // Test notification
        sendLocalNotification(
            title = "Notification service started",
            body = "Monitoring your orders and tickets..."
        )

        // Subscribe to orders
        scope.launch {
            ordersRepository.observeMyOrders().collect { orders ->
                processOrders(orders)
            }
        }

        // Monitor GPS
        scope.launch {
            // TODO: Logic to observe GPS coordinates and trigger local notifications
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
                    title = "Your show for ${movie?.title ?: "your movie"} is starting!",
                    body = "Don't forget to check in for order: ${order.orderCode}",
                    scheduledAt = showing.startsAt
                )
            }
        }
    }

    private fun sendLocalNotification(
        title: String,
        body: String,
        scheduledAt: Instant? = null
    ) {
        val notifier = LocalNotifications.notifier
        notifier.notify {
            this.title = title
            this.body = body
            this.scheduledAt =
                scheduledAt?.toEpochMilliseconds() ?: Clock.System.now().toEpochMilliseconds()
        }
    }
}
