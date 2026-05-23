package com.ivarvisser.cineapp.ui.feature.ordering

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState


@Composable
fun SeatMapScreen(component: SeatMapComponent) {

    val state by component.state.subscribeAsState()

    LazyColumn {
        item {
            Text(
                "SCHERM",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        items(state.auditorium.getRowsAsList().size) { rowIndex ->

            val row = state.auditorium.getRowsAsList()[rowIndex]

            val rowSeats = state.seats
                .filter { it.row == rowIndex }
                .sortedBy { it.col }

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {

                Text("${rowIndex + 1}", modifier = Modifier.width(24.dp))

                rowSeats.forEach { seat ->

                    val key = "${seat.row}-${seat.col}"
                    val occupied = key in state.occupied
                    val suggested = key in state.suggested

                    SeatButton(
                        seat = seat,
                        occupied = occupied,
                        suggested = suggested,
                        onClick = { component.onSeatClick(seat) },
                        showZones = true
                    )
                }
            }
        }
    }
}