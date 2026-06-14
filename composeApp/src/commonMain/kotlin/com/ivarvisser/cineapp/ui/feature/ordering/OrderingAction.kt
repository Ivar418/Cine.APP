package com.ivarvisser.cineapp.ui.feature.ordering

import androidx.compose.ui.platform.UriHandler
import com.ivarvisser.cineapp.domain.enums.PaymentMethods

sealed interface OrderingAction {
    data object OnBack : OrderingAction
    data object IncreaseNormalSeats : OrderingAction
    data object DecreaseNormalSeats : OrderingAction
    data object IncreaseWheelchairSeats : OrderingAction
    data object DecreaseWheelchairSeats : OrderingAction
    data object SearchSeats : OrderingAction
    data object CancelPending : OrderingAction
    data class SeatClicked(val seatId: String) : OrderingAction
    data object ConfirmSeats : OrderingAction
    data class OnPaymentConfirmed(val uriHandler: UriHandler) : OrderingAction
    data class TicketTypeChanged(
        val seatId: String,
        val ticketType: String
    ) : OrderingAction

    data object GoToOverview : OrderingAction
    data object GoToPaymentMethods : OrderingAction
    data class PaymentMethodSelected(
        val paymentMethod: PaymentMethods
    ) : OrderingAction

    data object BackToPaymentMethods : OrderingAction
    data class ProcessOrder(val uriHandler: UriHandler) : OrderingAction
    data object CancelCheckout : OrderingAction
    data object Login : OrderingAction
    data class PerformLogin(val username: String, val password: String) : OrderingAction
}
