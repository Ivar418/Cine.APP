package com.ivarvisser.cineapp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.arkivanov.essenty.lifecycle.resume
import com.ivarvisser.cineapp.di.initKoin
import com.ivarvisser.cineapp.ui.feature.navigation.RootComponent

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    val lifecycle = LifecycleRegistry().apply {
        resume() // move lifecycle to RESUMED state
    }
    val root = RootComponent(DefaultComponentContext(lifecycle))
    lifecycle.create()
    ComposeViewport {
        App(root = root)
    }
}