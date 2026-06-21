package com.ivarvisser.cineapp.data.remote.api.network.implementations

import com.ivarvisser.cineapp.data.dto.auth.request.LoginRequest
import com.ivarvisser.cineapp.data.dto.auth.request.LogoutRequest
import com.ivarvisser.cineapp.data.dto.auth.request.RegisterRequest
import com.ivarvisser.cineapp.data.dto.auth.response.AuthResponse
import com.ivarvisser.cineapp.data.dto.users.request.UpdateUserRequest
import com.ivarvisser.cineapp.data.dto.users.response.UserFavoriteMoviesListResponse
import com.ivarvisser.cineapp.data.dto.users.response.UserResponse
import com.ivarvisser.cineapp.data.remote.api.network.interfaces.UsersApi
import com.ivarvisser.cineapp.data.remote.util.NetworkConstants
import com.ivarvisser.cineapp.data.remote.util.safeApiCall
import com.ivarvisser.cineapp.utils.ResultOf
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import net.codinux.log.Log

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
        Log.debug(loggerName = "UsersApiImpl") { "Debug: Register result: $result" }
        result
    }

    override suspend fun logout(refreshToken: String): ResultOf<Unit> = safeApiCall {
        client.post("${NetworkConstants.Endpoints.AUTH}/logout") {
            contentType(ContentType.Application.Json)
            setBody(LogoutRequest(refreshToken))
        }
        Log.debug(loggerName = "UsersApiImpl") { "Debug: Logout result: Unit" }
    }


    override suspend fun getFavoriteMovies(): ResultOf<UserFavoriteMoviesListResponse> =
        safeApiCall {
            val result = client.get("${NetworkConstants.Endpoints.USERS}/me/favorites")
                .body<UserFavoriteMoviesListResponse>()
            Log.debug(loggerName = "UsersApiImpl") { "Debug: Get favorite movies result: $result" }
            result
        }

    override suspend fun addFavoriteMovie(movieId: Int): ResultOf<UserFavoriteMoviesListResponse> =
        safeApiCall {
            val result = client.post("${NetworkConstants.Endpoints.USERS}/me/favorites/$movieId")
                .body<UserFavoriteMoviesListResponse>()
            Log.debug(loggerName = "UsersApiImpl") { "Debug: Add favorite movie result: $result" }
            result
        }

    override suspend fun removeFavoriteMovie(movieId: Int): ResultOf<UserFavoriteMoviesListResponse> =
        safeApiCall {
            val result = client.delete("${NetworkConstants.Endpoints.USERS}/me/favorites/$movieId")
                .body<UserFavoriteMoviesListResponse>()
            Log.debug(loggerName = "UsersApiImpl") { "Debug: Remove favorite movie result: $result" }
            result
        }

    override suspend fun getProfile(): ResultOf<UserResponse> = safeApiCall {
        val result = client.get("${NetworkConstants.Endpoints.USERS}/me/profile")
            .body<UserResponse>()
        Log.debug(loggerName = "UsersApiImpl") { "Debug: Get profile result: $result" }
        result
    }

    override suspend fun updateProfile(request: UpdateUserRequest): ResultOf<UserResponse> =
        safeApiCall {
            val result = client.put("${NetworkConstants.Endpoints.USERS}/me/profile") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
                .body<UserResponse>()
            Log.debug(loggerName = "UsersApiImpl") { "Debug: Update profile result: $result" }
            result
        }
}
