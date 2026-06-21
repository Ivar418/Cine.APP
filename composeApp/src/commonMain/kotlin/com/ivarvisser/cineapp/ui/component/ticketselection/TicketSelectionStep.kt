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
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.back_button
import cineapp.composeapp.generated.resources.next_button
import cineapp.composeapp.generated.resources.row_seat_format
import cineapp.composeapp.generated.resources.ticket_type_select
import com.ivarvisser.cineapp.ui.feature.ordering.OrderingAction
import com.ivarvisser.cineapp.ui.feature.ordering.OrderingUiState
import org.jetbrains.compose.resources.stringResource

@Composable
fun TicketSelectionStep(
    state: OrderingUiState,
    onAction: (OrderingAction) -> Unit
) {
    val selectPlaceholder = stringResource(Res.string.ticket_type_select)
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        state.seats.forEach { seat ->
            Card {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(Res.string.row_seat_format, seat.row, seat.seatNumber))
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
            ) { Text(stringResource(Res.string.back_button)) }
            Button(
                onClick = { onAction(OrderingAction.GoToOverview) },
                enabled = state.seats.all { it.ticketType != null && it.ticketType != selectPlaceholder }
            ) { Text(stringResource(Res.string.next_button)) }
        }
    }
}
