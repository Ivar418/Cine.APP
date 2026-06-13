package com.ivarvisser.cineapp.ui.feature.favorite

import com.ivarvisser.cineapp.domain.Movie

data class FavoritesState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val movies: List<Movie> = emptyList()
)
