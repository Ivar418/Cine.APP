package com.ivarvisser.cineapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

/**
 * Represents a response class that contains details for displaying a scheduled movie showing.
 *
 * This data class is primarily utilized for transferring relevant information about a specific
 * movie showing, including its association with a movie and an auditorium, scheduling details,
 * and optional metadata related to the showing.
 *
 * @property id Unique identifier for the movie showing.
 * @property movieId Identifier for the movie being shown.
 * @property auditoriumId Identifier for the auditorium where the showing takes place.
 * @property is3D Indicates whether the movie is shown in 3D.
 * @property startsAt The timestamp representing when the showing starts.
 * @property movieTitle Optional title of the movie.
 * @property auditoriumName Optional name of the auditorium.
 * @property runtime Optional runtime of the movie, measured in minutes.
 * @property auditoriumLayoutSnapshot Optional snapshot of the auditorium layout at the time of the showing.
 */
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