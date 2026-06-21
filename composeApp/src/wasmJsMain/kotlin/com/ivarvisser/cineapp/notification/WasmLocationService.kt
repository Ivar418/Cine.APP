package com.ivarvisser.cineapp.notification

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class WasmLocationService : LocationService {
    override fun observeLocation(): Flow<AppLocation> = emptyFlow()
}
