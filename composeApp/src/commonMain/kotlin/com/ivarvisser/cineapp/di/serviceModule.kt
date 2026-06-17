package com.ivarvisser.cineapp.di

import com.ivarvisser.cineapp.notification.NotificationService
import org.koin.dsl.module

val serviceModule = module {
    single<NotificationService> {
        NotificationService(
            ordersRepository = get(),
            showingsRepository = get(),
            moviesRepository = get()
        )
    }
}