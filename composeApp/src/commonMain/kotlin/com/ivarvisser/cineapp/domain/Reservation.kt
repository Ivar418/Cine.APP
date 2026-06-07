package com.ivarvisser.cineapp.domain

import kotlinx.serialization.Serializable

@Serializable
data class Reservation(
    val id: String,
    val showingId: String,
    val seats: List<Seat> = emptyList(),
    val status: String,
    val showing: Showing? = null
)