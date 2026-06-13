package com.ivarvisser.cineapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

/**
 * Represents a scheduled movie showing event.
 *
 * This class is used to encapsulate details of a specific showing in the cinema,
 * including its association with a movie, auditorium, and additional metadata such as
 * whether it includes 3D, when it starts, and a snapshot of the auditorium layout.
 *
 * @property id Unique identifier for the showing.
 * @property auditoriumId Identifies the auditorium where the showing takes place.
 * @property movieId Identifies the movie being shown.
 * @property is3D Indicates whether the showing is in 3D.
 * @property startsAt Timestamp indicating when the showing begins.
 * @property auditoriumLayoutSnapshot Stores a snapshot of the auditorium layout as it exists at the time of the showing.
 * @property movie Optional property holding details about the associated movie, when available.
 * @property auditorium Optional property holding details about the associated auditorium, when available.
 */
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