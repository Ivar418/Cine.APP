package com.ivarvisser.cineapp.ui.component.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TabComponent(
    modifier: Modifier = Modifier, item: TabBarItem, isSelected: Boolean
) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            tint = if (isSelected) Color.Blue else Color.Gray,
            contentDescription = null,
            imageVector = item.image
        )
        Spacer(Modifier.height(4.dp))
        Text(item.title, color = if (isSelected) Color.Blue else Color.Gray)
    }
}