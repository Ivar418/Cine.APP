package com.ivarvisser.cineapp.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Represents an auditorium in the cinema system.
 *
 * This class contains essential information about an auditorium, including its unique identifier,
 * name, and a JSON string representing the configuration of its seating rows.
 *
 * @property id Unique identifier of the auditorium.
 * @property name Name of the auditorium.
 * @property rowConfigJson JSON string that defines the configuration for seating rows within the auditorium.
 */
@Serializable
data class Auditorium(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("rowConfigJson")
    val rowConfigJson: String
) {
    /**
     * Decodes the `rowConfigJson` property of the `Auditorium` class into a list of `RowConfig` objects.
     *
     * This function uses the `Json` deserializer configured within the `Auditorium` class
     * to parse the JSON string and map it to a list of `RowConfig` instances.
     *
     * @return A list of `RowConfig` objects containing the configuration for auditorium rows.
     * @throws SerializationException If the JSON string cannot be parsed into the expected format.
     */
    fun getRowsAsList() = json.decodeFromString<List<RowConfig>>(rowConfigJson)

    /**
     * Companion object for the Auditorium class.
     * Contains utility elements to support the functionality of the enclosing class.
     */
    companion object {
        /**
         * A Json instance configured to ignore unknown keys during JSON deserialization.
         *
         * This configuration is useful when dealing with JSON structures that may include
         * additional fields that are not explicitly defined in the corresponding Kotlin data models.
         */
        private val json = Json { ignoreUnknownKeys = true }
    }
}