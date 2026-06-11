package com.ivarvisser.cineapp.data.remote.api.network

import com.ivarvisser.cineapp.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Creates and configures an instance of [HttpClient] with pre-defined settings.
 *
 * The configured client supports JSON content negotiation with customizable settings such as:
 * - Allowing default values during serialization and deserialization (`encodeDefaults`).
 * - Permitting lenient parsing of JSON (`isLenient`).
 * - Coercing unexpected data types to expected types where possible (`coerceInputValues`).
 * - Ignoring unknown keys while parsing JSON objects (`ignoreUnknownKeys`).
 * - Supporting case-insensitive parsing of enum values (`decodeEnumsCaseInsensitive`).
 *
 * The client defaults to expecting successful HTTP responses. Any response in the 4xx or 5xx range
 * results in a [ResponseException].
 *
 * By default, the client is configured to target the base URL and protocol defined
 * in [BuildKonfig]. The protocol is selected based on the `PROTOCOL` value ("HTTPS" or "HTTP"),
 * and the host is defined by `BASE_URL`.
 *
 * @return A fully configured [HttpClient] instance.
 */
fun createHttpClient(
    tokenStorage: TokenStorage
): HttpClient = HttpClient {

    expectSuccess = true

    install(ContentNegotiation) {
        json(
            Json {
                encodeDefaults = true
                isLenient = true
                coerceInputValues = true
                ignoreUnknownKeys = true
                decodeEnumsCaseInsensitive = true
            }
        )
    }

    install(Auth) {
        bearer {

            loadTokens {
                val accessToken = tokenStorage.accessToken
                val refreshToken = tokenStorage.refreshToken

                if (accessToken != null && refreshToken != null) {
                    BearerTokens(accessToken, refreshToken)
                } else {
                    null
                }
            }

            refreshTokens {
                val refreshToken = oldTokens?.refreshToken
                    ?: return@refreshTokens null

                try {
                    val response: TokenResponse =
                        client.post("/auth/refresh") {
                            setBody(
                                RefreshRequest(
                                    refreshToken = refreshToken
                                )
                            )
                        }.body()

                    tokenStorage.accessToken = response.accessToken
                    tokenStorage.refreshToken = response.refreshToken

                    BearerTokens(
                        response.accessToken,
                        response.refreshToken
                    )
                } catch (e: Exception) {
                    tokenStorage.clear()
                    null
                }
            }
        }
    }

    defaultRequest {
        url {
            protocol =
                if (BuildKonfig.PROTOCOL == "HTTPS")
                    URLProtocol.HTTPS
                else
                    URLProtocol.HTTP

            host = BuildKonfig.BASE_URL
        }
    }
}