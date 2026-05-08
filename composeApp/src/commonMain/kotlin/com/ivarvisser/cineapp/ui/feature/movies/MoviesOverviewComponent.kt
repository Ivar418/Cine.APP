package com.ivarvisser.cineapp.ui.feature.movies

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import com.ivarvisser.cineapp.utils.ResultOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MoviesOverviewComponent(
    componentContext: ComponentContext,
    private val repo: MoviesRepository,
    private val onGoBack: () -> Unit
) : ComponentContext by componentContext {
    //Defaults needed for every component----------------------------------------------
    private val scope = coroutineScope()

    private val _state = MutableStateFlow(MoviesState())
    val state: StateFlow<MoviesState> = _state.asStateFlow()

    init {
        doOnCreate {
            loadMovies()

        }
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
                when (val movies = repo.getMovies()) {
                    is ResultOf.Success -> {
                        println("Debug: Successfully fetched movies: ${movies.value.size}")
                        _state.value = _state.value.copy(
                            movies = movies.value,
                            error = null
                        )
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