package com.ivarvisser.cineapp.ui.feature.ordering

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivarvisser.cineapp.domain.ENUM.SeatType
import com.ivarvisser.cineapp.domain.Seat

@Composable
fun SeatButton(
    seat: Seat,
    occupied: Boolean,
    suggested: Boolean,
    showZones: Boolean,
    onClick: () -> Unit
) {
    val isWC = seat.type == SeatType.Wheelchair

    val backgroundColor = when {
        suggested -> Color(0xFFFFC107)
        occupied -> Color(0xFFB0B0B0)
        isWC -> Color(0xFF64B5F6)
        showZones -> zoneColor(seat.category)
        else -> Color(0xFFE0E0E0)
    }

    val label = when {
        suggested && isWC -> "♿"
        suggested -> "★"
        occupied -> "✓"
        isWC -> "♿"
        else -> ""
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .padding(2.dp)
            .background(backgroundColor, shape = RoundedCornerShape(6.dp))
            .clickable(enabled = !(occupied && !suggested)) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, fontSize = 12.sp)
    }
}