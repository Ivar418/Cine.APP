package com.ivarvisser.cineapp.ui.feature.favorite

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.fakes.FakeMoviesRepository
import com.ivarvisser.cineapp.fakes.FakeUsersRepository
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

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesComponentTest {

    private val lifecycle = LifecycleRegistry()
    private val usersRepository = FakeUsersRepository()
    private val moviesRepository = FakeMoviesRepository()
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    private fun createComponent(onMovieSelected: (Movie) -> Unit = {}): FavoritesComponent {
        return FavoritesComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            usersRepository = usersRepository,
            moviesRepository = moviesRepository,
            onGoBack = {},
            onMovieSelected = onMovieSelected
        )
    }

    @Test
    fun loadsFavoriteMoviesOnCreate() = runTest(testDispatcher) {
        val movie = Movie(id = 1, title = "Favorite Movie")
        moviesRepository.movies = mutableListOf(movie)
        usersRepository.favoriteMovieIds = mutableListOf(1)

        val component = createComponent()
        lifecycle.create()
        advanceUntilIdle()

        assertFalse(component.state.value.isLoading)
        assertEquals(1, component.state.value.movies.size)
        assertEquals("Favorite Movie", component.state.value.movies[0].title)
    }

    @Test
    fun showsErrorWhenFavoritesFail() = runTest(testDispatcher) {
        usersRepository.error = "Failed to load favorites"

        val component = createComponent()
        lifecycle.create()
        advanceUntilIdle()

        assertFalse(component.state.value.isLoading)
        assertEquals("Failed to load favorites", component.state.value.error)
    }

    @Test
    fun movieSelectedTriggersCallback() {
        var selected: Movie? = null
        val movie = Movie(id = 1, title = "Test Movie")
        val component = createComponent(onMovieSelected = { selected = it })

        component.movieSelected(movie)

        assertEquals(movie, selected)
    }

    @Test
    fun goBackTriggersCallback() {
        var backCalled = false
        val component = FavoritesComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            usersRepository = usersRepository,
            moviesRepository = moviesRepository,
            onGoBack = { backCalled = true },
            onMovieSelected = {}
        )

        component.goBack()

        assertTrue(backCalled)
    }
}
