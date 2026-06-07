package com.ivarvisser.cineapp.data.remote.util

object NetworkConstants {

    // Timeouts
    const val TIMEOUT_MS = 30_000L
    const val CONNECT_TIMEOUT_MS = 30_000L
    const val SOCKET_TIMEOUT_MS = 30_000L

    // API Endpoints
    object Endpoints {
        const val MOVIES = "/api/movies"
        const val SHOWINGS = "/api/showings"
        const val USERS = "/api/users"
        const val AUTH = "/api/auth"
        const val ORDER = "/api/orders"
        const val RESERVATION = "/api/reservations"


        const val BASE_URL = ""


        // Headers
        object Headers {
            const val AUTHORIZATION = "Authorization"
            const val CONTENT_TYPE = "Content-Type"
            const val ACCEPT = "Accept"
        }

        object WasmVersions {
            const val PAYMENT_MOCK = "https://acc-cinenetwa.ivarvisser.nl/payment-mock"
            const val PAYMENT_RESULT = "https://acc-cinenetwa.ivarvisser.nl/checkout/payment-result"
        }
    }
}