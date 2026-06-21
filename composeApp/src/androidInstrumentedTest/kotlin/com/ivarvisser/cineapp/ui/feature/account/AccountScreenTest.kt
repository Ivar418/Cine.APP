package com.ivarvisser.cineapp.ui.feature.account

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.ivarvisser.cineapp.domain.User
import com.ivarvisser.cineapp.fakes.FakeAppSettingsRepository
import com.ivarvisser.cineapp.fakes.FakeUsersRepository
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Test
import kotlin.test.assertNull

class AccountScreenTest {

    private val lifecycle = LifecycleRegistry()
    private val usersRepository = FakeUsersRepository()
    private val appSettingsRepository = FakeAppSettingsRepository()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testLoggedInUserDetailsAreDisplayed() =
        runComposeUiTest(effectContext = UnconfinedTestDispatcher()) {
            usersRepository.user = User(
                userId = 1,
                userName = "test",
                photoId = null,
                photoUrl = null,
                firstName = "Test",
                lastName = "User",
                email = "test@test.com"
            )

            val component = AccountComponent(
                componentContext = DefaultComponentContext(lifecycle = lifecycle),
                usersRepository = usersRepository,
                appSettingsRepository = appSettingsRepository,
                onGoBack = {},
                onNavigateToFavorites = {},
                onNavigateToEditProfile = {}
            )
            lifecycle.create()

            setContent {
                AccountScreen(component = component)
            }

            onNodeWithText("Test User").assertIsDisplayed()
            onNodeWithText("test@test.com").assertIsDisplayed()
        }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testLogoutButtonClearsUser() =
        runComposeUiTest(effectContext = UnconfinedTestDispatcher()) {
            usersRepository.user = User(
                userId = 1,
                userName = "test",
                photoId = null,
                photoUrl = null,
                firstName = "Test",
                lastName = "User",
                email = "test@test.com"
            )

            val component = AccountComponent(
                componentContext = DefaultComponentContext(lifecycle = lifecycle),
                usersRepository = usersRepository,
                appSettingsRepository = appSettingsRepository,
                onGoBack = {},
                onNavigateToFavorites = {},
                onNavigateToEditProfile = {}
            )
            lifecycle.create()

            setContent {
                AccountScreen(component = component)
            }

            onNodeWithText("Logout", substring = true).performClick()

            waitForIdle()

            assertNull(usersRepository.user)
        }
}
