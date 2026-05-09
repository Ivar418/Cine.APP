package com.ivarvisser.cineapp.ui.feature.movies

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.arrow_back_24px
import com.ivarvisser.cineapp.ui.component.ErrorMessage
import com.ivarvisser.cineapp.ui.component.MovieList
import org.jetbrains.compose.resources.painterResource

@Composable
fun MoviesOverviewScreen(
    component: MoviesOverviewComponent,
) {
    val state by component.state.collectAsState()
    Column {
        Row {
            Button(
                onClick = { component.goBack() }) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_back_24px),
                    contentDescription = "Movies Icon"
                )
                Text("Go Back")
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
                message = state.error ?: "An unknown error occurred",
                onRetry = { component.onRefresh() }
            )
        }
        MovieList(
            movies = state.movies, modifier = Modifier.fillMaxHeight()
        )
    }
}
