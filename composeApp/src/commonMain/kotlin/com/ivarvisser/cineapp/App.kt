package com.ivarvisser.cineapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.ivarvisser.cineapp.theming.CineAppTheme
import com.ivarvisser.cineapp.ui.feature.movies.MoviesOverviewScreen


@Composable
fun App(root: RootComponent) {
    CineAppTheme(darkTheme = true) {
        Surface(Modifier.fillMaxSize()) {
            Box(Modifier.safeDrawingPadding()) {
                val childStack by root.childStack.subscribeAsState()
                Children(stack = childStack) { child ->
                    when (val instance = child.instance) {
                        is RootComponent.Child.CineAppScreen -> CineappScreen(component = instance.componentContext)
                        is RootComponent.Child.MoviesOverviewScreen -> MoviesOverviewScreen(
                            component = instance.componentContext
                        )
                    }
                }
            }
        }
    }
}
