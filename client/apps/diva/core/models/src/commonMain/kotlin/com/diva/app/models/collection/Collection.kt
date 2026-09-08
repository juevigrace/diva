package com.diva.app.models.collection

import com.diva.app.models.media.Media
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.user.User
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Collection(
    val id: Uuid,
    val owner: User = User(id = Uuid.NIL),
    val name: String,
    val description: String = "",
    val collectionType: CollectionType = CollectionType.UNSPECIFIED,
    val visibility: VisibilityType = VisibilityType.PRIVATE,
    val coverMedia: Option<Media> = None,
    val createdAt: Instant,
    val updatedAt: Instant,
    val deletedAt: Option<Instant> = None,
)
