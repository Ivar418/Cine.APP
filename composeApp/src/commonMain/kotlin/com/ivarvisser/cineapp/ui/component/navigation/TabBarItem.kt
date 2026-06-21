package com.ivarvisser.cineapp.ui.component.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TabBarItem(
    val image: ImageVector,
    val title: String
) {
    data object Home : TabBarItem(
        image = Icons.Default.Home,
        title = "Home"
    )

    data object MoviesOverviewScreen : TabBarItem(
        image = Icons.Default.Movie,
        title = "Movies"
    )

    data object OrderHistory : TabBarItem(
        image = Icons.Default.Receipt,
        title = "Orders"
    )

    data object Account : TabBarItem(
        image = Icons.Default.AccountBox,
        title = "Account"
    )
}