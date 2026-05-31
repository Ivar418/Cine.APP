package com.ivarvisser.cineapp.domain

import com.ivarvisser.cineapp.domain.ENUM.SeatType

/**
 * Factory object responsible for creating seat arrangements for a given auditorium.
 *
 * The SeatFactory is used to generate a list of `Seat` objects based on the row configuration
 * provided by an `Auditorium`. Each row in the auditorium can define a number of seats,
 * along with specific allocations for wheelchair-accessible seats.
 */
object SeatFactory {

    /**
     * Generates a list of seats based on the configuration of the specified auditorium.
     * Each seat is assigned a row, column, type (normal or wheelchair-accessible),
     * and a category.
     *
     * @param auditorium The auditorium for which the seats are to be created. It contains
     *                   the row configuration defining the number of seats and wheelchair-accessible seats per row.
     * @return A list of Seat objects representing all the seats in the specified auditorium.
     */
    fun createSeats(auditorium: Auditorium): List<Seat> {
        return auditorium.getRowsAsList().flatMapIndexed { rowIndex, row ->

            (0 until row.seats).map { col ->

                val isWheelchair =
                    col < row.leftWheelchair ||
                            col >= row.seats - row.rightWheelchair

                Seat(
                    row = rowIndex,
                    col = col,
                    type = if (isWheelchair) SeatType.Wheelchair else SeatType.Normal,
                    category = 1
                )
            }
        }
    }
}