package com.ivarvisser.cineapp.ui.feature.movies

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.utils.ResultOf
import kotlinx.coroutines.launch

class MoviesOverviewComponent(
    componentContext: ComponentContext,
    private val repo: MoviesRepository,
    private val onGoBack: () -> Unit,
    private val _onMovieSelected: (Movie) -> Unit
) : ComponentContext by componentContext {
    //Defaults needed for every component----------------------------------------------
    private val scope = coroutineScope()

    private val _state = MutableValue(MoviesOverviewState())
    val state: Value<MoviesOverviewState> = _state

    init {
        doOnCreate {
            loadMovies()

        }
    }

    fun onMovieSelected(movie: Movie) {
        _onMovieSelected(movie)
    }

    fun onRefresh() {
        loadMovies()
    }

    fun setLoading(isLoading: Boolean) {
        _state.value = _state.value.copy(isLoading = isLoading)
    }

    fun setError(error: String?) {
        _state.value = _state.value.copy(error = error)
        setLoading(false)
    }

    fun goBack() {
        onGoBack()
    }
    //--------------------------------------------------------------------------------

    fun loadMovies() {
        scope.launch {
            try {
                setLoading(true)
                when (val movies = repo.getMoviesWithUpcomingShowings()) {
                    is ResultOf.Success -> {
                        println("Debug: Successfully fetched movies: ${movies.value.size}")
                        _state.update {
                            _state.value.copy(
                                movies = movies.value,
                                error = null
                            )
                        }

                        setLoading(false)
                        println("Debug: loaded movies: ${movies.value.size}")
                    }

                    is ResultOf.Failure -> {
                        setError(movies.message)
                        return@launch
                    }
                }
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }
}