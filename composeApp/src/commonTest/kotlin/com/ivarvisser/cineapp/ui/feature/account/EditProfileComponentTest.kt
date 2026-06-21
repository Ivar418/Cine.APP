package com.ivarvisser.cineapp.ui.feature.account

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.ivarvisser.cineapp.domain.User
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class EditProfileComponentTest {

    private val lifecycle = LifecycleRegistry()
    private val usersRepository = FakeUsersRepository()
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        usersRepository.user = User(
            userId = 1,
            userName = "test",
            photoId = null,
            photoUrl = null,
            firstName = "Test",
            lastName = "User",
            email = "test@test.com"
        )
    }

    private fun createComponent(
        onGoBack: () -> Unit = {},
        onProfileUpdated: () -> Unit = {}
    ): EditProfileComponent {
        return EditProfileComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            usersRepository = usersRepository,
            onGoBack = onGoBack,
            onProfileUpdated = onProfileUpdated
        )
    }

    @Test
    fun loadsUserDataOnCreate() = runTest(testDispatcher) {
        val component = createComponent()
        advanceUntilIdle()

        assertEquals("Test", component.state.value.firstName)
        assertEquals("User", component.state.value.lastName)
        assertEquals("test@test.com", component.state.value.email)
    }

    @Test
    fun updatingFieldsChangesState() {
        val component = createComponent()

        component.onFirstNameChange("New")
        component.onLastNameChange("Name")
        component.onEmailChange("new@test.com")

        assertEquals("New", component.state.value.firstName)
        assertEquals("Name", component.state.value.lastName)
        assertEquals("new@test.com", component.state.value.email)
    }

    @Test
    fun updateProfileSuccessTriggersCallback() = runTest(testDispatcher) {
        var updated = false
        val component = createComponent(onProfileUpdated = { updated = true })
        advanceUntilIdle()

        component.onFirstNameChange("Updated")
        component.updateProfile()
        advanceUntilIdle()

        assertTrue(updated)
        assertNull(component.state.value.error)
    }

    @Test
    fun updateProfileFailureSetsError() = runTest(testDispatcher) {
        usersRepository.user = null
        usersRepository.error = "Update failed"
        val component = createComponent()
        advanceUntilIdle()

        component.updateProfile()
        advanceUntilIdle()

        assertEquals("Update failed", component.state.value.error)
    }
}
