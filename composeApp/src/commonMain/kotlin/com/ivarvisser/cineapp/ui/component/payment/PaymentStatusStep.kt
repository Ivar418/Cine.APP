package com.ivarvisser.cineapp.ui.component.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivarvisser.cineapp.domain.Order
import com.ivarvisser.cineapp.domain.PaymentResultData
import com.ivarvisser.cineapp.domain.Reservation

@Composable
fun PaymentStatusStep(reservation: Reservation?, order: Order?) {
    if (reservation == null || order == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Bestelling wordt geladen...", style = MaterialTheme.typography.bodyLarge)
            }
        }
    } else {
        val result = when (order.paymentStatus) {
            "Paid", "Success", "Succeeded" -> PaymentResultData(
                icon = "✅",
                title = "Betaling geslaagd!",
                subtitle = "Uw betaling is succesvol ontvangen.",
                badgeColor = Color(0xFF4CAF50),
                status = "Geslaagd"
            )

            "Failed" -> PaymentResultData(
                icon = "❌",
                title = "Betaling mislukt",
                subtitle = "Er is een fout opgetreden bij de betaling.",
                badgeColor = Color(0xFFF44336),
                status = "Mislukt"
            )

            "Cancelled" -> PaymentResultData(
                icon = "🚫",
                title = "Betaling geannuleerd",
                subtitle = "U heeft de betaling geannuleerd.",
                badgeColor = Color(0xFF757575),
                status = "Geannuleerd"
            )

            "Expired" -> PaymentResultData(
                icon = "⏱️",
                title = "Betaling verlopen",
                subtitle = "De betaaltermijn is verlopen. Probeer opnieuw.",
                badgeColor = Color(0xFFFF9800),
                status = "Verlopen"
            )

            else -> PaymentResultData( // Pending / Open
                icon = "🔄",
                title = "In behandeling",
                subtitle = "Uw betaling wordt nog verwerkt.",
                badgeColor = Color(0xFF2196F3),
                status = "In behandeling"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = result.icon,
                        fontSize = 48.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = result.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = result.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .background(color = result.badgeColor, shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = result.status.uppercase(),
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                    DetailRow("Referentie", order.orderCode)
                    DetailRow("Bedrag", "€ ${order.totalAmount.toString().replace(".", ",")}")
                    DetailRow("Betaalmethode", order.paymentMethod)
                    DetailRow("Bestel ID", "#${order.orderId}")

                    if (order.paymentStatus == "Pending") {
                        Spacer(modifier = Modifier.height(24.dp))
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Status aan het controleren...",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}