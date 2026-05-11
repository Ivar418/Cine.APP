package com.ivarvisser.cineapp.ui.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope

class DefaultHomeComponent(
    componentContext: ComponentContext,
    private val onNavigateToAccount: () -> Unit,
    private val onNavigateToHistory: () -> Unit,
    private val onNavigateToOverview: () -> Unit
) : HomeComponent, ComponentContext by componentContext {

    private val scope = coroutineScope()

    private val _state = MutableValue(HomeState())
    override val state: Value<HomeState> = _state
    init {
    }

    private val _component = MutableValue(HomeComponent.StartScreenModel())
    override val component: Value<HomeComponent.StartScreenModel> = _component

    override fun onEvent(event: CineAppScreenEvent) = when (event) {
        CineAppScreenEvent.OnAccountClick -> onNavigateToAccount()
        CineAppScreenEvent.OnHistoryClick -> onNavigateToHistory()
        CineAppScreenEvent.OnOverviewClick -> onNavigateToOverview()
    }

}