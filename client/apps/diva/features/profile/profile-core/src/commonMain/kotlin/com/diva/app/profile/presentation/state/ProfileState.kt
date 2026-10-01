package com.diva.app.profile.presentation.state

import com.diva.app.media.models.Media
import com.diva.app.profile.models.Profile

data class ProfileQuickStat(
    val label: String,
    val value: String,
)

data class ProfileState(
    val profile: Profile? = null,
    val stats: List<ProfileQuickStat> = emptyList(),
    val recentMedia: List<Media> = emptyList(),
)