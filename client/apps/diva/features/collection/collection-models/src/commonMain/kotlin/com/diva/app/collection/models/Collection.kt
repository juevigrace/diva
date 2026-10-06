@file:DivaJsExport

package com.diva.app.collection.models

import com.diva.app.collection.models.api.CollectionResponse
import com.diva.app.core.models.VisibilityType
import com.diva.app.core.models.safeVisibilityType
import com.diva.app.media.models.Media
import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.user.models.User
import kotlin.time.Clock

private const val MILLIS_PER_SECOND = 1000L

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
                    response.coverMediaId?.let { Media(id = it, title = "", uri = "") },
                ),
                createdAt = response.createdAt * MILLIS_PER_SECOND,
                updatedAt = response.updatedAt * MILLIS_PER_SECOND,
                deletedAt = Option.of(response.deletedAt?.times(MILLIS_PER_SECOND)),
            )
        }
    }
}
