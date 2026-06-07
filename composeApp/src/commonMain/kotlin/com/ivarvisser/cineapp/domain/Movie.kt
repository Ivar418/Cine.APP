package com.ivarvisser.cineapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a movie entity within the application.
 *
 * This class contains metadata and details about a movie, such as its title, identifier,
 * associated resource paths, and additional descriptive attributes.
 *
 * @property id Unique identifier for the movie.
 * @property title Title of the movie. Defaults to an empty string if not provided.
 * @property tmdbId Unique identifier for the movie as recognized by TMDB (The Movie Database).
 * @property informationLanguage Specifies the language of the metadata about the movie. Uses ISO 639-1 codes.
 *                               Defaults to "und" (undefined).
 * @property language Language of the movie content, represented as an ISO 639-1 code. Optional.
 * @property posterPath Relative path to the poster image of the movie. Optional.
 * @property backdropPath Relative path to a backdrop image of the movie. Optional.
 * @property youtubeTrailerKey YouTube video key for the movie trailer. Optional.
 * @property runtime Duration of the movie in minutes. Optional.
 * @property imdbId Unique identifier for the movie as listed on IMDb. Optional.
 * @property releaseDate Release date of the movie in ISO-8601 string format. Optional.
 * @property about Description or summary of the movie. Optional.
 * @property ageIndication Age rating or parental guidance information for the movie. Optional.
 * @property spokenLanguageName Name of the primary spoken language in the movie. Optional.
 * @property spokenLanguageCodeIso6391 ISO 639-1 code of the primary spoken language in the movie. Optional.
 * @property genresIds List of genre IDs associated with the movie. Optional.
 */
@Serializable
data class Movie(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String = "",
    @SerialName("tmdbId")
    val tmdbId: Int = 0,

    /**
     * Language of the *information* about the movie (not the movie itself)
     * e.g. "en", "nl", or "und"
     */
    @SerialName("informationLanguage")
    val informationLanguage: String = "und",

    @SerialName("language")
    val language: String? = null,
    @SerialName("posterPath")
    val posterPath: String? = null,
    @SerialName("backdropPath")
    val backdropPath: String? = null,
    @SerialName("youtubeTrailerKey")
    val youtubeTrailerKey: String? = null,
    @SerialName("runtime")
    val runtime: Int? = null,
    @SerialName("imdbId")
    val imdbId: String? = null,
    @SerialName("releaseDate")
    val releaseDate: String? = null,
    @SerialName("about")
    val about: String? = null,
    @SerialName("ageIndication")
    val ageIndication: String? = null,
    @SerialName("spokenLanguageName")
    val spokenLanguageName: String? = null,
    @SerialName("spokenLanguageCodeIso6391")
    val spokenLanguageCodeIso6391: String? = null,
    @SerialName("genresIds")
    val genresIds: List<Int>? = null,
)