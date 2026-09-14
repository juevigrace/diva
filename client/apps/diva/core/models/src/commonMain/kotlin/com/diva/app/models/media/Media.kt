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

data class Media(
    val id: String,
    val submittedBy: User = User(id = ""),
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
    val publishedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val fingerprint: Option<String> = None,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val deletedAt: Option<Long> = None,
    val tags: List<Tag> = emptyList(),
) {
    companion object {
        fun fromResponse(response: MediaResponse): Media {
            return Media(
                id = response.id,
                submittedBy = User(id = response.submittedBy),
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
                publishedAt = response.publishedAt * 1000L,
                fingerprint = Option.of(response.fingerprint),
                createdAt = response.createdAt * 1000L,
                updatedAt = response.updatedAt * 1000L,
                deletedAt = Option.of(response.deletedAt?.times(1000L)),
                tags = emptyList(),
            )
        }
    }
}
