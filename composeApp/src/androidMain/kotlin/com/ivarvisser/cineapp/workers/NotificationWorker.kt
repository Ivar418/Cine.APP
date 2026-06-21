package com.ivarvisser.cineapp.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ivarvisser.cineapp.notification.NotificationService
import net.codinux.log.Log
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val notificationService: NotificationService by inject()

    override suspend fun doWork(): Result {
        return try {
            Log.debug(loggerName = "NotificationWorker") { "Executing NotificationWorker" }
            // Trigger the monitoring logic
            notificationService.checkAndScheduleNotifications()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}