package com.ivarvisser.cineapp.data.repository.interfaces

import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.utils.ResultOf

interface MoviesRepository {
    suspend fun getMovies(): ResultOf<List<Movie>>
    suspend fun getMoviesWithUpcomingShowings(): ResultOf<List<Movie>>
}