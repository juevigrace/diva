@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.collection

import com.diva.app.models.api.collection.media.response.CollectionMediaResponse
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.models.user.User
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
