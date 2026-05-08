package com.ivarvisser.cineapp.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ivarvisser.cineapp.domain.Movie

@Composable
fun MovieList(
    movies: List<Movie>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(movies) { movie ->
            MovieItem(movie = movie)
        }
    }
}