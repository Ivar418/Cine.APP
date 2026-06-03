package com.ivarvisser.cineapp.ui.component.seatselection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivarvisser.cineapp.domain.ENUM.SeatType
import com.ivarvisser.cineapp.domain.Seat
import com.ivarvisser.cineapp.ui.feature.ordering.zoneColor

@Composable
fun SeatButton(
    seat: Seat,
    occupied: Boolean,
    suggested: Boolean,
    showZones: Boolean,
    onClick: () -> Unit,
    seatSize: Dp = 32.dp
) {
    val isWC = seat.type == SeatType.Wheelchair

    val shape = if (isWC) {
        RoundedCornerShape(8.dp)
    } else {
        RoundedCornerShape(4.dp)
    }

    val (backgroundColor, borderColor, textColor) = when {
        suggested -> Triple(
            Color(0xFFFBBF24).copy(alpha = 0.35f),
            Color(0xFFF59E0B),
            Color(0xFFFBBF24)
        )

        occupied -> Triple(
            Color(0xFF131820),
            Color(0xFF1E293B),
            Color(0xFF374151)
        )

        showZones -> {
            val zone = zoneColor(seat.category)

            Triple(
                zone.copy(alpha = 0.15f),
                zone,
                zone
            )
        }

        else -> Triple(
            Color(0xFF1E293B),
            Color(0xFF293548),
            Color(0xFF475569)
        )
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
            .size(seatSize)
            .padding(2.dp)
            .border(
                width = 1.5.dp,
                color = borderColor,
                shape = shape
            )
            .background(
                color = backgroundColor,
                shape = shape
            )
            .clickable(
                enabled = !(occupied && !suggested)
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = if (isWC || suggested) 14.sp else 10.sp
        )
    }
}