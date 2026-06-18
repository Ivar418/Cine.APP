package com.ivarvisser.cineapp.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ivarvisser.cineapp.notification.NotificationService
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import net.codinux.log.Log
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds

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
            notificationService.sendLocalNotification(
                titleContent = "TestTitleOfScheduledNotification",
                bodyContent = "TestContentOfScheduledNotification"
            )
            notificationService.sendLocalNotification(
                titleContent = "ScheduledTestTItle",
                bodyContent = "schedule ran at ${
                    kotlin.time.Clock.System.now().toLocalDateTime(
                        TimeZone.currentSystemDefault()
                    )
                }",
                scheduledAtEpochMs = kotlin.time.Clock.System.now().plus(15.seconds)
                    .toEpochMilliseconds()
            ) // Schedule for 1 minute later
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}