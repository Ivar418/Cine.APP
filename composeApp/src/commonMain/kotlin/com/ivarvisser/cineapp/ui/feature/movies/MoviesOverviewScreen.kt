package com.ivarvisser.cineapp.ui.feature.movies

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ivarvisser.cineapp.ui.component.MovieList

@Composable
fun MoviesOverviewScreen(component: MoviesOverviewComponent) {
    val uiState by component.uiState.collectAsState()

    LaunchedEffect(Unit) {
        component.loadMovies()
    }

    MovieList(
        movies = uiState.movies
    )

    DisposableEffect(Unit) {
        onDispose {
            component.clear()
        }
    }
}