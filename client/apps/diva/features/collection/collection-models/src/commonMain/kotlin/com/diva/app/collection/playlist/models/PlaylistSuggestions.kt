@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.collection.playlist.models

import com.diva.app.collection.playlist.models.api.PlaylistSuggestionsResponse
import com.diva.app.collection.playlist.models.ModerationStatus
import com.diva.app.collection.playlist.models.safeModerationStatus
import com.diva.app.media.models.Media
import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.user.models.User
import kotlin.js.ExperimentalJsExport

data class PlaylistSuggestions(
    val id: String,
    val suggesterId: User,
    val mediaId: Media,
    val status: ModerationStatus = ModerationStatus.PENDING,
    val suggestedAt: Long,
) {
    companion object {
        fun fromResponse(response: PlaylistSuggestionsResponse): PlaylistSuggestions {
            return PlaylistSuggestions(
                id = response.id,
                suggesterId = User(id = response.suggesterId),
                mediaId = Media(
                    id = response.mediaId,
                    title = "",
                    uri = ""
                ),
                status = safeModerationStatus(response.status),
                suggestedAt = response.suggestedAt * 1000L,
            )
        }
    }
}
