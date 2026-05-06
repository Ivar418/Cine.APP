package com.ivarvisser.cineapp.ui.startScreen

sealed interface CineAppScreenEvent {
    object OnAccountClick : CineAppScreenEvent
    object OnHistoryClick : CineAppScreenEvent
    object OnOverviewClick : CineAppScreenEvent
}