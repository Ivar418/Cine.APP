package com.ivarvisser.cineapp.domain

import com.ivarvisser.cineapp.domain.ENUM.SeatType
import kotlinx.serialization.Serializable

/**
 * Represents a seat within an auditorium's layout.
 *
 * This class models the attributes of a seat, including its position within a row
 * and column, its type, and its category. It optionally supports virtual column
 * information, which can be used for flexible seat arrangements.
 *
 * @property row The row number of the seat within the auditorium.
 * @property col The column number of the seat within the row.
 * @property virtualCol Used for grouping seats into a virtual layout with color capabilities.
 * @property type The type of the seat, defining its usage or accessibility (e.g., normal or wheelchair).
 * @property category The category of the seat, representing its classification or pricing tier.
 */
@Serializable
data class Seat(
    val row: Int,
    val col: Int,
    val virtualCol: Int,
    val type: SeatType,
    val category: Int
)

data class SeatCell(
    val seat: Seat?,
    val colorGroup: Int
)

data class SeatRow(
    val rowIndex: Int,
    val seats: List<SeatCell>
)