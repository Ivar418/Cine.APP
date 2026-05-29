package com.ivarvisser.cineapp.domain

import com.ivarvisser.cineapp.domain.ENUM.SeatType

object SeatFactory {

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