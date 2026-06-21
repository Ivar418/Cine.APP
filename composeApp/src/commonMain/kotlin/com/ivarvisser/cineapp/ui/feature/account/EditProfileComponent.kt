package com.ivarvisser.cineapp.ui.feature.account

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.ivarvisser.cineapp.data.dto.users.request.UpdateUserRequest
import com.ivarvisser.cineapp.data.repository.interfaces.UsersRepository
import com.ivarvisser.cineapp.utils.ResultOf
import kotlinx.coroutines.launch

class EditProfileComponent(
    componentContext: ComponentContext,
    private val usersRepository: UsersRepository,
    private val onGoBack: () -> Unit,
    private val onProfileUpdated: () -> Unit
) : ComponentContext by componentContext {
    private val scope = coroutineScope()
    private val _state = MutableValue(EditProfileState())
    val state: Value<EditProfileState> = _state

    init {
        loadUser()
    }

    private fun loadUser() {
        scope.launch {
            usersRepository.getUser()?.let { user ->
                _state.update {
                    it.copy(
                        firstName = user.firstName,
                        lastName = user.lastName,
                        email = user.email
                    )
                }
            }
        }
    }

    fun onFirstNameChange(firstName: String) {
        _state.update { it.copy(firstName = firstName) }
    }

    fun onLastNameChange(lastName: String) {
        _state.update { it.copy(lastName = lastName) }
    }

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun updateProfile() {
        scope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val request = UpdateUserRequest(
                firstName = _state.value.firstName,
                lastName = _state.value.lastName,
                email = _state.value.email,
                password = _state.value.password.takeIf { it.isNotBlank() }
            )
            when (val result = usersRepository.updateProfile(request)) {
                is ResultOf.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    onProfileUpdated()
                }

                is ResultOf.Failure -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }

    fun goBack() {
        onGoBack()
    }
}

data class EditProfileState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
