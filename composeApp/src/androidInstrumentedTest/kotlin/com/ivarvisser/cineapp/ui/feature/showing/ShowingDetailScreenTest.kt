package com.ivarvisser.cineapp.ui.feature.showing

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.domain.Showing
import com.ivarvisser.cineapp.fakes.FakeMoviesRepository
import com.ivarvisser.cineapp.fakes.FakeShowingsRepository
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Test
import kotlin.time.Instant

class ShowingDetailScreenTest {

    private val lifecycle = LifecycleRegistry()
    private val moviesRepository = FakeMoviesRepository()
    private val showingsRepository = FakeShowingsRepository()

    private val movieId = 1
    private val showingId = 10

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testMovieDetailsDisplayedAndOrderButtonNavigates() =
        runComposeUiTest(effectContext = UnconfinedTestDispatcher()) {
            moviesRepository.movies = mutableListOf(Movie(id = movieId, title = "Test Movie"))
            showingsRepository.showings = mutableListOf(
                Showing(
                    id = showingId,
                    auditoriumId = 1,
                    movieId = movieId,
                    is3D = false,
                    startsAt = Instant.fromEpochMilliseconds(1700000000000),
                    auditoriumLayoutSnapshot = "",
                    movie = null,
                    auditorium = null
                )
            )

            var navigated = false
            val component = ShowingDetailComponent(
                componentContext = DefaultComponentContext(lifecycle = lifecycle),
                movieId = movieId,
                showingId = showingId,
                moviesRepository = moviesRepository,
                showingsRepository = showingsRepository,
                onGoBack = {},
                onNavigateToOrder = { navigated = true }
            )
            lifecycle.create()

            setContent {
                ShowingDetailScreen(component = component)
            }

            onNodeWithText("Test Movie").assertIsDisplayed()

            onNodeWithText("Order Tickets").performClick()

            assert(navigated)
        }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testMismatchShowsErrorMessage() =
        runComposeUiTest(effectContext = UnconfinedTestDispatcher()) {
            moviesRepository.movies = mutableListOf(Movie(id = movieId, title = "Test Movie"))
            showingsRepository.showings = mutableListOf(
                Showing(
                    id = showingId,
                    auditoriumId = 1,
                    movieId = movieId + 1,
                    is3D = false,
                    startsAt = Instant.fromEpochMilliseconds(1700000000000),
                    auditoriumLayoutSnapshot = "",
                    movie = null,
                    auditorium = null
                )
            )

            val component = ShowingDetailComponent(
                componentContext = DefaultComponentContext(lifecycle = lifecycle),
                movieId = movieId,
                showingId = showingId,
                moviesRepository = moviesRepository,
                showingsRepository = showingsRepository,
                onGoBack = {},
                onNavigateToOrder = {}
            )
            lifecycle.create()

            setContent {
                ShowingDetailScreen(component = component)
            }

            onNodeWithText("Showing does not belong to this movie").assertIsDisplayed()
        }
}
