package com.ivarvisser.cineapp.di

import com.ivarvisser.cineapp.notification.AndroidLocationService
import com.ivarvisser.cineapp.notification.LocationService
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<LocationService> { AndroidLocationService(get()) }
}
