package com.ivarvisser.cineapp.ui.feature.movies

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.ivarvisser.cineapp.theming.BrandColors
import com.ivarvisser.cineapp.ui.component.ErrorMessage
import com.ivarvisser.cineapp.ui.component.ExpandablePanel

@Composable
fun MovieItemDetailsScreen(
    component: MovieDetailsComponent,
) {
    val state by component.state.subscribeAsState()
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
    if (!state.isLoading && !state.hasError) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Row {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w154/" + state.movie.posterPath,
                        contentDescription = "Movie Poster",
                        modifier = Modifier.align(alignment = Alignment.Top)
                            .padding(top = 16.dp, bottom = 5.dp, start = 16.dp, end = 16.dp),
                    )
                    Text(
                        text = state.movie.title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                            .padding(start = 16.dp)
                    )
                }
                Column(
                    modifier = Modifier.padding(16.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                ) {

                    HorizontalDivider(
                        thickness = 2.dp,
                        modifier = Modifier.padding(8.dp),
                        color = BrandColors.SoftDivider
                    )
                    if (!state.movie.about.isNullOrBlank()) {
                        ExpandablePanel(text = state.movie.about!!)
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Genres",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    LazyRow {
                        items(state.genres.size) { index ->
                            SuggestionChip(
                                onClick = { },
                                label = { Text(state.genres[index].Name) },
                                modifier = Modifier.padding(2.dp),
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = BrandColors.DarkOrange,
                                    labelColor = Color.Black
                                )
                            )
                        }
                    }
                    Row {
                        SuggestionChip(
                            onClick = { },
                            label = { Text("Taal:${state.movie.language}") },
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
                Row { //Here all the future showings will be shown }

                }
            }
        }
    }
}
