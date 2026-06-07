package com.ivarvisser.cineapp.ui.feature.ordering

import androidx.compose.ui.graphics.Color

fun zoneColor(category: Int): Color {
    return when (category) {
        1 -> Color(0xFFF59E0B)
        2 -> Color(0xFFA3E635)
        3 -> Color(0xFF34D399)
        4 -> Color(0xFF60A5FA)
        5 -> Color(0xFFC084FC)
        6 -> Color(0xFFF87171)
        else -> Color(0xFF475569)
    }
}