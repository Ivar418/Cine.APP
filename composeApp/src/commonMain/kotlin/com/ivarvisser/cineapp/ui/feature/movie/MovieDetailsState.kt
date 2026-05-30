package com.ivarvisser.cineapp.ui.feature.movie

import com.ivarvisser.cineapp.domain.Genre
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.domain.Showing

data class MovieDetailsState(
    val movie: Movie,
    val genres: List<Genre> = emptyList(),
    val upcomingShowings: List<Showing> = emptyList(),
    val isLoadingShowings: Boolean = false,
    val isLoadingGenres: Boolean = false,
    val error: String? = null
) {
    val hasError: Boolean get() = error != null
    val isLoading = isLoadingShowings || isLoadingGenres

}

sealed class WhatIsLoading {
    object Showings : WhatIsLoading()
    object Genres : WhatIsLoading()
}