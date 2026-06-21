package com.ivarvisser.cineapp.ui.feature.movie

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.ivarvisser.cineapp.domain.Genre
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.domain.Showing
import com.ivarvisser.cineapp.domain.User
import com.ivarvisser.cineapp.fakes.FakeMoviesRepository
import com.ivarvisser.cineapp.fakes.FakeShowingsRepository
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
import kotlin.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsComponentTest {

    private val lifecycle = LifecycleRegistry()
    private val moviesRepository = FakeMoviesRepository()
    private val showingsRepository = FakeShowingsRepository()
    private val usersRepository = FakeUsersRepository()
    private val testDispatcher = StandardTestDispatcher()

    private val movie = Movie(id = 1, title = "Test Movie", genresIds = listOf(10))

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    private fun createComponent(): MovieDetailsComponent {
        return MovieDetailsComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            movie = movie,
            onGoBack = {},
            onNavigateToOrder = { _, _ -> },
            showingsRepository = showingsRepository,
            moviesRepository = moviesRepository,
            usersRepository = usersRepository
        )
    }

    @Test
    fun loadsShowingsAndGenresOnCreate() = runTest(testDispatcher) {
        val showing = Showing(
            id = 1,
            auditoriumId = 1,
            movieId = 1,
            is3D = false,
            startsAt = Instant.fromEpochMilliseconds(1700000000000),
            auditoriumLayoutSnapshot = "",
            movie = null,
            auditorium = null
        )
        showingsRepository.showings = mutableListOf(showing)

        val genre = Genre(id = 10, TmdbId = 10, Name = "Action")
        moviesRepository.genres[10] = genre

        val component = createComponent()
        lifecycle.create()

        advanceUntilIdle()

        assertEquals(1, component.state.value.upcomingShowings.size)
        assertEquals(1, component.state.value.genres.size)
        assertEquals("Action", component.state.value.genres[0].Name)
    }

    @Test
    fun togglesFavoriteStatus() = runTest(testDispatcher) {
        usersRepository.user = User(
            userId = 1,
            userName = "test",
            photoId = null,
            photoUrl = null,
            firstName = "Test",
            lastName = "User",
            email = "test@test.com"
        )

        val component = createComponent()
        lifecycle.create()
        advanceUntilIdle()

        assertFalse(component.state.value.isFavorite)

        component.onFavoriteMoviePress()
        advanceUntilIdle()

        assertTrue(component.state.value.isFavorite)
        assertTrue(usersRepository.favoriteMovieIds.contains(movie.id))

        component.onFavoriteMoviePress()
        advanceUntilIdle()

        assertFalse(component.state.value.isFavorite)
        assertFalse(usersRepository.favoriteMovieIds.contains(movie.id))
    }
}
