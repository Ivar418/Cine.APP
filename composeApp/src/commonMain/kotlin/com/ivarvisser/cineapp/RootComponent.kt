package com.ivarvisser.cineapp

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import com.ivarvisser.cineapp.RootComponent.Child.CineAppScreen
import com.ivarvisser.cineapp.RootComponent.Child.MoviesOverviewScreen
import com.ivarvisser.cineapp.ui.feature.movies.MoviesOverviewComponent
import com.ivarvisser.cineapp.ui.startScreen.DefaultCineAppComponent
import kotlinx.serialization.Serializable
import org.koin.mp.KoinPlatform.getKoin

class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    private val navigation = StackNavigation<Configuration>()
    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.CineAppScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(config: Configuration, context: ComponentContext): Child {
        return when (config) {
            is Configuration.CineAppScreen ->
                CineAppScreen(
                    DefaultCineAppComponent(
                        context,
                        onNavigateToAccount = { navigation.pushNew(Configuration.Account) },
                        onNavigateToHistory = { navigation.pushNew(Configuration.OrderHistory) },
                        onNavigateToOverview = { navigation.pushNew(Configuration.MoviesOverviewScreen) }
                    )
                )

            is Configuration.MoviesOverviewScreen ->
                MoviesOverviewScreen(
                    MoviesOverviewComponent(componentContext = context, repo = getKoin().get())
                )

            Configuration.Account -> {
                error("AccountScreen not implemented yet")
            }

            Configuration.OrderHistory -> {
                error("OrderHistoryScreen not implemented yet")
            }

            Configuration.Settings -> {
                error("SettingsScreen not implemented yet")
            }
        }

    }

    sealed class Child {
        data class CineAppScreen(val componentContext: DefaultCineAppComponent) : Child()

        data class MoviesOverviewScreen(val componentContext: MoviesOverviewComponent) : Child()
    }

    @Serializable
    sealed class Configuration {
        //        This is the starting screen
        @Serializable
        data object CineAppScreen : Configuration()

        @Serializable
        data object MoviesOverviewScreen : Configuration()

        @Serializable
        data object Settings : Configuration()

        @Serializable
        data object Account : Configuration()

        @Serializable
        data object OrderHistory : Configuration()
    }
}