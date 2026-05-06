package com.ivarvisser.cineapp.ui.startScreen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class DefaultCineAppComponent(
    componentContext: ComponentContext,
    private val onNavigateToAccount: () -> Unit,
    private val onNavigateToHistory: () -> Unit,
    private val onNavigateToOverview: () -> Unit
) : CineAppComponent, ComponentContext by componentContext {

    private val _startScreenModel = MutableValue(CineAppComponent.StartScreenModel())
    override val startScreenModel: Value<CineAppComponent.StartScreenModel> = _startScreenModel

    override fun onEvent(event: CineAppScreenEvent) = when (event) {
        CineAppScreenEvent.OnAccountClick -> onNavigateToAccount()
        CineAppScreenEvent.OnHistoryClick -> onNavigateToHistory()
        CineAppScreenEvent.OnOverviewClick -> onNavigateToOverview()
    }

}