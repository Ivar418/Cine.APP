package com.ivarvisser.cineapp.data.repository.interfaces

import kotlinx.coroutines.flow.StateFlow

interface AppSettingsRepository {
    val locationNotificationsEnabled: StateFlow<Boolean>
    val showTimeNotificationsEnabled: StateFlow<Boolean>

    suspend fun setLocationNotificationsEnabled(enabled: Boolean)
    suspend fun setShowTimeNotificationsEnabled(enabled: Boolean)
}
