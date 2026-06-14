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
import com.ivarvisser.cineapp.ui.feature.account.AccountComponent
import com.ivarvisser.cineapp.ui.feature.favorite.FavoritesComponent
import com.ivarvisser.cineapp.ui.feature.movie.MovieDetailsComponent
import com.ivarvisser.cineapp.ui.feature.movie.MoviesOverviewComponent
import com.ivarvisser.cineapp.ui.feature.ordering.OrderingComponent
import com.ivarvisser.cineapp.ui.feature.showing.ShowingDetailComponent
import com.ivarvisser.cineapp.ui.home.DefaultHomeComponent
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent


class RootComponent(
    componentContext: ComponentContext,
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
            Configuration.MoviesOverviewScreen, is Configuration.MovieDetailsScreen, is Configuration.ShowingDetailsScreen, is Configuration.OrderingScreen -> TabBarItem.MoviesOverviewScreen // Both map to the same tab
            Configuration.OrderHistory -> TabBarItem.OrderHistory
            Configuration.Account, Configuration.Favorites -> TabBarItem.Account
            Configuration.Settings -> TabBarItem.Settings
            else -> TabBarItem.Home
        }
    }

    private fun createChild(config: Configuration, context: ComponentContext): Child {
        return when (config) {
            is Configuration.Home ->
                Child.Home(
                    DefaultHomeComponent(
                        componentContext = context,
                        onNavigateToAccount = { navigation.bringToFront(Configuration.Account) },
                        onNavigateToHistory = { navigation.bringToFront(Configuration.OrderHistory) },
                        onNavigateToOverview = { navigation.bringToFront(Configuration.MoviesOverviewScreen) }
                    )
                )

            is Configuration.MoviesOverviewScreen ->
                Child.MoviesOverviewScreen(
                    MoviesOverviewComponent(
                        componentContext = context,
                        repo = getKoin().get(),
                        onGoBack = { navigation.pop() },
                        _onMovieSelected = { movie: Movie ->
                            navigation.bringToFront(Configuration.MovieDetailsScreen(movie))
                        }
                    )
                )

            is Configuration.MovieDetailsScreen ->
                Child.MovieDetailsScreen(
                    MovieDetailsComponent(
                        componentContext = context,
                        movie = config.movie,
                        onGoBack = { navigation.pop() },
                        onNavigateToOrder = { showingId, movieId ->
                            navigation.bringToFront(
                                Configuration.ShowingDetailsScreen(
                                    showingId,
                                    movieId
                                )
                            )
                        },
                        showingsRepository = getKoin().get(),
                        moviesRepository = getKoin().get(),
                        usersRepository = getKoin().get()
                    )
                )

            is Configuration.Account -> {
                Child.Account(
                    AccountComponent(
                        componentContext = context,
                        usersRepository = getKoin().get(),
                        onGoBack = { navigation.pop() },
                        onNavigateToFavorites = { navigation.bringToFront(Configuration.Favorites) }
                    )
                )
            }

            is Configuration.Favorites -> {
                Child.Favorites(
                    FavoritesComponent(
                        componentContext = context,
                        usersRepository = getKoin().get(),
                        moviesRepository = getKoin().get(),
                        onGoBack = { navigation.pop() },
                        onMovieSelected = { movie ->
                            navigation.bringToFront(Configuration.MovieDetailsScreen(movie))
                        }
                    )
                )
            }

            is Configuration.OrderHistory -> {
                Child.OrderHistory(
                    NotImplementedComponent(
                        componentContext = context,
                        onRetry = { navigation.pop() },
                        textContent = "Order History is not implemented yet."
                    )
                )
            }

            is Configuration.Settings -> {
                Child.Settings(
                    NotImplementedComponent(
                        componentContext = context,
                        onRetry = { navigation.pop() },
                        textContent = "Settings is not implemented yet. Press to go back."
                    )
                )
            }

            is Configuration.ShowingDetailsScreen -> {
                Child.ShowingDetailsScreen(
                    ShowingDetailComponent(
                        componentContext = context,
                        showingId = config.showingId,
                        movieId = config.movieId,
                        onGoBack = { navigation.pop() },
                        moviesRepository = getKoin().get(),
                        showingsRepository = getKoin().get(),
                        onNavigateToOrder = {
                            navigation.bringToFront(
                                Configuration.OrderingScreen(
                                    showingId = config.showingId,
                                    movieId = config.movieId
                                )
                            )
                        }
                    )
                )
            }

            is Configuration.OrderingScreen -> {
                Child.OrderingScreen(
                    OrderingComponent(
                        componentContext = context,
                        showingId = config.showingId,
                        movieId = config.movieId,
                        onGoBack = { navigation.pop() },
                        moviesRepository = getKoin().get(),
                        showingsRepository = getKoin().get(),
                        ordersRepository = getKoin().get(),
                        reservationsRepository = getKoin().get(),
                        usersRepository = getKoin().get(),
                    )
                )
            }

            else -> {
                Child.NotImplemented(
                    NotImplementedComponent(
                        componentContext = context,
                        onRetry = { navigation.pop() },
                        textContent = "Not implemented yet. Press to go back."
                    )
                )
            }
        }
    }


    fun showTabItem(item: TabBarItem) {
        when (item) {
            is TabBarItem.Home -> navigation.bringToFront(Configuration.Home)
            is TabBarItem.MoviesOverviewScreen -> navigation.bringToFront(Configuration.MoviesOverviewScreen)
            is TabBarItem.OrderHistory -> navigation.bringToFront(Configuration.OrderHistory)
            is TabBarItem.Account -> navigation.bringToFront(Configuration.Account)
            is TabBarItem.Settings -> navigation.bringToFront(Configuration.Settings)
        }
    }

    fun goBack() {
        navigation.pop()
    }


    sealed class Child {
        data class Home(val componentContext: DefaultHomeComponent) : Child()
        data class MoviesOverviewScreen(val componentContext: MoviesOverviewComponent) : Child()
        data class OrderHistory(val componentContext: NotImplementedComponent) : Child()
        data class Account(val componentContext: AccountComponent) : Child()
        data class Favorites(val componentContext: FavoritesComponent) : Child()
        data class Settings(val componentContext: NotImplementedComponent) : Child()
        data class NotImplemented(val componentContext: NotImplementedComponent) : Child()
        data class MovieDetailsScreen(val componentContext: MovieDetailsComponent) : Child()
        data class ShowingDetailsScreen(val componentContext: ShowingDetailComponent) : Child()
        data class OrderingScreen(val componentContext: OrderingComponent) : Child()

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
        data object Favorites : Configuration()

        @Serializable
        data object OrderHistory : Configuration()

        @Serializable
        data object NotImplemented : Configuration()

        @Serializable
        data class MovieDetailsScreen(val movie: Movie) : Configuration()

        @Serializable
        data class ShowingDetailsScreen(val showingId: Int, val movieId: Int) : Configuration()

        @Serializable
        data class OrderingScreen(val showingId: Int, val movieId: Int) : Configuration()
    }
}