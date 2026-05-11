package com.ivarvisser.cineapp.di

import com.ivarvisser.cineapp.data.remote.api.MoviesApi
import com.ivarvisser.cineapp.data.remote.api.MoviesApiImpl
import com.ivarvisser.cineapp.data.remote.api.network.createHttpClient
import com.ivarvisser.cineapp.data.repository.implementations.MoviesRepositoryImpl
import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import io.ktor.client.HttpClient
import org.koin.dsl.module

val dataModule = module {
    single<HttpClient> { createHttpClient() }
    single<MoviesApi> { MoviesApiImpl(client = get()) }
    single<MoviesRepository> { MoviesRepositoryImpl(api = get()) }
}