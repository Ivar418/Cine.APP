package com.ivarvisser.cineapp.data.dto.showings.response

import kotlinx.serialization.Serializable

@Serializable
data class ShowingPricesResponse(
    val adult: TicketPriceResponse,
    val child: TicketPriceResponse,
    val student: TicketPriceResponse,
    val senior: TicketPriceResponse
)
