package com.ivarvisser.cineapp.ui.component.payment

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ivarvisser.cineapp.domain.Order
import com.ivarvisser.cineapp.domain.Reservation

@Composable
fun PaymentStatusStep(reservation: Reservation?, order: Order?) {
    if (reservation == null || order == null) {
        Text("No reservation or order found")
    } else {
        Text("Reservation ID: ${reservation.id}")
        Text("Reservation status: ${reservation.status}")
        Text("Order ID: ${order.orderId}")
        Text("Payment type: ${order.paymentMethod}")
        Text("Payment status: ${order.paymentStatus}")
    }


}
