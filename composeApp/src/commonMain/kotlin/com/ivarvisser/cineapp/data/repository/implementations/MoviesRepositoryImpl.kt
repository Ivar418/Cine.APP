package com.ivarvisser.cineapp.data.repository.implementations

import com.ivarvisser.cineapp.data.remote.api.network.interfaces.MoviesApi
import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import com.ivarvisser.cineapp.domain.Genre
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.utils.ResultOf
import kotlin.time.Instant

/**
 * Implementation of the MoviesRepository interface, providing methods to interact with movie-related data
 * by using a MoviesApi instance as the data source.
 *
 * This repository is responsible for retrieving movie data, fetching genres, and handling requests for
 * specific movies.
 *
 * @constructor Creates an instance of MoviesRepositoryImpl with the provided MoviesApi.
 * @param api The API implementation used for retrieving movie data.
 */
class MoviesRepositoryImpl(
    private val api: MoviesApi
) : MoviesRepository {

    /**
     *
     */
    override suspend fun getMovies(): ResultOf<List<Movie>> {
        return api.getMovies()
    }

    /**
     * Retrieves a list of movies that have upcoming showings scheduled.
     *
     * @return A [ResultOf] containing either a list of [Movie] objects with upcoming showings
     *         in case of success, or a failure result indicating an error.
     */
    override suspend fun getMoviesWithUpcomingShowings(filter: Instant?): ResultOf<List<Movie>> {
        return api.getMoviesWithUpcomingShowings(filter)
    }

    /**
     * Retrieves the details of a genre based on the provided genre ID.
     *
     * @param genreId The unique identifier of the genre to retrieve details for.
     * @return A [ResultOf] instance containing the details of the genre as [Genre]
     *         in case of success, or error information in case of failure.
     */
    override suspend fun getGenreDetails(genreId: Int): ResultOf<Genre> {
        return api.getGenreDetails(genreId)
    }

    /**
     * Fetches a movie by its unique identifier.
     *
     * @param movieId The unique identifier of the movie to retrieve.
     * @return A [ResultOf] wrapping the fetched [Movie] on success, or an error representation on failure.
     */
    override suspend fun getMovieById(movieId: Int): ResultOf<Movie> {
        return api.getMovieById(movieId)
    }
}
