package com.ivarvisser.cineapp.mapper

import com.ivarvisser.cineapp.data.dto.users.response.UserResponse
import com.ivarvisser.cineapp.domain.User

fun User.toUserResponse(): UserResponse = UserResponse(
    userId = userId,
    userName = userName,
    photoId = photoId,
    photoUrl = photoUrl,
    firstName = firstName,
    lastName = lastName,
    email = email,
)


fun UserResponse.toUser(): User = User(
    userId = userId,
    userName = userName,
    photoId = photoId,
    photoUrl = photoUrl,
    firstName = firstName,
    lastName = lastName,
    email = email,
)
