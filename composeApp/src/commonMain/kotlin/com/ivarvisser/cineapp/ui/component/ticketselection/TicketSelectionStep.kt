package com.ivarvisser.cineapp.ui.component.ticketselection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ivarvisser.cineapp.ui.feature.ordering.OrderingAction
import com.ivarvisser.cineapp.ui.feature.ordering.OrderingUiState

@Composable
fun TicketSelectionStep(
    state: OrderingUiState,
    onAction: (OrderingAction) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        state.seats.forEach { seat ->
            Card {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Row ${seat.row} Seat ${seat.seatNumber}")
                    TicketTypeDropdown(
                        selected = seat.ticketType,
                        onSelected = { onAction(OrderingAction.TicketTypeChanged(seat.id, it)) }
                    )
                    Text("€${seat.price}")
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = { onAction(OrderingAction.OnBack) },
            ) { Text("Back") }
            Button(
                onClick = { onAction(OrderingAction.GoToOverview) },
                enabled = state.seats.all { it.ticketType != null && it.ticketType != "Select" }
            ) { Text("Next") }
        }
    }
}