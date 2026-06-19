package com.ivarvisser.cineapp.notification

import kotlinx.coroutines.flow.Flow

data class AppLocation(val latitude: Double, val longitude: Double)

interface LocationService {
    fun observeLocation(): Flow<AppLocation>
}
