package com.ivarvisser.cineapp.di

import com.ivarvisser.cineapp.data.local.implementations.TokenStorageImpl
import com.ivarvisser.cineapp.data.local.implementations.UserStorageImpl
import com.ivarvisser.cineapp.data.local.interfaces.TokenStorage
import com.ivarvisser.cineapp.data.local.interfaces.UserStorage
import com.ivarvisser.cineapp.data.remote.api.network.createHttpClient
import com.ivarvisser.cineapp.data.remote.api.network.implementations.MoviesApiImpl
import com.ivarvisser.cineapp.data.remote.api.network.implementations.OrderApiImpl
import com.ivarvisser.cineapp.data.remote.api.network.implementations.ReservationsApiImpl
import com.ivarvisser.cineapp.data.remote.api.network.implementations.ShowingsApiImpl
import com.ivarvisser.cineapp.data.remote.api.network.implementations.TicketsApiImpl
import com.ivarvisser.cineapp.data.remote.api.network.implementations.UsersApiImpl
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.MoviesApi
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.OrdersApi
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.ReservationsApi
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.ShowingsApi
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.TicketsApi
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.UsersApi
import com.ivarvisser.cineapp.data.repository.implementations.AppSettingsRepositoryImpl
import com.ivarvisser.cineapp.data.repository.implementations.MoviesRepositoryImpl
import com.ivarvisser.cineapp.data.repository.implementations.OrdersRepositoryImpl
import com.ivarvisser.cineapp.data.repository.implementations.ReservationsRepositoryImpl
import com.ivarvisser.cineapp.data.repository.implementations.ShowingsRepositoryImpl
import com.ivarvisser.cineapp.data.repository.implementations.TicketsRepositoryImpl
import com.ivarvisser.cineapp.data.repository.implementations.UsersRepositoryImpl
import com.ivarvisser.cineapp.data.repository.interfaces.AppSettingsRepository
import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import com.ivarvisser.cineapp.data.repository.interfaces.OrdersRepository
import com.ivarvisser.cineapp.data.repository.interfaces.ReservationsRepository
import com.ivarvisser.cineapp.data.repository.interfaces.ShowingsRepository
import com.ivarvisser.cineapp.data.repository.interfaces.TicketsRepository
import com.ivarvisser.cineapp.data.repository.interfaces.UsersRepository
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import org.koin.dsl.module

val dataModule = module {
    //Storage
    single<Settings> { Settings() }
    single<TokenStorage> { TokenStorageImpl(settings = get()) }
    single<UserStorage> { UserStorageImpl(settings = get(), tokenStorage = get()) }

    single<HttpClient> { createHttpClient(tokenStorage = get()) }

    //Api
    single<MoviesApi> { MoviesApiImpl(client = get()) }
    single<ShowingsApi> { ShowingsApiImpl(client = get()) }
    single<UsersApi> { UsersApiImpl(client = get()) }
    single<OrdersApi> { OrderApiImpl(client = get()) }
    single<ReservationsApi> { ReservationsApiImpl(client = get()) }
    single<TicketsApi> { TicketsApiImpl(client = get()) }

    //Repository
    single<MoviesRepository> { MoviesRepositoryImpl(api = get()) }
    single<ShowingsRepository> { ShowingsRepositoryImpl(showingsApi = get()) }
    single<UsersRepository> { UsersRepositoryImpl(usersApi = get(), storage = get()) }
    single<OrdersRepository> { OrdersRepositoryImpl(ordersApi = get()) }
    single<ReservationsRepository> { ReservationsRepositoryImpl(reservationsApi = get()) }
    single<TicketsRepository> { TicketsRepositoryImpl(ticketsApi = get()) }
    single<AppSettingsRepository> { AppSettingsRepositoryImpl(settings = get()) }
}
