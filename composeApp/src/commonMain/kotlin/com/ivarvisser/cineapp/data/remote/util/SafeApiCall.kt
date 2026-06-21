package com.ivarvisser.cineapp.data.remote.util

import cineapp.composeapp.generated.resources.Res
import cineapp.composeapp.generated.resources.error_bad_request
import cineapp.composeapp.generated.resources.error_forbidden
import cineapp.composeapp.generated.resources.error_io
import cineapp.composeapp.generated.resources.error_not_found
import cineapp.composeapp.generated.resources.error_retry_failure
import cineapp.composeapp.generated.resources.error_server
import cineapp.composeapp.generated.resources.error_timeout
import cineapp.composeapp.generated.resources.error_too_many_requests
import cineapp.composeapp.generated.resources.error_unauthorized
import com.ivarvisser.cineapp.utils.ResultOf
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.delay
import kotlinx.io.IOException
import net.codinux.log.Log
import org.jetbrains.compose.resources.getString

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
                    getString(Res.string.error_timeout),
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
                            getString(Res.string.error_unauthorized)

                        HttpStatusCode.Forbidden ->
                            getString(Res.string.error_forbidden)

                        HttpStatusCode.NotFound ->
                            getString(Res.string.error_not_found)

                        HttpStatusCode.TooManyRequests ->
                            getString(Res.string.error_too_many_requests)

                        in HttpStatusCode.BadRequest..HttpStatusCode.UnprocessableEntity ->
                            getString(Res.string.error_bad_request, e.response.status.value)

                        in HttpStatusCode.InternalServerError..HttpStatusCode.GatewayTimeout ->
                            getString(Res.string.error_server)

                        else ->
                            "Network error: ${e.response.status.value}"
                    }
                }

                is IOException ->
                    getString(Res.string.error_io)

                else ->
                    e.message ?: "An unexpected error occurred"
            }

            Log.error(loggerName = "SafeApiCall") {
                "Error: ${e.message}, Type: ${e::class.simpleName}, StackTrace: ${e.stackTraceToString()}"
            }

            return ResultOf.Failure(errorMessage, e)
        }
    }

    return ResultOf.Failure(getString(Res.string.error_retry_failure), null)
}
