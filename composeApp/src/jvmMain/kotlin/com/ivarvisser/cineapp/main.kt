package com.ivarvisser.cineapp

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.ivarvisser.cineapp.di.initKoin

fun main() = application {
    initKoin {
        printLogger()
    }
    val lifecycle = remember { LifecycleRegistry() }
    val root = remember {
        RootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle)
        )
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "CineApp",
    ) {
        App(
            root = root
        )
    }
}



