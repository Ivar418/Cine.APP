package com.ivarvisser.cineapp.di

import com.ivarvisser.cineapp.data.remote.api.network.createHttpClient
import com.ivarvisser.cineapp.data.remote.api.network.implementations.MoviesApiImpl
import com.ivarvisser.cineapp.data.remote.api.network.implementations.ShowingsApiImpl
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.MoviesApi
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.ShowingsApi
import com.ivarvisser.cineapp.data.repository.implementations.MoviesRepositoryImpl
import com.ivarvisser.cineapp.data.repository.implementations.ShowingsRepositoryImpl
import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import com.ivarvisser.cineapp.data.repository.interfaces.ShowingsRepository
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import org.koin.dsl.module

val dataModule = module {
    single<HttpClient> { createHttpClient() }

    //Api
    single<MoviesApi> { MoviesApiImpl(client = get()) }
    single<ShowingsApi> { ShowingsApiImpl(client = get()) }
    //Repository
    single<MoviesRepository> { MoviesRepositoryImpl(api = get()) }
    single<ShowingsRepository> { ShowingsRepositoryImpl(showingsApi = get()) }
    single<Settings> { Settings() }
}