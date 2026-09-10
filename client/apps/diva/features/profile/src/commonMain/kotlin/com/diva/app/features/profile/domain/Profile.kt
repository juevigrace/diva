package com.diva.app.features.profile.domain

import io.github.juevigrace.diva.lib.models.Theme
import io.github.juevigrace.diva.lib.models.user.Role
import io.github.juevigrace.diva.lib.models.user.UserStatus

data class Profile(
    val username: String = "",
    val email: String? = null,
    val phoneNumber: String? = null,
    val role: Role = Role.USER,
    val alias: String = "",
    val bio: String = "",
    val avatar: String = "",
    val verified: Boolean = false,
    val status: UserStatus = UserStatus.ACTIVE,
    val theme: Theme = Theme.SYSTEM,
    val language: String = "en",
    val devices: List<String> = emptyList(),
)