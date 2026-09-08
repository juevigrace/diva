package com.diva.app.models.collection

import com.diva.app.models.api.collection.response.CollectionResponse
import com.diva.app.models.media.Media
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.user.User
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Collection(
    val id: Uuid,
    val owner: User = User(id = Uuid.NIL),
    val name: String = "",
    val description: String = "",
    val collectionType: CollectionType = CollectionType.UNSPECIFIED,
    val visibility: VisibilityType = VisibilityType.PRIVATE,
    val coverMedia: Option<Media> = None,
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = Clock.System.now(),
    val deletedAt: Option<Instant> = None,
) {
    companion object {
        fun fromResponse(response: CollectionResponse): Collection {
            return Collection(
                id = Uuid.parse(response.id),
                owner = User(id = Uuid.parse(response.owner)),
                name = response.name,
                description = response.description,
                collectionType = safeCollectionType(response.collectionType),
                visibility = safeVisibilityType(response.visibility),
                coverMedia = Option.of(
                    response.coverMediaId?.let { Media(id = Uuid.parse(it), title = "", uri = "") }
                ),
                createdAt = Instant.fromEpochSeconds(response.createdAt),
                updatedAt = Instant.fromEpochSeconds(response.updatedAt),
                deletedAt = Option.of(response.deletedAt?.let { Instant.fromEpochSeconds(it) }),
            )
        }
    }
}
