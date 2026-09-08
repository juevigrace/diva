package com.diva.app.models.collection.playlist

import com.diva.app.models.api.collection.playlist.response.PlaylistSuggestionsResponse
import com.diva.app.models.collection.ModerationStatus
import com.diva.app.models.collection.safeModerationStatus
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.lib.models.user.User
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class PlaylistSuggestions(
    val id: Uuid,
    val suggesterId: User,
    val mediaId: Media,
    val status: ModerationStatus = ModerationStatus.PENDING,
    val suggestedAt: Instant,
) {
    companion object {
        fun fromResponse(response: PlaylistSuggestionsResponse): PlaylistSuggestions {
            return PlaylistSuggestions(
                id = Uuid.parse(response.id),
                suggesterId = User(id = Uuid.parse(response.suggesterId)),
                mediaId = Media(
                    id = Uuid.parse(response.mediaId),
                    title = "",
                    uri = ""
                ),
                status = safeModerationStatus(response.status),
                suggestedAt = Instant.fromEpochSeconds(response.suggestedAt),
            )
        }
    }
}
