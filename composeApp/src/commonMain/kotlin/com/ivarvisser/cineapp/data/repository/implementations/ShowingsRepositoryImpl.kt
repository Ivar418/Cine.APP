package com.ivarvisser.cineapp.data.repository.implementations

import com.ivarvisser.cineapp.data.dto.ShowingStateResponse
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.ShowingsApi
import com.ivarvisser.cineapp.data.repository.interfaces.ShowingsRepository
import com.ivarvisser.cineapp.domain.Showing
import com.ivarvisser.cineapp.utils.ResultOf

/**
 * Implementation of the ShowingsRepository interface, providing methods to access showing-related
 * data by utilizing the ShowingsApi as the underlying data source.
 *
 * This repository acts as the intermediary between the application's business logic and the remote
 * API layer for showings, enabling seamless fetching of showing details and lists of showings
 * based on specific criteria.
 *
 * @constructor Instantiates an instance of ShowingsRepositoryImpl with the specified ShowingsApi.
 * @param showingsApi The API implementation used for accessing showing data.
 */
class ShowingsRepositoryImpl(
    private val showingsApi: ShowingsApi
) : ShowingsRepository {
    /**
     * Fetches the details of a showing by its unique identifier.
     *
     * @param showingId The unique identifier of the showing to retrieve.
     * @return A [ResultOf] wrapping the fetched [Showing] on success or an error representation on failure.
     */
    override suspend fun getShowingById(showingId: Int): ResultOf<Showing> {
        return showingsApi.getShowingById(showingId)
    }

    /**
     * Retrieves a list of showings for a specific movie based on the given movie ID.
     *
     * @param movieId The unique identifier of the movie whose showings need to be fetched.
     * @return A [ResultOf] instance containing either a list of [Showing] objects in case of success,
     *         or an error representation in case of failure.
     */
    override suspend fun getShowingsByMovieId(movieId: Int): ResultOf<List<Showing>> {
        return showingsApi.getShowingsByMovieId(movieId)
    }

    override suspend fun getShowingStateById(id: Int): ResultOf<ShowingStateResponse> {
        return showingsApi.getShowingStateById(id)
    }
}