package com.ivarvisser.cineapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Auditorium(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("rowConfigJson")
    val rowConfigJson: String
) {
    fun getRowsAsList() = Json.decodeFromString<List<RowConfig>>(rowConfigJson)
}