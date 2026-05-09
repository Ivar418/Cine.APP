package com.ivarvisser.cineapp.ui.startScreen

import com.arkivanov.decompose.value.Value

interface CineAppComponent {

    fun onEvent(event: CineAppScreenEvent)
    val startScreenModel: Value<StartScreenModel>

    data class StartScreenModel(
        val title: String = "🎬 CineApp",
        val subtitle: String = "Your Ultimate Cinema Experience"
    )
}