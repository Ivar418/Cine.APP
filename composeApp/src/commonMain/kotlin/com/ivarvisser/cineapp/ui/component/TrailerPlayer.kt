package com.ivarvisser.cineapp.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun TrailerPlayer(
    videoId: String,
    modifier: Modifier = Modifier
)
