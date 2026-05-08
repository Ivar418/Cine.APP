package com.ivarvisser.cineapp.ui.startScreen

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DefaultCineAppComponent(
    componentContext: ComponentContext,
    private val onNavigateToAccount: () -> Unit,
    private val onNavigateToHistory: () -> Unit,
    private val onNavigateToOverview: () -> Unit
) : CineAppComponent, ComponentContext by componentContext {

    private val scope = coroutineScope()

    private val _state = MutableStateFlow(CineAppState())
    val state: StateFlow<CineAppState> = _state.asStateFlow()

    init {
    }

    private val _startScreenModel = MutableValue(CineAppComponent.StartScreenModel())
    override val startScreenModel: Value<CineAppComponent.StartScreenModel> = _startScreenModel

    override fun onEvent(event: CineAppScreenEvent) = when (event) {
        CineAppScreenEvent.OnAccountClick -> onNavigateToAccount()
        CineAppScreenEvent.OnHistoryClick -> onNavigateToHistory()
        CineAppScreenEvent.OnOverviewClick -> onNavigateToOverview()
    }

}