package com.ivarvisser.cineapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RowConfig(
    @SerialName("Seats")
    val seats: Int,
    @SerialName("Wheelchair")
    val wheelchair: Int
) {
    val leftWheelchair: Int get() = (wheelchair + 1) / 2
    val rightWheelchair: Int get() = wheelchair / 2
}