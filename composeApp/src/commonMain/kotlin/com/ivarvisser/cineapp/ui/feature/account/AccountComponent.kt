package com.ivarvisser.cineapp.ui.feature.account

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.ivarvisser.cineapp.data.repository.interfaces.UsersRepository
import com.ivarvisser.cineapp.mapper.toUser
import com.ivarvisser.cineapp.utils.ResultOf
import kotlinx.coroutines.launch

class AccountComponent(
    componentContext: ComponentContext, private val usersRepository: UsersRepository
) : ComponentContext by componentContext {
    private val scope = coroutineScope()
    private val _state = MutableValue(AccountState())
    val state: Value<AccountState> = _state

    init {
        loadData()
    }

    fun loadData() {
        getUser()
        scope.launch {
            _state.update { current -> current.copy(isLoading = false) }
        }
    }

    fun getUser() {
        scope.launch {
            usersRepository.getUser()?.let {
                _state.update { current -> current.copy(user = it) }
            }
        }
    }

    fun login(userName: String, password: String) {
        scope.launch {
            setLoading(true)
            when (val result = usersRepository.login(userName, password)) {
                is ResultOf.Success -> {
                    _state.update { current ->
                        current.copy(
                            user = result.value.user.toUser()
                        )
                    }
                    setLoading(false)
                }

                is ResultOf.Failure -> {
                    setError("Login failed: ${result.message}")
                    setLoading(false)
                }
            }


        }
    }

    fun setLoading(isLoading: Boolean) {
        _state.update { current -> current.copy(isLoading = isLoading) }
    }

    fun setError(error: String?) {
        _state.update { current -> current.copy(error = error) }
    }

    fun setRegistering(isRegistering: Boolean) {
        _state.update { current -> current.copy(isRegistering = isRegistering) }
    }

    fun register(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) {
        scope.launch {
            setLoading(true)
            when (val result =
                usersRepository.register(username, firstName, lastName, email, password)) {
                is ResultOf.Success -> {
                    _state.update { current ->
                        current.copy(
                            user = result.value.user.toUser(),
                            isRegistering = false
                        )
                    }
                    setLoading(false)
                }

                is ResultOf.Failure -> {
                    setError("Registration failed: ${result.message}")
                    setLoading(false)
                }
            }
        }
    }

    fun logout() {
        scope.launch {
            usersRepository.logout(_state.value.user?.userId ?: return@launch)
            _state.value = AccountState()
        }
    }
}
