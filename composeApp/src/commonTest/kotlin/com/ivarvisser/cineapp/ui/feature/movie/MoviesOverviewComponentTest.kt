package com.ivarvisser.cineapp.ui.feature.movie

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.fakes.FakeMoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesOverviewComponentTest {

    private val lifecycle = LifecycleRegistry()
    private val moviesRepository = FakeMoviesRepository()
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    private fun createComponent(onMovieSelected: (Movie) -> Unit = {}): MoviesOverviewComponent {
        return MoviesOverviewComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            repo = moviesRepository,
            onGoBack = {},
            _onMovieSelected = onMovieSelected
        )
    }

    @Test
    fun loadsMoviesOnResume() = runTest(testDispatcher) {
        val movie = Movie(id = 1, title = "Test Movie")
        moviesRepository.movies = mutableListOf(movie)

        val component = createComponent()
        lifecycle.resume()

        // assertTrue(component.state.value.isLoading)

        advanceUntilIdle()

        assertFalse(component.state.value.isLoading)
        assertEquals(1, component.state.value.movies.size)
        assertEquals("Test Movie", component.state.value.movies[0].title)
    }

    @Test
    fun showsErrorOnFailure() = runTest(testDispatcher) {
        moviesRepository.error = "Error"

        val component = createComponent()
        lifecycle.resume()

        advanceUntilIdle()

        assertFalse(component.state.value.isLoading)
        assertEquals("Error", component.state.value.error)
    }

    @Test
    fun triggersMovieSelection() = runTest(testDispatcher) {
        var selectedMovie: Movie? = null
        val movie = Movie(id = 1, title = "Test Movie")

        val component = createComponent(onMovieSelected = { selectedMovie = it })

        component.onMovieSelected(movie)

        assertEquals(movie, selectedMovie)
    }
}
