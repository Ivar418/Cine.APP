package com.ivarvisser.cineapp.data.repository.implementations

import com.ivarvisser.cineapp.domain.Genre
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.fakes.FakeMoviesApi
import com.ivarvisser.cineapp.utils.ResultOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MoviesRepositoryImplTest {

    private val api = FakeMoviesApi()
    private val repository = MoviesRepositoryImpl(api)

    @Test
    fun getMoviesReturnsApiResult() = runTest {
        api.movies = mutableListOf(Movie(id = 1, title = "Test Movie"))

        val result = repository.getMovies()

        assertTrue(result is ResultOf.Success)
        assertEquals(1, result.value.size)
        assertEquals("Test Movie", result.value[0].title)
    }

    @Test
    fun getMoviesPropagatesFailure() = runTest {
        api.error = "Network error"

        val result = repository.getMovies()

        assertTrue(result is ResultOf.Failure)
        assertEquals("Network error", result.message)
    }

    @Test
    fun getMovieByIdReturnsMovie() = runTest {
        api.movies = mutableListOf(Movie(id = 5, title = "Found Movie"))

        val result = repository.getMovieById(5)

        assertTrue(result is ResultOf.Success)
        assertEquals("Found Movie", result.value.title)
    }

    @Test
    fun getGenreDetailsReturnsGenre() = runTest {
        api.genres[1] = Genre(id = 1, TmdbId = 1, Name = "Action")

        val result = repository.getGenreDetails(1)

        assertTrue(result is ResultOf.Success)
        assertEquals("Action", result.value.Name)
    }
}
