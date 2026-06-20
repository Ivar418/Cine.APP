package com.ivarvisser.cineapp

import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.ivarvisser.cineapp.di.initKoin
import com.ivarvisser.cineapp.ui.feature.navigation.RootComponent
import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.extensions.composeDesktopResourcesPath
import com.mmk.kmpnotifier.local.LocalNotifications
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.multiplatform.webview.util.addTempDirectoryRemovalHook
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.codinux.log.Log
import net.codinux.log.LogLevel
import net.codinux.log.LoggerFactory
import java.io.File
import kotlin.math.max

fun main() = application {
    KMPNotifier.initialize(
        NotificationPlatformConfiguration.Desktop(
            showPushNotification = true,
            notificationIconPath = composeDesktopResourcesPath() + File.separator + "logo.png"
        ),
        LocalNotifications,
    )
    addTempDirectoryRemovalHook()
    initKoin {
        printLogger()
    }

    val env = System.getenv("ENVIRONMENT")
    if (env == "dev") {
        if (BuildKonfig.IS_DEBUG) {
            LoggerFactory.config.rootLevel = LogLevel.Debug
        }
    }
    Log.debug(loggerName = "MAIN") { "Debug: Environment: $env" }

    val lifecycle = remember { LifecycleRegistry() }
    val root = remember {
        RootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle)
        )
    }
    lifecycle.create()


    Window(
        onCloseRequest = ::exitApplication,
        title = "CineApp",
    )
    {
        var restartRequired by remember { mutableStateOf(false) }
        var downloading by remember { mutableStateOf(0F) }
        var initialized by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                KCEF.init(
                    builder = {
                        installDir(File("kcef-bundle"))

                        progress {
                            onDownloading {
                                downloading = max(it, 0F)
                            }
                            onInitialized {
                                initialized = true
                            }
                        }
                        settings {
                            cachePath = File("cache").absolutePath
                        }
                    }, onError = {
                        it?.printStackTrace()
                    }, onRestartRequired = {
                        restartRequired = true
                    })
            }
        }

        if (restartRequired) {
            Text(text = "Restart required.")
        } else {
            if (initialized) {
                App(
                    root = root
                )
            } else {
                Text(text = "Downloading $downloading%")
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                KCEF.disposeBlocking()
            }
        }
    }
}



