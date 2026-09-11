@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.media.tag

import com.diva.app.models.api.media.tag.response.TagResponse
import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import kotlin.js.ExperimentalJsExport
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Tag(
    val id: Uuid,
    val name: String,
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = Clock.System.now(),
    val deletedAt: Option<Instant> = None,
) {
    companion object {
        fun fromResponse(response: TagResponse): Tag {
            return Tag(
                id = Uuid.parse(response.id),
                name = response.name,
                createdAt = Instant.fromEpochSeconds(response.createdAt),
                updatedAt = Instant.fromEpochSeconds(response.updatedAt),
                deletedAt = Option.of(response.deletedAt?.let { Instant.fromEpochSeconds(it) }),
            )
        }
    }
}