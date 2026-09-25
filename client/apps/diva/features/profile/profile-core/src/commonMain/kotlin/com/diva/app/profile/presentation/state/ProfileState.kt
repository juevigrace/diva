package com.diva.app.profile.presentation.state

import com.diva.app.profile.domain.Profile

data class ProfileState(
    val profile: Profile? = null,
)