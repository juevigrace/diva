@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.media

import com.diva.app.models.api.media.response.MediaResponse
import com.diva.app.models.collection.VisibilityType
import com.diva.app.models.collection.safeVisibilityType
import com.diva.app.models.media.tag.Tag
import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.user.User
import kotlin.js.ExperimentalJsExport
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Media(
    val id: Uuid,
    val submittedBy: User = User(id = Uuid.NIL),
    val mediaType: MediaType = MediaType.UNSPECIFIED,
    val title: String,
    val uri: String,
    val mimeType: String = "",
    val sizeBytes: Long = 0,
    val durationMs: Option<Long> = None,
    val width: Int = 0,
    val height: Int = 0,
    val altText: String = "",
    val visibility: VisibilityType = VisibilityType.PRIVATE,
    val sensitiveContent: Boolean = false,
    val adultContent: Boolean = false,
    val publishedAt: Instant = Clock.System.now(),
    val fingerprint: Option<String> = None,
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = Clock.System.now(),
    val deletedAt: Option<Instant> = None,
    val tags: List<Tag> = emptyList(),
) {
    companion object {
        fun fromResponse(response: MediaResponse): Media {
            return Media(
                id = Uuid.parse(response.id),
                submittedBy = User(id = Uuid.parse(response.submittedBy)),
                mediaType = safeMediaType(response.mediaType),
                title = response.title,
                uri = response.uri,
                mimeType = response.mimeType,
                sizeBytes = response.sizeBytes,
                durationMs = Option.of(response.durationMs),
                width = response.width,
                height = response.height,
                altText = response.altText,
                visibility = safeVisibilityType(response.visibility),
                sensitiveContent = response.sensitiveContent,
                adultContent = response.adultContent,
                publishedAt = Instant.fromEpochSeconds(response.publishedAt),
                fingerprint = Option.of(response.fingerprint),
                createdAt = Instant.fromEpochSeconds(response.createdAt),
                updatedAt = Instant.fromEpochSeconds(response.updatedAt),
                deletedAt = Option.of(response.deletedAt?.let { Instant.fromEpochSeconds(it) }),
                tags = emptyList(),
            )
        }
    }
}
