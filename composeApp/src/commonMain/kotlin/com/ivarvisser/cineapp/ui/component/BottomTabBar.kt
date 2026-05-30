// C:/Users/ivarv/dev/AndroidStudioProjects/CineApp/composeApp/src/commonMain/kotlin/com/ivarvisser/cineapp/ui/component/BottomTabBar.kt

package com.ivarvisser.cineapp.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.ivarvisser.cineapp.theming.BrandColors
import com.ivarvisser.cineapp.ui.component.navigation.TabBarItem
import com.ivarvisser.cineapp.ui.feature.navigation.RootComponent

@Composable
fun BottomTabBar(
    modifier: Modifier = Modifier,
    items: List<TabBarItem>,
    root: RootComponent,
    onSelect: (TabBarItem) -> Unit
) {
    val selectedItem by root.activeTab.subscribeAsState()
    val colors = NavigationBarItemColors(
        selectedTextColor = BrandColors.DarkOrange,
        unselectedTextColor = Color.White,
        selectedIconColor = BrandColors.DarkOrange,
        unselectedIconColor = Color.White,
        selectedIndicatorColor = BrandColors.DarkOrange.copy(alpha = 0.2f),
        disabledIconColor = BrandColors.Anthracite,
        disabledTextColor = BrandColors.Anthracite,
    )
    NavigationBar(
        modifier = modifier,
        containerColor = BrandColors.TopBar,
        // You can use containerColor = BrandColors.TopBar if you want to keep your brand color
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedItem == item,
                onClick = { onSelect(item) },
                icon = {
                    Icon(
                        imageVector = item.image,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(item.title)
                },
                colors = colors
            )
        }
    }
}
