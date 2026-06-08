package com.ivarvisser.cineapp.ui.feature.movie

import com.ivarvisser.cineapp.domain.Movie


data class MoviesOverviewState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedInstant: kotlin.time.Instant? = null,
) {
    val hasError: Boolean get() = error != null
}
