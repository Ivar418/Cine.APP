package com.ivarvisser.cineapp.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ivarvisser.cineapp.domain.Movie

@Composable
fun MovieItem(movie: Movie) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = movie.title, style = MaterialTheme.typography.titleLarge)
            if (!movie.about.isNullOrBlank()) {
                Text(text = movie.about, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}