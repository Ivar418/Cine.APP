package com.ivarvisser.cineapp.ui.component.payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.back_button
import cineapp.composeapp.generated.resources.booking_overview
import cineapp.composeapp.generated.resources.confirm_button
import cineapp.composeapp.generated.resources.row_seat_ticket_format
import cineapp.composeapp.generated.resources.total_label
import com.ivarvisser.cineapp.ui.feature.ordering.OrderingAction
import com.ivarvisser.cineapp.ui.feature.ordering.OrderingUiState
import org.jetbrains.compose.resources.stringResource

@Composable
fun OverviewStep(
    state: OrderingUiState,
    onAction: (OrderingAction) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(Res.string.booking_overview),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(state.summary.movieTitle)
                Text(state.summary.startsAt)
            }
        }
        state.seats.forEach {
            Text(
                stringResource(
                    Res.string.row_seat_ticket_format,
                    it.row,
                    it.seatNumber,
                    it.ticketType ?: ""
                )
            )
        }
        Card {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(Res.string.total_label),
                    style = MaterialTheme.typography.titleLarge
                )
                Text("€${state.summary.totalPrice}", style = MaterialTheme.typography.titleLarge)
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

            Button(
                onClick = { onAction(OrderingAction.OnBack) },
            ) { Text(stringResource(Res.string.back_button)) }
            Button(
                onClick = { onAction(OrderingAction.GoToPaymentMethods) },
            ) { Text(stringResource(Res.string.confirm_button)) }
        }
    }

}
