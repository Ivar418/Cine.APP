package com.ivarvisser.cineapp.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.ivarvisser.cineapp.notification.NotificationService
import org.koin.android.ext.android.inject

class NotificationBackgroundService : Service() {

    private val notificationService: NotificationService by inject()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Start de monitoring logica
        notificationService.startMonitoring()

        // START_STICKY zorgt ervoor dat de service herstart als het systeem hem afsluit
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}