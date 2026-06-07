package com.ivarvisser.cineapp.ui.component.seatselection

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ivarvisser.cineapp.ui.feature.ordering.OrderingAction
import com.ivarvisser.cineapp.ui.feature.ordering.OrderingUiState

@Composable
fun SeatMap(
    state: OrderingUiState,
    onAction: (OrderingAction) -> Unit
) {
    if (state.seatSelection.allSeats.isEmpty()) return
    if (state.seatSelection.auditorium == null) return
    val maxRow =
        remember(state.seatSelection.allSeats) { state.seatSelection.allSeats.maxOf { it.row } }
    val maxCol =
        remember(state.seatSelection.allSeats) { state.seatSelection.allSeats.maxOf { it.col } }
    val seatMap = remember(state.seatSelection.allSeats) {
        state.seatSelection.allSeats.associateBy { it.row to it.col }
    }
    val seatsByRow = remember(state.seatSelection.allSeats) {
        state.seatSelection.allSeats.groupBy { it.row }
    }
    val maxRowWidth = maxCol + 1
    val seatSize = 32.dp
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
        )
        Text(
            "---------------SCREEN---------------",
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall,
        )
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val minWidth = maxWidth
            Column(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .widthIn(min = minWidth),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                state.seatSelection.grid.forEach { row ->
                    Row {
                        row.seats.forEach { seatCell ->
                            if (seatCell.seat == null) return@Column
                            SeatButton(
                                seat = seatCell.seat,
                                occupied = "${seatCell.seat.row}-${seatCell.seat.col}" in state.seatSelection.occupiedSeatKeys,
                                suggested = "${seatCell.seat.row}-${seatCell.seat.col}" in state.seatSelection.suggestedSeatKeys,
                                showZones = true,
                                onClick = {
                                    onAction(
                                        OrderingAction.SeatClicked("${seatCell.seat.row}-${seatCell.seat.col}")
                                    )
                                },
                                seatSize = seatSize
                            )
                        }
                    }


                }

            }
        }
    }
}