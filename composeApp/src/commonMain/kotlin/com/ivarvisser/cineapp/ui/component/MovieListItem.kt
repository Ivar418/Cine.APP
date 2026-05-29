package com.ivarvisser.cineapp.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.movie_poster_desc
import coil3.compose.AsyncImage
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.theming.BrandColors
import org.jetbrains.compose.resources.stringResource

@Composable
fun MovieListItem(
    movie: Movie,
    onMovieClick: (Movie) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(8.dp)
            .clickable(onClick = { onMovieClick(movie) })
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w342/" + movie.posterPath,
            contentDescription = stringResource(Res.string.movie_poster_desc),
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
        )
        Column(modifier = Modifier.padding(16.dp).align(alignment = Alignment.CenterHorizontally)) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
            HorizontalDivider(
                thickness = 2.dp,
                modifier = Modifier.padding(8.dp),
                color = BrandColors.SoftDivider
            )
            if (!movie.about.isNullOrBlank()) {
                Text(
                    text = movie.about,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                )
            }
        }
    }
}