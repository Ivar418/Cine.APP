package com.ivarvisser.cineapp.ui.feature.account

data class AccountState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false,
    val userId: Int? = null,
    val username: String? = null,
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val photoId: Int? = null,
    val favoriteMovies: List<Int> = emptyList(),
) {
    val hasError: Boolean get() = error != null
}