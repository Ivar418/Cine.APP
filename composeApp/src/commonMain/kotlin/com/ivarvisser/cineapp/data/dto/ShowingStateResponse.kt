package com.ivarvisser.cineapp.data.dto

import com.ivarvisser.cineapp.domain.Seat
import com.ivarvisser.cineapp.domain.Showing
import kotlinx.serialization.Serializable

@Serializable
data class ShowingStateResponse(
    val showing: Showing?,
    val allSeats: List<Seat>,
    val occupiedKeys: HashSet<String>
)
