@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.collection.models

import com.diva.app.collection.models.api.CollectionMediaResponse
import com.diva.app.media.models.Media
import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.user.models.User
import kotlin.js.ExperimentalJsExport

data class CollectionMedia(
    val media: Media,
    val position: Int,
    val addedBy: User = User(id = ""),
    val score: Float = 0f,
    val addedAt: Long,
) {
    companion object {
        fun fromResponse(response: CollectionMediaResponse): CollectionMedia {
            return CollectionMedia(
                media = Media(id = response.mediaId, title = "", uri = ""),
                position = response.position,
                addedBy = User(id = response.addedBy),
                score = response.score,
                addedAt = response.addedAt * 1000L,
            )
        }
    }
}
