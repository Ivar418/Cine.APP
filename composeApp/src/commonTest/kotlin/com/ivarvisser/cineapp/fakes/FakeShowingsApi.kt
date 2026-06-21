package com.ivarvisser.cineapp.fakes

import com.ivarvisser.cineapp.data.dto.showings.response.ShowingStateResponse
import com.ivarvisser.cineapp.data.dto.showings.response.ShowingsWithPricesResponse
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.ShowingsApi
import com.ivarvisser.cineapp.domain.Showing
import com.ivarvisser.cineapp.utils.ResultOf

class FakeShowingsApi : ShowingsApi {
    var showings = mutableListOf<Showing>()
    var showingState: ShowingStateResponse? = null
    var showingPrices: ShowingsWithPricesResponse? = null
    var error: String? = null

    override suspend fun getShowingById(id: Int): ResultOf<Showing> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: showings.find { it.id == id }?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("Showing not found", null)
    }

    override suspend fun getShowingsByMovieId(movieId: Int): ResultOf<List<Showing>> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: ResultOf.Success(showings.filter { it.movieId == movieId })
    }

    override suspend fun getShowingStateById(id: Int): ResultOf<ShowingStateResponse> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: showingState?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("No state set", null)
    }

    override suspend fun getShowingPrices(id: Int): ResultOf<ShowingsWithPricesResponse> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: showingPrices?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("No prices set", null)
    }
}
