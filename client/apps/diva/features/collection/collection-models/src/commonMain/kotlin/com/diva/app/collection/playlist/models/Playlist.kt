@file:DivaJsExport

package com.diva.app.collection.playlist.models

import com.diva.app.collection.models.Collection
import com.diva.app.collection.playlist.models.api.PlaylistResponse
import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.user.models.User

data class Playlist(
    val collection: Collection,
    val isCollaborative: Boolean = false,
    val allowSuggestions: Boolean = true,
    val contributors: List<User> = emptyList(),
    val suggestions: List<PlaylistSuggestions> = emptyList(),
) {
    companion object {
        fun fromResponse(response: PlaylistResponse): Playlist {
            return Playlist(
                collection = Collection(
                    id = response.collectionId,
                    name = "",
                ),
                isCollaborative = response.isCollaborative,
                allowSuggestions = response.allowSuggestions,
                contributors = response.contributors.map { User(id = it) },
                suggestions = response.suggestions.map { PlaylistSuggestions.fromResponse(it) },
            )
        }
    }
}
