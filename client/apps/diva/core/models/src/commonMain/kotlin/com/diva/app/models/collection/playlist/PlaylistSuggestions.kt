@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.collection.playlist

import com.diva.app.models.api.collection.playlist.response.PlaylistSuggestionsResponse
import com.diva.app.models.collection.ModerationStatus
import com.diva.app.models.collection.safeModerationStatus
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.models.user.User
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
