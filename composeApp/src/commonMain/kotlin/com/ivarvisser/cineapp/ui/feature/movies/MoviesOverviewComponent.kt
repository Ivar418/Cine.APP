package com.ivarvisser.cineapp.ui.feature.movies

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.ivarvisser.cineapp.data.repository.Interfaces.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MoviesOverviewComponent(
    componentContext: ComponentContext,
    private val repo: MoviesRepository
) : ComponentContext by componentContext {

    private val scope = coroutineScope()

    private val _uiState = MutableStateFlow(MoviesState())
    val uiState: StateFlow<MoviesState> = _uiState.asStateFlow()

    init {
        lifecycle.doOnCreate {
            loadMovies()
        }
    }


    fun loadMovies() {
        scope.launch {
            try {
                val movies = repo.getMovies()
                _uiState.value = _uiState.value.copy(
                    movies = movies,
                    error = null
                )
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
}