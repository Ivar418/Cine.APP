package com.ivarvisser.cineapp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ShowingPricesResponse(
    val adult: TicketPriceResponse,
    val child: TicketPriceResponse,
    val student: TicketPriceResponse,
    val senior: TicketPriceResponse
)
