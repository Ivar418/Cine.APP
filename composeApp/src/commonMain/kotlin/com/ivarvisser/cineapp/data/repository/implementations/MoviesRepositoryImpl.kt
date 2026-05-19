package com.ivarvisser.cineapp.data.repository.implementations

import com.ivarvisser.cineapp.data.remote.api.network.interfaces.MoviesApi
import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import com.ivarvisser.cineapp.domain.Genre
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.utils.ResultOf

class MoviesRepositoryImpl(
    private val api: MoviesApi
) : MoviesRepository {

    override suspend fun getMovies(): ResultOf<List<Movie>> {
        return api.getMovies()
    }

    override suspend fun getMoviesWithUpcomingShowings(): ResultOf<List<Movie>> {
        return api.getMoviesWithUpcomingShowings()
    }

    override suspend fun getGenreDetails(genreId: Int): ResultOf<Genre> {
        return api.getGenreDetails(genreId)
    }
}
