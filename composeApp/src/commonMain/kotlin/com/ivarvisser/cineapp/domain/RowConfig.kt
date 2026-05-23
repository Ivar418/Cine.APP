package com.ivarvisser.cineapp.domain

import kotlinx.serialization.Serializable

@Serializable
data class RowConfig(
    val seats: Int,
    val wheelchair: Int
) {
    val leftWheelchair: Int get() = (wheelchair + 1) / 2
    val rightWheelchair: Int get() = wheelchair / 2
}