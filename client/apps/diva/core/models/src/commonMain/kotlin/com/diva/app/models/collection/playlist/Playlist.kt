package com.diva.app.models.collection.playlist

import com.diva.app.models.collection.Collection
import io.github.juevigrace.diva.lib.models.user.User

data class Playlist(
    val collection: Collection,
    val isCollaborative: Boolean = false,
    val allowSuggestions: Boolean = true,
    val contributors: List<User> = emptyList(),
    val suggestions: List<PlaylistSuggestions> = emptyList(),
)
