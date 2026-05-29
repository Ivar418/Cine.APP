@file:OptIn(ExperimentalMaterial3Api::class)

package com.ivarvisser.cineapp.ui.feature.ordering

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

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
                .verticalScroll(rememberScrollState())
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
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StepIndicator(
    currentStep: Int
) {
    val labels = listOf("Seats", "Tickets", "Overview", "Payment")
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        labels.forEachIndexed { index, label ->
            FilterChip(
                selected = currentStep == index + 1,
                onClick = {},
                label = { Text(label) }
            )
        }
    }
}

@Composable
private fun SeatSelectionStep(
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
                        state.legend.forEach {
                            AssistChip(onClick = {}, label = { Text(it.label) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SeatCounter(
    title: String,
    count: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Column {
        Text(text = title, style = MaterialTheme.typography.labelMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDecrease) {
                Icon(
                    Icons.Default.Remove,
                    contentDescription = null
                )
            }
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onIncrease) { Icon(Icons.Default.Add, contentDescription = null) }
        }
    }
}

@Composable
private fun SeatMap(
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
                for (row in 0..maxRow) {

                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${row + 1}",
                            modifier = Modifier.width(24.dp)
                        )

                        Spacer(Modifier.width(8.dp))

                        Row {
                            for (col in 0..maxCol) {
                                val seat = seatMap[row to col]

                                if (seat != null) {
                                    SeatButton(
                                        seat = seat,
                                        occupied = "${row}-${col}" in state.seatSelection.occupiedSeatKeys,
                                        suggested = "${row}-${col}" in state.seatSelection.suggestedSeatKeys,
                                        showZones = state.showZones,
                                        onClick = {
                                            onAction(
                                                OrderingAction.SeatClicked("${row}-${col}")
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
    }
}


@Composable
private fun TicketSelectionStep(
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
            ) { Text("Next") }
        }
    }
}

@Composable
private fun TicketTypeDropdown(
    selected: String?,
    onSelected: (String) -> Unit
) {
    val items = listOf("Adult", "Student", "Child", "Senior")
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }) { Text(selected ?: "Select") }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        expanded = false
                        onSelected(it)
                    }
                )
            }
        }
    }
}

@Composable
private fun OverviewStep(
    state: OrderingUiState,
    onAction: (OrderingAction) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Booking Overview", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(state.summary.movieTitle)
                Text(state.summary.startsAt)
            }
        }
        state.seats.forEach {
            Text("Row ${it.row} Seat ${it.seatNumber} - ${it.ticketType}")
        }
        Card {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", style = MaterialTheme.typography.titleLarge)
                Text("€${state.summary.totalPrice}", style = MaterialTheme.typography.titleLarge)
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

            Button(
                onClick = { onAction(OrderingAction.OnBack) },
            ) { Text("Back") }
            Button(
                onClick = { onAction(OrderingAction.GoToPaymentMethods) },
            ) { Text("Confirm") }
        }
    }

}

@Composable
private fun PaymentMethodStep(
    state: OrderingUiState,
    onAction: (OrderingAction) -> Unit
) {
    Column {
        Text(text = "Choose Payment Method", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            state.paymentMethods.forEach {
                Card(
                    onClick = { onAction(OrderingAction.PaymentMethodSelected(it)) },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Payment,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(it)
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentConfirmationStep(
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
            Button(
                onClick = { onAction(OrderingAction.ProcessOrder) },
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
