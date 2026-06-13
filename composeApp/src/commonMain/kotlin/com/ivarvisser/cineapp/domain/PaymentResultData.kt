package com.ivarvisser.cineapp.domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class PaymentResultData(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val badgeColor: Color,
    val status: String
)
