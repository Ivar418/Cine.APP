package com.ivarvisser.cineapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.retainedComponent
import com.ivarvisser.cineapp.services.NotificationBackgroundService
import com.ivarvisser.cineapp.ui.feature.navigation.RootComponent
import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.local.LocalNotifications
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.mmk.kmpnotifier.permission.permissionUtil

class MainActivity : ComponentActivity() {

    private lateinit var root: RootComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        val permissionUtil by permissionUtil()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        permissionUtil.askNotificationPermission()


        KMPNotifier.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.logo,
                showPushNotification = true,
                // optional — customize the notification channel (see "Notification channel & sound"):
                notificationChannelData = NotificationPlatformConfiguration.Android.NotificationChannelData(),
            ),
            LocalNotifications, // omit for local-only usage (pass LocalNotifications instead)
        )
        val intent = Intent(this, NotificationBackgroundService::class.java)
        startService(intent)

        root = retainedComponent {
            RootComponent(
                componentContext = it,
            )
        }

        setContent {
            App(
                root = root
            )
        }
    }
}

