package com.ivarvisser.cineapp.fakes

import com.ivarvisser.cineapp.data.dto.showings.response.ShowingStateResponse
import com.ivarvisser.cineapp.data.dto.showings.response.ShowingsWithPricesResponse
import com.ivarvisser.cineapp.data.repository.interfaces.ShowingsRepository
import com.ivarvisser.cineapp.domain.Showing
import com.ivarvisser.cineapp.utils.ResultOf

class FakeShowingsRepository : ShowingsRepository {
    var showings = mutableListOf<Showing>()
    var error: String? = null

    override suspend fun getShowingById(showingId: Int): ResultOf<Showing> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: showings.find { it.id == showingId }?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("Showing not found", null)
    }

    override suspend fun getShowingsByMovieId(movieId: Int): ResultOf<List<Showing>> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: ResultOf.Success(showings.filter { it.movieId == movieId })
    }

    override suspend fun getShowingStateById(id: Int): ResultOf<ShowingStateResponse> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: ResultOf.Failure("Not implemented in fake", null)
    }

    override suspend fun getShowingPrices(showingId: Int): ResultOf<ShowingsWithPricesResponse> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: ResultOf.Failure("Not implemented in fake", null)
    }
}
