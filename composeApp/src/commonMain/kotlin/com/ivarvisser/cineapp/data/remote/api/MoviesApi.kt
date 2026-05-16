package com.ivarvisser.cineapp.data.remote.api

import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.utils.ResultOf

interface MoviesApi {
    suspend fun getMovies(): ResultOf<List<Movie>>
    suspend fun getMoviesWithUpcomingShowings(): ResultOf<List<Movie>>

}