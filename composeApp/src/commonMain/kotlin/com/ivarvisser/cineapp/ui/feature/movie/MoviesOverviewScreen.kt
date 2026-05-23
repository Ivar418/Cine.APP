package com.ivarvisser.cineapp.ui.feature.movie

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.arrow_back_24px
import cineapp.composeapp.generated.resources.error_generic
import cineapp.composeapp.generated.resources.movies_back_button
import cineapp.composeapp.generated.resources.movies_back_icon_desc
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.ivarvisser.cineapp.ui.component.ErrorMessage
import com.ivarvisser.cineapp.ui.component.MovieList
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MoviesOverviewScreen(
    component: MoviesOverviewComponent,
) {
    val state by component.state.subscribeAsState()
    Column {
        Row {
            Button(
                onClick = { component.goBack() }) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_back_24px),
                    contentDescription = stringResource(Res.string.movies_back_icon_desc)
                )
                Text(stringResource(Res.string.movies_back_button))
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
