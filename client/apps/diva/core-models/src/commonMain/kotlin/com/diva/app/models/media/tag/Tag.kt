@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.media.tag

import com.diva.app.models.api.media.tag.response.TagResponse
import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import kotlin.js.ExperimentalJsExport
import kotlin.time.Clock

data class Tag(
    val id: String,
    val name: String,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val deletedAt: Option<Long> = None,
) {
    companion object {
        fun fromResponse(response: TagResponse): Tag {
            return Tag(
                id = response.id,
                name = response.name,
                createdAt = response.createdAt * 1000L,
                updatedAt = response.updatedAt * 1000L,
                deletedAt = Option.of(response.deletedAt?.times(1000L)),
            )
        }
    }
}