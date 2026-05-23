package com.ivarvisser.cineapp.ui.feature.showing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.genre_label
import cineapp.composeapp.generated.resources.item_auditorium_fallback
import cineapp.composeapp.generated.resources.movie_3d
import cineapp.composeapp.generated.resources.movie_age_indication
import cineapp.composeapp.generated.resources.movie_description_label
import cineapp.composeapp.generated.resources.movie_duration
import cineapp.composeapp.generated.resources.movie_language
import cineapp.composeapp.generated.resources.no_description_available
import cineapp.composeapp.generated.resources.not_available_abbreviation
import cineapp.composeapp.generated.resources.order_tickets_button
import cineapp.composeapp.generated.resources.release_date
import cineapp.composeapp.generated.resources.showing_auditorium
import cineapp.composeapp.generated.resources.showing_date
import cineapp.composeapp.generated.resources.showing_mismatch
import cineapp.composeapp.generated.resources.showing_not_found
import cineapp.composeapp.generated.resources.showing_start_time
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.ivarvisser.cineapp.theming.BrandColors
import com.ivarvisser.cineapp.ui.component.InfoRow
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

@Composable
fun ShowingDetailScreen(
    component: ShowingDetailComponent,
) {
    val state by component.state.subscribeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }

            state.movie == null || state.showing == null -> {
                Text(
                    text = stringResource(Res.string.showing_not_found),
                    color = MaterialTheme.colorScheme.error
                )
            }

            state.mismatch -> {
                Text(
                    text = stringResource(Res.string.showing_mismatch),
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> {
                val movie = state.movie
                val showing = state.showing

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(24.dp)
                            ) {

                                AsyncImage(
                                    model = component.posterUrl(),
                                    contentDescription = movie?.title,
                                    modifier = Modifier
                                        .width(220.dp)
                                        .height(320.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )

                            }

                            Column {

                                Text(
                                    text = movie?.title
                                        ?: stringResource(Res.string.not_available_abbreviation),
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                InfoRow(
                                    label = stringResource(Res.string.movie_duration),
                                    value = "${movie?.runtime ?: Res.string.not_available_abbreviation} min"
                                )

                                InfoRow(
                                    label = stringResource(Res.string.release_date),
                                    value = movie?.releaseDate
                                        ?: stringResource(Res.string.not_available_abbreviation)
                                )

                                InfoRow(
                                    label = stringResource(Res.string.movie_age_indication),
                                    value = movie?.ageIndication
                                        ?: stringResource(Res.string.not_available_abbreviation)
                                )

                                InfoRow(
                                    label = stringResource(Res.string.movie_language),
                                    value = movie?.spokenLanguageName
                                        ?: stringResource(Res.string.not_available_abbreviation)
                                )

                                InfoRow(
                                    label = stringResource(Res.string.genre_label),
                                    value = ""
                                )
                                if (state.genres.isNotEmpty()) {
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
                                } else {
                                    Text(
                                        stringResource(Res.string.not_available_abbreviation)
                                    )
                                }

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )

                                InfoRow(
                                    label = stringResource(Res.string.showing_date),
                                    value = showing?.startsAt
                                        ?.toLocalDateTime(TimeZone.currentSystemDefault())
                                        ?.date
                                        .toString()
                                )

                                InfoRow(
                                    label = stringResource(Res.string.showing_start_time),
                                    value = showing?.startsAt
                                        ?.toLocalDateTime(TimeZone.currentSystemDefault())
                                        ?.time
                                        .toString()
                                )

                                InfoRow(
                                    label = stringResource(Res.string.showing_auditorium),
                                    value = showing?.auditorium?.name
                                        ?: stringResource(Res.string.item_auditorium_fallback)
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${stringResource(Res.string.movie_3d)}: ",
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Icon(
                                        imageVector = if (showing?.is3D == true) {
                                            Icons.Default.CheckCircle
                                        } else {
                                            Icons.Default.Close
                                        },
                                        contentDescription = null,
                                        tint = if (showing?.is3D == true) {
                                            Color.Green
                                        } else {
                                            Color.Red
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Button(
                                    onClick = component::navigateToOrder
                                ) {
                                    Text(stringResource(Res.string.order_tickets_button))
                                }
                            }


                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 24.dp)
                            )

                            Text(
                                text = stringResource(Res.string.movie_description_label),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = movie?.about
                                    ?: stringResource(Res.string.no_description_available)
                            )
                        }
                    }
                }
            }
        }
    }
}