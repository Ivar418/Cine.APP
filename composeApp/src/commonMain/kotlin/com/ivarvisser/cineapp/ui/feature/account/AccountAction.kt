package com.ivarvisser.cineapp.ui.feature.account

sealed interface AccountAction {
    data object OnBack : AccountAction
    data object OnFavorites : AccountAction
    data class OnToggleLocationNotifications(val enabled: Boolean) : AccountAction
    data class OnToggleShowTimeNotifications(val enabled: Boolean) : AccountAction
}