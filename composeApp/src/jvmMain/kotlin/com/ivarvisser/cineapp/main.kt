package com.ivarvisser.cineapp

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.ivarvisser.cineapp.di.initKoin
import com.ivarvisser.cineapp.ui.feature.navigation.RootComponent
import com.kdroid.kmplog.Log
import com.kdroid.kmplog.d

fun main() = application {
    initKoin {
        printLogger()
    }
    val env = System.getenv("ENVIRONMENT")
    if (env == "dev") {
        Log.setDevelopmentMode(true)
    }
    Log.d("MAIN", "Debug: Environment: $env")

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



