package com.ivarvisser.cineapp.ui.feature.movies

import com.arkivanov.decompose.ComponentContext
import com.ivarvisser.cineapp.data.repository.Interfaces.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MoviesOverviewComponent(
    componentContext: ComponentContext,
    private val repo: MoviesRepository
) : ComponentContext by componentContext {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _uiState = MutableStateFlow(MoviesState())
    val uiState: StateFlow<MoviesState> = _uiState.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies() {
        scope.launch {
            try {
                val movies = repo.getMovies()
                _uiState.value = MoviesState(movies = movies)
                println("Debug: loaded movies: ${movies.size}")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message
                )
            }
        }
    }

    fun onRefresh() {
        loadMovies()
    }

    fun clear() {
        scope.cancel()
    }
}