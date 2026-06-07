package com.ivarvisser.cineapp

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.window.ComposeViewport
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.noto_color_emoji_regular
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.arkivanov.essenty.lifecycle.resume
import com.ivarvisser.cineapp.di.initKoin
import com.ivarvisser.cineapp.ui.feature.navigation.RootComponent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.preloadFont

@OptIn(ExperimentalComposeUiApi::class, ExperimentalResourceApi::class)
fun main() {
    initKoin()
    val lifecycle = LifecycleRegistry().apply {
        resume() // move lifecycle to RESUMED state
    }
    val root = RootComponent(DefaultComponentContext(lifecycle))
    lifecycle.create()

    ComposeViewport {
        val resolver = LocalFontFamilyResolver.current

        val emojiFont by preloadFont(Res.font.noto_color_emoji_regular)
        LaunchedEffect(emojiFont) {
            if (emojiFont != null) {
                resolver.preload(
                    fontFamily = FontFamily(emojiFont!!)
                )
            }
        }
        App(root = root)
    }
}