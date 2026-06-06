@file:OptIn(ExperimentalMaterial3Api::class)

package com.ivarvisser.cineapp.ui.feature.ordering

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ivarvisser.cineapp.ui.component.payment.OverviewStep
import com.ivarvisser.cineapp.ui.component.payment.PaymentConfirmationStep
import com.ivarvisser.cineapp.ui.component.payment.PaymentMethodStep
import com.ivarvisser.cineapp.ui.component.payment.PaymentStatusStep
import com.ivarvisser.cineapp.ui.component.seatselection.SeatSelectionStep
import com.ivarvisser.cineapp.ui.component.seatselection.StepIndicator
import com.ivarvisser.cineapp.ui.component.ticketselection.TicketSelectionStep

@Composable
fun OrderingScreen(
    state: OrderingUiState,
    onAction: (OrderingAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                StepIndicator(currentStep = state.step)
            }
        },
        bottomBar = {
            if (state.step == 1) {
                Surface(
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp
                ) {
                    Button(
                        onClick = { onAction(OrderingAction.ConfirmSeats) },
                        enabled = state.pendingId != null || state.seatSelection.suggestedSeatKeys.isNotEmpty(),
                        modifier = Modifier
                            .padding(16.dp).fillMaxWidth()
                    ) {
                        Text("Confirm Seats")
                    }
                }

            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            when (state.step) {
                1 -> {
                    SeatSelectionStep(
                        state = state,
                        onAction = onAction
                    )
                }

                2 -> {
                    TicketSelectionStep(
                        state = state,
                        onAction = onAction
                    )
                }

                3 -> {
                    OverviewStep(
                        state = state,
                        onAction = onAction
                    )
                }

                4 -> {
                    PaymentMethodStep(
                        state = state,
                        onAction = onAction
                    )
                }

                5 -> {
                    PaymentConfirmationStep(
                        state = state,
                        onAction = onAction
                    )

                }

                6 -> {
                    PaymentStatusStep(
                        reservation = state.confirmedReservation,
                        order = state.order
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


