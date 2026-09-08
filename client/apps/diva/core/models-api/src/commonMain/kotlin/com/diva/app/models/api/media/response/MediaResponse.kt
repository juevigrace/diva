package com.diva.app.models.api.media.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaResponse(
    @SerialName("id")
    val id: String,
    @SerialName("submitted_by")
    val submittedBy: String,
    @SerialName("media_type")
    val mediaType: String,
    @SerialName("title")
    val title: String,
    @SerialName("uri")
    val uri: String,
    @SerialName("mime_type")
    val mimeType: String = "",
    @SerialName("size_bytes")
    val sizeBytes: Long = 0,
    @SerialName("duration_ms")
    val durationMs: Long? = null,
    @SerialName("width")
    val width: Int = 0,
    @SerialName("height")
    val height: Int = 0,
    @SerialName("alt_text")
    val altText: String = "",
    @SerialName("visibility")
    val visibility: String,
    @SerialName("sensitive_content")
    val sensitiveContent: Boolean = false,
    @SerialName("adult_content")
    val adultContent: Boolean = false,
    @SerialName("published_at")
    val publishedAt: Long,
    @SerialName("fingerprint")
    val fingerprint: String? = null,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("updated_at")
    val updatedAt: Long,
    @SerialName("deleted_at")
    val deletedAt: Long? = null,
)
