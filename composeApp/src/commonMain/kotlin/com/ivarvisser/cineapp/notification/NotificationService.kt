package com.ivarvisser.cineapp.notification

import com.ivarvisser.cineapp.data.dto.orders.response.CreateOrderResponse
import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import com.ivarvisser.cineapp.data.repository.interfaces.OrdersRepository
import com.ivarvisser.cineapp.data.repository.interfaces.ShowingsRepository
import com.ivarvisser.cineapp.data.repository.interfaces.TicketsRepository
import com.ivarvisser.cineapp.utils.ResultOf
import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.local.localNotifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.codinux.log.Log
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes


class NotificationService(
    private val ordersRepository: OrdersRepository,
    private val showingsRepository: ShowingsRepository,
    private val moviesRepository: MoviesRepository,
    private val locationService: LocationService,
    private val ticketsRepository: TicketsRepository
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var lastLocationNotificationSentAt: Long = 0
    private var ordersJob: kotlinx.coroutines.Job? = null
    private var locationJob: kotlinx.coroutines.Job? = null

    fun startMonitoring() {
        // Subscribe to orders
        if (ordersJob == null || ordersJob?.isActive == false) {
            ordersJob = scope.launch {
                ordersRepository.observeMyOrders(1_800_000).collect { orders ->
                    Log.debug(loggerName = "NotificationService") { "Received ${orders.size} orders" }
                    sendLocalNotification(
                        titleContent = "Just checked for new orders via the monitoring!",
                        bodyContent = "Just checked for new orders via the monitoring",
                    )
                    processOrders(orders)
                }
            }
        }

        // Monitor GPS
        if (locationJob == null || locationJob?.isActive == false) {
            locationJob = scope.launch {
                locationService.observeLocation().collect { location ->
                    checkLocationAndNotify(location.latitude, location.longitude)
                }
            }
        }
    }

    private fun checkLocationAndNotify(lat: Double, lon: Double) {
        val targetLat = 51.557861
        val targetLon = 5.089944
        val distance = calculateDistance(lat, lon, targetLat, targetLon)

        // If within 500 meters and haven't notified in the last hour
        val now = Clock.System.now().toEpochMilliseconds()
        if (distance < 500.0 && now - lastLocationNotificationSentAt > 3600000L) {
            sendLocalNotification(
                titleContent = "Cinema in sight!",
                bodyContent = "You're close to the cinema. Have your tickets ready!",
            )
            lastLocationNotificationSentAt = now
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371e3 // Earth radius in metres
        val phi1 = lat1 * kotlin.math.PI / 180
        val phi2 = lat2 * kotlin.math.PI / 180
        val deltaPhi = (lat2 - lat1) * kotlin.math.PI / 180
        val deltaLambda = (lon2 - lon1) * kotlin.math.PI / 180

        val a = kotlin.math.sin(deltaPhi / 2) * kotlin.math.sin(deltaPhi / 2) +
                kotlin.math.cos(phi1) * kotlin.math.cos(phi2) *
                kotlin.math.sin(deltaLambda / 2) * kotlin.math.sin(deltaLambda / 2)
        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))

        return r * c
    }

    suspend fun checkAndScheduleNotifications() {
        sendLocalNotification(
            titleContent = "Just checked for new orders via the worker!",
            bodyContent = "Just checked for new orders via the worker!",
        )
        val orders = ordersRepository.getMyOrders()
        Log.debug(loggerName = "NotificationService") { "Fetched ${if (orders is ResultOf.Success) orders.value.size else 0} orders for notification scheduling" }
        if (orders is ResultOf.Success) {
            Log.debug(loggerName = "NotificationService") { "Checking ${orders.value.size} orders" }
            processOrders(orders.value)
        }
    }

    private suspend fun processOrders(orders: List<CreateOrderResponse>) {
        Log.debug(loggerName = "NotificationService") { "Processing ${orders.size} orders" }
        Log.debug(loggerName = "NotificationService") { "Before getMovies()" }
        val moviesResult = moviesRepository.getMovies()
        Log.debug(loggerName = "NotificationService") { "After getMovies()" }
        val allMovies = (moviesResult as? ResultOf.Success)?.value ?: emptyList()

        for (order in orders) {
            val ticket = ticketsRepository.getTicketsByOrderIdAsync(order.orderId)
            if (ticket !is ResultOf.Success) continue
            Log.debug(loggerName = "NotificationService") { "Getting showing for order ${order.orderCode}" }
            val showingResult = showingsRepository.getShowingById(ticket.value.first().showingId)
            Log.debug(loggerName = "NotificationService") { "Showing Get Result was ${if (showingResult is ResultOf.Success) "Success" else "Failure"}. " }
            Log.debug(loggerName = "NotificationService") { "Processing order ${order.orderCode}" }
            if (showingResult is ResultOf.Success) {
                val showing = showingResult.value
                val movie = allMovies.find { it.id == showing.movieId }

                val now = Clock.System.now()
                val thirtyMinutesFromNow = now.plus(30.minutes)

                when {
                    // voorstelling is al gestart of voorbij
                    showing.startsAt <= now -> {
                        Log.debug(loggerName = "NotificationService") {
                            "Showing ${showing.id} already started, skipping notification"
                        }
                    }

                    // start binnen 30 minuten -> direct notificatie
                    showing.startsAt <= thirtyMinutesFromNow -> {
                        Log.debug(loggerName = "NotificationService") {
                            "Showing ${showing.id} starts within 30 minutes"
                        }

                        sendLocalNotification(
                            titleContent = "Your show for ${movie?.title ?: "your movie"} is starting!",
                            bodyContent = "Don't forget to check in for order: ${order.orderCode}",
                            scheduledAtEpochMs = null
                        )
                    }

                    // start later dan 30 minuten -> notificatie plannen
                    else -> {
                        val notificationTime =
                            showing.startsAt.toEpochMilliseconds() - 30.minutes.inWholeMilliseconds

                        Log.debug(loggerName = "NotificationService") {
                            "Scheduling notification for showing ${showing.id}"
                        }

                        sendLocalNotification(
                            titleContent = "Your show for ${movie?.title ?: "your movie"} is starting!",
                            bodyContent = "Don't forget to check in for order: ${order.orderCode}",
                            scheduledAtEpochMs = notificationTime
                        )
                    }
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

