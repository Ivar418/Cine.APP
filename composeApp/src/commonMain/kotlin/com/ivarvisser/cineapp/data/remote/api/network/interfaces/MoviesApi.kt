package com.ivarvisser.cineapp.data.remote.api.network.interfaces

import com.ivarvisser.cineapp.domain.Genre
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.utils.ResultOf

interface MoviesApi {
    suspend fun getMovies(): ResultOf<List<Movie>>
    suspend fun getMoviesWithUpcomingShowings(): ResultOf<List<Movie>>
    suspend fun getGenreDetails(genreId: Int): ResultOf<Genre>
    suspend fun getMovieById(movieId: Int): ResultOf<Movie>

}