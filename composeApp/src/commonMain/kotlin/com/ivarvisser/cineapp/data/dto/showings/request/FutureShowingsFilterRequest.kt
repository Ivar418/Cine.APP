package com.ivarvisser.cineapp.data.dto.showings.request

import kotlin.time.Instant

data class FutureShowingsFilterRequest(
    val from: Instant?
)
