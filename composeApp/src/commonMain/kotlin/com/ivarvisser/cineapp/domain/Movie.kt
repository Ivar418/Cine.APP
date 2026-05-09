package com.ivarvisser.cineapp.domain

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Movie(
    val id: Int,
    val title: String = "",
    val tmdbId: Int = 0,

    /**
     * Language of the *information* about the movie (not the movie itself)
     * e.g. "en", "nl", or "und"
     */
    val informationLanguage: String = "und",

    val language: String? = null,

    val posterPath: String? = null,
    val backdropPath: String? = null,
    val youtubeTrailerKey: String? = null,

    val runtime: Int? = null,
    val imdbId: String? = null,

    val releaseDate: String? = null,
    val about: String? = null,
    val ageIndication: String? = null,

    val spokenLanguageName: String? = null,
    val spokenLanguageCodeIso6391: String? = null,

    val genresIds: List<Int>? = null,

    val rowCreatedTimestampUtc: Instant,
    val rowUpdatedTimestampUtc: Instant? = null,
    val rowDeletedTimestampUtc: Instant? = null
)