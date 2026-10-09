package com.diva.app.home.presentation.state

import com.diva.app.collection.models.Collection
import com.diva.app.media.models.Media
import com.diva.app.settings.models.AppSettings
import io.github.juevigrace.diva.lib.user.models.User

data class HomeState(
    val settings: AppSettings = AppSettings(),
    val user: User = User(id = ""),

    val showRecommended: Boolean = true,
    val recommendedCollections: List<Collection> = emptyList(),
    val recommendedMedia: List<Media> = emptyList(),

    val showRecent: Boolean = true,
    val recentCollections: List<Collection> = emptyList(),
    val recentMedia: List<Media> = emptyList(),
)
