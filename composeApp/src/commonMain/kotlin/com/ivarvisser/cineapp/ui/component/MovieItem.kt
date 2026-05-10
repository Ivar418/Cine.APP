package com.ivarvisser.cineapp.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ivarvisser.cineapp.domain.Movie

@Composable
fun MovieItem(movie: Movie) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w342/" + movie.posterPath,
            contentDescription = "Movie Poster",
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
        )
        Column(modifier = Modifier.padding(16.dp).align(alignment = Alignment.CenterHorizontally)) {
            Text(text = movie.title, style = MaterialTheme.typography.titleLarge)
            if (!movie.about.isNullOrBlank()) {
                Text(text = movie.about, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}