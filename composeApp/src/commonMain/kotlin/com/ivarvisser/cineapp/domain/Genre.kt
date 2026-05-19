package com.ivarvisser.cineapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    @SerialName("id") val id: Int,
    @SerialName("tmdbId") val TmdbId: Int,
    @SerialName("name") val Name: String = "Unknown", // Default value for name if not provided;
    @SerialName("language") val Language: String = "und"
)
