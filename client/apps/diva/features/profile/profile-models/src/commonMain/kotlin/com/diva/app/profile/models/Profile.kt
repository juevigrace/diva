package com.diva.app.profile.models

import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.models.Role
import io.github.juevigrace.diva.lib.user.models.UserStatus
import io.github.juevigrace.diva.lib.user.preferences.models.Theme

// todo: remove this and just use User
data class Profile(
    val username: String = "",
    val email: Option<String> = None,
    val phoneNumber: Option<String> = None,
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
