package com.ivarvisser.cineapp.di

import com.arkivanov.decompose.ComponentContext
import com.ivarvisser.cineapp.NotImplementedComponent
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.ui.feature.movie.MovieDetailsComponent
import com.ivarvisser.cineapp.ui.feature.movie.MoviesOverviewComponent
import com.ivarvisser.cineapp.ui.home.DefaultHomeComponent
import org.koin.dsl.module

val componentModule = module {
    // Factory for DefaultHomeComponent
    factory<DefaultHomeComponent> { (componentContext: ComponentContext, onAccount: () -> Unit, onHistory: () -> Unit, onOverview: () -> Unit) ->
        DefaultHomeComponent(
            componentContext = componentContext,
            onNavigateToAccount = onAccount,
            onNavigateToHistory = onHistory,
            onNavigateToOverview = onOverview
        )
    }

    // Factory for MoviesOverviewComponent
    factory<MoviesOverviewComponent> { (componentContext: ComponentContext, onGoBack: () -> Unit, onMovieSelected: (Movie) -> Unit) ->
        MoviesOverviewComponent(
            componentContext = componentContext,
            repo = get(), // Automatically injected from dataModule
            onGoBack = onGoBack,
            _onMovieSelected = onMovieSelected
        )
    }
    factory<MovieDetailsComponent> { (componentContext: ComponentContext, movie: Movie, onGoBack: () -> Unit, onNavigateToOrder: (showingId: Int, movieId: Int) -> Unit) ->
        MovieDetailsComponent(
            componentContext = componentContext,
            movie = movie,
            onGoBack = onGoBack,
            showingsRepository = get(),
            moviesRepository = get(),
            onNavigateToOrder = { showingId, movieId -> onNavigateToOrder(showingId, movieId) }
        )
    }

    // Factory for NotImplementedComponent
    factory<NotImplementedComponent> { (componentContext: ComponentContext, onRetry: () -> Unit, text: String) ->
        NotImplementedComponent(
            componentContext = componentContext,
            onRetry = onRetry,
            textContent = text
        )
    }
}