package com.ivarvisser.cineapp.ui.feature.movies

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.ivarvisser.cineapp.ui.component.ErrorMessage
import com.ivarvisser.cineapp.ui.component.MovieList

@Composable
fun MoviesOverviewScreen(
    component: MoviesOverviewComponent,
) {
    val state by component.state.subscribeAsState()
    Column {
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        if (state.error != null) {
            ErrorMessage(
                message = state.error ?: "An unknown error occurred",
                onRetry = { component.onRefresh() }
            )
        }
        MovieList(
            movies = state.movies,
            modifier = Modifier.fillMaxHeight(),
            onMovieClick = { component.onMovieSelected(it) }
        )
    }
}
