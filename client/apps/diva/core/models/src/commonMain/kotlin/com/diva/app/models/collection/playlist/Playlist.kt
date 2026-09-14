@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.collection.playlist

import com.diva.app.models.api.collection.playlist.response.PlaylistResponse
import com.diva.app.models.collection.Collection
import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.models.user.User
import kotlin.js.ExperimentalJsExport

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
