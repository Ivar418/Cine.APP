package com.ivarvisser.cineapp.ui.feature.ordering

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
    data class TicketTypeChanged(
        val seatId: String,
        val ticketType: String
    ) : OrderingAction

    data object GoToOverview : OrderingAction
    data object GoToPaymentMethods : OrderingAction
    data class PaymentMethodSelected(
        val paymentMethod: String
    ) : OrderingAction

    data object BackToPaymentMethods : OrderingAction
    data object ProcessOrder : OrderingAction
    data object CancelCheckout : OrderingAction
}
