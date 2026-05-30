package com.ivarvisser.cineapp.ui.feature.showing

import com.ivarvisser.cineapp.domain.Genre
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.domain.Showing

data class ShowingDetailUiState(
    val isLoading: Boolean = true,
    val movie: Movie? = null,
    val showing: Showing? = null,
    val mismatch: Boolean = false,
    val genres: List<Genre> = emptyList(),
    val error: String? = null
) {
    val hasError: Boolean get() = error != null
}