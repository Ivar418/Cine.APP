package com.ivarvisser.cineapp.theming

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun CineAppTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        content = content
    )
}