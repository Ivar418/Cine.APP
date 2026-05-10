
package com.ivarvisser.cineapp.ui.component.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.arrow_back_24px
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.ivarvisser.cineapp.theming.BrandColors
import com.ivarvisser.cineapp.ui.feature.navigation.RootComponent
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    items: List<TabBarItem>,
    onSelect: (TabBarItem) -> Unit,
    root: RootComponent,
) {
    val selectedItem by root.activeTab.subscribeAsState()

    // Check if we can go back to show/hide the back button
    val stack by root.childStack.subscribeAsState()
    val canGoBack = stack.backStack.isNotEmpty()

    TopAppBar(
        modifier = modifier,
        title = {
            SecondaryTabRow(
                selectedTabIndex = items.indexOf(selectedItem).coerceAtLeast(0),
                containerColor = Color.Transparent,
                divider = {}
            ) {
                items.forEach { item ->
                    Tab(
                        selected = selectedItem == item,
                        onClick = { onSelect(item) },
                        text = { Text(item.title) },
                        icon = { Icon(item.image, contentDescription = null) },
                        selectedContentColor = BrandColors.DarkOrange,
                        unselectedContentColor = Color.White
                    )
                }
            }
        },
        navigationIcon = {
            if (canGoBack) {
                IconButton(onClick = { root.goBack() }) {
                    Icon(
                        painter = painterResource(Res.drawable.arrow_back_24px),
                        contentDescription = "Go Back"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BrandColors.TopBar,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}