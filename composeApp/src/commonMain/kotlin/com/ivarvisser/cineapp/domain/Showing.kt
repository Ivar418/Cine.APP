package com.ivarvisser.cineapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
class Showing(
    @SerialName("id")
    val id: Int,
    @SerialName("auditoriumId")
    val auditoriumId: Int,
    @SerialName("movieId")
    val movieId: Int,
    @SerialName("is3D")
    val is3D: Boolean,
    @SerialName("startsAt")
    val startsAt: Instant,
    @SerialName("auditoriumLayoutSnapshot")
    val auditoriumLayoutSnapshot: String,
    @SerialName("movie")
    val movie: Movie?,
    @SerialName("auditorium")
    val auditorium: Auditorium?
) {


}