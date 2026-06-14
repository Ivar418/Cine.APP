package com.ivarvisser.cineapp.ui.feature.movie

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.arrow_back_24px
import cineapp.composeapp.generated.resources.error_generic
import cineapp.composeapp.generated.resources.movies_back_button
import cineapp.composeapp.generated.resources.movies_back_icon_desc
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.ivarvisser.cineapp.ui.component.ErrorMessage
import com.ivarvisser.cineapp.ui.component.MovieList
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesOverviewScreen(
    component: MoviesOverviewComponent,
) {
    val state by component.state.subscribeAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    val todayMillis = remember { Clock.System.now().toEpochMilliseconds() }

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // Allow today and later. 
                return utcTimeMillis >= todayMillis - 86400000
            }
        }
    )

    val selectedDateText = remember(state.selectedInstant) {
        state.selectedInstant?.toLocalDateTime(TimeZone.currentSystemDefault())?.date?.toString()
            ?: "Kies een datum"
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { component.goBack() }) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_back_24px),
                    contentDescription = stringResource(Res.string.movies_back_icon_desc)
                )
                Text(stringResource(Res.string.movies_back_button))
            }
            Spacer(modifier = Modifier.weight(1f))
            if (state.selectedInstant != null) {
                Row(
                    modifier = Modifier.clickable(onClick = { component.onInstantSelected(null) }
                    ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Filter wissen")

                    Icon(Icons.Default.Clear, contentDescription = "Wis filter")

                }
            }
        }

        // Calendar Filter Button
        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Icon(Icons.Default.CalendarMonth, contentDescription = null)
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text(selectedDateText)
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let {
                                val instant = Instant.fromEpochMilliseconds(it)
                                component.onInstantSelected(instant)
                            }
                            showDatePicker = false
                        },
                        enabled = datePickerState.selectedDateMillis != null
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Annuleren")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

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
                message = state.error ?: stringResource(Res.string.error_generic),
                onRetry = {
                    component.loadMovies()
                }
            )
        }
        MovieList(
            movies = state.movies,
            modifier = Modifier.fillMaxHeight(),
            onMovieClick = { component.onMovieSelected(it) }
        )
    }
}
