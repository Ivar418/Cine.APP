package com.ivarvisser.cineapp.data.remote.util

import com.ivarvisser.cineapp.utils.ResultOf
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.delay
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
    val maxRetries = 8
    val retryDelayMs = 2_000L

    repeat(maxRetries) { attempt ->
        try {
            return ResultOf.Success(apiCall())
        } catch (e: ConnectTimeoutException) {
            if (attempt == maxRetries - 1) {
                return ResultOf.Failure(
                    "The connection timed out. The server might be down.",
                    e
                )
            }

            Log.warn(loggerName = "SafeApiCall") {
                "Timeout on attempt ${attempt + 1}/$maxRetries. Retrying in 2 seconds..."
            }

            delay(retryDelayMs)
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is ResponseException -> {
                    when (e.response.status) {
                        HttpStatusCode.Unauthorized ->
                            "Session expired. Please log in again."

                        HttpStatusCode.Forbidden ->
                            "You don't have permission to view this."

                        HttpStatusCode.NotFound ->
                            "The requested resource was not found."

                        HttpStatusCode.TooManyRequests ->
                            "Too many requests. Please slow down."

                        in HttpStatusCode.BadRequest..HttpStatusCode.UnprocessableEntity ->
                            "Something went wrong with the request (${e.response.status.value})."

                        in HttpStatusCode.InternalServerError..HttpStatusCode.GatewayTimeout ->
                            "Server is currently having issues. Please try again later."

                        else ->
                            "Network error: ${e.response.status.value}"
                    }
                }

                is IOException ->
                    "Network error. Please check your internet connection."

                else ->
                    e.message ?: "An unexpected error occurred"
            }

            Log.error(loggerName = "SafeApiCall") {
                "Error: ${e.message}, Type: ${e::class.simpleName}, StackTrace: ${e.stackTraceToString()}"
            }

            return ResultOf.Failure(errorMessage, e)
        }
    }

    return ResultOf.Failure("Unexpected retry failure", null)
}