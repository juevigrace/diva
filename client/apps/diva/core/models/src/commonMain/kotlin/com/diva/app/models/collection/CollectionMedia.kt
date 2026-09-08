package com.diva.app.models.collection

import com.diva.app.models.media.Media
import io.github.juevigrace.diva.lib.models.user.User
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
)
