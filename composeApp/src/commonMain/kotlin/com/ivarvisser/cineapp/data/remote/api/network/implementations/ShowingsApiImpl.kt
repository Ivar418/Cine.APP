package com.ivarvisser.cineapp.data.remote.api.network.implementations

import com.ivarvisser.cineapp.data.dto.ShowingStateResponse
import com.ivarvisser.cineapp.data.dto.ShowingsWithPricesResponse
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.ShowingsApi
import com.ivarvisser.cineapp.data.remote.util.NetworkConstants.Endpoints.SHOWINGS
import com.ivarvisser.cineapp.data.remote.util.safeApiCall
import com.ivarvisser.cineapp.domain.Showing
import com.ivarvisser.cineapp.utils.ResultOf
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import net.codinux.log.Log

/**
 * Implementation of the ShowingsApi interface, providing functions to retrieve showing details from the API.
 *
 * @constructor Initializes the implementation with an instance of HttpClient.
 * @property client The HTTP client used to make API calls.
 */
class ShowingsApiImpl(
    private val client: HttpClient
) : ShowingsApi {
    /**
     * Retrieves a showing by its unique identifier.
     *
     * @param id The unique identifier of the showing to fetch.
     * @return A [ResultOf] containing the [Showing] if the operation is successful, or an error if it fails.
     */
    override suspend fun getShowingById(id: Int): ResultOf<Showing> =
        safeApiCall {
            Log.debug(loggerName = "SHOWINGSAPIIMPL") { "Test: Fetching showing by id: $id." }
            val result = client.get("$SHOWINGS/$id").body<Showing>()
            Log.debug(loggerName = "SHOWINGSAPIIMPL") { "Debug: Fetched showing by id: $id: $result." }
            result
        }

    /**
     * Fetches a list of upcoming showings for the specified movie ID.
     *
     * @param movieId The unique identifier of the movie for which showings are to be retrieved.
     * @return A ResultOf containing either a list of showings as a successful result
     *         or an error as a failure result.
     */
    override suspend fun getShowingsByMovieId(movieId: Int): ResultOf<List<Showing>> =
        safeApiCall {
            Log.debug(loggerName = "SHOWINGSAPIIMPL") { "Test: Fetching showings for movieId: $movieId." }
            val result =
                client.get("$SHOWINGS/movie/$movieId/upcoming")
                    .body<List<Showing>>()
            Log.debug(loggerName = "SHOWINGSAPIIMPL") { "Debug: Fetched showings for movieId: $movieId: $result." }
            result
        }

    /**
     * Fetches the showing state for a specific showing by its unique identifier.
     * The showing state includes information about the showing, all available seats,
     * and a set of occupied seat keys.
     *
     * @param id The unique identifier of the showing whose state is to be fetched.
     * @return A [ResultOf] containing the [ShowingStateResponse] if the operation is successful,
     *         or an error if it fails.
     */
    override suspend fun getShowingStateById(id: Int): ResultOf<ShowingStateResponse> =
        safeApiCall {
            val result = client.get("$SHOWINGS/$id/state").body<ShowingStateResponse>()
            Log.debug(loggerName = "SHOWINGSAPIIMPL") { "Debug: Fetched showing state by id: $id: $result." }
            result
        }

    override suspend fun getShowingPrices(id: Int): ResultOf<ShowingsWithPricesResponse> =
        safeApiCall {
            Log.debug(loggerName = "SHOWINGSAPIIMPL") { "Fetching showing prices for id: $id" }
            // Endpoint: GET /api/showings/{id}/prices
            val result = client.get("$SHOWINGS/$id/prices")
                .body<ShowingsWithPricesResponse>()
            Log.debug(loggerName = "SHOWINGSAPIIMPL") { "Fetched showing prices for id: $id: $result" }
            result
        }


}