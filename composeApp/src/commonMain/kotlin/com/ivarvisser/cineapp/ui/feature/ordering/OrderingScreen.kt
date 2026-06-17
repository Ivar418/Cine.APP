package com.ivarvisser.cineapp.ui.feature.ordering

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.back_button
import cineapp.composeapp.generated.resources.confirm_seats
import cineapp.composeapp.generated.resources.login_to_continue
import com.ivarvisser.cineapp.ui.component.payment.OverviewStep
import com.ivarvisser.cineapp.ui.component.payment.PaymentConfirmationStep
import com.ivarvisser.cineapp.ui.component.payment.PaymentMethodStep
import com.ivarvisser.cineapp.ui.component.payment.PaymentStatusStep
import com.ivarvisser.cineapp.ui.component.seatselection.SeatSelectionStep
import com.ivarvisser.cineapp.ui.component.seatselection.StepIndicator
import com.ivarvisser.cineapp.ui.component.ticketselection.TicketSelectionStep
import com.ivarvisser.cineapp.ui.feature.account.LoginScreen
import org.jetbrains.compose.resources.stringResource

@Composable
fun OrderingScreen(
    state: OrderingUiState,
    onAction: (OrderingAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    StepIndicator(currentStep = state.step)
                }
            },
            bottomBar = {
                if (state.step == 1 && state.isLoggedIn) {
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
                            Text(stringResource(Res.string.confirm_seats))
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
                if (state.isLoggedIn) {
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
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(stringResource(Res.string.login_to_continue))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (!state.isLoggedIn) {
            Dialog(
                onDismissRequest = { /* Geen actie bij dismiss */ },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        LoginScreen(
                            onLoginClick = { username, password ->
                                onAction(OrderingAction.PerformLogin(username, password))
                            },
                            onRegisterClick = { onAction(OrderingAction.Login) } // Fallback to full login screen for registration
                        )
                        Button(
                            onClick = { onAction(OrderingAction.OnBack) },
                            modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
                        ) {
                            Text(stringResource(Res.string.back_button))
                        }
                    }
                }
            }
        }
    }
}


