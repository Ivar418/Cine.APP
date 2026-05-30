package com.ivarvisser.cineapp.ui.feature.ordering

import androidx.compose.ui.graphics.Color

fun zoneColor(category: Int): Color {
    return when (category) {
        1 -> Color(0xFF81C784)
        2 -> Color(0xFF64B5F6)
        3 -> Color(0xFFFFB74D)
        else -> Color(0xFFE0E0E0)
    }
}