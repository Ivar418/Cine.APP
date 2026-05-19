package com.ivarvisser.cineapp.domain

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
class Showing(
    val Id: Int,
    val MovieId: Int,
    val AuditoriumId: Int,
    val isThreeD: Boolean,
    val StartsAt: Instant,
    val AuditoriumLayoutSnapshot: String,
    val Movie: Movie,
    val Auditorium: Auditorium
) {


}