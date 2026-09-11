@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.collection

import com.diva.app.models.api.collection.media.response.CollectionMediaResponse
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.models.user.User
import kotlin.js.ExperimentalJsExport
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class CollectionMedia(
    val media: Media,
    val position: Int,
    val addedBy: User = User(id = Uuid.NIL),
    val score: Float = 0f,
    val addedAt: Instant,
) {
    companion object {
        fun fromResponse(response: CollectionMediaResponse): CollectionMedia {
            return CollectionMedia(
                media = Media(id = Uuid.parse(response.mediaId), title = "", uri = ""),
                position = response.position,
                addedBy = User(id = Uuid.parse(response.addedBy)),
                score = response.score,
                addedAt = Instant.fromEpochSeconds(response.addedAt),
            )
        }
    }
}
