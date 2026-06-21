package com.ivarvisser.cineapp

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.ivarvisser.cineapp.di.initKoin
import com.ivarvisser.cineapp.services.NotificationBackgroundService
import com.ivarvisser.cineapp.workers.NotificationWorker
import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.local.LocalNotifications
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import net.codinux.log.LogLevel
import net.codinux.log.LoggerFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import java.util.concurrent.TimeUnit

class CineApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@CineApp)
            androidLogger()
        }
        KMPNotifier.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.logo,
                showPushNotification = false,
                // optional — customize the notification channel (see "Notification channel & sound"):
                notificationChannelData = NotificationPlatformConfiguration.Android.NotificationChannelData(
                    id = "cineapp_notifications",
                    name = "CineApp Notifications"
                )
            ),
            LocalNotifications, // omit for local-only usage (pass LocalNotifications instead)

        )
        net.codinux.log.android.AndroidContext.applicationContext = this.applicationContext

        if (BuildKonfig.IS_DEBUG) {
            LoggerFactory.config.rootLevel = LogLevel.Debug
        }
        scheduleNotificationWorker()
        val intent = Intent(this, NotificationBackgroundService::class.java)

        ContextCompat.startForegroundService(this, intent)
    }

    private fun scheduleNotificationWorker() {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            15, TimeUnit.MINUTES // Minimum interval allowed by Android
        ).setConstraints(
            androidx.work.Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "NotificationWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
