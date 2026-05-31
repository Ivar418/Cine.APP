package com.ivarvisser.cineapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data model representing a film genre.
 *
 * This class is used to encapsulate details about a specific genre of movies, including
 * its unique identifier, associated TMDB (The Movie Database) identifier, name, and language.
 *
 * @property id Unique identifier for the genre.
 * @property TmdbId Unique identifier for the genre as recognized by TMDB.
 * @property Name Name of the genre. Defaults to "Unknown" if not provided.
 * @property Language Language in which the genre information is provided, represented as an ISO 639-1 code.
 *                     Defaults to "und" to indicate undefined.
 */
@Serializable
data class Genre(
    @SerialName("id") val id: Int,
    @SerialName("tmdbId") val TmdbId: Int,
    @SerialName("name") val Name: String = "Unknown", // Default value for name if not provided;
    @SerialName("language") val Language: String = "und"
)
