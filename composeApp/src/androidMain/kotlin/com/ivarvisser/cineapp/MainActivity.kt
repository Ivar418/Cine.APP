package com.ivarvisser.cineapp

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.arkivanov.decompose.retainedComponent
import com.ivarvisser.cineapp.services.NotificationBackgroundService
import com.ivarvisser.cineapp.ui.feature.navigation.RootComponent
import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.extensions.onCreateOrOnNewIntent
import com.mmk.kmpnotifier.permission.permissionUtil

class MainActivity : ComponentActivity() {

    private lateinit var root: RootComponent

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineGranted || coarseGranted) {
            // Restart service to enable location tracking
            val intent = Intent(this, NotificationBackgroundService::class.java)
            ContextCompat.startForegroundService(this, intent)

            // On Android 10+ (API 29), we might also want background location
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                requestBackgroundLocationPermission()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val permissionUtil by permissionUtil()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        permissionUtil.askNotificationPermission()
        requestExactAlarmPermissionIfNeeded()
        requestLocationPermissionsIfNeeded()
        KMPNotifier.onCreateOrOnNewIntent(intent)


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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        KMPNotifier.onCreateOrOnNewIntent(intent)
    }

    private fun requestLocationPermissionsIfNeeded() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val needsRequest = permissions.any {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (needsRequest) {
            requestPermissionLauncher.launch(permissions.toTypedArray())
        }
    }

    private fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // For simplicity, we just use a one-off launcher here if needed.
                // Note: Better to handle this with its own registerForActivityResult if you need the result.
                val backgroundLauncher =
                    registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
                backgroundLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
        }
    }

    private fun requestExactAlarmPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(AlarmManager::class.java)
            if (!alarmManager.canScheduleExactAlarms()) {
                startActivity(
                    Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                        data = "package:$packageName".toUri()
                    }
                )
            }
        }
    }
}

