package com.ivarvisser.cineapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.ivarvisser.cineapp.theming.CineAppTheme
import com.ivarvisser.cineapp.ui.component.BottomTabBar
import com.ivarvisser.cineapp.ui.component.navigation.TabBarItem
import com.ivarvisser.cineapp.ui.component.navigation.TopBar
import com.ivarvisser.cineapp.ui.feature.account.AccountScreen
import com.ivarvisser.cineapp.ui.feature.movie.MovieItemDetailsScreen
import com.ivarvisser.cineapp.ui.feature.movie.MoviesOverviewScreen
import com.ivarvisser.cineapp.ui.feature.navigation.RootComponent
import com.ivarvisser.cineapp.ui.feature.ordering.OrderingScreen
import com.ivarvisser.cineapp.ui.feature.showing.ShowingDetailScreen
import com.ivarvisser.cineapp.ui.home.HomeScreen


@Composable
fun App(root: RootComponent) {
    CineAppTheme(darkTheme = true) {
        Surface(Modifier.fillMaxSize()) {
            Box(Modifier.safeDrawingPadding()) {
                val platform = getPlatform()
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    if (!platform.isMobile) {
                        TopBar(
                            items = listOf(
                                TabBarItem.Home,
                                TabBarItem.MoviesOverviewScreen,
                                TabBarItem.OrderHistory,
                                TabBarItem.Account,
                                TabBarItem.Settings
                            ), onSelect = { root.showTabItem(it) }, root = root
                        )
                    }
                }, bottomBar = {
                    if (platform.isMobile) {
                        BottomTabBar(
                            items = listOf(
                                TabBarItem.Home,
                                TabBarItem.MoviesOverviewScreen,
                                TabBarItem.OrderHistory,
                                TabBarItem.Account,
                                TabBarItem.Settings
                            ), onSelect = { root.showTabItem(it) }, root = root
                        )
                    }
                }, content = { paddingValues ->
                    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                        val childStack by root.childStack.subscribeAsState()

                        Children(stack = childStack) { child ->
                            when (val instance = child.instance) {
                                is RootComponent.Child.Home -> HomeScreen(component = instance.componentContext)
                                is RootComponent.Child.MoviesOverviewScreen -> MoviesOverviewScreen(
                                    component = instance.componentContext
                                )

                                is RootComponent.Child.MovieDetailsScreen -> {
                                    MovieItemDetailsScreen(
                                        component = instance.componentContext,
                                    )
                                }

                                is RootComponent.Child.OrderHistory -> NotImplemented(component = instance.componentContext)
                                is RootComponent.Child.Account -> AccountScreen(component = instance.componentContext)
                                is RootComponent.Child.Settings -> NotImplemented(component = instance.componentContext)
                                is RootComponent.Child.NotImplemented -> NotImplemented(component = instance.componentContext)
                                is RootComponent.Child.ShowingDetailsScreen -> ShowingDetailScreen(
                                    component = instance.componentContext,
                                )

                                is RootComponent.Child.OrderingScreen -> {
                                    val state by instance.componentContext.state.subscribeAsState()
                                    OrderingScreen(
                                        state = state,
                                        onAction = instance.componentContext::onAction
                                    )
                                }
                            }
                        }
                    }
                })
            }
        }
    }
}
