package com.ivarvisser.cineapp.ui.feature.movie

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.ivarvisser.cineapp.data.repository.interfaces.MoviesRepository
import com.ivarvisser.cineapp.data.repository.interfaces.ShowingsRepository
import com.ivarvisser.cineapp.data.repository.interfaces.UsersRepository
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
    private val onNavigateToOrder: (showingId: Int, movieId: Int) -> Unit,
    private val showingsRepository: ShowingsRepository,
    private val moviesRepository: MoviesRepository,
    private val usersRepository: UsersRepository
) : ComponentContext by componentContext {
    private val scope = coroutineScope()

    private val _state = MutableValue(MovieDetailsState(movie = movie))
    val state: Value<MovieDetailsState> = _state


    init {

        doOnCreate {
            getUpcomingShowingsForMovieByMovieId(movie.id)
            if (!movie.genresIds.isNullOrEmpty()) {
                getGenreNames(movie.genresIds)
                isFavorite()
                isLoggedIn()
            }
        }
    }

    fun onRefresh() {
        getUpcomingShowingsForMovieByMovieId(movie.id)
        if (!movie.genresIds.isNullOrEmpty()) {
            getGenreNames(movie.genresIds)
        }
        isFavorite()
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
                setLoading(false, WhatIsLoading.Genres)

            }
        } catch (e: Exception) {
            setError(e.message, WhatIsLoading.Genres)
        }
    }

    fun isLoggedIn() {
        try {
            scope.launch {
                val isLoggedIn = usersRepository.isLoggedIn()
                _state.update { current -> current.copy(isLoggedIn = isLoggedIn) }
                Log.debug(loggerName = "MovieDetailsComponent") { "Debug: User is logged in: $isLoggedIn" }
            }
        } catch (e: Exception) {
            Log.error(loggerName = "MovieDetailsComponent") { "Error checking if user is logged in: ${e.message}" }
        }
    }

    fun isFavorite() {
        try {
            scope.launch {
                Log.debug(loggerName = "MovieDetailsComponent") { "Debug: Checking if movie is favorite" }
                val isFavoriteResult = usersRepository.getFavoriteMovies()
                var isFavoriteCheck: Boolean
                if (isFavoriteResult is ResultOf.Success) {
                    isFavoriteCheck =
                        isFavoriteResult.value.favoriteMovies.any { it.movieId == movie.id }
                    _state.update { current -> current.copy(isFavorite = isFavoriteCheck) }
                } else isFavoriteCheck = false
                _state.update { current -> current.copy(isFavorite = isFavoriteCheck) }
            }
            Log.debug(loggerName = "MovieDetailsComponent") { "Debug: Movie is favorite: ${state.value.isFavorite}" }
        } catch (e: Exception) {
            Log.error(loggerName = "MovieDetailsComponent") { "Error checking if movie is favorite: ${e.message}" }
        }
    }

    fun onFavoriteMoviePress() {
        Log.debug(loggerName = "MovieDetailsComponent") { "Debug: Favorite button pressed" }
        if (state.value.isFavorite) {
            removeFavorite()
        } else {
            addFavorite()
        }

    }

    fun addFavorite() {
        try {
            scope.launch {
                val result = usersRepository.addFavoriteMovie(movie.id)
                if (result is ResultOf.Success) {
                    isFavorite()
                }
            }
        } catch (e: Exception) {
            Log.error(loggerName = "MovieDetailsComponent") { "Error adding favorite: ${e.message}" }
        }
    }

    fun removeFavorite() {
        try {
            scope.launch {
                val result = usersRepository.removeFavoriteMovie(movie.id)
                if (result is ResultOf.Success) {
                    isFavorite()
                }
            }
        } catch (e: Exception) {
            Log.error(loggerName = "MovieDetailsComponent") { "Error removing favorite: ${e.message}" }
        }
    }

    fun onShowingSelected(showingId: Int, movieId: Int) {
        onNavigateToOrder(showingId, movieId)
    }
}