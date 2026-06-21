package com.ivarvisser.cineapp.ui.feature.movie

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.fakes.FakeMoviesRepository
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Test

class MoviesOverviewScreenTest {

    private val lifecycle = LifecycleRegistry()
    private val moviesRepository = FakeMoviesRepository()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testMovieTitleIsDisplayedAndSelectable() =
        runComposeUiTest(effectContext = UnconfinedTestDispatcher()) {
            moviesRepository.movies = mutableListOf(Movie(id = 1, title = "My Awesome Movie"))

            var selectedMovie: Movie? = null
            val component = MoviesOverviewComponent(
                componentContext = DefaultComponentContext(lifecycle = lifecycle),
                repo = moviesRepository,
                onGoBack = {},
                _onMovieSelected = { selectedMovie = it }
            )
            lifecycle.resume()

            setContent {
                MoviesOverviewScreen(component = component)
            }

            onNodeWithText("My Awesome Movie").assertIsDisplayed()
            onNodeWithText("My Awesome Movie").performClick()

            assert(selectedMovie?.id == 1)
        }
}
