package com.ivarvisser.cineapp.ui.feature.favorite

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnResume
import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import com.ivarvisser.cineapp.data.repository.interfaces.UsersRepository
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.utils.ResultOf
import kotlinx.coroutines.launch
import net.codinux.log.Log

class FavoritesComponent(
    componentContext: ComponentContext,
    private val usersRepository: UsersRepository,
    private val moviesRepository: MoviesRepository,
    private val onGoBack: () -> Unit,
    private val onMovieSelected: (Movie) -> Unit
) : ComponentContext by componentContext {
    private val scope = coroutineScope()
    private val _state = MutableValue(FavoritesState())
    val state: Value<FavoritesState> = _state

    init {
        doOnCreate {
            loadFavorites()
        }
        doOnResume {
            loadFavorites()
        }
    }

    fun loadFavorites() {
        scope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val favoritesResult = usersRepository.getFavoriteMovies()) {
                is ResultOf.Success -> {
                    val favoriteMovieIds = favoritesResult.value.favoriteMovies.map { it.movieId }
                    val movies = mutableListOf<Movie>()

                    for (id in favoriteMovieIds) {
                        when (val movieResult = moviesRepository.getMovieById(id)) {
                            is ResultOf.Success -> movies.add(movieResult.value)
                            is ResultOf.Failure -> {
                                Log.debug(loggerName = "FavoritesComponent") { "Failed to load movie with ID $id: ${movieResult.message}" }
                            }
                        }
                    }

                    _state.update { it.copy(isLoading = false, movies = movies) }
                }

                is ResultOf.Failure -> {
                    _state.update { it.copy(isLoading = false, error = favoritesResult.message) }
                }
            }
        }
    }

    fun goBack() {
        onGoBack()
    }

    fun movieSelected(movie: Movie) {
        onMovieSelected(movie)
    }
}
