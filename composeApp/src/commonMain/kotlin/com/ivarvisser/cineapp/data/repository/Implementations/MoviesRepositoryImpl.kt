package com.ivarvisser.cineapp.data.repository.Implementations

import com.ivarvisser.cineapp.data.remote.api.MoviesApi
import com.ivarvisser.cineapp.data.repository.Interfaces.MoviesRepository
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.utils.ResultOf

class MoviesRepositoryImpl(
    private val api: MoviesApi
) : MoviesRepository {

    override suspend fun getMovies(): List<Movie> {
        return try {
            val movies = api.getMovies()
            println("Debug: MoviesRepositoryImpl.getMovies() - API call result: $movies")
            when (movies) {
                is ResultOf.Success -> {
                    movies.value
                }

                is ResultOf.Failure -> {
                    emptyList<Movie>()
                }
            }

        } catch (e: Exception) {
            return emptyList<Movie>()
        }
    }
}