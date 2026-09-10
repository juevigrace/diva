package com.diva.app.features.profile.presentation.state

import com.diva.app.features.profile.domain.Profile

data class ProfileState(
    val profile: Profile? = null,
)