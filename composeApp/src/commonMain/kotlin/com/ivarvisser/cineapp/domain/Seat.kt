package com.ivarvisser.cineapp.domain

import com.ivarvisser.cineapp.domain.ENUM.SeatType

data class Seat(
    val row: Int,
    val col: Int,
    val type: SeatType,
    val category: Int
)