package com.diva.app.profile.domain

import io.github.juevigrace.diva.lib.user.preferences.models.Theme
import io.github.juevigrace.diva.lib.core.models.Role
import io.github.juevigrace.diva.lib.user.models.UserStatus

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