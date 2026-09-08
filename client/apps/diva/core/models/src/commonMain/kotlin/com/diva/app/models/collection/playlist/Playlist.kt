package com.diva.app.models.collection.playlist

import com.diva.app.models.api.collection.playlist.response.PlaylistResponse
import com.diva.app.models.collection.Collection
import io.github.juevigrace.diva.lib.models.user.User
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
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
                    id = Uuid.parse(response.collectionId),
                    name = "",
                ),
                isCollaborative = response.isCollaborative,
                allowSuggestions = response.allowSuggestions,
                contributors = response.contributors.map { User(id = Uuid.parse(it)) },
                suggestions = response.suggestions.map { PlaylistSuggestions.fromResponse(it) },
            )
        }
    }
}
