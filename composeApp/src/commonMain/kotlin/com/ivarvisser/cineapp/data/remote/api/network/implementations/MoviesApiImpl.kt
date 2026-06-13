package com.ivarvisser.cineapp.data.remote.api.network.implementations

import com.ivarvisser.cineapp.data.remote.api.network.interfaces.MoviesApi
import com.ivarvisser.cineapp.data.remote.util.NetworkConstants
import com.ivarvisser.cineapp.data.remote.util.safeApiCall
import com.ivarvisser.cineapp.domain.Genre
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.utils.ResultOf
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.request
import net.codinux.log.Log
import kotlin.time.Instant

/**
 *
 */
class MoviesApiImpl(
    private val client: HttpClient
) : MoviesApi {
    /**
     * Fetches a list of movies from the remote data source.
     *
     * The method performs a network call to the movies endpoint
     * and retrieves a list of movies wrapped in a [ResultOf].
     * This result can represent either a successful operation with the fetched list of movies
     * or a failure with error details.
     *
     * @return A [ResultOf] wrapping a list of [Movie] objects on success or error details on failure.
     */
    override suspend fun getMovies(): ResultOf<List<Movie>> = safeApiCall {
        val result = client.get("${NetworkConstants.Endpoints.MOVIES}?language=en")
            .body<List<Movie>>()
        Log.debug(loggerName = "MoviesAPIIMPL") { "Debug: Fetched movies: $result." }
        result
    }


    /**
     * Fetches a list of movies that have upcoming showings available.
     *
     * This method performs a network call to retrieve movies with future showings while
     * using a safe API call mechanism to handle any potential exceptions.
     *
     * The result is wrapped in a [ResultOf] class, which will either contain a list of [Movie]
     * objects on success or an error message on failure.
     *
     * @return A [ResultOf] containing a list of [Movie] objects with upcoming showings
     *         or an error if the operation fails.
     */
    override suspend fun getMoviesWithUpcomingShowings(filter: Instant?): ResultOf<List<Movie>> =
        safeApiCall {
            val response = client.get("${NetworkConstants.Endpoints.MOVIES}/future-showings") {
                url {
                    filter?.let {
                        parameters.append("From", it.toString())
                    }
                    parameters.append("language", "en")
                }
            }

            Log.debug(loggerName = "MoviesAPIIMPL") {
                "URL used: ${response.request.url}"
            }

            val result = response.body<List<Movie>>()

            Log.debug(loggerName = "MoviesAPIIMPL") {
                "First item was: ${result.firstOrNull()}"
            }

            result
        }

    /**
     * Retrieves the details of a specific genre by its id.
     *
     * This method performs a remote API call to fetch the details of a genre
     * identified by the provided `genreId`. The returned result is wrapped in
     * a [ResultOf] object, which represents either a success with the genre details
     * or a failure in case of an error during the API call.
     *
     * @param genreId The unique identifier of the genre to retrieve details for.
     * @return A [ResultOf] containing the genre details in case of success or an
     * error description in case of failure.
     */
    override suspend fun getGenreDetails(genreId: Int): ResultOf<Genre> = safeApiCall {
        val result = client.get("${NetworkConstants.Endpoints.MOVIES}/genres/$genreId?language=en")
            .body<Genre>()
        Log.debug(loggerName = "MoviesAPIIMPL") { "Debug: Fetched genres: $result." }
        result
    }

    /**
     * Retrieves a movie by its unique identifier.
     *
     * @param movieId The unique ID of the movie to be fetched.
     * @return A [ResultOf] wrapper containing the requested [Movie] if the operation is successful,
     * or an error message if the operation fails.
     */
    override suspend fun getMovieById(movieId: Int): ResultOf<Movie> =
        safeApiCall {
            val result = client.get("${NetworkConstants.Endpoints.MOVIES}/$movieId?language=en")
                .body<Movie>()
            Log.debug(loggerName = "MoviesAPIIMPL") { "Debug: Fetched movie by ID: $result." }
            result
        }

}