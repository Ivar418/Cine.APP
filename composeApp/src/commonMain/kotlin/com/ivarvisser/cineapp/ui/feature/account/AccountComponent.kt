package com.ivarvisser.cineapp.ui.feature.account

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.ivarvisser.cineapp.data.repository.interfaces.UsersRepository

class AccountComponent(
    componentContext: ComponentContext,
    usersRepository: UsersRepository
) : ComponentContext by componentContext {
    private val scope = coroutineScope()
    private val _state = MutableValue(AccountState())
    val state: Value<AccountState> = _state

    init {
        loadData()
    }

    fun loadData() {}
}