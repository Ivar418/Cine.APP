package com.ivarvisser.cineapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class ShowingDisplayResponse(

    @SerialName("id")
    val id: Int,

    @SerialName("movieId")
    val movieId: Int,

    @SerialName("auditoriumId")
    val auditoriumId: Int,

    @SerialName("is3D")
    val is3D: Boolean,

    @SerialName("startsAt")
    val startsAt: Instant,

    @SerialName("movieTitle")
    val movieTitle: String? = null,

    @SerialName("auditoriumName")
    val auditoriumName: String? = null,

    @SerialName("runtime")
    val runtime: Int? = null,

    @SerialName("auditoriumLayoutSnapshot")
    val auditoriumLayoutSnapshot: String? = null
) {
}