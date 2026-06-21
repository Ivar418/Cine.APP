package com.ivarvisser.cineapp.ui.feature.account

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.ivarvisser.cineapp.domain.User
import com.ivarvisser.cineapp.fakes.FakeAppSettingsRepository
import com.ivarvisser.cineapp.fakes.FakeUsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AccountComponentTest {

    private val lifecycle = LifecycleRegistry()
    private val usersRepository = FakeUsersRepository()
    private val appSettingsRepository = FakeAppSettingsRepository()
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    private fun createComponent(
        onGoBack: () -> Unit = {},
        onNavigateToFavorites: () -> Unit = {},
        onNavigateToEditProfile: () -> Unit = {}
    ): AccountComponent {
        return AccountComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            usersRepository = usersRepository,
            appSettingsRepository = appSettingsRepository,
            onGoBack = onGoBack,
            onNavigateToFavorites = onNavigateToFavorites,
            onNavigateToEditProfile = onNavigateToEditProfile
        )
    }

    @Test
    fun loadsUserWhenLoggedIn() = runTest(testDispatcher) {
        usersRepository.user = User(
            userId = 1,
            userName = "test",
            photoId = null,
            photoUrl = null,
            firstName = "Test",
            lastName = "User",
            email = "test@test.com"
        )

        val component = createComponent()
        lifecycle.create()
        advanceUntilIdle()

        assertFalse(component.state.value.isLoading)
        assertTrue(component.state.value.isLoggedIn)
        assertEquals("test", component.state.value.user?.userName)
    }

    @Test
    fun keepsUserNullWhenNotLoggedIn() = runTest(testDispatcher) {
        val component = createComponent()
        lifecycle.create()
        advanceUntilIdle()

        assertFalse(component.state.value.isLoading)
        assertNull(component.state.value.user)
        assertFalse(component.state.value.isLoggedIn)
    }

    @Test
    fun logoutClearsUser() = runTest(testDispatcher) {
        usersRepository.user = User(
            userId = 1,
            userName = "test",
            photoId = null,
            photoUrl = null,
            firstName = "Test",
            lastName = "User",
            email = "test@test.com"
        )

        val component = createComponent()
        lifecycle.create()
        advanceUntilIdle()
        assertTrue(component.state.value.isLoggedIn)

        component.logout()
        advanceUntilIdle()

        assertNull(component.state.value.user)
        assertNull(usersRepository.user)
    }

    @Test
    fun togglingNotificationsUpdatesAppSettings() = runTest(testDispatcher) {
        val component = createComponent()
        lifecycle.create()
        advanceUntilIdle()

        assertTrue(component.state.value.locationNotificationsEnabled)

        component.onEvent(AccountAction.OnToggleLocationNotifications(false))
        advanceUntilIdle()

        assertFalse(component.state.value.locationNotificationsEnabled)
        assertFalse(appSettingsRepository.locationNotificationsEnabled.value)
    }

    @Test
    fun onBackActionTriggersCallback() {
        var backCalled = false
        val component = createComponent(onGoBack = { backCalled = true })

        component.onEvent(AccountAction.OnBack)

        assertTrue(backCalled)
    }

    @Test
    fun onFavoritesActionNavigates() {
        var navigated = false
        val component = createComponent(onNavigateToFavorites = { navigated = true })

        component.onEvent(AccountAction.OnFavorites)

        assertTrue(navigated)
    }
}
