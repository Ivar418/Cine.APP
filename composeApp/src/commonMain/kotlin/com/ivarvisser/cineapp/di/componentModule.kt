package com.ivarvisser.cineapp.di

import com.arkivanov.decompose.ComponentContext
import com.ivarvisser.cineapp.NotImplementedComponent
import com.ivarvisser.cineapp.ui.feature.movies.MoviesOverviewComponent
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
    factory<MoviesOverviewComponent> { (componentContext: ComponentContext, onGoBack: () -> Unit) ->
        MoviesOverviewComponent(
            componentContext = componentContext,
            repo = get(), // Automatically injected from dataModule
            onGoBack = onGoBack
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