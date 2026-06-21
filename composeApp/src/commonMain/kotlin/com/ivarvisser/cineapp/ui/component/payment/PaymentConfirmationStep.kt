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
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.back_button
import cineapp.composeapp.generated.resources.confirm_order
import cineapp.composeapp.generated.resources.confirm_reservation
import cineapp.composeapp.generated.resources.payment_method_label
import cineapp.composeapp.generated.resources.proceed_to_payment
import cineapp.composeapp.generated.resources.row_seat_ticket_price_format
import cineapp.composeapp.generated.resources.total_label
import com.ivarvisser.cineapp.domain.enums.PaymentMethods
import com.ivarvisser.cineapp.ui.feature.ordering.OrderingAction
import com.ivarvisser.cineapp.ui.feature.ordering.OrderingUiState
import org.jetbrains.compose.resources.stringResource

@Composable
fun PaymentConfirmationStep(
    state: OrderingUiState,
    onAction: (OrderingAction) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(Res.string.confirm_order),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    stringResource(
                        Res.string.payment_method_label,
                        state.selectedPaymentMethod.displayName
                    )
                )
            }
        }
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(state.summary.movieTitle, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(state.summary.startsAt)
                Spacer(modifier = Modifier.height(16.dp))
                state.seats.forEach {
                    Text(
                        stringResource(
                            Res.string.row_seat_ticket_price_format,
                            it.row,
                            it.seatNumber,
                            it.ticketType ?: "",
                            it.price.toString()
                        )
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(Res.string.total_label),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        "€${state.summary.totalPrice}",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = { onAction(OrderingAction.BackToPaymentMethods) }) {
                Text(
                    stringResource(Res.string.back_button)
                )
            }
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
                Text(
                    if (state.selectedPaymentMethod == PaymentMethods.Reservation) stringResource(
                        Res.string.confirm_reservation
                    ) else stringResource(Res.string.proceed_to_payment)
                )
            }
        }
    }
}
