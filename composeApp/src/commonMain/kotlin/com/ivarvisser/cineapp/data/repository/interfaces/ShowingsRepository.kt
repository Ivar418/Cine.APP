package com.ivarvisser.cineapp.data.repository.interfaces

import com.ivarvisser.cineapp.domain.Showing
import com.ivarvisser.cineapp.domain.ShowingDisplayResponse
import com.ivarvisser.cineapp.utils.ResultOf

interface ShowingsRepository {
    suspend fun getShowingById(id: Int): ResultOf<Showing>
    suspend fun getShowingsByMovieId(movieId: Int): ResultOf<List<ShowingDisplayResponse>>
}