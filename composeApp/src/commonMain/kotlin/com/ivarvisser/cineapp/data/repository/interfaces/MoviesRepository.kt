package com.ivarvisser.cineapp.data.repository.interfaces

import com.ivarvisser.cineapp.domain.Genre
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.utils.ResultOf
import kotlin.time.Instant

interface MoviesRepository {
    suspend fun getMovies(): ResultOf<List<Movie>>
    suspend fun getMoviesWithUpcomingShowings(filter: Instant?): ResultOf<List<Movie>>
    suspend fun getGenreDetails(genreId: Int): ResultOf<Genre>
    suspend fun getMovieById(movieId: Int): ResultOf<Movie>
}