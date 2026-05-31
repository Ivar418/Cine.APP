package com.ivarvisser.cineapp.di

import com.ivarvisser.cineapp.data.local.implementations.UserStorageImpl
import com.ivarvisser.cineapp.data.local.interfaces.UserStorage
import com.ivarvisser.cineapp.data.remote.api.network.createHttpClient
import com.ivarvisser.cineapp.data.remote.api.network.implementations.MoviesApiImpl
import com.ivarvisser.cineapp.data.remote.api.network.implementations.ShowingsApiImpl
import com.ivarvisser.cineapp.data.remote.api.network.implementations.UsersApiImpl
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.MoviesApi
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.ShowingsApi
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.UsersApi
import com.ivarvisser.cineapp.data.repository.implementations.MoviesRepositoryImpl
import com.ivarvisser.cineapp.data.repository.implementations.ShowingsRepositoryImpl
import com.ivarvisser.cineapp.data.repository.implementations.UsersRepositoryImpl
import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import com.ivarvisser.cineapp.data.repository.interfaces.ShowingsRepository
import com.ivarvisser.cineapp.data.repository.interfaces.UsersRepository
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import org.koin.dsl.module

val dataModule = module {
    single<HttpClient> { createHttpClient() }
    //Storage
    single<Settings> { Settings() }
    single<UserStorage> { UserStorageImpl(settings = get()) }

    //Api
    single<MoviesApi> { MoviesApiImpl(client = get()) }
    single<ShowingsApi> { ShowingsApiImpl(client = get()) }
    single<UsersApi> { UsersApiImpl(client = get()) }
    //Repository
    single<MoviesRepository> { MoviesRepositoryImpl(api = get()) }
    single<ShowingsRepository> { ShowingsRepositoryImpl(showingsApi = get()) }
    single<UsersRepository> { UsersRepositoryImpl(usersApi = get(), storage = get()) }
}