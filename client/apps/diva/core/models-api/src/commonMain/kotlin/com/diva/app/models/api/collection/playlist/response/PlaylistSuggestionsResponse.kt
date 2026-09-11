@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.api.collection.playlist.response

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistSuggestionsResponse(
    @SerialName("id")
    val id: String,
    @SerialName("suggester_id")
    val suggesterId: String,
    @SerialName("media_id")
    val mediaId: String,
    @SerialName("status")
    val status: String,
    @SerialName("suggested_at")
    val suggestedAt: Long,
)
