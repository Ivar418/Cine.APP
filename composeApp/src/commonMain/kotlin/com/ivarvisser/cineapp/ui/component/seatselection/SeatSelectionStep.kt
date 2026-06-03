package com.ivarvisser.cineapp.ui.component.seatselection

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ChipColors
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ivarvisser.cineapp.ui.feature.ordering.OrderingAction
import com.ivarvisser.cineapp.ui.feature.ordering.OrderingUiState

@Composable
fun SeatSelectionStep(
    state: OrderingUiState,
    onAction: (OrderingAction) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                state.showing?.let {
                    Text(text = it.movieTitle, style = MaterialTheme.typography.titleLarge)
                    Text(text = it.startsAt, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    SeatCounter(
                        title = "🪑 Normal",
                        count = state.seatSelection.normalCount,
                        onIncrease = { onAction(OrderingAction.IncreaseNormalSeats) },
                        onDecrease = { onAction(OrderingAction.DecreaseNormalSeats) }
                    )
                    SeatCounter(
                        title = "♿ Wheelchair",
                        count = state.seatSelection.wheelchairCount,
                        onIncrease = { onAction(OrderingAction.IncreaseWheelchairSeats) },
                        onDecrease = { onAction(OrderingAction.DecreaseWheelchairSeats) }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { onAction(OrderingAction.SearchSeats) }) {
                        Icon(Icons.Default.Search, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Search Seats")
                    }
                    if (state.pendingId != null) {
                        OutlinedButton(onClick = { onAction(OrderingAction.CancelPending) }) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }

        if (state.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        state.showing?.let {
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AssistChip(onClick = {}, label = { Text(it.auditoriumName) })
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    SeatMap(state = state, onAction = onAction)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        state.legend.forEach { legendItem ->
                            val color: ChipColors = when (legendItem.label) {
                                "Available" -> AssistChipDefaults.assistChipColors(containerColor = legendItem.color)
                                "Occupied" -> AssistChipDefaults.assistChipColors(containerColor = legendItem.color)
                                "Selected" -> AssistChipDefaults.assistChipColors(containerColor = legendItem.color)
                                "Wheelchair" -> AssistChipDefaults.assistChipColors(containerColor = legendItem.color)
                                else -> AssistChipDefaults.assistChipColors()
                            }
                            AssistChip(
                                onClick = {},
                                label = { Text(legendItem.label) },
                                colors = color
                            )
                        }
                    }
                }
            }
        }
    }
}