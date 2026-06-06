package com.ivarvisser.cineapp.ui.component.payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.ivarvisser.cineapp.ui.feature.ordering.OrderingAction
import com.ivarvisser.cineapp.ui.feature.ordering.OrderingUiState

@Composable
fun PaymentConfirmationStep(
    state: OrderingUiState,
    onAction: (OrderingAction) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Confirm Order", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Payment Method: ${state.selectedPaymentMethod}")
            }
        }
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(state.summary.movieTitle, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(state.summary.startsAt)
                Spacer(modifier = Modifier.height(16.dp))
                state.seats.forEach {
                    Text("Row ${it.row} · Seat ${it.seatNumber} · ${it.ticketType} · €${it.price}")
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total", style = MaterialTheme.typography.titleLarge)
                    Text(
                        "€${state.summary.totalPrice}",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = { onAction(OrderingAction.BackToPaymentMethods) }) { Text("Back") }
            val uriHandler = LocalUriHandler.current
            Button(
                onClick = {
                    onAction(
                        OrderingAction.ProcessOrder(uriHandler)
                    )
                },
                enabled = !state.orderBusy
            ) {
                if (state.orderBusy) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(if (state.selectedPaymentMethod == "Reserveren") "Confirm Reservation" else "Proceed To Payment")
            }
        }
    }
}