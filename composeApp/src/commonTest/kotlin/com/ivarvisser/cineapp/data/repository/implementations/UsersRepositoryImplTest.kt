package com.ivarvisser.cineapp.data.repository.implementations

import com.ivarvisser.cineapp.data.dto.auth.response.AuthResponse
import com.ivarvisser.cineapp.data.dto.users.request.UpdateUserRequest
import com.ivarvisser.cineapp.data.dto.users.response.UserResponse
import com.ivarvisser.cineapp.fakes.FakeUserStorage
import com.ivarvisser.cineapp.fakes.FakeUsersApi
import com.ivarvisser.cineapp.utils.ResultOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UsersRepositoryImplTest {

    private val api = FakeUsersApi()
    private val storage = FakeUserStorage()
    private val repository = UsersRepositoryImpl(api, storage)

    private fun userResponse() = UserResponse(
        userId = 1,
        userName = "test",
        photoId = null,
        photoUrl = null,
        firstName = "Test",
        lastName = "User",
        email = "test@test.com"
    )

    @Test
    fun loginSavesUserAndTokens() = runTest {
        api.authResponse = AuthResponse(
            accessToken = "access",
            refreshToken = "refresh",
            user = userResponse()
        )

        val result = repository.login("test", "password")

        assertTrue(result is ResultOf.Success)
        assertEquals("test", storage.getUser()?.userName)
        assertEquals("access", storage.getAccessToken())
        assertEquals("refresh", storage.getRefreshToken())
    }

    @Test
    fun loginFailureDoesNotSaveUser() = runTest {
        api.error = "Invalid credentials"

        val result = repository.login("test", "wrong")

        assertTrue(result is ResultOf.Failure)
        assertNull(storage.getUser())
    }

    @Test
    fun logoutClearsStorage() = runTest {
        storage.saveUser(userResponse().let {
            com.ivarvisser.cineapp.domain.User(
                userId = it.userId,
                userName = it.userName,
                photoId = it.photoId,
                photoUrl = it.photoUrl,
                firstName = it.firstName,
                lastName = it.lastName,
                email = it.email
            )
        })
        storage.saveRefreshToken("refresh")

        repository.logout(1)

        assertNull(storage.getUser())
        assertNull(storage.getRefreshToken())
        assertEquals("refresh", api.lastLogoutToken)
    }

    @Test
    fun isLoggedInFalseWithoutRefreshToken() = runTest {
        assertFalse(repository.isLoggedIn())
    }

    @Test
    fun isLoggedInTrueWhenProfileFetchSucceeds() = runTest {
        storage.saveRefreshToken("refresh")
        api.profileResponse = userResponse()

        assertTrue(repository.isLoggedIn())
    }

    @Test
    fun updateProfileSavesUserOnSuccess() = runTest {
        api.updatedProfileResponse = userResponse().copy(firstName = "Updated")

        val result = repository.updateProfile(UpdateUserRequest(firstName = "Updated"))

        assertTrue(result is ResultOf.Success)
        assertEquals("Updated", storage.getUser()?.firstName)
    }
}
