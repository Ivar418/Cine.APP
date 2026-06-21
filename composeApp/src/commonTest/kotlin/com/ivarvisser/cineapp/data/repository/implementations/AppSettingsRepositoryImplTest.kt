package com.ivarvisser.cineapp.data.repository.implementations

import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AppSettingsRepositoryImplTest {

    @Test
    fun defaultsAreEnabled() {
        val repository = AppSettingsRepositoryImpl(MapSettings())

        assertTrue(repository.locationNotificationsEnabled.value)
        assertTrue(repository.showTimeNotificationsEnabled.value)
    }

    @Test
    fun settingLocationNotificationsUpdatesFlowAndPersists() = runTest {
        val settings = MapSettings()
        val repository = AppSettingsRepositoryImpl(settings)

        repository.setLocationNotificationsEnabled(false)

        assertFalse(repository.locationNotificationsEnabled.value)

        // A new repository instance backed by the same settings should read the persisted value
        val reloaded = AppSettingsRepositoryImpl(settings)
        assertFalse(reloaded.locationNotificationsEnabled.value)
    }

    @Test
    fun settingShowTimeNotificationsUpdatesFlowAndPersists() = runTest {
        val settings = MapSettings()
        val repository = AppSettingsRepositoryImpl(settings)

        repository.setShowTimeNotificationsEnabled(false)

        assertFalse(repository.showTimeNotificationsEnabled.value)

        val reloaded = AppSettingsRepositoryImpl(settings)
        assertFalse(reloaded.showTimeNotificationsEnabled.value)
    }
}
