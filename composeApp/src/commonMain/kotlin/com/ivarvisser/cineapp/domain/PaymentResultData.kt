package com.ivarvisser.cineapp.domain

import androidx.compose.ui.graphics.Color

data class PaymentResultData(
    val icon: String,
    val title: String,
    val subtitle: String,
    val badgeColor: Color,
    val status: String
)