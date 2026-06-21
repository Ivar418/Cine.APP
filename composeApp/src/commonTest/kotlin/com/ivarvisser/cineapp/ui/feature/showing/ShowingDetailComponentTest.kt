package com.ivarvisser.cineapp.ui.feature.showing

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.ivarvisser.cineapp.domain.Genre
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.domain.Showing
import com.ivarvisser.cineapp.fakes.FakeMoviesRepository
import com.ivarvisser.cineapp.fakes.FakeShowingsRepository
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
import kotlin.test.assertTrue
import kotlin.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class ShowingDetailComponentTest {

    private val lifecycle = LifecycleRegistry()
    private val moviesRepository = FakeMoviesRepository()
    private val showingsRepository = FakeShowingsRepository()
    private val testDispatcher = StandardTestDispatcher()

    private val movieId = 1
    private val showingId = 10

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    private fun createComponent(
        onGoBack: () -> Unit = {},
        onNavigateToOrder: () -> Unit = {}
    ): ShowingDetailComponent {
        return ShowingDetailComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            movieId = movieId,
            showingId = showingId,
            moviesRepository = moviesRepository,
            showingsRepository = showingsRepository,
            onGoBack = onGoBack,
            onNavigateToOrder = onNavigateToOrder
        )
    }

    @Test
    fun loadsMovieShowingAndGenresOnCreate() = runTest(testDispatcher) {
        val movie = Movie(id = movieId, title = "Test Movie", genresIds = listOf(5))
        moviesRepository.movies = mutableListOf(movie)
        moviesRepository.genres[5] = Genre(id = 5, TmdbId = 5, Name = "Drama")

        val showing = Showing(
            id = showingId,
            auditoriumId = 1,
            movieId = movieId,
            is3D = false,
            startsAt = Instant.fromEpochMilliseconds(1700000000000),
            auditoriumLayoutSnapshot = "",
            movie = null,
            auditorium = null
        )
        showingsRepository.showings = mutableListOf(showing)

        val component = createComponent()
        lifecycle.create()
        advanceUntilIdle()

        assertFalse(component.state.value.isLoading)
        assertFalse(component.state.value.mismatch)
        assertEquals("Test Movie", component.state.value.movie?.title)
        assertEquals(1, component.state.value.genres.size)
        assertEquals("Drama", component.state.value.genres[0].Name)
    }

    @Test
    fun setsMismatchWhenShowingBelongsToDifferentMovie() = runTest(testDispatcher) {
        val movie = Movie(id = movieId, title = "Test Movie")
        moviesRepository.movies = mutableListOf(movie)

        val showing = Showing(
            id = showingId,
            auditoriumId = 1,
            movieId = movieId + 1,
            is3D = false,
            startsAt = Instant.fromEpochMilliseconds(1700000000000),
            auditoriumLayoutSnapshot = "",
            movie = null,
            auditorium = null
        )
        showingsRepository.showings = mutableListOf(showing)

        val component = createComponent()
        lifecycle.create()
        advanceUntilIdle()

        assertEquals("Movie and showing data mismatch", component.state.value.error)
    }

    @Test
    fun setsErrorWhenMovieFailsToLoad() = runTest(testDispatcher) {
        val component = createComponent()
        lifecycle.create()
        advanceUntilIdle()

        assertEquals("Failed to load movie", component.state.value.error)
    }

    @Test
    fun goBackTriggersCallback() {
        var backCalled = false
        val component = createComponent(onGoBack = { backCalled = true })

        component.goBack()

        assertTrue(backCalled)
    }

    @Test
    fun navigateToOrderTriggersCallback() {
        var navigated = false
        val component = createComponent(onNavigateToOrder = { navigated = true })

        component.navigateToOrder()

        assertTrue(navigated)
    }
}
