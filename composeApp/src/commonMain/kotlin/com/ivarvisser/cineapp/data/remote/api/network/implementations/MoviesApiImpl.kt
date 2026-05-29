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
import net.codinux.log.Log

class MoviesApiImpl(
    private val client: HttpClient
) : MoviesApi {
    override suspend fun getMovies(): ResultOf<List<Movie>> = safeApiCall {
        val result = client.get("${NetworkConstants.Endpoints.MOVIES}?language=en")
            .body<List<Movie>>()
        Log.debug(loggerName = "MoviesAPIIMPL") { "Debug: Fetched movies: $result." }
        result
    }

    override suspend fun getMoviesWithUpcomingShowings(): ResultOf<List<Movie>> = safeApiCall {
        val result = client.get("${NetworkConstants.Endpoints.MOVIES}/future-showings?language=en")
            .body<List<Movie>>()
        Log.debug(loggerName = "MoviesAPIIMPL") { "First item was: ${result.first()}" }
        result
    }

    override suspend fun getGenreDetails(genreId: Int): ResultOf<Genre> = safeApiCall {
        val result = client.get("${NetworkConstants.Endpoints.MOVIES}/genres/$genreId?language=en")
            .body<Genre>()
        Log.debug(loggerName = "MoviesAPIIMPL") { "Debug: Fetched genres: $result." }
        result
    }

    override suspend fun getMovieById(movieId: Int): ResultOf<Movie> =
        safeApiCall {
            val result = client.get("${NetworkConstants.Endpoints.MOVIES}/$movieId?language=en")
                .body<Movie>()
            Log.debug(loggerName = "MoviesAPIIMPL") { "Debug: Fetched movie by ID: $result." }
            result
        }

}