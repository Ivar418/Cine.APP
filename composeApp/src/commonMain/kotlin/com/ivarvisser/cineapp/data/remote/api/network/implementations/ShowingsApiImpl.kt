package com.ivarvisser.cineapp.data.remote.api.network.implementations

import com.ivarvisser.cineapp.data.remote.api.network.interfaces.ShowingsApi
import com.ivarvisser.cineapp.data.remote.util.NetworkConstants
import com.ivarvisser.cineapp.data.remote.util.safeApiCall
import com.ivarvisser.cineapp.domain.Showing
import com.ivarvisser.cineapp.domain.ShowingDisplayResponse
import com.ivarvisser.cineapp.utils.ResultOf
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import net.codinux.log.Log

class ShowingsApiImpl(
    private val client: HttpClient
) : ShowingsApi {
    override suspend fun getShowingById(id: Int): ResultOf<Showing> {
        TODO("Not yet implemented")
    }

    override suspend fun getShowingsByMovieId(movieId: Int): ResultOf<List<ShowingDisplayResponse>> =
        safeApiCall {
            val result =
                client.get("${NetworkConstants.Endpoints.SHOWINGS}/movie/$movieId/upcoming")
                    .body<List<ShowingDisplayResponse>>()
            Log.debug(loggerName = "SHOWINGSAPIIMPL") { "Debug: Fetched showings for movieId: $movieId: $result." }
            result
        }
}