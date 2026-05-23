package com.ivarvisser.cineapp.data.repository.implementations

import com.ivarvisser.cineapp.data.remote.api.network.interfaces.ShowingsApi
import com.ivarvisser.cineapp.data.repository.interfaces.ShowingsRepository
import com.ivarvisser.cineapp.domain.Showing
import com.ivarvisser.cineapp.utils.ResultOf

class ShowingsRepositoryImpl(
    private val showingsApi: ShowingsApi
) : ShowingsRepository {
    override suspend fun getShowingById(showingId: Int): ResultOf<Showing> {
        return showingsApi.getShowingById(showingId)
    }

    override suspend fun getShowingsByMovieId(movieId: Int): ResultOf<List<Showing>> {
        return showingsApi.getShowingsByMovieId(movieId)
    }
}