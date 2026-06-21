package com.ivarvisser.cineapp.di

import com.ivarvisser.cineapp.notification.LocationService
import com.ivarvisser.cineapp.notification.WasmLocationService
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<LocationService> { WasmLocationService() }
}
