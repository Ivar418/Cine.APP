package com.ivarvisser.cineapp

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.ivarvisser.cineapp.di.initKoin
import com.ivarvisser.cineapp.ui.feature.navigation.RootComponent
import net.codinux.log.Log
import net.codinux.log.LogLevel
import net.codinux.log.LoggerFactory

fun main() = application {
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
    ) {
        App(
            root = root
        )
    }
}



