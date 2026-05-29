package com.ivarvisser.cineapp.data.remote.api.network.interfaces

import com.ivarvisser.cineapp.domain.Showing
import com.ivarvisser.cineapp.utils.ResultOf

interface ShowingsApi {
    suspend fun getShowingById(id: Int): ResultOf<Showing>
    suspend fun getShowingsByMovieId(movieId: Int): ResultOf<List<Showing>>
}