@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.collection

import com.diva.app.models.api.collection.response.CollectionResponse
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.user.User
import kotlin.js.ExperimentalJsExport
import kotlin.time.Clock

data class Collection(
    val id: String,
    val owner: User = User(id = ""),
    val name: String = "",
    val description: String = "",
    val collectionType: CollectionType = CollectionType.UNSPECIFIED,
    val visibility: VisibilityType = VisibilityType.PRIVATE,
    val coverMedia: Option<Media> = None,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val deletedAt: Option<Long> = None,
) {
    companion object {
        fun fromResponse(response: CollectionResponse): Collection {
            return Collection(
                id = response.id,
                owner = User(id = response.owner),
                name = response.name,
                description = response.description,
                collectionType = safeCollectionType(response.collectionType),
                visibility = safeVisibilityType(response.visibility),
                coverMedia = Option.of(
                    response.coverMediaId?.let { Media(id = it, title = "", uri = "") }
                ),
                createdAt = response.createdAt * 1000L,
                updatedAt = response.updatedAt * 1000L,
                deletedAt = Option.of(response.deletedAt?.times(1000L)),
            )
        }
    }
}
