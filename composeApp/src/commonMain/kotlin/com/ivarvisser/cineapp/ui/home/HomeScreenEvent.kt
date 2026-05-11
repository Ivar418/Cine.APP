package com.ivarvisser.cineapp.ui.home

sealed interface CineAppScreenEvent {
    object OnAccountClick : CineAppScreenEvent
    object OnHistoryClick : CineAppScreenEvent
    object OnOverviewClick : CineAppScreenEvent
}