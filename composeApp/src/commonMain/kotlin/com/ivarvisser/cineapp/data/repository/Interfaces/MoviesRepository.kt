package com.ivarvisser.cineapp.data.repository.Interfaces

import com.ivarvisser.cineapp.domain.Movie

interface MoviesRepository {
    suspend fun getMovies(): List<Movie>
}