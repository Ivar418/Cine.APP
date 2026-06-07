package com.ivarvisser.cineapp.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmRequest(
    val suggestionId: String
)
