package com.ivarvisser.cineapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.ivarvisser.cineapp.R
import com.ivarvisser.cineapp.notification.NotificationService
import com.mmk.kmpnotifier.KMPNotifier
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.codinux.log.Log
import org.koin.android.ext.android.inject
import kotlin.time.Duration.Companion.minutes

class NotificationBackgroundService : Service() {

    private val notificationService: NotificationService by inject()

    companion object {
        private const val CHANNEL_ID = "monitoring_channel"
        private const val NOTIFICATION_ID = 9
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("CineApp Monitoring")
            .setContentText("Monitoring your orders and tickets...")
            .setSmallIcon(R.drawable.logo)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()

        // Promoting to Foreground Service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            Log.debug(loggerName = "NotificationBackgroundService") { "Promoting to Foreground Service" }
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }

        // Start the common monitoring logic
        notificationService.startMonitoring()
        notificationService.startTestNotification()
        notificationService.sendLocalNotification(
            titleContent = "BackgroundServiceScheduledTestTItle",
            bodyContent = "schedule ran at ${
                kotlin.time.Clock.System.now().toLocalDateTime(
                    TimeZone.currentSystemDefault()
                )
            } by the background notificationservice",
            scheduledAtEpochMs = kotlin.time.Clock.System.now()
                .plus(1.minutes)
                .toEpochMilliseconds()
        )
        return START_STICKY
    }

    private fun createNotificationChannel() {
        Log.debug(loggerName = "NotificationBackgroundService") { "Creating notification channel for monitoring service" }
        KMPNotifier.setLogger { message ->
            println(message)
        }
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "Monitoring Service Channel",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(serviceChannel)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}