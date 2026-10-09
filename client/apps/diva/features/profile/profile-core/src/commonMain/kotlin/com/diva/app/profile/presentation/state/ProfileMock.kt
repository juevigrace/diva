package com.diva.app.profile.presentation.state

import io.github.juevigrace.diva.lib.user.models.User

private val OWNER = User(id = "user-1", username = "juevigrace")

internal fun mockStats(): List<ProfileQuickStat> = listOf(
    ProfileQuickStat("Albums", "42"),
    ProfileQuickStat("Playlists", "8"),
    ProfileQuickStat("Favourites", "17"),
    ProfileQuickStat("Hours", "128"),
)
