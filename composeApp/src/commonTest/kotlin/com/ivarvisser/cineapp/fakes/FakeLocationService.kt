package com.ivarvisser.cineapp.fakes

import com.ivarvisser.cineapp.notification.AppLocation
import com.ivarvisser.cineapp.notification.LocationService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FakeLocationService : LocationService {
    private val locations = MutableSharedFlow<AppLocation>(replay = 1)

    suspend fun emit(location: AppLocation) {
        locations.emit(location)
    }

    override fun observeLocation(): Flow<AppLocation> = locations.asSharedFlow()
}
