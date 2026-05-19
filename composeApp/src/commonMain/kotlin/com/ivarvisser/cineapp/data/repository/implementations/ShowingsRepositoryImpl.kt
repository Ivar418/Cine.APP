package com.ivarvisser.cineapp.data.repository.implementations

import com.ivarvisser.cineapp.data.remote.api.network.interfaces.ShowingsApi
import com.ivarvisser.cineapp.data.repository.interfaces.ShowingsRepository
import com.ivarvisser.cineapp.domain.Showing
import com.ivarvisser.cineapp.domain.ShowingDisplayResponse
import com.ivarvisser.cineapp.utils.ResultOf

class ShowingsRepositoryImpl(
    private val showingsApi: ShowingsApi
) : ShowingsRepository {
    override suspend fun getShowingById(id: Int): ResultOf<Showing> {
        TODO("Not yet implemented")
    }

    override suspend fun getShowingsByMovieId(movieId: Int): ResultOf<List<ShowingDisplayResponse>> {
        return showingsApi.getShowingsByMovieId(movieId)
    }
}