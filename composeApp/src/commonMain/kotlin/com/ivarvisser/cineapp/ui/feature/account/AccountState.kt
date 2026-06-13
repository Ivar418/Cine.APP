package com.ivarvisser.cineapp.ui.feature.account

import com.ivarvisser.cineapp.domain.User

data class AccountState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: User? = null,
    val favoriteMovies: List<Int> = emptyList(),
    val isRegistering: Boolean = false,
) {
    val hasError: Boolean get() = error != null
    val isLoggedIn: Boolean get() = user != null

}