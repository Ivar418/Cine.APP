package com.ivarvisser.cineapp.ui.feature.movies

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import com.ivarvisser.cineapp.data.repository.interfaces.ShowingsRepository
import com.ivarvisser.cineapp.domain.Genre
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.domain.ShowingDisplayResponse
import com.ivarvisser.cineapp.utils.ResultOf
import kotlinx.coroutines.launch
import net.codinux.log.Log

class MovieDetailsComponent(
    componentContext: ComponentContext,
    val movie: Movie,
    private val upcomingShowings: List<ShowingDisplayResponse> = emptyList(),
    private val onGoBack: () -> Unit,
    private val showingsRepository: ShowingsRepository,
    private val moviesRepository: MoviesRepository
) : ComponentContext by componentContext {
    private val scope = coroutineScope()

    private val _state = MutableValue(MovieDetailsState(movie = movie))
    val state: Value<MovieDetailsState> = _state


    init {
        doOnCreate {
            getUpcomingShowingsForMovieByMovieId(movie.id)
            if (!movie.genresIds.isNullOrEmpty()) {
                getGenreNames(movie.genresIds)
            }
        }
    }

    fun onRefresh() {
        getUpcomingShowingsForMovieByMovieId(movie.id)
        if (!movie.genresIds.isNullOrEmpty()) {
            getGenreNames(movie.genresIds)
        }
    }

    fun setLoading(isLoading: Boolean, whatIsLoading: WhatIsLoading) {
        when (whatIsLoading) {
            is WhatIsLoading.Showings -> _state.update { current -> current.copy(isLoadingShowings = isLoading) }
            is WhatIsLoading.Genres -> _state.update { current -> current.copy(isLoadingGenres = isLoading) }
        }
    }

    fun setError(error: String?, whatIsError: WhatIsLoading) {
        _state.update { current -> current.copy(error = error) }
        setLoading(false, whatIsError)
    }

    fun goBack() = onGoBack()
    fun getUpcomingShowingsForMovieByMovieId(movieId: Int) {
        scope.launch {
            try {
                setLoading(true, WhatIsLoading.Showings)
                when (val showings = showingsRepository.getShowingsByMovieId(movieId)) {
                    is ResultOf.Success -> {
                        _state.update { current -> current.copy(upcomingShowings = showings.value) }
                        setLoading(false, WhatIsLoading.Showings)
                    }

                    is ResultOf.Failure -> {
                        setError(showings.message, WhatIsLoading.Showings)
                        return@launch
                    }
                }
            } catch (e: Exception) {
                setError(e.message, WhatIsLoading.Showings)
            }
        }
    }

    fun getGenreNames(genreIds: List<Int>) {
        val result = mutableListOf<Genre>()
        try {
            scope.launch {
                setLoading(true, WhatIsLoading.Genres)
                genreIds.forEach { id ->
                    val genre = moviesRepository.getGenreDetails(id)
                    if (genre is ResultOf.Success) {
                        result.add(genre.value)
                    }
                }
                _state.update { current -> current.copy(genres = result.toList()) }
                Log.debug(loggerName = "MovieDetailsComponent") { "Debug: GenresResult: $result" }
                Log.debug(loggerName = "MovieDetailsComponent") { "Debug2: GenresState: ${state.value.genres}" }
                setLoading(false, WhatIsLoading.Genres)

            }
        } catch (e: Exception) {
            setError(e.message, WhatIsLoading.Genres)
        }
    }
}