package com.ivarvisser.cineapp.data.remote.api.network.implementations

import com.ivarvisser.cineapp.data.dto.AuthResponse
import com.ivarvisser.cineapp.data.dto.LoginRequest
import com.ivarvisser.cineapp.data.dto.LogoutRequest
import com.ivarvisser.cineapp.data.dto.RegisterRequest
import com.ivarvisser.cineapp.data.dto.UserFavoriteMoviesListResponse
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.UsersApi
import com.ivarvisser.cineapp.data.remote.util.NetworkConstants
import com.ivarvisser.cineapp.data.remote.util.safeApiCall
import com.ivarvisser.cineapp.utils.ResultOf
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class UsersApiImpl(
    private val client: HttpClient
) : UsersApi {
    override suspend fun login(
        username: String,
        password: String
    ): ResultOf<AuthResponse> = safeApiCall {
        val loginRequest = LoginRequest(username, password)
        val result =
            client.post("${NetworkConstants.Endpoints.AUTH}/login") {
                contentType(ContentType.Application.Json)
                setBody(loginRequest)
            }
                .body<AuthResponse>()
        result
    }

    override suspend fun register(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): ResultOf<AuthResponse> = safeApiCall {
        val registerRequest = RegisterRequest(username, firstName, lastName, email, password)
        val result =
            client.post("${NetworkConstants.Endpoints.AUTH}/register") {
                contentType(ContentType.Application.Json)
                setBody(registerRequest)
            }
                .body<AuthResponse>()
        result
    }

    override suspend fun logout(refreshToken: String): ResultOf<Unit> = safeApiCall {
        client.post("${NetworkConstants.Endpoints.AUTH}/logout") {
            contentType(ContentType.Application.Json)
            setBody(LogoutRequest(refreshToken))
        }
    }

    override suspend fun refreshToken(refreshToken: String): ResultOf<AuthResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteMovies(): ResultOf<UserFavoriteMoviesListResponse> =
        safeApiCall {
            val result = client.get("${NetworkConstants.Endpoints.USERS}/me/favorites")
                .body<UserFavoriteMoviesListResponse>()
            result
        }

    override suspend fun addFavoriteMovie(movieId: Int): ResultOf<UserFavoriteMoviesListResponse> =
        safeApiCall {
            val result = client.post("${NetworkConstants.Endpoints.USERS}/me/favorites/$movieId")
                .body<UserFavoriteMoviesListResponse>()
            result
        }

    override suspend fun removeFavoriteMovie(movieId: Int): ResultOf<UserFavoriteMoviesListResponse> =
        safeApiCall {
            val result = client.delete("${NetworkConstants.Endpoints.USERS}/me/favorites/$movieId")
                .body<UserFavoriteMoviesListResponse>()
            result
        }
}
