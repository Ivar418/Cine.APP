package com.ivarvisser.cineapp.data.remote.api.network

import com.ivarvisser.cineapp.BuildKonfig
import com.ivarvisser.cineapp.data.dto.auth.request.RefreshRequest
import com.ivarvisser.cineapp.data.dto.auth.response.TokenResponse
import com.ivarvisser.cineapp.data.local.interfaces.TokenStorage
import com.ivarvisser.cineapp.data.remote.util.NetworkConstants.Endpoints.AUTH
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import net.codinux.log.Log


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
 * @param tokenStorage The storage used to manage authentication tokens.
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
                Log.debug(loggerName = "HttpClientManager") { "Loading tokens for request" }
                val accessToken = tokenStorage.getAccessToken()
                val refreshToken = tokenStorage.getRefreshToken()

                if (accessToken != null && refreshToken != null) {
                    BearerTokens(accessToken, refreshToken)
                } else {
                    Log.debug(loggerName = "HttpClientManager") { "No tokens found in storage" }
                    null
                }
            }

            refreshTokens {
                Log.debug(loggerName = "HttpClientManager") { "Refreshing tokens..." }
                val refreshToken = oldTokens?.refreshToken
                    ?: return@refreshTokens run {
                        Log.debug(loggerName = "HttpClientManager") { "No old refresh token available, cannot refresh" }
                        null
                    }
                val refreshClient = HttpClient {
                    install(ContentNegotiation) {
                        json(Json { ignoreUnknownKeys = true })
                    }
                }
                try {
                    val request = refreshClient.post("$AUTH/refresh") {
                        val refreshRequest = RefreshRequest(refreshToken)
                        contentType(ContentType.Application.Json)
                        setBody(refreshRequest)
                    }
                    Log.debug(loggerName = "HttpClientManager") { "Refresh response: $request" }
                    val response = request.body<TokenResponse>()
                    Log.debug(loggerName = "HttpClientManager") { "Tokens refreshed successfully" }
                    tokenStorage.saveTokens(response.accessToken, response.refreshToken)

                    BearerTokens(
                        response.accessToken,
                        response.refreshToken
                    )
                } catch (e: Exception) {
                    Log.debug(loggerName = "HttpClientManager") { "Failed to refresh tokens: ${e.message}" }
//                    tokenStorage.clearTokens()
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
