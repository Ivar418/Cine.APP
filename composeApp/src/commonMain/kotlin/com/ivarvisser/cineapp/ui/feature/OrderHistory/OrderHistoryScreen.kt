package com.ivarvisser.cineapp.ui.feature.OrderHistory

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.arrow_back_24px
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.ivarvisser.cineapp.data.dto.orders.response.TicketResponse
import com.ivarvisser.cineapp.domain.enums.OrderTypes
import com.ivarvisser.cineapp.ui.component.ErrorMessage
import org.jetbrains.compose.resources.painterResource

@Composable
fun OrderHistoryScreen(component: OrderHistoryComponent) {
    val state by component.state.subscribeAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { component.onBack() }) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_back_24px),
                    contentDescription = "Back"
                )
                Text("Terug")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "Mijn Bestellingen",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(end = 16.dp)
            )
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.error != null) {
            ErrorMessage(message = state.error!!, onRetry = { component.loadOrders() })
        } else if (state.orders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Je hebt nog geen bestellingen.")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                items(state.orders) { item ->
                    OrderItem(item, component)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun OrderItem(item: OrderWithDetails, component: OrderHistoryComponent) {
    val order = item.order
    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable { component.toggleOrderExpansion(order.orderId) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Resolved Movie Poster
                if (item.movie != null) {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w92/" + item.movie.posterPath,
                        contentDescription = item.movie.title,
                        modifier = Modifier.size(60.dp, 90.dp).clip(RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.movie?.title ?: "Bestelling #${order.orderCode}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "€${order.totalAmount}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Status: ${order.paymentStatus}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // PDF Icons
                Column {
                    IconButton(onClick = { component.downloadAndOpenOrderPdf(order.orderId) }) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = "Download PDF",
                            tint = if (item.order.orderType == OrderTypes.Reservation) Color.Red else Color.Blue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            AnimatedVisibility(visible = item.isExpanded) {
                Column {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    if (item.isLoadingTickets) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                    } else if (item.tickets.isEmpty()) {
                        Text(
                            text = "Geen tickets gevonden voor deze bestelling.",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        item.tickets.forEach { ticket ->
                            TicketItem(ticket)
                            if (ticket != item.tickets.last()) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    thickness = 0.5.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        }
                    }
                    Button(
                        onClick = { component.downloadAndOpenOrderPdf(order.orderId) },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Open Ticket (PDF)")
                    }
                }
            }
        }
    }
}

@Composable
fun TicketItem(ticket: TicketResponse) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Stoel ${ticket.seatNumber} (${ticket.ticketType})",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Status: ${ticket.status}",
                style = MaterialTheme.typography.bodySmall,
                color = if (ticket.status == "Valid") Color(0xFF4CAF50) else MaterialTheme.colorScheme.error
            )
        }
        Text(
            text = "€${ticket.price}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
