package com.ivarvisser.cineapp.data.repository.implementations

import com.ivarvisser.cineapp.data.repository.interfaces.AppSettingsRepository
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppSettingsRepositoryImpl(private val settings: Settings) : AppSettingsRepository {
    companion object {
        private const val LOCATION_NOTIFICATIONS_KEY = "location_notifications_enabled"
        private const val SHOW_TIME_NOTIFICATIONS_KEY = "show_time_notifications_enabled"
    }

    private val _locationNotificationsEnabled = MutableStateFlow(
        settings.getBoolean(LOCATION_NOTIFICATIONS_KEY, true)
    )
    override val locationNotificationsEnabled: StateFlow<Boolean> =
        _locationNotificationsEnabled.asStateFlow()

    private val _showTimeNotificationsEnabled = MutableStateFlow(
        settings.getBoolean(SHOW_TIME_NOTIFICATIONS_KEY, true)
    )
    override val showTimeNotificationsEnabled: StateFlow<Boolean> =
        _showTimeNotificationsEnabled.asStateFlow()

    override suspend fun setLocationNotificationsEnabled(enabled: Boolean) {
        settings[LOCATION_NOTIFICATIONS_KEY] = enabled
        _locationNotificationsEnabled.value = enabled
    }

    override suspend fun setShowTimeNotificationsEnabled(enabled: Boolean) {
        settings[SHOW_TIME_NOTIFICATIONS_KEY] = enabled
        _showTimeNotificationsEnabled.value = enabled
    }
}
