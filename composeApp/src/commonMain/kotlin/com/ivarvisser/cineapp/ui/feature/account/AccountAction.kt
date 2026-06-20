package com.ivarvisser.cineapp.ui.feature.account

sealed interface AccountAction {
    data object OnBack : AccountAction
    data object OnLogin : AccountAction
    data object OnLogout : AccountAction
    data object OnRegister : AccountAction
    data object OnForgotPassword : AccountAction
    data object OnChangePassword : AccountAction
    data object OnChangeEmail : AccountAction
    data object OnChangeUsername : AccountAction
    data object OnChangeName : AccountAction
    data object OnChangePhoto : AccountAction
    data object OnFavorites : AccountAction
    data class OnToggleLocationNotifications(val enabled: Boolean) : AccountAction
    data class OnToggleShowTimeNotifications(val enabled: Boolean) : AccountAction
}