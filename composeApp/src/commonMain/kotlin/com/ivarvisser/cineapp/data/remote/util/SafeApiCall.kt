package com.ivarvisser.cineapp.data.remote.util

import com.ivarvisser.cineapp.utils.ResultOf
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import net.codinux.log.Log

/**
 * Executes a suspending API call safely, capturing any exceptions and returning a [ResultOf] instance
 * that indicates either success or failure. The method handles specific exceptions such as network errors
 * and server-side or client-side HTTP status codes, converting them to user-friendly error messages.
 *
 * @param T The type of the result expected from the API call.
 * @param apiCall A suspending lambda function representing the API call to be executed.
 * @return A [ResultOf] instance containing the success result or a failure with error details in case of an exception.
 */
suspend fun <T> safeApiCall(apiCall: suspend () -> T): ResultOf<T> {
    return try {
        ResultOf.Success(apiCall())
    } catch (e: Exception) {
        val errorMessage = when (e) {
            is ResponseException -> {
                when (e.response.status) {
                    // Actionable: User can fix this by logging in
                    HttpStatusCode.Unauthorized -> "Session expired. Please log in again."

                    // Actionable: User is restricted
                    HttpStatusCode.Forbidden -> "You don't have permission to view this."

                    // Actionable: Resource is gone
                    HttpStatusCode.NotFound -> "The requested resource was not found."

                    // Actionable: Server is overloaded
                    HttpStatusCode.TooManyRequests -> "Too many requests. Please slow down."

                    // Category: All other 4xx errors (Client side issues)
                    in HttpStatusCode.BadRequest..HttpStatusCode.UnprocessableEntity ->
                        "Something went wrong with the request (${e.response.status.value})."

                    // Category: All 5xx errors (Server side issues)
                    in HttpStatusCode.InternalServerError..HttpStatusCode.GatewayTimeout ->
                        "Server is currently having issues. Please try again later."

                    else -> "Network error: ${e.response.status.value}"
                }
            }

            is IOException -> "Network error. Please check your internet connection."
            is ConnectTimeoutException -> "The connection timed out. The server might be down."
            else -> e.message ?: "An unexpected error occurred"
        }
        Log.error(loggerName = "SafeApiCall") { "Error: ${e.message}, Type: ${e::class.simpleName}, StackTrace: ${e.stackTraceToString()}" }
        ResultOf.Failure(errorMessage, e)
    }
}