package com.ivarvisser.cineapp.ui.feature.showing

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import com.ivarvisser.cineapp.data.repository.interfaces.ShowingsRepository
import com.ivarvisser.cineapp.domain.Genre
import com.ivarvisser.cineapp.utils.ResultOf
import kotlinx.coroutines.launch
import net.codinux.log.Log

class ShowingDetailComponent(
    componentContext: ComponentContext,
    private val movieId: Int,
    private val showingId: Int,
    private val moviesRepository: MoviesRepository,
    private val showingsRepository: ShowingsRepository,
    private val onGoBack: () -> Unit,
    private val onNavigateToOrder: () -> Unit

) : ComponentContext by componentContext {

    private val scope = coroutineScope()

    private val _state = MutableValue(
        ShowingDetailUiState(),
    )
    val state: Value<ShowingDetailUiState> = _state

    init {
        doOnCreate {
            load()
        }
    }

    fun onRefresh() {
        load()
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

    private fun load() {
        scope.launch {
            try {
                setLoading(true)

                val movie = when (val result = moviesRepository.getMovieById(movieId)) {
                    is ResultOf.Success -> result.value
                    is ResultOf.Failure -> {
                        setError("Failed to load movie")
                        return@launch
                    }
                }

                val showing = when (val result = showingsRepository.getShowingById(showingId)) {
                    is ResultOf.Success -> result.value
                    is ResultOf.Failure -> {
                        setError("Failed to load showing")
                        return@launch
                    }
                }
                val mismatch = showing.movieId != movie.id

                if (mismatch) {
                    setError("Movie and showing data mismatch")
                    return@launch
                }
                val genres = mutableListOf<Genre>()

                movie.genresIds?.forEach { genreId ->
                    Log.debug(loggerName = "ShowingDetailComponent") { "Debug: genreId: $genreId" }
                    val genre = moviesRepository.getGenreDetails(genreId)
                    if (genre is ResultOf.Success) {
                        genres.add(genre.value)
                    } else {
                        return@forEach
                    }

                }

                _state.update { current -> current.copy(genres = genres) }

                _state.value = ShowingDetailUiState(
                    isLoading = false,
                    movie = movie,
                    showing = showing,
                    mismatch = mismatch,
                    genres = genres
                )
            } catch (e: Exception) {
                _state.value = ShowingDetailUiState(
                    isLoading = false
                )
            }
        }
    }

    fun navigateToOrder() {
        onNavigateToOrder()
    }

    fun posterUrl(): String {
        return "https://image.tmdb.org/t/p/w500${state.value.movie?.posterPath.orEmpty()}"
    }
}