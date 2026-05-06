package com.ivarvisser.cineapp.data.remote.api

import com.ivarvisser.cineapp.data.remote.util.NetworkConstants.Endpoints.MOVIES
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.utils.ResultOf
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class MoviesApiImpl(
    private val client: HttpClient
) : MoviesApi {
    override suspend fun getMovies(): ResultOf<List<Movie>> {
        return try {
            val result = client.get(MOVIES)
                .body<List<Movie>>()
            println("Debug: Fetched movies: $result")
            ResultOf.Success(result)

        } catch (e: Exception) {
            ResultOf.Failure(
                message = e.message ?: "Failed to load movie list",
                throwable = e
            )
        }
    }
}