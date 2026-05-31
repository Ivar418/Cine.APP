package com.ivarvisser.cineapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the configuration of a row within an auditorium.
 *
 * This class defines the number of seats in a row and how many of those seats
 * are designated as wheelchair accessible. Additional calculated properties
 * provide the distribution of wheelchair-accessible seats on the left and right
 * sides of the row.
 *
 * @property seats The total number of seats available in the row.
 * @property wheelchair The total number of wheelchair-accessible seats within the row.
 */
@Serializable
data class RowConfig(
    @SerialName("Seats")
    val seats: Int,
    @SerialName("Wheelchair")
    val wheelchair: Int
) {
    /**
     * Represents the count of wheelchair-accessible seats located on the left side of a row.
     *
     * This property calculates the number of left-side wheelchair seats based on the total wheelchair
     * seats in the configuration and ensures that the distribution is as balanced as possible.
     *
     * @return The number of wheelchair seats on the left side of the row.
     */
    val leftWheelchair: Int get() = (wheelchair + 1) / 2

    /**
     * Represents the count of wheelchair-accessible seats on the right side of a row in the auditorium.
     *
     * This property calculates its value as half of the total wheelchair-accessible seats
     * defined for a specific row configuration (`wheelchair`), rounding down to the nearest integer.
     */
    val rightWheelchair: Int get() = wheelchair / 2
}