package com.ivarvisser.cineapp.fakes

import com.ivarvisser.cineapp.data.repository.interfaces.AppSettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeAppSettingsRepository : AppSettingsRepository {
    private val _locationNotificationsEnabled = MutableStateFlow(true)
    override val locationNotificationsEnabled: StateFlow<Boolean> =
        _locationNotificationsEnabled.asStateFlow()

    private val _showTimeNotificationsEnabled = MutableStateFlow(true)
    override val showTimeNotificationsEnabled: StateFlow<Boolean> =
        _showTimeNotificationsEnabled.asStateFlow()

    override suspend fun setLocationNotificationsEnabled(enabled: Boolean) {
        _locationNotificationsEnabled.value = enabled
    }

    override suspend fun setShowTimeNotificationsEnabled(enabled: Boolean) {
        _showTimeNotificationsEnabled.value = enabled
    }
}
