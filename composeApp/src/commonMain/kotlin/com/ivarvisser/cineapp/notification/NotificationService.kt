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
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.codinux.log.Log
import kotlin.time.Clock
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
            Log.debug(loggerName = "NotificationService") { "Checking ${orders.value.size} orders" }
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

                //Shows notification that the showing is about to start within half an hour
                if (showing.startsAt.toLocalDateTime(TimeZone.currentSystemDefault()) <= Clock.System.now()
                        .plus(30.minutes).toLocalDateTime(TimeZone.currentSystemDefault())
                ) {
                    Log.debug(loggerName = "NotificationService") { "Showing ${showing.id} is about to start" }
                    sendLocalNotification(
                        titleContent = "Your show for ${movie?.title ?: "your movie"} is starting!",
                        bodyContent = "Don't forget to check in for order: ${order.orderCode}",
                    )
                } else {
                    Log.debug(loggerName = "NotificationService") { "Showing ${showing.id} is not about to start" }
                    sendLocalNotification(
                        titleContent = "Your show for ${movie?.title ?: "your movie"} is starting!",
                        bodyContent = "Don't forget to check in for order: ${order.orderCode}",
                        scheduledAtEpochMs = showing.startsAt.toEpochMilliseconds() - 1_800_000L
                    )
                }

            }
        }
    }


    private fun sendLocalNotification(
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

