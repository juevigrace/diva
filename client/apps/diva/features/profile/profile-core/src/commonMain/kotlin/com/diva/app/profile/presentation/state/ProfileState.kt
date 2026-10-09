package com.diva.app.profile.presentation.state

import io.github.juevigrace.diva.lib.core.models.Role
import io.github.juevigrace.diva.lib.user.models.User

data class ProfileQuickStat(
    val label: String,
    val value: String,
)

data class ProfileState(
    val user: User = User(id = "", username = "", role = Role.USER),
    val stats: List<ProfileQuickStat> = emptyList(),
)
