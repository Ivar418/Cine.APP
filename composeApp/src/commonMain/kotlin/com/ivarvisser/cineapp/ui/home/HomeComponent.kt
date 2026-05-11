package com.ivarvisser.cineapp.ui.home

import com.arkivanov.decompose.value.Value

interface HomeComponent {
    val state: Value<HomeState>
    fun onEvent(event: CineAppScreenEvent)
    val component: Value<StartScreenModel>

    data class StartScreenModel(
        val title: String = "🎬 CineApp",
        val subtitle: String = "Your Ultimate Cinema Experience"
    )
}