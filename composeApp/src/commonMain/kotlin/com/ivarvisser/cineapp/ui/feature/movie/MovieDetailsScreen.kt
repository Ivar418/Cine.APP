package com.ivarvisser.cineapp.ui.feature.movie

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.bookmark
import cineapp.composeapp.generated.resources.bookmark_check
import cineapp.composeapp.generated.resources.item_auditorium_fallback
import cineapp.composeapp.generated.resources.item_genre
import cineapp.composeapp.generated.resources.item_trailer
import cineapp.composeapp.generated.resources.item_upcoming_showings
import cineapp.composeapp.generated.resources.lang_en
import cineapp.composeapp.generated.resources.lang_nl
import cineapp.composeapp.generated.resources.movie_3d
import cineapp.composeapp.generated.resources.movie_age_indication
import cineapp.composeapp.generated.resources.movie_duration
import cineapp.composeapp.generated.resources.movie_language
import cineapp.composeapp.generated.resources.movie_poster_desc
import cineapp.composeapp.generated.resources.not_available_abbreviation
import cineapp.composeapp.generated.resources.unknown_error
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.ivarvisser.cineapp.theming.BrandColors
import com.ivarvisser.cineapp.ui.component.ErrorMessage
import com.ivarvisser.cineapp.ui.component.ExpandablePanel
import com.ivarvisser.cineapp.ui.component.InfoRow
import com.ivarvisser.cineapp.ui.component.TrailerPlayer
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.minutes

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
            message = state.error ?: stringResource(Res.string.unknown_error),
            onRetry = { component.onRefresh() }
        )
    }
    if (!state.isLoading && !state.hasError) {
        val scrollState = rememberScrollState()
        val trailerKey = state.movie.youtubeTrailerKey ?: "gm2M8oUrEyg"

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w154/" + state.movie.posterPath,
                            contentDescription = stringResource(Res.string.movie_poster_desc),
                            modifier = Modifier
                                .padding(top = 16.dp, bottom = 5.dp, start = 16.dp, end = 16.dp)
                                .clip(RoundedCornerShape(12.dp)),
                        )
                        Text(
                            text = state.movie.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = BrandColors.VividPurple,
                            modifier = Modifier
                                .align(alignment = Alignment.CenterVertically)
                                .padding(start = 16.dp, end = 48.dp)
                        )
                    }
                    if (state.isLoggedIn) {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                                .clickable {
                                    component.onFavoriteMoviePress()
                                },
                            painter = painterResource(
                                if (state.isFavorite) {
                                    Res.drawable.bookmark_check
                                } else {
                                    Res.drawable.bookmark
                                }
                            ),
                            contentDescription = null
                        )
                    }
                }
                Column(
                    modifier = Modifier.padding(16.dp)
                        .align(alignment = Alignment.Start)
                ) {
                    InfoRow(
                        stringResource(Res.string.movie_duration),
                        state.movie.runtime?.let { "$it min" }
                            ?: stringResource(Res.string.not_available_abbreviation))
                    InfoRow(
                        stringResource(Res.string.movie_age_indication),
                        state.movie.ageIndication
                            ?: stringResource(Res.string.not_available_abbreviation)
                    )
                    InfoRow(
                        stringResource(Res.string.movie_language),
                        when (state.movie.language) {
                            "nl" -> stringResource(Res.string.lang_nl)
                            "en" -> stringResource(Res.string.lang_en)
                            else -> stringResource(Res.string.not_available_abbreviation)
                        }
                    )
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
                    InfoRow(
                        stringResource(Res.string.item_genre),
                        ""
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
                }
                Spacer(modifier = Modifier.size(8.dp))
                val groupedShowings = remember(state.upcomingShowings) {
                    state.upcomingShowings
                        .groupBy { it.startsAt.toLocalDateTime(TimeZone.currentSystemDefault()).date }
                }
                val dates = remember(groupedShowings) { groupedShowings.keys.sorted() }
                Text(
                    text = stringResource(Res.string.item_trailer),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    TrailerPlayer(
                        videoId = trailerKey,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                if (dates.isNotEmpty()) {
                    var selectedTabIndex by remember { mutableStateOf(0) }

                    Column(modifier = Modifier.fillMaxWidth().padding(0.dp)) {
                        Box(modifier = Modifier.padding(16.dp)) {
                            InfoRow(
                                stringResource(Res.string.item_upcoming_showings),
                                ""
                            )
                        }

                        SecondaryScrollableTabRow(
                            selectedTabIndex = selectedTabIndex,
                            edgePadding = 16.dp,
                            containerColor = Color.Transparent,
                            divider = {}
                        ) {
                            dates.forEachIndexed { index, date ->
                                Tab(
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                    text = {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
                                            Text(
                                                text = date.day.toString(),
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                            Text(
                                                text = date.month.name.take(3),
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                )
                            }
                        }
                        Box(modifier = Modifier.padding(16.dp)) {
                            val selectedDate = dates.getOrNull(selectedTabIndex)
                            if (selectedDate != null) {
                                val showingsForSelectedDate =
                                    groupedShowings[selectedDate]?.sortedBy { it.startsAt }
                                        ?: emptyList()
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    showingsForSelectedDate.forEach { showing ->
                                        val start =
                                            showing.startsAt.toLocalDateTime(TimeZone.currentSystemDefault())
                                        val runtime = state.movie.runtime
                                        val end = if (runtime != null) {
                                            showing.startsAt.plus(runtime.minutes)
                                                .toLocalDateTime(TimeZone.currentSystemDefault())
                                        } else null

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 12.dp)
                                                .clickable(onClick = {
                                                    component.onShowingSelected(
                                                        showing.id,
                                                        state.movie.id
                                                    )
                                                }),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                val startText =
                                                    "${start.hour.toString().padStart(2, '0')}:${
                                                        start.minute.toString().padStart(2, '0')
                                                    }"
                                                val endText = end?.let {
                                                    "${it.hour.toString().padStart(2, '0')}:${
                                                        it.minute.toString().padStart(2, '0')
                                                    }"
                                                } ?: ""
                                                Text(
                                                    text = if (endText.isNotEmpty()) "$startText - $endText" else startText,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                if (showing.is3D) {
                                                    Text(
                                                        text = stringResource(Res.string.movie_3d),
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = BrandColors.DarkOrange
                                                    )
                                                }
                                            }

                                            Text(
                                                text = showing.auditorium?.name
                                                    ?: stringResource(Res.string.item_auditorium_fallback),
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        HorizontalDivider(
                                            thickness = 0.5.dp,
                                            color = BrandColors.SoftDivider
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
