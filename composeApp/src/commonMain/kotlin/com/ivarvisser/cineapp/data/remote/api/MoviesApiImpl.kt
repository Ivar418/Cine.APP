package com.ivarvisser.cineapp.data.remote.api

import com.ivarvisser.cineapp.data.remote.util.NetworkConstants.Endpoints.MOVIES
import com.ivarvisser.cineapp.data.remote.util.safeApiCall
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
        val result = client.get("$MOVIES?language=en")
            .body<List<Movie>>()
        Log.debug(loggerName = "MoviesAPIIMPL") { "Debug: Fetched movies: $result from url: $client." }
        result
    }

    override suspend fun getMoviesWithUpcomingShowings(): ResultOf<List<Movie>> = safeApiCall {
        val result = client.get("$MOVIES/future-showings?language=en")
            .body<List<Movie>>()
        Log.debug(loggerName = "MoviesAPIIMPL") { "First item was: ${result.first()}" }
        result
    }

}