package com.ivarvisser.cineapp.fakes

import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import com.ivarvisser.cineapp.domain.Genre
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.utils.ResultOf
import kotlin.time.Instant

class FakeMoviesRepository : MoviesRepository {
    var movies = mutableListOf<Movie>()
    var genres = mutableMapOf<Int, Genre>()
    var error: String? = null

    override suspend fun getMovies(): ResultOf<List<Movie>> {
        return error?.let { ResultOf.Failure(it, null) } ?: ResultOf.Success(movies)
    }

    override suspend fun getMoviesWithUpcomingShowings(filter: Instant?): ResultOf<List<Movie>> {
        return error?.let { ResultOf.Failure(it, null) } ?: ResultOf.Success(movies)
    }

    override suspend fun getGenreDetails(genreId: Int): ResultOf<Genre> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: genres[genreId]?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("Genre not found", null)
    }

    override suspend fun getMovieById(movieId: Int): ResultOf<Movie> {
        return error?.let { ResultOf.Failure(it, null) }
            ?: movies.find { it.id == movieId }?.let { ResultOf.Success(it) }
            ?: ResultOf.Failure("Movie not found", null)
    }
}
