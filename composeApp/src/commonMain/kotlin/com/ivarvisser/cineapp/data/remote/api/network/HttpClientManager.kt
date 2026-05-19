package com.ivarvisser.cineapp.data.remote.api.network

import com.ivarvisser.cineapp.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient() = HttpClient {
    expectSuccess = true // 4xx and 5xx will throw ResponseException
    install(ContentNegotiation) {
        json(Json {
            encodeDefaults = true
            isLenient = true
            coerceInputValues = true
            ignoreUnknownKeys = true
            decodeEnumsCaseInsensitive = true
        })
    }
    defaultRequest {
        url {
            protocol = if (BuildKonfig.PROTOCOL == "HTTPS") URLProtocol.HTTPS else URLProtocol.HTTP
            host = BuildKonfig.BASE_URL
        }
    }
}