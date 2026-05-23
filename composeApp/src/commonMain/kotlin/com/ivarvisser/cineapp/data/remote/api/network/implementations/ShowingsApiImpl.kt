package com.ivarvisser.cineapp.data.remote.api.network.implementations

import com.ivarvisser.cineapp.data.remote.api.network.interfaces.ShowingsApi
import com.ivarvisser.cineapp.data.remote.util.NetworkConstants.Endpoints.SHOWINGS
import com.ivarvisser.cineapp.data.remote.util.safeApiCall
import com.ivarvisser.cineapp.domain.Showing
import com.ivarvisser.cineapp.utils.ResultOf
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import net.codinux.log.Log

class ShowingsApiImpl(
    private val client: HttpClient
) : ShowingsApi {
    override suspend fun getShowingById(id: Int): ResultOf<Showing> =
        safeApiCall {
            Log.debug(loggerName = "SHOWINGSAPIIMPL") { "Test: Fetching showing by id: $id." }
            val result = client.get("$SHOWINGS/$id").body<Showing>()
            Log.debug(loggerName = "SHOWINGSAPIIMPL") { "Debug: Fetched showing by id: $id: $result." }
            result
        }

    override suspend fun getShowingsByMovieId(movieId: Int): ResultOf<List<Showing>> =
        safeApiCall {
            Log.debug(loggerName = "SHOWINGSAPIIMPL") { "Test: Fetching showings for movieId: $movieId." }
            val result =
                client.get("$SHOWINGS/movie/$movieId/upcoming")
                    .body<List<Showing>>()
            Log.debug(loggerName = "SHOWINGSAPIIMPL") { "Debug: Fetched showings for movieId: $movieId: $result." }
            result
        }
}