package com.ivarvisser.cineapp.data.repository.interfaces

import com.ivarvisser.cineapp.data.dto.showings.response.ShowingStateResponse
import com.ivarvisser.cineapp.data.dto.showings.response.ShowingsWithPricesResponse
import com.ivarvisser.cineapp.domain.Showing
import com.ivarvisser.cineapp.utils.ResultOf

interface ShowingsRepository {
    suspend fun getShowingById(showingId: Int): ResultOf<Showing>
    suspend fun getShowingsByMovieId(movieId: Int): ResultOf<List<Showing>>
    suspend fun getShowingStateById(id: Int): ResultOf<ShowingStateResponse>
    suspend fun getShowingPrices(showingId: Int): ResultOf<ShowingsWithPricesResponse>
}
