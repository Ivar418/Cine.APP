package com.ivarvisser.cineapp.notification

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class DesktopLocationService : LocationService {
    override fun observeLocation(): Flow<AppLocation> = emptyFlow()
}
