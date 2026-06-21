package com.ivarvisser.cineapp.ui.feature.favorite

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.fakes.FakeMoviesRepository
import com.ivarvisser.cineapp.fakes.FakeUsersRepository
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Test

class FavoritesScreenTest {

    private val lifecycle = LifecycleRegistry()
    private val usersRepository = FakeUsersRepository()
    private val moviesRepository = FakeMoviesRepository()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testFavoriteMovieIsDisplayed() =
        runComposeUiTest(effectContext = UnconfinedTestDispatcher()) {
            val movie = Movie(id = 1, title = "Favorite Movie")
            moviesRepository.movies = mutableListOf(movie)
            usersRepository.favoriteMovieIds = mutableListOf(1)

            val component = FavoritesComponent(
                componentContext = DefaultComponentContext(lifecycle = lifecycle),
                usersRepository = usersRepository,
                moviesRepository = moviesRepository,
                onGoBack = {},
                onMovieSelected = {}
            )
            lifecycle.create()

            setContent {
                FavoritesScreen(component = component)
            }

            onNodeWithText("Favorite Movie").assertIsDisplayed()
        }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testEmptyFavoritesShowsPlaceholder() =
        runComposeUiTest(effectContext = UnconfinedTestDispatcher()) {
            val component = FavoritesComponent(
                componentContext = DefaultComponentContext(lifecycle = lifecycle),
                usersRepository = usersRepository,
                moviesRepository = moviesRepository,
                onGoBack = {},
                onMovieSelected = {}
            )
            lifecycle.create()

            setContent {
                FavoritesScreen(component = component)
            }

            onNodeWithText("haven't added any favorites", substring = true).assertIsDisplayed()
        }
}
