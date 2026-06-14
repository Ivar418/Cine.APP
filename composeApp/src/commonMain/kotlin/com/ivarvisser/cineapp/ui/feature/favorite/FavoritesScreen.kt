package com.ivarvisser.cineapp.ui.feature.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.arrow_back_24px
import cineapp.composeapp.generated.resources.error_generic
import cineapp.composeapp.generated.resources.favorites_title
import cineapp.composeapp.generated.resources.movies_back_button
import cineapp.composeapp.generated.resources.movies_back_icon_desc
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.ivarvisser.cineapp.ui.component.ErrorMessage
import com.ivarvisser.cineapp.ui.component.MovieList
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun FavoritesScreen(
    component: FavoritesComponent
) {
    val state by component.state.subscribeAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { component.goBack() }) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_back_24px),
                    contentDescription = stringResource(Res.string.movies_back_icon_desc)
                )
                Text(stringResource(Res.string.movies_back_button))
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(Res.string.favorites_title),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(end = 16.dp)
            )
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
                onRetry = { component.loadFavorites() }
            )
        }

        if (!state.isLoading && (state.error == null)) {
            if (state.movies.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Je hebt nog geen favorieten toegevoegd.")
                }
            } else {
                MovieList(
                    movies = state.movies,
                    modifier = Modifier.fillMaxHeight()
                ) { component.movieSelected(it) }
            }
        }
    }
}
