package com.ivarvisser.cineapp.domain

import com.ivarvisser.cineapp.domain.ENUM.SeatType

/**
 * Represents a single seat within an auditorium in the cinema system.
 *
 * This class provides information about the specific seat, including its
 * location in the auditorium (row and column), type (e.g., normal or wheelchair-accessible),
 * and category (for potential pricing or section differentiation).
 *
 * @property row The row number where the seat is located.
 * @property col The column number where the seat is located.
 * @property type The type of the seat, indicating whether it is a normal seat or wheelchair-accessible.
 * @property category Identifies the category of the seat, which can be used for pricing or section purposes.
 */
data class Seat(
    val row: Int,
    val col: Int,
    val type: SeatType,
    val category: Int
)