package com.ivarvisser.cineapp.di

import com.ivarvisser.cineapp.data.remote.api.MoviesApi
import com.ivarvisser.cineapp.data.remote.api.MoviesApiImpl
import com.ivarvisser.cineapp.data.remote.api.network.createHttpClient
import com.ivarvisser.cineapp.data.repository.Implementations.MoviesRepositoryImpl
import com.ivarvisser.cineapp.data.repository.Interfaces.MoviesRepository
import io.ktor.client.HttpClient
import org.koin.dsl.module

val sharedModule = module {
    single<HttpClient> {
        createHttpClient()
    }
    single<MoviesApi> {
        MoviesApiImpl(client = get())
    }
    single<MoviesRepository> {
        MoviesRepositoryImpl(
            api = get()
        )
    }
}