package com.ivarvisser.cineapp.ui.feature.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.ivarvisser.cineapp.NotImplementedComponent
import com.ivarvisser.cineapp.domain.Movie
import com.ivarvisser.cineapp.ui.component.navigation.TabBarItem
import com.ivarvisser.cineapp.ui.feature.movies.MovieDetailsComponent
import com.ivarvisser.cineapp.ui.feature.movies.MoviesOverviewComponent
import com.ivarvisser.cineapp.ui.home.DefaultHomeComponent
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf


class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext, KoinComponent {
    private val navigation = StackNavigation<Configuration>()
    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.Home,
        handleBackButton = true,
        childFactory = ::createChild
    )

    // Map the active configuration to the corresponding TabBarItem
    //Data class needs a "is" and the objects do not.
    val activeTab: Value<TabBarItem> = childStack.map { stack ->
        when (stack.active.configuration) {
            Configuration.Home -> TabBarItem.Home
            Configuration.MoviesOverviewScreen, is Configuration.MovieDetailsScreen -> TabBarItem.MoviesOverviewScreen // Both map to the same tab
            Configuration.OrderHistory -> TabBarItem.OrderHistory
            Configuration.Account -> TabBarItem.Account
            Configuration.Settings -> TabBarItem.Settings
            else -> TabBarItem.Home
        }
    }

    private fun createChild(config: Configuration, context: ComponentContext): Child {
        return when (config) {
            is Configuration.Home ->
                Child.Home(
                    get {
                        parametersOf(
                            context,
                            { navigation.bringToFront(Configuration.Account) },
                            { navigation.bringToFront(Configuration.OrderHistory) },
                            { navigation.bringToFront(Configuration.MoviesOverviewScreen) }
                        )
                    }
                )

            is Configuration.MoviesOverviewScreen ->
                Child.MoviesOverviewScreen(
                    get {
                        parametersOf(
                            context,
                            { navigation.pop() },
                            { movie: Movie ->
                                navigation.bringToFront(
                                    Configuration.MovieDetailsScreen(
                                        movie
                                    )
                                )
                            })
                    }
                )

            is Configuration.MovieDetailsScreen ->
                Child.MovieDetailsScreen(
                    get { parametersOf(context, config.movie, { navigation.pop() }) }
                )

            is Configuration.Account -> {
                Child.Account(
                    get {
                        parametersOf(
                            context,
                            { navigation.pop() },
                            "Account is not implemented yet."
                        )
                    }
                )
            }

            is Configuration.OrderHistory -> {
                Child.OrderHistory(
                    get {
                        parametersOf(
                            context,
                            { navigation.pop() },
                            "Order History is not implemented yet."
                        )
                    }
                )
            }

            is Configuration.Settings -> {
                Child.Settings(
                    get {
                        parametersOf(
                            context,
                            { navigation.pop() },
                            "Settings is not implemented yet. Press to go back."
                        )
                    }
                )
            }

            else -> {
                Child.NotImplemented(
                    get {
                        parametersOf(
                            context,
                            { navigation.pop() },
                            "Not implemented yet. Press to go back."
                        )
                    }
                )
            }
        }
    }

    fun showTabItem(item: TabBarItem) {
        when (item) {
            is TabBarItem.Home -> navigation.bringToFront(RootComponent.Configuration.Home)
            is TabBarItem.MoviesOverviewScreen -> navigation.bringToFront(RootComponent.Configuration.MoviesOverviewScreen)
            is TabBarItem.OrderHistory -> navigation.bringToFront(RootComponent.Configuration.OrderHistory)
            is TabBarItem.Account -> navigation.bringToFront(RootComponent.Configuration.Account)
            is TabBarItem.Settings -> navigation.bringToFront(RootComponent.Configuration.Settings)
        }
    }

    fun goBack() {
        navigation.pop()
    }

    sealed class Child {
        data class Home(val componentContext: DefaultHomeComponent) : Child()
        data class MoviesOverviewScreen(val componentContext: MoviesOverviewComponent) : Child()
        data class OrderHistory(val componentContext: NotImplementedComponent) : Child()
        data class Account(val componentContext: NotImplementedComponent) : Child()
        data class Settings(val componentContext: NotImplementedComponent) : Child()
        data class NotImplemented(val componentContext: NotImplementedComponent) : Child()
        data class MovieDetailsScreen(val componentContext: MovieDetailsComponent) : Child()

    }
    @Serializable
    sealed class Configuration {
        @Serializable
        data object Home : Configuration()

        @Serializable
        data object MoviesOverviewScreen : Configuration()

        @Serializable
        data object Settings : Configuration()

        @Serializable
        data object Account : Configuration()

        @Serializable
        data object OrderHistory : Configuration()

        @Serializable
        data object NotImplemented : Configuration()

        @Serializable
        data class MovieDetailsScreen(val movie: Movie) : Configuration()
    }
}